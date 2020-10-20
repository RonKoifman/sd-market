const REGION_STORES_URL = buildUrlWithContextPath('region-stores');
const REGION_ITEMS_URL = buildUrlWithContextPath('region-items');
const USER_INFO_URL = buildUrlWithContextPath('user-info');
const REGION_NAME_URL = buildUrlWithContextPath('region-name');
const refreshRate = 2000;

$(function () {
    setInterval(ajaxRegionStores, refreshRate);
    setInterval(ajaxRegionItems, refreshRate);
});

$(function () {
    $.ajax({
        url: REGION_STORES_URL,
        success: function (regionStores) {
            refreshRegionStores(regionStores);
        },
        error: function () {
            console.error('Error from region stores URL');
        }
    });
});

$(function () {
    $.ajax({
        url: REGION_ITEMS_URL,
        success: function (regionItems) {
            refreshRegionItems(regionItems);
        },
        error: function () {
            console.error('Error from region items URL');
        }
    });
});

$(function () {
   $.ajax({
      url: REGION_NAME_URL,
       success: function (regionName) {
           $('#regionName').text(regionName);
       },
       error: function () {
           console.error('Error from region-name URL');
       }
   });
});

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        success: function (loggedInUser) {
            switch (loggedInUser['userRole']) {
                case 'Customer':
                    renderCustomerNavbar();
                    break;
                case 'Store Owner':
                    renderStoreOwnerNavbar();
                    break;
            }
        },
        error: function () {
            console.error('Error from user-info URL');
        }
    });
});

function refreshRegionStores(regionStores) {
    // TODO: implement refresh region stores
}

function refreshRegionItems(regionItems) {
    const itemsTable = $('#itemsTable');

    itemsTable.empty();
    $('<tr>' +
        '<th>Name</th>' +
        '<th>ID</th>' +
        '<th>Purchase Form</th>' +
        '<th>Stores Selling</th>' +
        '<th>Average Price</th>' +
        '<th>Total Purchase Amount</th>' +
        '</tr>').appendTo(itemsTable);

    $.each(regionItems || [], function (index, item) {
        $('<tr>' +
            '<td>' + item['name'] + '</td>' +
            '<td>' + item['id'] + '</td>' +
            '<td>' + item['purchaseForm'] + '</td>' +
            '<td>' + item['amountOfStoresSelling'] + '</td>' +
            '<td>' + '$' + parseFloat(item['averagePrice']).toFixed(2) + '</td>' +
            '<td>' + parseFloat(item['purchaseAmount']).toFixed(2) + '</td>' +
            '</tr>').appendTo(itemsTable);
    });
}

function renderCustomerNavbar() {
    $('#navbarUl').append(
        '<li class="nav-item"><a class="nav-link" href="#">My Orders History</a></li>' +
        '<li class="nav-item"><a class="nav-link" href="#">Place New Order</a></li>'
    );
}

function renderStoreOwnerNavbar() {
    $('#navbarUl').append(
        '<li class="nav-item"><a class="nav-link" href="#">My Stores Orders History</a></li>' +
        '<li class="nav-item"><a class="nav-link" href="#">My Feedbacks</a></li>' +
        '<li class="nav-item"><a class="nav-link" href="#">Open New Store</a></li>'
    );
}

function ajaxRegionStores() {
    $.ajax({
        url: REGION_STORES_URL,
        success: function (regionStores) {
            refreshRegionStores(regionStores);
        },
        error: function () {
            console.error('Error from region stores URL');
        }
    });
}

function ajaxRegionItems() {
    $.ajax({
        url: REGION_ITEMS_URL,
        success: function (regionItems) {
            refreshRegionItems(regionItems);
        },
        error: function () {
            console.error('Error from region items URL');
        }
    });
}
