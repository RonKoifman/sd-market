const ADD_NEW_STORE_URL = buildUrlWithContextPath('add-new-store');
const REGION_ITEMS_URL = buildUrlWithContextPath('region-items');
let regionItems;

$(function () {
    $.ajax({
        url: REGION_ITEMS_URL,
        success: function (regionItemsResponse) {
            regionItems = regionItemsResponse;
            buildItemsTable(regionItems);
        },
        error: function () {
            console.error('Error from region items URL');
        }
    });
});

$(function () {
    $('#chooseStoreItemsForm').submit(function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: ADD_NEW_STORE_URL,
            method: 'POST',
            timeout: 2000,
            success: function () {
                $('.alert').removeClass('alert-danger').text('');
                $('.alert').addClass('alert-success').text('Your new store was added successfully!');
                document.getElementById('submitButton').disabled = true;
                $('.items-table').addClass('disabledButton');
            },
            error: function (res) {
                $('.alert').addClass('alert-danger').text(res.responseText);
            }
        });

        return false;
    });
});

function getPriceTableData(itemId) {
    return '<td><input id="idToPrice" name="' + itemId + '" type="number" min="0.1" step="0.01" class="form-control" placeholder="Price in Store"></td>';
}

function buildItemsTable() {
    const itemsTable = $('#itemsTable');

    itemsTable.empty();
    $('<tr>' +
        '<th>Item Name</th>' +
        '<th>ID</th>' +
        '<th>Purchase Form</th>' +
        '<th>Price in Store</th>' +
        '</tr>').appendTo(itemsTable);

    $.each(regionItems || [], function (index, item) {
        $('<tr>' +
            '<td>' + item['name'] + '</td>' +
            '<td>' + item['id'] + '</td>' +
            '<td>' + item['purchaseForm'] + '</td>' +
            getPriceTableData(item['id']) +
            '</tr>').appendTo(itemsTable);
    });
}