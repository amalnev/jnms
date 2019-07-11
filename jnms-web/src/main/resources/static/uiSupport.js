function onDocumentLoaded() {
    var changePasswordCheckbox = document.getElementById("changePasswordCheckbox");
    var saveButton = document.getElementById("saveButton");
    var eventsTable = document.getElementById("events");

    if (changePasswordCheckbox != null) {
        changePasswordCheckbox.addEventListener("click", function () {
            var passwordField = document.getElementById("passwordField");
            var passwordConfirmationField = document.getElementById("passwordConfirmationField");

            if (passwordField.disabled) {
                passwordField.disabled = false;
                passwordConfirmationField.disabled = false;
            } else {
                passwordField.disabled = true;
                passwordConfirmationField.disabled = true;
            }
        });
    }

    if (saveButton != null) {
        saveButton.addEventListener("click", function () {
            var passwordField = document.getElementById("passwordField");
            var passwordConfirmationField = document.getElementById("passwordConfirmationField");
            var errorBox = document.getElementById("errorBox");
            var userForm = document.getElementById("userForm");

            if (passwordField.value != passwordConfirmationField.value) {
                errorBox.innerText = "Passwords do not match";
            } else {
                userForm.submit();
            }
        });
    }

    if(eventsTable != null){
        var socket = new SockJS('/jnms/jnms-websocket');
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/events', function (notification) {
                var notificationMessage = JSON.parse(notification.body);
                var fontColor = notificationMessage.outcome ? "green" : "red";
                $("#events").append("<tr><td><font color='" + fontColor + "'>" + notificationMessage.content + "</font></td></tr>");
            });
        });
    }
}


function processAllEntities(className, userName, passWord, processor){
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var myArr = JSON.parse(this.responseText);
            for(var i = 0; i < myArr.length; i++){
                processor(myArr[i]);
            }
        }
    };
    var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9\+\/\=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);return t},_utf8_encode:function(e){e=e.replace(/\r\n/g,"\n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}};
    xmlhttp.open("GET", '/jnms/api/entities/' + className, true);
    xmlhttp.setRequestHeader("Authorization",'Basic ' + Base64.encode( userName + ':' + passWord));
    xmlhttp.send();
}

