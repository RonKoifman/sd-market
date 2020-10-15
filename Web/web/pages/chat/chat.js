const USER_INFO_URL = buildUrlWithContextPath("user-info");

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        success: function (loggedInUser) {
            $('#username').text(loggedInUser.username);
            switch (loggedInUser.userRole) {
                case 'Customer':
                    $('#uploadNavLink').hide();
                    break;
            }
        },
        error: function () {
            console.error('Error from user-info URL')
        }
    })
});