package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {
    private final List<Book> testBookCollection = List.of(
            new Book("1", "Book_1",
                    new Author("1", "Author_1"),
                    List.of(new Genre("1", "Genre_1"), new Genre("2","Genre_2"))),
            new Book("2", "Book_2",
                    new Author("2", "Author_2"),
                    List.of(new Genre("3", "Genre_3"), new Genre("4","Genre_4"))),
            new Book("3", "Book_3",
                    new Author("3", "Author_3"),
                    List.of(new Genre("5", "Genre_5"), new Genre("6","Genre_6")))
    );

    private final List<Comment> testComments = List.of(
            new Comment("1", "Comment_1", testBookCollection.get(0)),
            new Comment("2", "Comment_2", testBookCollection.get(1)),
            new Comment("3", "Comment_3", testBookCollection.get(2)),
            new Comment("4", "Comment_4", testBookCollection.get(0)),
            new Comment("5", "Comment_5", testBookCollection.get(1)),
            new Comment("6", "Comment_6", testBookCollection.get(2))
    );

    @ChangeSet(order = "001", id = "dropDb", author = "dshirokov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertGenres", author = "dshirokov")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genreCollection = db.getCollection("genres");
        List<Document> docs = List.of(
                new Document().append("_id", "1").append("name", "Genre_1"),
                new Document().append("_id", "2").append("name", "Genre_2"),
                new Document().append("_id", "3").append("name", "Genre_3"),
                new Document().append("_id", "4").append("name", "Genre_4"),
                new Document().append("_id", "5").append("name", "Genre_5"),
                new Document().append("_id", "6").append("name", "Genre_6")
        );
        genreCollection.insertMany(docs);
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "dshirokov")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> authorCollection = db.getCollection("authors");
        List<Document> docs = List.of(
                new Document().append("_id", "1").append("fullName", "Author_1"),
                new Document().append("_id", "2").append("fullName", "Author_2"),
                new Document().append("_id", "3").append("fullName", "Author_3"),
                new Document().append("_id", "4").append("fullName", "Author_4"),
                new Document().append("_id", "5").append("fullName", "Author_5"),
                new Document().append("_id", "6").append("fullName", "Author_6")
        );
        authorCollection.insertMany(docs);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "dshirokov")
    public void invertBooks(BookRepository bookRepository) {
        bookRepository.saveAll(testBookCollection);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "dshirokov")
    public void insertComments(CommentRepository commentRepository) {
        commentRepository.saveAll(testComments);
    }
}
