package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPARatingsRepository implements RatingsRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPARatingsRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Ratings> add(final Ratings ratings) {
        return supplyAsync(() -> wrap(em -> insert(em, ratings)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Ratings>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Ratings insert(EntityManager em, Ratings ratings) {
        em.persist(ratings);
        return ratings;
    }

    private Stream<Ratings> list(EntityManager em) {
        List<Ratings> ratings = em.createQuery("select r from rating r", Ratings.class).getResultList();
        return ratings.stream();
    }

}
