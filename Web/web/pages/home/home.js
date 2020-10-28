const USER_INFO_URL = buildUrlWithContextPath('user-info');
const REGIONS_URL = buildUrlWithContextPath('regions');
const USERS_URL = buildUrlWithContextPath('users');
const SET_REGION_URL = buildUrlWithContextPath('set-region');
const refreshRate = 2000;

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        success: function (loggedInUser) {
            $('#username').text(loggedInUser.username);
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

$(function () {
    setInterval(ajaxUsersInfo, refreshRate);
    setInterval(ajaxRegionsInfo, refreshRate);
    ajaxUsersInfo();
    ajaxRegionsInfo();
    setTimeout(function () {}, 500);
});

function refreshUsersDiv(users) {
    const usersDiv = $('#users');

    usersDiv.empty();
    $.each(users || [], function (index, user) {
        const imgByUser = user['userRole'] === 'Customer' ? '<img class="user-avatar" src="common/images/customer-avatar.png" alt="">' : '<img class="user-avatar" src="common/images/store-owner-avatar.png" alt="">';

        $('<li class="text-center">' +
        imgByUser +
        '<div class="user-info"></div><br>' +
        '</li>').appendTo(usersDiv);
    });

    const userInfoElements = document.getElementsByClassName('user-info');
    for (let i = 0; i < userInfoElements.length; i++) {
        $('<div id="usernameFont">' + users[i]['username'] + '</>' +
        '<div id="userRoleFont" class="font-italic">' + users[i]['userRole'] + '</div>').appendTo(userInfoElements[i]);
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
        '<th>Average Order Items Cost</th>' +
        '<th>See More</th>' +
        '</tr>').appendTo(regionsTable);

    $.each(regions || [], function (index, region) {
        $('<tr>' +
            '<td>' + region.name + '</td>' +
            '<td>' + region['ownerUsername'] + '</td>' +
            '<td>' + region['totalItems'] + '</td>' +
            '<td>' + region['totalStores'] + '</td>' +
            '<td>' + region['totalOrdersMade'] + '</td>' +
            '<td>' + '$' + parseFloat(region['averageOrderItemsCost']).toFixed(2) + '</td>' +
            '<td>' + "<a href='" + "region-info.html" +"'>Go to region &raquo;</a>" + '</td>' +
            '</tr>').appendTo(regionsTable)
            .find('a')
            .click(function () {
                onRegionChosen(region);
            });
    });

    if (regions.length === 0) {
        $('<tr>' +
        '<td>No regions uploaded yet.</td>' +
        '</tr>').appendTo(regionsTable);
    }
}

function onRegionChosen(region) {
    setRegionNameOnSession(region.name);
    window.location.assign('region-info.html');
}

function setRegionNameOnSession(regionName) {
    $.ajax({
        url: SET_REGION_URL,
        data: `region_name=${regionName}`,
        success: function () {
        },
        error: function () {
            console.error('Error from set region URL');
        }
    });
}

function ajaxRegionsInfo() {
    $.ajax({
        url: REGIONS_URL,
        success: function (regions) {
            refreshRegionsTable(regions);
        },
        error: function () {
            console.error('Error from regions URL');
        }
    });
}

function ajaxUsersInfo() {
    $.ajax({
        url: USERS_URL,
        success: function (users) {
            refreshUsersDiv(users);
        },
        error: function () {
            console.error('Error from users URL');
        }
    });
}