$(document).ready(function () {

        $('#forgotPasswordForm').on('submit', function(event) {
        event.preventDefault();

        var email = $('#email').val();

        $('#error-message').text('');
        $('#success-message').text('');

        $.ajax({
            url: '/api/auth/password/forgot',
            type: 'POST',
            data: {
                email: email
            },
            success: function(response) {
                $('#success-message').text("We've sent an email with the link to reset your password.");
            },
            error: function(xhr, status, error) {
                $('#error-message').text("The Email address doesn't exist. Please try again.");
            }
        });
    });

})