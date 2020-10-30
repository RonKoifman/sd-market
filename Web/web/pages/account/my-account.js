const USER_INFO_URL = buildUrlWithContextPath('user-info');
const USER_ACCOUNT_URL = buildUrlWithContextPath('user-account');
const DEPOSIT_TRANSACTION_URL = buildUrlWithContextPath('new-deposit-transaction');
const refreshRate = 2000;

$(function () {
    setInterval(ajaxUserAccount, refreshRate);
    document.getElementById('date').setAttribute('min', getLocalDate())
});

function getLocalDate() {
    let today = new Date().toLocaleDateString();
    let todayStrings = today.split('/');
    return todayStrings[2] + '-' + todayStrings[0] + '-' + todayStrings[1];
}

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        success: function (loggedInUser) {
            $('#username').text(loggedInUser['username']);
            switch (loggedInUser['userRole']) {
                case 'Customer':
                    $('#uploadNavLink').hide();
                    break;

                case 'Store Owner':
                    $('#depositLabels').hide();
                    break;
            }
        },
        error: function () {
            console.error('Error from user-info URL')
        }
    });
});

$(function () {
    ajaxUserAccount();
});

$(function () {
    $('#formAccount').submit(function () {
        let parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            method: 'POST',
            url: DEPOSIT_TRANSACTION_URL,
            timeout: 2000,
            success: function (res) {
                $('#amount').val('');
                $('#date').val('');
                if (!$('.alert')[0]) {
                    $('#formAccount').append('<br><div class="alert alert-success" role="alert"></div>');
                }

                $('.alert').text(res);
            },
            error: function () {
                console.error('Error from new-deposit-transaction URL')
            }
        });

        return false;
    });
});

function refreshTransactionsTable(transactions) {
    const transactionsTable = $('#transactionsTable');
    const tableDiv = $('.table-responsive');

    tableDiv.find('p').empty();
    transactionsTable.empty();
    $('<tr>' +
        '<th>Transaction Type</th>' +
        '<th>Date</th>' +
        '<th>Transaction Amount</th>' +
        '<th>Balance Before</th>' +
        '<th>Balance After</th>' +
        '</tr>').appendTo(transactionsTable);

    $.each(transactions || [], function (index, transaction) {
        let classColor = transaction['transactionType'] === 'Charge' ? 'red' : 'green';
        $('<tr class="' + classColor + '">' +
            '<td>' + transaction['transactionType'] + '</td>' +
            '<td>' + transaction['date'] + '</td>' +
            '<td>' + '$' + parseFloat(transaction['amount']).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(transaction['balanceBefore']).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(transaction['balanceAfter']).toFixed(2) + '</td>' +
            '</tr>').appendTo(transactionsTable);
    });

    if (transactions.length === 0) {
        $('<p>No transactions were made yet.</p>').appendTo(tableDiv);
    }
}

function refreshBalance(balance) {
    $('#balance').text('$' + parseFloat(balance).toFixed(2));
}

function ajaxUserAccount() {
    $.ajax({
        url: USER_ACCOUNT_URL,
        success: function (account) {
            refreshTransactionsTable(account['transactions']);
            refreshBalance(account['balance']);
        },
        error: function () {
            console.error('Error from user-account URL');
        }
    });
}