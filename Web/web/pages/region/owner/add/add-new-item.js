const ADD_NEW_ITEM_URL = buildUrlWithContextPath('add-new-item');
const OWNER_STORES_URL = buildUrlWithContextPath('owner-owned-stores');
let ownerStores;

$(function () {
    $.ajax({
        url: OWNER_STORES_URL,
        success: function (ownerStoresResponse) {
            ownerStores = ownerStoresResponse;
            buildStoresTable(ownerStores);
        },
        error: function () {
            console.error('Error from owner owned stores URL');
        }
    });
});

$(function () {
    $('#newItemForm').submit(function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: ADD_NEW_ITEM_URL,
            method: 'POST',
            timeout: 2000,
            success: function () {
                $('.alert').removeClass('alert-danger').text('');
                $('.alert').addClass('alert-success').text('Your new item was added successfully!');
                $('#newItemDiv').addClass('disabled-button');
            },
            error: function (res) {
                $('.alert').addClass('alert-danger').text(res.responseText);
            }
        });

        return false;
    });
});

function getPriceTableData(itemId) {
    return '<td><input id="idToPrice" name="' + 'store_id' + itemId + '" type="number" min="0.1" step="0.01" class="form-control" placeholder="Price in Store"></td>';
}

function buildStoresTable() {
    const storesTable = $('#storesTable');

    storesTable.empty();
    $('<tr>' +
        '<th>Store Name</th>' +
        '<th>ID</th>' +
        '<th>Price in Store</th>' +
        '</tr>').appendTo(storesTable);

    $.each(ownerStores || [], function (index, store) {
        $('<tr>' +
            '<td>' + store['name'] + '</td>' +
            '<td>' + store['id'] + '</td>' +
            getPriceTableData(store['id']) +
            '</tr>').appendTo(storesTable);
    });
}