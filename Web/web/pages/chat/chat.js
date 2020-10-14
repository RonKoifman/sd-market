const USER_INFO_URL = buildUrlWithContextPath("user-info");

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        async: false,
        success: function (loggedInUser) {
            $('#username').text(loggedInUser.username);
            switch (loggedInUser.userRole) {
                case 'Customer':
                    $('#uploadNavLink').hide();
                    break;
            }
        }
    })
});