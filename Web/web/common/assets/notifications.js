const USER_NOTIFICATIONS_URL = buildUrlWithContextPath('user-notifications');
const notificationsRefreshRate = 2000;

$(function () {
    setInterval(ajaxUserNotifications, notificationsRefreshRate);
    $(document).on('hidden.bs.modal', function () {
        let notificationModals = $('.notification-modals').toArray();
        for (let notificationModal of notificationModals) {
            if (!notificationModal.classList.contains('show')) {
                notificationModal.remove();
            }
        }
    });
});

function popNewNotificationModal(userUnseenNotifications) {
    $.each(userUnseenNotifications || [], function (index, notification) {
        $(
        '<div class="modal notification-modals" tabindex="-1">' +
            '<div class="modal-dialog">' +
                '<div class="modal-content">' +
                    '<div class="modal-header">' +
                        '<h5 class="modal-title">' + notification['title'] + '</h5>' +
                        '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                            '<span aria-hidden="true">&times;</span>' +
                        '</button>' +
                    '</div>' +
                    '<div class="modal-body">' +
                    '<img class="mb-1" src="common/images/notification.png" height="44" width="44" alt="">' +
                        '<p>' + notification['date'] + '</p>' +
                        '<p>' + notification['message'] + '</p>' +
                    '</div>' +
                    '<div class="modal-footer">' +
                        '<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>' +
                    '</div>' +
                '</div>' +
            '</div>' +
        '</div>').appendTo($(document.body));
        $('.notification-modals').modal('show');
    });
}

function ajaxUserNotifications() {
    $.ajax({
        url: USER_NOTIFICATIONS_URL,
        success: function (userUnseenNotifications) {
            if (userUnseenNotifications !== null) {
                popNewNotificationModal(userUnseenNotifications);
            }
        },
        error: function () {
        }
    });
}