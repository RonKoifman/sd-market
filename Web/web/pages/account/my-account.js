const USER_INFO_URL = buildUrlWithContextPath("user-info");
const USER_ACCOUNT_URL = buildUrlWithContextPath("user-account");
const refreshRate = 2000;

$(function () {
    setInterval(ajaxUserAccount, refreshRate);
});

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        success: function (loggedInUser) {
            $('#username').text(loggedInUser.username);
            switch (loggedInUser.userRole) {
                case 'Customer':
                    $('#uploadNavLink').hide();
                    break;
                case 'Store Owner':
                    $('#depositLabels').hide();
            }
        },
        error: function () {
            console.error('Error from user-info URL')
        }
    })
});

$(function () {
    $.ajax({
        url: USER_ACCOUNT_URL,
        success: function (account) {
            refreshBalance(account.balance)
            refreshTransactionsTable(account.transactions);
        },
        error: function () {
            console.error('Error from user-account URL')
        }
    })
});

$(function () {
    $('#formAccount').submit(function () {
        let parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            method: 'POST',
            url: this.action,
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
    transactionsTable.empty();

    $('<tr>' +
        '<th>Transaction Type</th>' +
        '<th>Date</th>' +
        '<th>Transaction Amount</th>' +
        '<th>Balance Before</th>' +
        '<th>Balance After</th>' +
        '</tr>').appendTo(transactionsTable);

    $.each(transactions || [], function (index, transaction) {
        $('<tr>' +
            '<td>' + transaction.transactionType + '</td>' +
            '<td>' + transaction.date + '</td>' +
            '<td>' + '$' + parseFloat(transaction.amount).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(transaction.balanceBefore).toFixed(2) + '</td>' +
            '<td>' + '$' + parseFloat(transaction.balanceAfter).toFixed(2) + '</td>' +
            '</tr>').appendTo(transactionsTable);
    });
}

function refreshBalance(balance) {
    $('#balance').text('$' + parseFloat(balance).toFixed(2));
}

function ajaxUserAccount() {
    $.ajax({
        url: USER_ACCOUNT_URL,
        success: function (account) {
            refreshTransactionsTable(account.transactions);
            refreshBalance(account.balance);
        },
        error: function () {
            console.error('Error from user-account URL')
        }
    });
}