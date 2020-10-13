const UPLOAD_FILE_URL = buildUrlWithContextPath("upload-file");

$(function () {
    $('#uploadForm').submit(function () {
        const alert = $('.alert');
        const file = this[0].files[0];
        const formData = new FormData();

        alert.removeClass('alert-success');
        alert.removeClass('alert-danger');
        formData.append('file-key', file);

        $.ajax({
            method:'POST',
            data: formData,
            url: UPLOAD_FILE_URL,
            processData: false,
            contentType: false,
            timeout: 4000,
            success: function (res) {
                $('.alert').addClass('alert-success').text(res);
            },
            error: function (res) {
                $('.alert').addClass('alert-danger').text(res.responseText);
            }
        });

        return false;
    })
})