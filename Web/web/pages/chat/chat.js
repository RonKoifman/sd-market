const USER_INFO_URL = buildUrlWithContextPath('user-info');
const SEND_MESSAGE_URL = buildUrlWithContextPath('send');
const GET_CHAT_URL = buildUrlWithContextPath('chat');
const RESET_CHAT_VERSION_URL = buildUrlWithContextPath('reset-chat-version');
let isDarker = true;
const refreshRate = 1000;

$(function () {
    $.ajax({
        url: USER_INFO_URL,
        success: function (loggedInUser) {
            switch (loggedInUser['userRole']) {
                case 'Customer':
                    $('#uploadNavLink').hide();
                    break;
            }
        },
        error: function () {
            console.error('Error from user-info URL');
        }
    });
});

$(function () {
    setInterval(ajaxChatEntries, refreshRate);
    resetChatVersion();
    ajaxChatEntries();
});

$(function () {
    $('#sendButton').on('click', function () {
        onSendClicked($('#userMessage').val());
    });
});

function onSendClicked(userMessage) {
    $('#userMessage').val('');
    $.ajax({
        method: 'POST',
        data: `user_message=${userMessage}`,
        url: SEND_MESSAGE_URL,
        success: function () {
        },
        error: function () {
        }
    });
}

function showChat(newChatEntries) {
    let chatEntries = $('#chatEntries');

    $.each(newChatEntries || [], function (index, chatEntry) {
        $(getChatEntryDiv() +
        '<img src="common/images/chat-person.png" alt="">' +
        '<br><span class="name-left">' + chatEntry['username'] + '</span><br><br>' +
        '<p>' + chatEntry['message'] + '</p>' +
        '<span class="time-right">' + chatEntry['time'] + '</span>' +
        '</div>').appendTo(chatEntries);
    });
}

function getChatEntryDiv() {
    isDarker = !isDarker;
    return isDarker ? '<div class="chat-container darker">' : '<div class="chat-container">';
}

function resetChatVersion() {
    $.ajax({
        url: RESET_CHAT_VERSION_URL,
        method: 'POST',
        success: function () {
        },
        error: function () {
        }
    });
}

function ajaxChatEntries() {
    $.ajax({
        url: GET_CHAT_URL,
        success: function (newChatEntries) {
            if (newChatEntries != null) {
                showChat(newChatEntries);
            }
        },
        error: function () {
        }
    });
}