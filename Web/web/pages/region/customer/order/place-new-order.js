const REGION_STORES_URL = buildUrlWithContextPath('region-stores');
const NEW_ORDER_URL = buildUrlWithContextPath('new-order');
let regionStores;

$(function () {
    $.ajax({
        url: REGION_STORES_URL,
        success: function (regionStoresResponse) {
            regionStores = regionStoresResponse;
            const chosenStoreSelect = $('#chosenStore');

            $.each(regionStoresResponse || [], function (index, store) {
                $('<option value="' + store['id'] + '">' + store['name'] + '</option>')
                    .appendTo(chosenStoreSelect);
            });

            renderOrderPrice();
        },
        error: function () {
            console.error('Error from region stores URL');
        }
    });
});

$(function () {
   $('#x').change(function () {
      renderOrderPrice();
   });

    $('#y').change(function () {
        renderOrderPrice();
    });

    $('#chosenStore').change(function () {
        renderOrderPrice();
    });

    $('#orderType').change(function () {
        if ($("#orderType option:selected").val() === 'dynamicOrder') {
            $('#staticOrderLabels').hide();
        } else {
            $('#staticOrderLabels').show();
        }
    });
});

$(function () {
    $('#newOrderForm').submit(function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: NEW_ORDER_URL,
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

function renderOrderPrice() {
    const chosenStoreId = $("#chosenStore option:selected").val();
    const chosenStore = regionStores.filter(store => store['id'] === parseInt(chosenStoreId))[0];
    const x1 = $('#x').val();
    const y1 = $('#y').val();
    const x2 = chosenStore['location']['x'];
    const y2 = chosenStore['location']['y'];
    const deliveryPPK = chosenStore['deliveryPPK'];
    const deliveryPrice = (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) * deliveryPPK).toString();
    $('#deliveryPrice').text('$' + parseFloat(deliveryPrice).toFixed(2));
}