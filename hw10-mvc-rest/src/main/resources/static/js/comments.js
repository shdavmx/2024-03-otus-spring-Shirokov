function getCommentsForBook(bookId) {
    $.ajax({
        url: '/api/comments',
        data: {bookId: bookId},
        type: 'GET',
        success: function(response) {
            fillComments(response)
        },
        error: function(error) {
            alert('Could not get comments for bookId ' + bookId);
        },
        complete: function() {
            $('#btnAddComment').unbind().click(function() {
                onAddCommentClick(bookId);
            });
        }
    });
}

function onAddCommentClick(bookId) {
    var commentModel = {
        comment: $('#comment-input').val(),
        bookId: bookId
    };

    $.ajax({
        url: '/api/comments',
        data: JSON.stringify(commentModel),
        contentType: 'application/json',
        type: 'POST',
        success: function(response) {
            getCommentsForBook(bookId);
        },
        error: function(error) {
            alert('Could not add new comment for this book: ' + error);
        }
    });
}

function fillComments(comments) {
    $('.list-comments').empty();

    comments.forEach(comment => {
        var dComment = $('<div></div>', {
            "class": "comment"
        })
        var pComment = $('<p></p>', {
            text: comment.comment
        })
        dComment.append(pComment);
        $('.list-comments').append(dComment);
    });
}