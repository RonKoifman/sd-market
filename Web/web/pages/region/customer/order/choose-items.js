const ORDER_TYPE_AND_STORE_URL = buildUrlWithContextPath('order-type-and-store');
const REGION_ITEMS_URL = buildUrlWithContextPath('region-items');
const CREATE_PENDING_ORDER_URL = buildUrlWithContextPath('create-pending-order');
let chosenStore;
let orderType;
let regionItems;

function ajaxOrderTypeAndStore() {
    return $.ajax({
        url: ORDER_TYPE_AND_STORE_URL,
        success: function (jsonObject) {
            orderType = jsonObject['orderType'];
            chosenStore = jsonObject['chosenStore'];
        },
        error: function () {
            console.error('Error from order type and store URL');
        }
    });
}

function ajaxRegionItems() {
    $.ajax({
        url: REGION_ITEMS_URL,
        success: function (regionItemsResponse) {
            regionItems = regionItemsResponse;
            buildCartTable(regionItems);
        },
        error: function () {
            console.error('Error from region items URL');
        }
    });
}

$(function () {
   ajaxOrderTypeAndStore().then(() => ajaxRegionItems());
});

$(function () {
    $('#chooseItemsForm').submit(function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: CREATE_PENDING_ORDER_URL,
            method: 'POST',
            timeout: 2000,
            success: function (res) {
                window.location.assign(res);
            },
            error: function (res) {
                $('.alert').addClass('alert-danger').text(res.responseText);
            }
        });

        return false;
    });
});

function buildCartTable(regionItems) {
   const cartTable = $('#cartTable');

   cartTable.empty();
   $('<tr>' +
       '<th>Item Name</th>' +
       '<th>ID</th>' +
       '<th>Purchase Form</th>' +
       (orderType === 'dynamicOrder' ? '' : '<th>Price</th>') +
       '<th>Purchase Quantity</th>' +
       '</tr>').appendTo(cartTable);

   $.each(regionItems || [], function (index, item) {
      $('<tr>' +
          '<td>' + item['name'] + '</td>' +
          '<td>' + item['id'] + '</td>' +
          '<td>' + item['purchaseForm'] + '</td>' +
          (orderType === 'dynamicOrder' ? '' : '<td>' + getItemPriceInStoreTableData(item['id']) + '</td>') +
          getQuantityTableData(item['purchaseForm'], item['id']) +
          '</tr>').appendTo(cartTable);
   });
}

function getItemInStoreById(itemId) {
    const storeItems = chosenStore['items'];
    const filteredItems = storeItems.filter(item => item['id'] === itemId);

    return filteredItems.length > 0 ? filteredItems[0] : null;
}

function getItemPriceInStoreTableData(itemId) {
    const item = getItemInStoreById(itemId);
    const itemPrice = item === null ? null : item['price'];

    return itemPrice === null ? 'Not Available' : '$' + parseFloat(itemPrice).toFixed(2);
}

function getQuantityTableData(itemPurchaseForm, itemId) {
    let quantityTableData = '';

    if (orderType === 'dynamicOrder' || getItemInStoreById(itemId) !== null) {
        switch (itemPurchaseForm) {
            case 'Quantity':
                quantityTableData = '<td><input id="idToQuantity" name="' + itemId + '" type="number" min="1" max="1000" class="form-control" placeholder="Units"></td>';
                break;

            case 'Weight':
                quantityTableData = '<td><input id="idToQuantity" name="' + itemId + '" type="number" min="0.1" step="0.05" max="1000" class="form-control" placeholder="Kilograms"></td>';
                break;
        }
    } else {
        quantityTableData = '<td>Not Available</td>';
    }

    return quantityTableData;
}