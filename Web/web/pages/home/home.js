const USER_INFO_URL = buildUrlWithContextPath('user-info');
const REGIONS_URL = buildUrlWithContextPath('regions');
const USERS_URL = buildUrlWithContextPath('users');
const SALE_REGION_URL = buildUrlWithContextPath('sale-region');
const refreshRate = 2000;

$(function () {
    setInterval(ajaxUsersInfo, refreshRate)
    setInterval(ajaxRegionsInfo, refreshRate);
});

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
        }
    })
});

$(function () {
    $.ajax({
        url: USERS_URL,
        success: function (users) {
            refreshUsersDiv(users);
        }
    })
});

$(function () {
    $.ajax({
        url: REGIONS_URL,
        success: function (regions) {
            refreshRegionsTable(regions);
        }
    })
});

function refreshUsersDiv(users) {
    const usersDiv = $('#users');

    usersDiv.empty();
    $.each(users || [], function (index, user) {
        const imgByUser = user.userRole === 'Customer' ? '<img class="user-avatar" src="common/images/customer-avatar.png" alt="">' : '<img class="user-avatar" src="common/images/store-owner-avatar.png" alt="">';

        $('<li class="text-center">' +
        imgByUser +
        '<div class="user-info"></div><br>' +
        '</li>').appendTo(usersDiv);
    });

    const userInfoElements = document.getElementsByClassName('user-info');
    for (let i = 0; i < userInfoElements.length; i++) {
        userInfoElements[i].innerHTML = users[i].username + '<br>' + users[i].userRole;
    }
}

function refreshRegionsTable(regions) {
    const regionsTable = $('#regionsTable');
    regionsTable.empty();

    $('<tr>' +
        '<th>Region Name</th>' +
        '<th>Owner</th>' +
        '<th>Total Items</th>' +
        '<th>Total Stores</th>' +
        '<th>Total Orders Made</th>' +
        '<th>Average Orders Cost</th>' +
        '<th>See More</th>' +
        '</tr>').appendTo(regionsTable);

    $.each(regions || [], function (index, region) {
        $('<tr>' +
            '<td>' + region.name + '</td>' +
            '<td>' + region.ownerUsername + '</td>' +
            '<td>' + region.totalItems + '</td>' +
            '<td>' + region.totalStores + '</td>' +
            '<td>' + region.totalOrdersMade + '</td>' +
            '<td>' + '$' + parseFloat(region.averageOrdersCost).toFixed(2) + '</td>' +
            '<td>' + "<a href='" + SALE_REGION_URL +"'>Go to region &raquo;</a>" + '</td>' +
            '</tr>').appendTo(regionsTable)
            .find('a')
            .click(function () {
                onRegionChosen();
            });
    });
}

function onRegionChosen() {
    // TODO: implement on region chosen
}

function ajaxRegionsInfo() {
    $.ajax({
        url: REGIONS_URL,
        success: function (regions) {
            refreshRegionsTable(regions);
        }
    });
}

function ajaxUsersInfo() {
    $.ajax({
        url: USERS_URL,
        success: function (users) {
            refreshUsersDiv(users);
        }
    });
}