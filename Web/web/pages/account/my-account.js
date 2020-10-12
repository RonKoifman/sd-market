const userInfoURL = buildUrlWithContextPath("userInfo");
let user;

$(function () {
    $.ajax({
        url: userInfoURL,
        async: false,
        success: function (loggedInUser) {
            user = loggedInUser;
            $('#username').text(user.username);
            switch (user.userRole) {
                case 'Customer':
                    $('#uploadNavLink').hide();
                    break;
                case 'Store Owner':
                    $('#depositLabels').hide();
            }
        }
    })
});