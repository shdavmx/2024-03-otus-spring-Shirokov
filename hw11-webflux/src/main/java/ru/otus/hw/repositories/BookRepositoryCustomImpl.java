package ru.otus.hw.repositories;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<UpdateResult> removeGenreArrayElementsById(String genreId) {
        Query query = Query.query(Criteria.where("$id").is(genreId));
        Update update = new Update().pull("genres", query);
        return mongoTemplate.updateMulti(new Query(), update, Book.class);
    }
}
