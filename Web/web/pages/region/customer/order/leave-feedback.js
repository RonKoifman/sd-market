const PENDING_ORDER_URL = buildUrlWithContextPath('get-pending-order');
const ADD_FEEDBACK_URL = buildUrlWithContextPath('add-feedback');
let stores;

function ajaxPendingOrder() {
    return $.ajax({
        url: PENDING_ORDER_URL,
        success: function (pendingOrder) {
            stores = pendingOrder['stores'];
            renderStoresForFeedback();
        },
        error: function () {
            console.error('Error from get pending order URL');
        }
    });
}

function setRateYoOptions() {
    $('.rateYoDiv').rateYo({
        rating: 0,
        fullStar: true
    });
}

$(function () {
    ajaxPendingOrder().then(() => setRateYoOptions());
});

function onFeedbackSubmitted(storeId) {
    let rating;
    let feedbackText;
    let feedbackDiv = $(`card-body${storeId}`);

   $.ajax({
       url: ADD_FEEDBACK_URL,
       method: 'POST',
       data: `rating=${rating}&feedback_text=${feedbackText}&store_id=${storeId}`,
       success: function () {
           $(`alert${storeId}`).addClass('alert-success').text('Thank you! Your feedback has been submitted successfully.')
       },
       error: function () {
           console.error('Error from add feedback URL');
       }
   });

   feedbackDiv.addClass('disabledButton');
}

function renderStoresForFeedback() {
    const storesDiv = $('#storesDiv');

    storesDiv.empty();
    $.each(stores || [], function (index, store) {
        $(
            '<div class="col mb-4">' +
            '<div class="card h-100">' +
            '<div id="card-body' + store['id'] + '" class="card-body">' +
            '<h5 class="card-title">' + store['name'] + '</h5>' +
            '<p class="card-text">' +
            'What rating would you give to the store?' +
            '</p>' +
            ' <div id="' + 'rateYo' + store['id'] + '" class="rateYoDiv center">' +
            '</div>' +
            '<br><div>' +
            '<textarea id="' + 'textarea' + store['id'] + '" name="text" rows="4" cols="50" placeholder="Write your opinion about the store..."></textarea>' +
            '</div>' +
            '<br> <div id="alert' + store['id'] + '" class="alert" role="alert"></div>' +
            '<br>' + '' + '<button id="' + 'buttonSubmit' + store['id'] + '" class="btn btn-primary" type="submit" onclick="onFeedbackSubmitted(this.id)">Submit Feedback</button> <br>' +
            '</div></div></div>').appendTo(storesDiv);
    });
}