const SIGNUP_URL = buildUrlWithContextPath('signup');

$(function () {
    const onSubmitClicked = function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: SIGNUP_URL,
            timeout: 2000,
            success: function (res) {
                window.location.assign(res);
            },
            error: function (res) {
                $('.alert').addClass('alert-danger').text(res.responseText);
                $('#username').val('');
            }
        });

        return false;
    }

    $('form.form-signup').submit(onSubmitClicked);
    $('form.form-signin').submit(onSubmitClicked);
});

