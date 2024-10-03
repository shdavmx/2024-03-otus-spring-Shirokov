function onPageLoaded() {
    fetch('http://localhost:8089/api/authors')
        .then(response => response.json())
        .then(json => buildAuthorsTable(json));
}

function buildAuthorsTable(authors) {
    var tbAuthors = $('#tbAuthors');
    tbAuthors.empty();

    authors.forEach((author) => {
        var trAuthor = $('<tr></tr>');

        var tdId = $('<td></td>', {
            text: author.id
        });

        var tdFullName = $('<td></td>', {
            text: author.fullName
        });

        var tdActions = $('<td></td>');
        var btnEdit = $('<button></button>', {
            text: 'Edit',
            on: {
                click: function(e) {
                onEditAuthorClick(author);
                }
            } 
        });
        var btnDelete = $('<button></button>', {
            text: 'Delete',
            on: {
                click: function(e) {
                    onDeleteAuthorClick(author);
                }
            } 
        });
        tdActions.append(btnEdit, btnDelete);

        trAuthor.append(tdId, tdFullName, tdActions);
        tbAuthors.append(trAuthor);
    });
}

function onEditAuthorClick(author) {
    console.log('Edit form called for: ' + author.fullName);
    $('.d-popup').removeClass('d-none');
    $('.errors').addClass('d-none');

    $('#author-name-input').val(author.fullName);
    $('#btnSaveAuthor').unbind().click(function() {
        author.fullName = $('#author-name-input').val();
        onSaveAuthorClick(author);
    });
}

function onDeleteAuthorClick(author) {
    console.log('Delete called for: ' + author.fullName);
    $.ajax({
        url: '/api/authors',
        data: { id: author.id },
        type: 'DELETE',
        success: function(response) {
            onPageLoaded();
        }
    });
}

function onSaveAuthorClick(author) {        
    $.ajax({
        url: '/api/authors',
        data: JSON.stringify(author),
        contentType: 'application/json',
        type: 'POST',
        success: function(response) {
            $('.d-popup').addClass('d-none');
            onPageLoaded();
        },
        error: function(error) {
            $('.errors').removeClass('d-none');
        }
    });
}

function onCancelAuthorClick() {
    $('.d-popup').addClass('d-none');
}