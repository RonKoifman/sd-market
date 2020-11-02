const OWNER_OWNED_STORES_URL = buildUrlWithContextPath('owner-owned-stores');
const IS_REGION_OWNER_URL = buildUrlWithContextPath('is-region-owner');
const refreshRate = 2000;

$(function () {
    setInterval(ajaxOwnerOwnedStores, refreshRate);
});

$(function () {
    $.ajax({
        url: IS_REGION_OWNER_URL,
        success: function (isUserRegionOwnerResponse) {
            if (isUserRegionOwnerResponse === 'true') {
                $('#addNewItemNav').removeAttr('hidden');
            }
        },
        error: function () {
            console.error('Error from is user region owner URL');
        }
    });
});

function collectStoresFeedbacks(stores) {
    let feedbacks = [];

    for (let store of stores) {
        for (let feedback of store['feedbacksReceived']) {
            feedbacks.push(feedback);
        }
    }

    return feedbacks;
}

function getOwnerOwnedStores() {
    return $.ajax({
        url: OWNER_OWNED_STORES_URL,
        success: function (ownerStores) {
            renderStoresFeedbacks(ownerStores);
        },
        error: function () {
            console.error('Error from owner owned stores URL');
        }
    });
}

function setRateYoOptions() {
    $('.rateYo1').rateYo({
        rating: 1,
        readonly: true,
    });

    $('.rateYo2').rateYo({
        rating: 2,
        readonly: true,
    });

    $('.rateYo3').rateYo({
        rating: 3,
        readonly: true,
    });

    $('.rateYo4').rateYo({
        rating: 4,
        readonly: true,
    });

    $('.rateYo5').rateYo({
        rating: 5,
        readonly: true,
    });
}

$(function () {
    getOwnerOwnedStores().then(() => setRateYoOptions());
});

function renderStoresFeedbacks(stores) {
    const storesDiv = $('#storesDiv');
    const storesFeedbacks = collectStoresFeedbacks(stores);

    storesDiv.empty();
    if (storesFeedbacks.length === 0) {
        $('<div><p class="bigger-font">' +
        'No feedback has been received yet.' + '</p></div>').appendTo(storesDiv);
    } else {
        $.each(storesFeedbacks || [], function (index, feedback) {
            $(
                '<div class="col mb-4">' +
                '<div class="card h-100">' +
                '<div id="card-body' + '" class="card-body disable-hover">' +
                '<h5 class="card-title">' + feedback['storeName'] + '</h5>' +
                '<p class="card-text bigger-font">' +
                feedback['orderDate'] +
                '</p>' +
                ' <div class="' + 'rateYo' + feedback['rating'] + ' center">' +
                '</div>' +
                '<br><div>' +
                (feedback['feedbackText'] === '' ? '' :
                '<p class="lead">' + String.fromCharCode(34) + feedback['feedbackText'] + String.fromCharCode(34) + '</p>') +
                '</div>' +
                '<p class="card-text bigger-font font-italic">' + '- ' +
                feedback['username'] +
                '</div></div></div>').appendTo(storesDiv);
        });
    }
}

function ajaxOwnerOwnedStores() {
    $.ajax({
        url: OWNER_OWNED_STORES_URL,
        success: function (ownerStores) {
            renderStoresFeedbacks(ownerStores);
            setRateYoOptions();
        },
        error: function () {
            console.error('Error from owner owned stores URL');
        }
    });
}