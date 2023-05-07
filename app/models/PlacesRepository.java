package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAPlacesRepository.class)
public interface PlacesRepository {

    CompletionStage<Places> add(Places place);

    CompletionStage<Stream<Places>> list();


}
