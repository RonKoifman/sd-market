const USER_INFO_URL = buildUrlWithContextPath("userInfo");
let user;

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        async: false,
        success: function (loggedInUser) {
            user = loggedInUser;
            $('#username').text(user.username);
            switch (user.userRole) {
                case 'Customer':
                    $('#uploadNavLink').hide();
                    break;
            }
        }
    })
});