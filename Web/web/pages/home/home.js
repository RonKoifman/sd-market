const USER_INFO_URL = buildUrlWithContextPath('userInfo');
const HOME_URL = buildUrlWithContextPath('home');
const REGION_URL = buildUrlWithContextPath('sale-region');
const refreshRate = 2000;
const regionsTableBodyId = 'regionsTableBody'; // SELL_ZONES_TABLE_BODY_ID
const regionsTableCellClass = 'regionsTableCell'; // SELL_ZONES_TABLE_CELL_ID
const regionCellLinkClass = 'region-cell-link'; // SELL_ZONE_TABLE_LINK_CELL_CLASS
let user;

$(function () {
    setInterval(ajaxRegionsTable, refreshRate);
});

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

$(function () {
    $.ajax({
        url: HOME_URL,
        success: function (regions) {
            refreshRegionsTable(regions);
        }
    })
});

function refreshRegionsTable(regions) {
    $('#regionsTableBody').empty();

    $.each(regions || [], function (index, region) {
        addElementToTable(region, regionsTableBodyId, regionsTableCellClass);
        addRegionLinksToTable(region);
    });
}

function addRegionLinksToTable(region) {
    const regionName = region['name'];
    let link = document.createElement('a');
    link.setAttribute('href', REGION_URL);
    link.className = regionCellLinkClass;

    link.innerHTML = 'Go to region &raquo;';
    link.addEventListener('click', function () {
        onRegionChosen(regionName);
    });

    let tableBody = document.getElementById(regionsTableBodyId);
    for (let i = 0; i < tableBody.rows.length; i++) {
        let row = tableBody.rows[i];
        let cell = row.insertCell();
        cell.classList.add(regionsTableCellClass);
        cell.appendChild(link);
    }
}

function onRegionChosen() {
    // ....implement
}

function addElementToTable(element, tableBodyId, tableCellClass) {
    const tableBody = document.getElementById(tableBodyId);
    const row = tableBody.insertRow();
    let cell;

    Object.keys(element).forEach(function (key) {
        cell = row.insertCell();
        cell.classList.add(tableCellClass);
        cell.textContent = element[key];
    })
}

function ajaxRegionsTable() {
    $.ajax({
        url: HOME_URL,
        success: function (regions) {
            refreshRegionsTable(regions);
        }
    });
}