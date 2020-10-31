const REGION_STORES_URL = buildUrlWithContextPath('region-stores');
const REGION_ITEMS_URL = buildUrlWithContextPath('region-items');
const USER_INFO_URL = buildUrlWithContextPath('user-info');
const REGION_NAME_URL = buildUrlWithContextPath('region-name');
const IS_REGION_OWNER_URL = buildUrlWithContextPath('is-region-owner');
const refreshRate = 2000;
let isUserRegionOwner;

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
        url: IS_REGION_OWNER_URL,
        success: function (isUserRegionOwnerResponse) {
            isUserRegionOwner = isUserRegionOwnerResponse === 'true';
        },
        error: function () {
            console.error('Error from is user region owner URL');
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

            hideAddItemNavbarLink();
        },
        error: function () {
            console.error('Error from user-info URL');
        }
    });
});

$(function () {
    setInterval(ajaxRegionStores, refreshRate);
    setInterval(ajaxRegionItems, refreshRate);
    ajaxRegionStores();
    ajaxRegionItems();
});

function hideAddItemNavbarLink() {
    if (!isUserRegionOwner) {
        $('#addNewItemNav').hide();
    }
}

function refreshRegionStores(regionStores) {
    const storesDiv = $('#storesDiv');

    storesDiv.empty();
    $.each(regionStores || [], function (index, store) {
        $(
            '<div class="col mb-4">' +
            '<div class="card h-100">' +
            '<div class="card-body">' +
                '<h5 class="card-title">' + store['name'] + '</h5>' +
                '<p class="card-text">' +
                    'ID: ' + store['id'] + '<br>' +
                    'Owner: ' + store['ownerUsername'] + '<br>' +
                    'Location: (' + store['location']['x'] + ', ' + store['location']['y'] + ')' + '<br>' +
                    'Orders Made: ' + store['ordersMade']['length'] + '<br>' +
                    'Income From Items Sold: $' + parseFloat(store['totalIncomeFromItems']).toFixed(2) + '<br>' +
                    'Delivery PPK: $' + parseFloat(store['deliveryPPK']).toFixed(2) + '<br>' +
                    'Income From Deliveries: $' + parseFloat(store['totalIncomeFromDeliveries']).toFixed(2) + '<br>' +
                    '</p> <br>' +
            '<h5 class="font-italic">Items For Sale</h5><br>' +
            '<div class="table-responsive">' +
            '<table id="' + 'storeItemsTable' + index + '" class="text-center table table-striped table-sm">' +
            '<thead>' +
            '</thead>' +
            '<tbody>' +
            '</tbody>' +
            '</table> </div></div></div></div>').appendTo(storesDiv);
            $(buildStoreItemsTable(store['items'], index)).appendTo(storesDiv);
    });
}

function buildStoreItemsTable(storeItems, storeIndex) {
    const storeItemsTable = $(`#storeItemsTable${storeIndex}`);

    storeItems.sort(function (item1, item2) {
        return item1['id'] - item2['id'];
    });

    storeItemsTable.empty();
    $('<tr>' +
        '<th>Item Name</th>' +
        '<th>ID</th>' +
        '<th>Purchase Form</th>' +
        '<th>Price</th>' +
        '<th>Total Purchases Amount</th>' +
        '</tr>').appendTo(storeItemsTable);

    $.each(storeItems || [], function (index, item) {
        $('<tr>' +
            '<td>' + item['name'] + '</td>' +
            '<td>' + item['id'] + '</td>' +
            '<td>' + item['purchaseForm'] + '</td>' +
            '<td>' + '$' + parseFloat(item['price']).toFixed(2) + '</td>' +
            '<td>' + parseFloat(item['purchaseAmount']).toFixed(2) + (item['purchaseForm'] === 'Weight' ? ' kg' : ' units') + '</td>' +
            '</tr>').appendTo(storeItemsTable);
    });
}

function refreshRegionItems(regionItems) {
    const itemsTable = $('#itemsTable');

    itemsTable.empty();
    $('<tr>' +
        '<th>Item Name</th>' +
        '<th>ID</th>' +
        '<th>Purchase Form</th>' +
        '<th>Stores Selling</th>' +
        '<th>Average Price</th>' +
        '<th>Total Purchases Amount</th>' +
        '</tr>').appendTo(itemsTable);

    $.each(regionItems || [], function (index, item) {
        $('<tr>' +
            '<td>' + item['name'] + '</td>' +
            '<td>' + item['id'] + '</td>' +
            '<td>' + item['purchaseForm'] + '</td>' +
            '<td>' + item['amountOfStoresSelling'] + '</td>' +
            '<td>' + '$' + parseFloat(item['averagePrice']).toFixed(2) + '</td>' +
            '<td>' + parseFloat(item['purchaseAmount']).toFixed(2) + (item['purchaseForm'] === 'Weight' ? ' kg' : ' units') + '</td>' +
            '</tr>').appendTo(itemsTable);
    });
}

function renderCustomerNavbar() {
    $('#navbarUl').append(
        '<li class="nav-item"><a class="nav-link" href="my-orders-history.html">My Orders History</a></li>' +
        '<li class="nav-item"><a class="nav-link" href="place-new-order.html">Place New Order</a></li>'
    );
}

function renderStoreOwnerNavbar() {
    $('#navbarUl').append(
        '<li class="nav-item"><a class="nav-link" href="my-stores-orders-history.html">My Stores Orders History</a></li>' +
        '<li class="nav-item"><a class="nav-link" href="my-stores-feedbacks.html">My Stores Feedbacks</a></li>' +
        '<li class="nav-item"><a class="nav-link" href="add-new-store.html">Add New Store</a></li>' +
        '<li id="addNewItemNav" class="nav-item"><a class="nav-link" href="add-new-item.html">Add New Item</a></li>');
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
