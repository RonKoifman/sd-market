const OWNER_STORES_FEEDBACKS_URL = buildUrlWithContextPath('owner-stores-feedbacks');
const refreshRate = 2000;

$(function () {
    setInterval(ajaxOwnerStoresFeedbacks, refreshRate);
});

function getOwnerStoresFeedbacks() {
    return $.ajax({
        url: OWNER_STORES_FEEDBACKS_URL,
        success: function (storesFeedbacks) {
            renderStoresFeedbacks(storesFeedbacks);
        },
        error: function () {
            console.error('Error from owner stores feedbacks URL');
        }
    });
}

function setRateYoOptions() {
    $('.rateYo1').rateYo({
        rating: 1,
        readonly: true,
        hover: false
    });

    $('.rateYo2').rateYo({
        rating: 2,
        readonly: true,
        hover: false
    });

    $('.rateYo3').rateYo({
        rating: 3,
        readonly: true,
        hover: false
    });

    $('.rateYo4').rateYo({
        rating: 4,
        readonly: true,
        hover: false
    });

    $('.rateYo5').rateYo({
        rating: 5,
        readonly: true,
    });
}

$(function () {
    getOwnerStoresFeedbacks().then(() => setRateYoOptions());
});

function renderStoresFeedbacks(storesFeedbacks) {
    const storesDiv = $('#storesDiv');

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

function ajaxOwnerStoresFeedbacks() {
    $.ajax({
        url: OWNER_STORES_FEEDBACKS_URL,
        success: function (storesFeedbacks) {
            renderStoresFeedbacks(storesFeedbacks);
            setRateYoOptions();
        },
        error: function () {
            console.error('Error from owner stores feedbacks URL');
        }
    });
}