create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table comments (
    id bigserial,
    comment varchar(max),
    book_id bigint,
    primary key (id),
    foreign key (book_id) references books(id) on delete cascade
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);