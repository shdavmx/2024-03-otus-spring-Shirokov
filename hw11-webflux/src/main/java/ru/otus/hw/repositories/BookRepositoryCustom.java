package ru.otus.hw.repositories;

import com.mongodb.client.result.UpdateResult;
import reactor.core.publisher.Mono;

public interface BookRepositoryCustom {
    Mono<UpdateResult> removeGenreArrayElementsById(String genreId);
}
