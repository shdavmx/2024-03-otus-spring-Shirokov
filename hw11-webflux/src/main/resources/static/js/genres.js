function onPageLoaded() {
    fetch('/api/genres')
        .then(response => response.json())
        .then(json => buildGenresTable(json));
}

function buildGenresTable(genres) {
    var tbGenres = $('#tbGenres');
    tbGenres.empty();

    genres.forEach((genre) => {
        var trGenre = $('<tr></tr>');

        var tdId = $('<td></td>', {
            text: genre.id
        });

        var tdFullName = $('<td></td>', {
            text: genre.name
        });

        var tdActions = $('<td></td>');
        var btnEdit = $('<button></button>', {
            text: 'Edit',
            on: {
                click: function(e) {
                    onEditGenreClick(genre);
                }
            } 
        });
        var btnDelete = $('<button></button>', {
            text: 'Delete',
            on: {
                click: function(e) {
                    onDeleteGenreClick(genre);
                }
            } 
        });
        tdActions.append(btnEdit, btnDelete);

        trGenre.append(tdId, tdFullName, tdActions);
        tbGenres.append(trGenre);
    });
}

function onEditGenreClick(genre) {
    console.log('Edit form called for: ' + genre.name);
    $('.d-popup').removeClass('d-none');
    $('.errors').addClass('d-none');

    $('#genre-name-input').val(genre.name);
    $('#btnSaveGenre').unbind().click(function() {
        genre.name = $('#genre-name-input').val();
        onSaveGenreClick(genre);
    });
}

function onDeleteGenreClick(genre) {
    console.log('Delete called for: ' + genre.name);
    $.ajax({
        url: '/api/genres/' + genre.id,
        type: 'DELETE',
        success: function(response) {
            onPageLoaded();
        }
    });
}

function onSaveGenreClick(genre) {        
    $.ajax({
        url: '/api/genres',
        data: JSON.stringify(genre),
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

function onCancelGenreClick() {
    $('.d-popup').addClass('d-none');
}