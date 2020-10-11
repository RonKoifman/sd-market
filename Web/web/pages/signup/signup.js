$(function () {
    const onSubmitClicked = function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: this.action,
            timeout: 2000,
            error: function (res) {
                $('.alert').addClass('alert-danger').text(res.responseText);
                $('#username').val('');
            },
            success: function (res) {
                window.location.assign(res);
            }
        });

        return false;
    }

    $('form.form-signup').submit(onSubmitClicked);
    $('form.form-signin').submit(onSubmitClicked);
});

