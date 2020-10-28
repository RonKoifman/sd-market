const CUSTOMER_ORDERS_URL = buildUrlWithContextPath('customer-orders-history');
let customerOrders;

$(function () {
    ajaxCustomerOrders();
});

function getItemSellingStoreNameByStoreId(selectedOrder, storeId) {
    const store = selectedOrder['stores'].filter(store => store['id'] === storeId)[0];

    return store['name'];
}

function onOrderClicked(orderId) {
    const selectedOrder = customerOrders.filter(order => order['id'] === parseInt(orderId))[0];
    const orderedItems = selectedOrder['orderedItems'];
    const itemsTable = $('#itemsTable');

    itemsTable.empty();
    $('<tr>' +
        '<th>Item Name</th>' +
        '<th>ID</th>' +
        '<th>Purchase Form</th>' +
        '<th>Store Name</th>' +
        '<th>Store ID</th>' +
        '<th>Price</th>' +
        '<th>Purchase Amount</th>' +
        '<th>Total Price</th>' +
        '<th>From Discount Offer</th>' +
        '</tr>').appendTo(itemsTable);

    $.each(orderedItems || [], function (index, item) {
        let sellingStoreId = item['item']['sellingStoreId'];
        let sellingStoreName = getItemSellingStoreNameByStoreId(selectedOrder, sellingStoreId);

        $('<tr>' +
            '<td>' + item['item']['name'] + '</td>' +
            '<td>' + item['item']['id'] + '</td>' +
            '<td>' + item['item']['purchaseForm'] + '</td>' +
            '<td>' + sellingStoreName + '</td>' +
            '<td>' + sellingStoreId + '</td>' +
            '<td>' + '$' + parseFloat(item['itemOrderPrice']).toFixed(2) + '</td>' +
            '<td>' + parseFloat(item['quantity']).toFixed(2) + (item['item']['purchaseForm'] === 'Weight' ? ' kg' : ' units') + '</td>' +
            '<td>' + '$' + (parseFloat(item['quantity']) * parseFloat(item['itemOrderPrice'])).toFixed(2) + '</td>' +
            '<td>' + (item['isFromDiscount'] ? String.fromCharCode(10003) : String.fromCharCode(10007)) + '</td>' +
            '</tr>').appendTo(itemsTable);
    });

    $('#orderModal').modal('show');
}

function renderCustomerOrders() {
    const ordersTable = $('#ordersTable');
    const tableDiv = $('.table-responsive');

    tableDiv.find('p').empty();
    ordersTable.empty();
    $('<tr>' +
        '<th>Order ID</th>' +
        '<th>Order Date</th>' +
        '<th>Order Destination</th>' +
        '<th>Stores Participated</th>' +
        '<th>Items Amount</th>' +
        '<th>Items Types</th>' +
        '<th>Total Items Cost</th>' +
        '<th>Total Delivery Cost</th>' +
        '<th>Total Order Cost</th>' +
        '</tr>').appendTo(ordersTable);

    $.each(customerOrders || [], function (index, order) {
        $('<tr class="hover-pointer" id="' + order['id'] + '" onclick="onOrderClicked(this.id)">' +
            '<td>' + order['id'] + '</td>' +
            '<td>' + order['orderDate'] + '</td>' +
            '<td>' + '(' + order['orderDestination']['x'] + ', ' + order['orderDestination']['y'] + ')' + '</td>' +
            '<td>' + order['stores'].length + '</td>' +
            '<td>' + order['totalItemsAmount'] + '</td>' +
            '<td>' + order['totalItemsTypes'] + '</td>' +
            '<td>' + '$' + parseFloat(order['totalItemsCost']).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(order['deliveryCost']).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(order['totalOrderCost']).toFixed(2) + '</td>' +
            '</tr>').appendTo(ordersTable);
    });

    if (customerOrders.length === 0) {
        $('<p>No orders have been placed yet.</p>').appendTo(tableDiv);
    }
}

function ajaxCustomerOrders() {
    $.ajax({
        url: CUSTOMER_ORDERS_URL,
        success: function (customerOrdersResponse) {
            customerOrders = customerOrdersResponse;
            renderCustomerOrders();
        },
        error: function () {
            console.error('Error from customer orders history URL');
        }
    });
}