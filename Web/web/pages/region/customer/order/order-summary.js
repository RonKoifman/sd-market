const PENDING_ORDER_URL = buildUrlWithContextPath('get-pending-order');
const CONFIRM_ORDER_URL = buildUrlWithContextPath('confirm-order');
const REGION_STORES_URL = buildUrlWithContextPath('region-stores');
const LEAVE_FEEDBACK_URL = 'leave-feedback.html';
let pendingOrder;
let regionStores;

$(function () {
    $.ajax({
        url: REGION_STORES_URL,
        success: function (regionStoresResponse) {
            regionStores = regionStoresResponse;
        },
        error: function () {
            console.error('Error from get region stores URL');
        }
    });

    $.ajax({
        url: PENDING_ORDER_URL,
        success: function (pendingOrderResponse) {
            pendingOrder = pendingOrderResponse;
            buildSummaryTable();
            renderOrderedStores();
        },
        error: function () {
            console.error('Error from get pending order URL');
        }
    });
});

$(function () {
    $('#confirmOrderButton').on('click', function () {
        $.ajax({
            url: CONFIRM_ORDER_URL,
            method: 'POST',
            success: function () {
                $('.alert').addClass('alert-success').text('Order completed successfully!');
                //window.location.assign(LEAVE_FEEDBACK_URL);
            },
            error: function () {
                console.error('Error from confirm order URL');
            }
        });

        document.getElementById('confirmOrderButton').disabled = true;

        return false;
    });
});

function buildSummaryTable() {
    const summaryTable = $('#summaryTable');
    summaryTable.empty();

    $('<tr>' +
        '<th>Customer Username</th>' +
        '<th>Order Date</th>' +
        '<th>Order Destination</th>' +
        '<th>Total Items Cost</th>' +
        '<th>Total Delivery Cost</th>' +
        '<th>Total Order Cost</th>' +
        '</tr>').appendTo(summaryTable);

    $('<tr>' +
        '<td>' + pendingOrder['customerUsername'] + '</td>' +
        '<td>' + pendingOrder['orderDate'] + '</td>' +
        '<td>' + '(' + pendingOrder['orderDestination']['x'] + ', ' + pendingOrder['orderDestination']['y'] + ')' + '</td>' +
        '<td>' + '$' + parseFloat(pendingOrder['totalItemsCost']).toFixed(2) + '</td>' +
        '<td>' + '$' + parseFloat(pendingOrder['deliveryCost']).toFixed(2) + '</td>' +
        '<td>' + '$' + parseFloat(pendingOrder['totalOrderCost']).toFixed(2) + '</td>' +
        '</tr>').appendTo(summaryTable);
}

function getStoreById(storeId) {
    return regionStores.filter(store => store['id'] === storeId)[0];
}

function renderOrderedStores() {
    const storesDiv = $('#storesDiv');
    const subOrders = pendingOrder['subOrders'];

    storesDiv.empty();
    $.each(subOrders || [], function (index, subOrder) {
        let orderedStoreId = subOrder['storeId'];
        let orderedStore = getStoreById(orderedStoreId);
        $(
            '<div class="col mb-4">' +
            '<div class="card h-100">' +
            '<div class="card-body">' +
            '<h5 class="card-title">' + orderedStore['name'] + '</h5>' +
            '<p class="card-text">' +
            'ID: ' + orderedStore['id'] + '<br>' +
            'Location: (' + orderedStore['location']['x'] + ', ' + orderedStore['location']['y'] + ')' + '<br>' +
            'Distance: ' + parseFloat(subOrder['distanceFromCustomer']).toFixed(2) + ' km' + '<br>' +
            'Delivery PPK: $' + parseFloat(orderedStore['deliveryPPK']).toFixed(2) + '<br>' +
            'Total Delivery Cost: $' + parseFloat(subOrder['deliveryCost']).toFixed(2) + '<br>' +
            'Purchased Items Types: ' + subOrder['totalItemsTypes'] + '<br>' +
            'Total Items Cost: $' + parseFloat(subOrder['totalItemsCost']).toFixed(2) + '<br>' +
            '</p> <br>' +
            '<h5 class="font-italic">Ordered Items</h5><br>' +
            '<div class="table-responsive">' +
            '<table id="' + 'orderedItemsTable' + index + '" class="text-center table table-striped table-sm">' +
            '<thead>' +
            '</thead>' +
            '<tbody>' +
            '</tbody>' +
            '</table> </div></div></div></div>').appendTo(storesDiv);
        $(buildOrderedItemsTable(subOrder['orderedItems'], index)).appendTo(storesDiv);
    });
}

function buildOrderedItemsTable(orderedItems, storeIndex) {
    const orderedItemsTable = $(`#orderedItemsTable${storeIndex}`);

    orderedItems.sort(function (item1, item2) {
        return item1['item']['id'] - item2['item']['id'];
    });

    orderedItemsTable.empty();
    $('<tr>' +
        '<th>Item Name</th>' +
        '<th>ID</th>' +
        '<th>Purchase Form</th>' +
        '<th>Price</th>' +
        '<th>Purchase Amount</th>' +
        '<th>Total Price</th>' +
        '<th>From Discount</th>' +
        '</tr>').appendTo(orderedItemsTable);

    $.each(orderedItems || [], function (index, item) {
        $('<tr>' +
            '<td>' + item['item']['name'] + '</td>' +
            '<td>' + item['item']['id'] + '</td>' +
            '<td>' + item['item']['purchaseForm'] + '</td>' +
            '<td>' + '$' + parseFloat(item['itemOrderPrice']).toFixed(2) + '</td>' +
            '<td>' + parseFloat(item['quantity']).toFixed(2) + (item['item']['purchaseForm'] === 'Weight' ? ' kg' : ' units') + '</td>' +
            '<td>' + '$' + (parseFloat(item['quantity']) * parseFloat(item['itemOrderPrice'])).toFixed(2) + '</td>' +
            '<td>' + (item['isFromDiscount'] ? String.fromCharCode(10003) : String.fromCharCode(10007)) + '</td>' +
            '</tr>').appendTo(orderedItemsTable);
    });
}