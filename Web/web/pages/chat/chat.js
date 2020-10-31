const USER_INFO_URL = buildUrlWithContextPath('user-info');

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        success: function (loggedInUser) {
            switch (loggedInUser['userRole']) {
                case 'Customer':
                    $('#uploadNavLink').hide();
                    break;
            }
        },
        error: function () {
            console.error('Error from user-info URL');
        }
    });
});

function onSendClicked(userMessage) {
    $('#userMessage').val('');
}

$(function () {
    $('#sendButton').on('click', function () {
       onSendClicked($('#userMessage').val());
    });
});