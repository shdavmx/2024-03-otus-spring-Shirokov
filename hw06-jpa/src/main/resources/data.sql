  insert into books(title)
  values ('BookTitle_1'), ('BookTitle_2'), ('BookTitle_3');

 insert into authors(full_name, book_id)
 values ('Author_1', 1), ('Author_2', 2), ('Author_3', 3);

 insert into genres(name)
 values ('Genre_1'), ('Genre_2'), ('Genre_3'),
        ('Genre_4'), ('Genre_5'), ('Genre_6');

 insert into comments(comment, book_id)
 values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3),
        ('Comment_4', 1), ('Comment_5', 2), ('Comment_6', 3);

 insert into books_genres(book_id, genre_id)
 values (1, 1),   (1, 2),
        (2, 3),   (2, 4),
        (3, 5),   (3, 6);