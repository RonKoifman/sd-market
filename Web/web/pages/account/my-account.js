const USER_INFO_URL = buildUrlWithContextPath("user-info");
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
        }
    })
});

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        success: function (user) {
            refreshBalance(user.account.balance)
            refreshTransactionsTable(user.account.transactions);
        }
    })
});

$(function () {
    $('#formAccount').submit(function () {
        const parameters = $(this).serialize();

        $.ajax({
            data: parameters,
            url: this.action,
            timeout: 2000,
            success: function (res) {
                $('#amount').val('');
                $('#date').val('');
                if (!$('.alert')[0]) {
                    $('#formAccount').append('<br><div class="alert alert-success" role="alert"></div>');
                }
                $('.alert').text(res);
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
        url: USER_INFO_URL,
        success: function (user) {
            refreshTransactionsTable(user.account.transactions);
            refreshBalance(user.account.balance);
        }
    });
}