const NEW_STORE_URL = buildUrlWithContextPath('set-store-details');

$(function () {
    $('#newStoreForm').submit(function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: NEW_STORE_URL,
            method: 'POST',
            timeout: 2000,
            success: function (res) {
                window.location.assign(res);
            },
            error: function (res) {
                $('.alert').addClass('alert-danger').text(res.responseText);
            }
        });

        return false;
    });
});