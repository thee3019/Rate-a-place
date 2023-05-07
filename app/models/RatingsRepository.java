package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPARatingsRepository.class)
public interface RatingsRepository {


    CompletionStage<Ratings> add(Ratings ratings);

    CompletionStage<Stream<Ratings>> list();

}
