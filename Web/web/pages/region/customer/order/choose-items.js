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
        const regex = /[0-9]/g;
        const found = parameters.match(regex);

        if (!found) {
            $('.alert').addClass('alert-danger').text('Please select at least one item to purchase before continue to checkout.');
        } else {
            $.ajax({
                data: parameters,
                url: CREATE_PENDING_ORDER_URL,
                method: 'POST',
                timeout: 2000,
                success: function (res) {
                    window.location.assign(res);
                },
                error: function () {
                    console.error('Error from create pending order URL');
                }
            });
        }

        return false;
    });
});

function buildCartTable(regionItems) {
   const cartTable = $('#cartTable');

   cartTable.empty();
   $('<tr>' +
       '<th>Name</th>' +
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

    return filteredItems.length > 0 ? filteredItems[0] : undefined;
}

function getItemPriceInStoreTableData(itemId) {
    const item = getItemInStoreById(itemId);
    const itemPrice = item === undefined ? undefined : item['price'];

    return itemPrice === undefined ? 'Not Available' : '$' + parseFloat(itemPrice).toFixed(2);
}

function getQuantityTableData(itemPurchaseForm, itemId) {
    let quantityTableData = '';

    if (orderType === 'dynamicOrder' || getItemInStoreById(itemId) !== undefined) {
        switch (itemPurchaseForm) {
            case 'Quantity':
                quantityTableData = '<td><input id="quantity" name="quantity" type="number" min="1" max="1000" class="form-control" placeholder="Quantity"></td>';
                break;

            case 'Weight':
                quantityTableData = '<td><input id="quantity" name="quantity" type="number" min="1" step="0.1" max="1000" class="form-control" placeholder="Quantity"></td>';
                break;
        }
    } else {
        quantityTableData = '<td><input id="quantity" name="quantity" type="number" min="1" step="0.1" max="1000" class="text-hide"></td>';
    }

    return quantityTableData;
}