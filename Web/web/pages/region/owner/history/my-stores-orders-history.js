const OWNER_STORE_T0_ORDERS_URL = buildUrlWithContextPath('owner-owned-stores');
const refreshRate = 2000;
let ownedStores;
let chosenStoreOrdersMade;
let areStoresDetailsRendered = false;

$(function () {
    $('#chosenStore').change(function () {
        renderStoreOrders();
    });

    setInterval(ajaxOwnerStores, refreshRate);
    ajaxOwnerStores();
});

function onOrderClicked(orderId) {
    const selectedOrder = chosenStoreOrdersMade.filter(order => order['id'] === parseInt(orderId))[0];
    const orderedItems = selectedOrder['orderedItems'];
    const itemsTable = $('#itemsTable');

    itemsTable.empty();
    $('<tr>' +
        '<th>Item Name</th>' +
        '<th>ID</th>' +
        '<th>Purchase Form</th>' +
        '<th>Price</th>' +
        '<th>Purchase Amount</th>' +
        '<th>Total Price</th>' +
        '<th>From Discount Offer</th>' +
        '</tr>').appendTo(itemsTable);

    $.each(orderedItems || [], function (index, item) {
        $('<tr>' +
            '<td>' + item['item']['name'] + '</td>' +
            '<td>' + item['item']['id'] + '</td>' +
            '<td>' + item['item']['purchaseForm'] + '</td>' +
            '<td>' + '$' + parseFloat(item['itemOrderPrice']).toFixed(2) + '</td>' +
            '<td>' + parseFloat(item['quantity']).toFixed(2) + (item['item']['purchaseForm'] === 'Weight' ? ' kg' : ' units') + '</td>' +
            '<td>' + '$' + (parseFloat(item['quantity']) * parseFloat(item['itemOrderPrice'])).toFixed(2) + '</td>' +
            '<td>' + (item['isFromDiscount'] ? String.fromCharCode(10003) : String.fromCharCode(10007)) + '</td>' +
            '</tr>').appendTo(itemsTable);
    });

    $('#orderModal').modal('show');
}

function getStoreById(storeId) {
    return ownedStores.filter(store => store['id'] === storeId)[0];
}

function renderStoreOrders() {
    const chosenStore = getStoreById(parseInt($('#chosenStore option:selected').val()));
    const ordersTable = $('#ordersTable');
    const tableDiv = $('.table-responsive');

    $('#currentStoreName').text(chosenStore['name']);
    chosenStoreOrdersMade = chosenStore['ordersMade'];
    tableDiv.find('p').empty();
    ordersTable.empty();
    $('<tr>' +
        '<th>Order ID</th>' +
        '<th>Date</th>' +
        '<th>Customer Username</th>' +
        '<th>Destination</th>' +
        '<th>Items Amount</th>' +
        '<th>Items Types</th>' +
        '<th>Total Items Cost</th>' +
        '<th>Total Delivery Cost</th>' +
        '<th>Total Order Cost</th>' +
        '</tr>').appendTo(ordersTable);

    $.each(chosenStoreOrdersMade || [], function (index, order) {
        $('<tr class="hover-pointer" id="' + order['id'] + '" onclick="onOrderClicked(this.id)">' +
            '<td>' + order['id'] + '</td>' +
            '<td>' + order['orderDate'] + '</td>' +
            '<td>' + order['customerUsername'] + '</td>' +
            '<td>' + '(' + order['orderDestination']['x'] + ', ' + order['orderDestination']['y'] + ')' + '</td>' +
            '<td>' + order['totalItemsAmount'] + '</td>' +
            '<td>' + order['totalItemsTypes'] + '</td>' +
            '<td>' + '$' + parseFloat(order['totalItemsCost']).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(order['deliveryCost']).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(order['totalOrderCost']).toFixed(2) + '</td>' +
            '</tr>').appendTo(ordersTable);
    });

    if (chosenStoreOrdersMade.length === 0) {
        $('<p>No orders have been placed yet.</p>').appendTo(tableDiv);
    }
}

function renderOwnerStores() {
    if (ownedStores.length === 0) {
        const hasStoreDiv = $('#hasStoreDiv');
        hasStoreDiv.empty();
        $('<br> <p class="bigger-font text-center">No owned stores have been added yet.</p>').appendTo(hasStoreDiv);
    } else {
        if (!areStoresDetailsRendered) {
            const chosenStoreSelect = $('#chosenStore');

            chosenStoreSelect.empty();
            $.each(ownedStores || [], function (index, store) {
                $('<option value="' + store['id'] + '">' + store['name'] + ' | ID: ' + store['id'] + '</option>')
                    .appendTo(chosenStoreSelect);
            });

            areStoresDetailsRendered = true;
        }

        renderStoreOrders();
    }
}

function ajaxOwnerStores() {
    $.ajax({
        url: OWNER_STORE_T0_ORDERS_URL,
        success: function (ownedStoresResponse) {
            ownedStores = ownedStoresResponse;
            renderOwnerStores();
        },
        error: function () {
            console.error('Error from get owner owned stores URL');
        }
    });
}