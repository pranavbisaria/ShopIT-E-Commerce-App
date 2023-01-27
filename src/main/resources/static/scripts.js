let stompClient = null;
let notificationCount = 0;
$(document).ready(function() {
    console.log("Index page is ready");
    connect();
    $("#notifications").click(function() {
        resetNotificationCount();
    });
});
function connect() {
    const socket = new SockJS('/shopIt');
    //                or
    // const socket = new SockJS('https://www.shopitanywhere.live/shopIt');
    stompClient = Stomp.over(socket);
    //                  or
    // stompClient = Stomp.over('ws://localhost/shopIt');
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        updateNotificationDisplay();
        stompClient.subscribe('/topic/globalNotifications', function (message) {
            notificationCount = notificationCount + 1;
            let notificationObj = JSON.parse(message.body);
            showMessage('<b>'+notificationObj.head+':</b> '+notificationObj.body);
            updateNotificationDisplay();
        });
        let id = Math.floor(Math.random()*1000000000);
        showMessage("User Logged In using the ID: "+id);
        stompClient.subscribe('/topic/privateNotifications/'+id, function (message) {
            notificationCount = notificationCount + 1;
            let notificationObj = JSON.parse(message.body);
            showMessage('<b>'+notificationObj.head+':</b> '+notificationObj.body);
            updateNotificationDisplay();
        });
    });
}
function showMessage(body) {
    $("#notification").append("<tr><td>" + body + "</td></tr>");
}
function updateNotificationDisplay() {
    if (notificationCount === 0) {
        $('#notifications').hide();
    } else {
        $('#notifications').show().text(notificationCount);
    }
}
function resetNotificationCount() {
    notificationCount = 0;
    updateNotificationDisplay();
}