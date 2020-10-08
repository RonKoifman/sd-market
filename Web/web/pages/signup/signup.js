$(function () {
    const onSubmitClicked = function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: this.action,
            timeout: 2000,
            success: function (res) {
                if (res === 'signup.html' || res === 'home.html') {
                    window.location.assign(res);
                } else {
                    $('.alert').addClass('alert-danger').text(res);
                    $('#username').val('');
                }
            }
        });

        return false
    }

    $('form.form-signup').submit(onSubmitClicked)
    $('form.form-signin').submit(onSubmitClicked)
});

