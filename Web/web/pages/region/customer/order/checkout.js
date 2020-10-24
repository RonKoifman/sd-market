const PENDING_ORDER_URL = buildUrlWithContextPath('get-pending-order');
const AVAILABLE_DISCOUNTS_URL = buildUrlWithContextPath('available-discounts');
const ADD_CHOSEN_OFFERS_URL = buildUrlWithContextPath('add-chosen-offers');
const ORDER_SUMMARY_URL = 'order-summary.html';
let pendingOrder;
let subOrders;
let stores;
let availableDiscounts;
let chosenOffers = [];
let offerIndex = -1;

$(function () {
    $.ajax({
        url: PENDING_ORDER_URL,
        success: function (pendingOrderResponse) {
            pendingOrder = pendingOrderResponse;
            subOrders = pendingOrder['subOrders'];
            stores = pendingOrder['stores'];
            buildCheckoutTable();
        },
        error: function () {
            console.error('Error from get pending order URL');
        }
    })
});

$(function () {
    $.ajax({
        url: AVAILABLE_DISCOUNTS_URL,
        success: function (availableDiscountsResponse) {
            availableDiscounts = availableDiscountsResponse;
            buildDiscountsDiv();
        },
        error: function () {
            console.error('Error from get available discounts URL');
        }
    });
});

$(function () {
    $('#finishOrderButton').on('click', function () {
        if (chosenOffers.length === 0) {
            window.location.assign(ORDER_SUMMARY_URL);
        } else {
            const offersJsonString = JSON.stringify(chosenOffers);
            $.ajax({
                url: ADD_CHOSEN_OFFERS_URL,
                method: 'POST',
                data: {
                    chosen_offers: offersJsonString
                },
                success: function () {
                    window.location.assign(ORDER_SUMMARY_URL);
                },
                error: function () {
                    console.error('Error from add chosen offers URL');
                }
            });
        }
    });
});

function onDiscountSubmitted(submittedDiscountIdentifier) {
    let discountIndex = submittedDiscountIdentifier.split('_')[0];
    let submittedDiscountName = submittedDiscountIdentifier.split('_')[1];
    let selectedDiv = $(`#discountDiv${discountIndex}`);
    let button = selectedDiv.find('.btn');
    button.remove();
    $(`<img src="https://i.ibb.co/wB5VVdL/green-check.png" alt="green-check" height="64" width="64"/>`).appendTo(selectedDiv);
    selectedDiv.addClass('disabledButton');
    addChosenOffers(selectedDiv, submittedDiscountName);
}
function addChosenOffers(selectedDiv, submittedDiscountName) {
    const submittedDiscount = getDiscountByDiscountName(submittedDiscountName);

    if (submittedDiscount['discountType'] === 'ONE OF') {
        let inputs = selectedDiv.find('input');
        let selectedOfferIndex;
        for (let i = 0; i < inputs.length; i++) {
            if (inputs[i].checked) {
                selectedOfferIndex = i;
                break;
            }
        }

        let selectedOffer = (submittedDiscount['discountOffers'])[selectedOfferIndex];
        chosenOffers.push(selectedOffer);
    } else {
        (submittedDiscount['discountOffers']).forEach(offer => chosenOffers.push(offer));
    }
}

function getDiscountByDiscountName(discountName) {
    return availableDiscounts.filter(discount => discount['name'] === discountName)[0];
}

function buildDiscountsDiv() {
    const discountsDiv = $('#discountsDiv');

    discountsDiv.empty();
    $.each(availableDiscounts || [], function (index, discount) {
        $(
            '<div class="col mb-4">' +
            '<div class="card h-100">' +
            '<div id="' + 'discountDiv' + index + '" class="card-body">' +
            '<h5 class="card-title">' + discount['name'] + '</h5>' +
            '<p class="card-text">' +
            'CHOOSE ' + (discount['discountType'] === 'IRRELEVANT' ? 'ALL OR NOTHING' : discount['discountType']) + '<br>' +
            buildOfferDivs(discount) +
            '</p> <br>' +
             '<button id="' + index + '_' + discount['name'] + '" class="btn btn-primary" type="submit" onclick="onDiscountSubmitted(this.id)">Apply Discount</button> <br>' +
            '</div></div></div>').appendTo(discountsDiv);
    });

    if (availableDiscounts.length === 0) {
        $('#discountsHeader').remove();
    }
}

function buildOfferDivs(discount) {
    let offerDivs = '';
    offerIndex++;

    $.each((discount['discountOffers']) || [], function (index, offer) {
        let offerDiv = buildOfferDiv(discount, offer, index);
        offerDivs = offerDivs.concat(offerDiv);
    })

    return offerDivs;
}

function buildOfferDiv(discount, offer, index) {
   let discountName = discount['Name'];
   let discountType = discount['discountType'];
   let offerIdentifier = `${discountName}#${index}`;
   let offerDiv;

   if (discountType === 'ONE OF') {
       offerDiv = '<div class="form-check form-check-inline">' +
       '<input class="form-check-input" type="radio" id="' + offerIdentifier + '" value="' + offerIdentifier + '" name="' + offerIndex + '"' + (index === 0 ? 'checked' : '') + '>' +
       '<label class="form-check-label" for="' + offerIdentifier + '">' +
       parseFloat(offer['quantity']).toFixed(2) + (offer['item']['purchaseForm'] === 'Weight' ? ' kg of ' : ' units of ') + offer['item']['name'] + ' for ' + (parseFloat(offer['itemOfferPrice']) === 0 ? 'free' : '$' + parseFloat(offer['itemOfferPrice']).toFixed(2)) +
       '</label>' +
       '</div>';
   } else {
       offerDiv = '<div class="form-check form-check-inline">' +
           '<p> &raquo; ' + parseFloat(offer['quantity']).toFixed(2) + (offer['item']['purchaseForm'] === 'Weight' ? ' kg of ' : ' units of ') + offer['item']['name'] + ' for ' + (parseFloat(offer['itemOfferPrice']) === 0 ? 'free' : '$' + parseFloat(offer['itemOfferPrice']).toFixed(2)) + '</p>' +
           '</div>';
   }

   return offerDiv;
}

function getOrderByStoreId(storeId) {
    let filtered = subOrders.filter(order => order['store']['id'] === storeId);

    return filtered === null ? null : filtered[0];
}

function buildCheckoutTable() {
    const checkoutTable = $('#checkoutTable');
    checkoutTable.empty();

    $('<tr>' +
        '<th>Ordered Store</th>' +
        '<th>ID</th>' +
        '<th>Location</th>' +
        '<th>Distance</th>' +
        '<th>Delivery PPK</th>' +
        '<th>Total Delivery Cost</th>' +
        '<th>Purchased Items Types</th>' +
        '<th>Total Items Cost</th>' +
        '</tr>').appendTo(checkoutTable);

    $.each(stores || [], function (index, store) {
        let storeOrder = getOrderByStoreId(store['id']);
        $('<tr>' +
            '<td>' + store['name'] + '</td>' +
            '<td>' + store['id'] + '</td>' +
            '<td>' + '(' + store['location']['x'] + ', ' + store['location']['y'] + ')' + '</td>' +
            '<td>' + parseFloat(storeOrder['distanceFromCustomer']).toFixed(2) + ' km' + '</td>' +
            '<td>' + '$' + parseFloat(store['deliveryPPK']).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(storeOrder['deliveryCost']).toFixed(2)  + '</td>' +
            '<td>' + storeOrder['totalItemsTypes'] + '</td>' +
            '<td>' + '$' + parseFloat(storeOrder['totalItemsCost']).toFixed(2) + '</td>' +
            '</tr>').appendTo(checkoutTable);
    });
}