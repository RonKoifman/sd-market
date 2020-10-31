const REGION_STORES_URL = buildUrlWithContextPath('region-stores');
const NEW_ORDER_URL = buildUrlWithContextPath('set-order-details');
let regionStores;

$(function () {
    $.ajax({
        url: REGION_STORES_URL,
        success: function (regionStoresResponse) {
            regionStores = regionStoresResponse;
            const chosenStoreSelect = $('#chosenStore');

            $.each(regionStoresResponse || [], function (index, store) {
                $('<option value="' + store['id'] + '">' + store['name'] + ' | ID: ' + store['id'] + ' | Location: (' + store['location']['x'] + ', ' + store['location']['y'] + ')' + '</option>')
                    .appendTo(chosenStoreSelect);
            });

            renderDeliveryPrice();
        },
        error: function () {
            console.error('Error from region stores URL');
        }
    });
});

function getLocalDate() {
    let today = new Date().toLocaleDateString();
    let todayStrings = today.split('/');
    let month = todayStrings[0].length === 1 ? '0' + todayStrings[0] : todayStrings[0];
    let day = todayStrings[1].length === 1 ? '0' + todayStrings[1] : todayStrings[1];
    let year = todayStrings[2];

    return year + '-' + month + '-' + day;
}

$(function () {
    document.getElementById('orderDate').setAttribute('min', getLocalDate())

   $('#x').change(function () {
      renderDeliveryPrice();
   });

    $('#y').change(function () {
        renderDeliveryPrice();
    });

    $('#chosenStore').change(function () {
        renderDeliveryPrice();
    });

    $('#orderType').change(function () {
        toggleStaticOrderLabels();
    });

    toggleStaticOrderLabels();
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

function toggleStaticOrderLabels() {
    if ($('#orderType option:selected').val() === 'dynamicOrder') {
        $('#staticOrderLabels').hide();
    } else {
        $('#staticOrderLabels').show();
    }
}

function renderDeliveryPrice() {
    const x1 = $('#x').val();
    const y1 = $('#y').val();

    if (!x1 || !y1) {
        $('#deliveryPrice').text('$0.00');
    } else {
        const storeId = $('#chosenStore option:selected').val();
        const chosenStore = regionStores.filter(store => store['id'] === parseInt(storeId))[0];
        const x2 = chosenStore['location']['x'];
        const y2 = chosenStore['location']['y'];
        const deliveryPPK = chosenStore['deliveryPPK'];
        const deliveryPrice = (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) * deliveryPPK).toString();
        $('#deliveryPrice').text('$' + parseFloat(deliveryPrice).toFixed(2));
    }
}