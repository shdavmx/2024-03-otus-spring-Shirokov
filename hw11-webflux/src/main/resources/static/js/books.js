function onPageLoaded() {
    fetch('/api/books')
        .then(response => response.json())
        .then(json => buildBooksTable(json));
}

function buildBooksTable(books) {
    var tbBooks = $('#tbBooks');
    tbBooks.empty();

    books.forEach((book) => {
        var trBook = $('<tr></tr>');

        var tdId = $('<td></td>', {
            text: book.id
        });

        var tdTitle = $('<td></td>', {
            text: book.title
        });

        var tdAuthor = $('<td></td>', {
            text: book.author.fullName
        });

        var genreString = '';
        book.genres.forEach((genre) => {
            genreString += genre.name + ',';
        });
        var tdGenres = $('<td></td>', {
            text: genreString.substring(0, genreString.length - 1)
        });

        var tdActions = $('<td></td>');
        var btnDetails = $('<button></button>', {
            text: 'Show details',
            on: {
                click: function(e) {
                    onShowBookDetailsClick(book);
                }
            } 
        });
        var btnEdit = $('<button></button>', {
            text: 'Edit',
            on: {
                click: function(e) {
                    onEditBookClick(book);
                }
            } 
        });
        var btnDelete = $('<button></button>', {
            text: 'Delete',
            on: {
                click: function(e) {
                    onDeleteBookClick(book);
                }
            } 
        });
        tdActions.append(btnDetails, btnEdit, btnDelete);

        trBook.append(tdId, tdTitle, tdAuthor, tdGenres, tdActions);
        tbBooks.append(trBook);
    });
}

function onShowBookDetailsClick(book) {
    console.log('Details form called for: ' + book.title);
    $('#detailsPopup').removeClass('d-none');

    $('#bookTitle').text(book.title);
    $('#bookAuthor').text(book.author.fullName);

    getCommentsForBook(book.id);
}

function onEditBookClick(book) {
    console.log('Edit form called for: ' + book.title);
    $('#editPopup').removeClass('d-none');
    getAuthors(book.author);
    getGenres(book.genres);

    $('#book-name-input').val(book.title);
    $('#btnSaveBook').unbind().click(function() {
        var bookModel = {
            id: book.id,
            title: $('#book-name-input').val(),
            authorId: $('#book-author-selector').val(),
            genreIds: $('#book-genre-selector').val()
        };
        
        onSaveBookClick(bookModel);
    });
}

function onDeleteBookClick(book) {
    console.log('Delete called for: ' + book.title);
    $.ajax({
        url: '/api/books/' + book.id,
        type: 'DELETE',
        success: function(response) {
            onPageLoaded();
        }
    });
}

function onSaveBookClick(book) {        
    $.ajax({
        url: '/api/books',
        data: JSON.stringify(book),
        contentType: 'application/json',
        type: 'POST',
        success: function(response) {
            $('#editPopup').addClass('d-none');
            onPageLoaded();
        },
        error: function(error) {
            alert(error);
        }
    });
}

function onCancelBookClick() {
    $('#editPopup').addClass('d-none');
    $('#detailsPopup').addClass('d-none');
}

function getAuthors(selectedAuthor) {
    $.ajax({
        url: '/api/authors',
        type: 'GET',
        success: function(response) {
            fillAuthorSelector(response, selectedAuthor)
        },
        error: function(error) {
            alert('Could not get all authors');
        }
    });
}

function fillAuthorSelector(authors, selectedAuthor) {
    $('#book-author-selector').empty();
    authors.forEach((author) => {
        var option = $('<option></option>', {
            value: author.id,
            text: author.fullName,
            selected: author.id == selectedAuthor.id
        });
        $('#book-author-selector').append(option);
    });
}

function getGenres(selectedGenres) {
    $.ajax({
        url: '/api/genres',
        type: 'GET',
        success: function(response) {
            fillGenreSelector(response, selectedGenres)
        },
        error: function(error) {
            alert('Could not get all genres');
        }
    });
}

function fillGenreSelector(genres, selectedGenres) {
    $('#book-genre-selector').empty();
    genres.forEach((genre) => {
        var option = $('<option></option>', {
            value: genre.id,
            text: genre.name,
            selected: selectedGenres.some(g => g.id == genre.id)
        });
        $('#book-genre-selector').append(option);
    });
}