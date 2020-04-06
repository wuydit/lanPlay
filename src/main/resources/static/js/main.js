'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/socket?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImlkZW50aXR5IjoiQURNSU4sVVNFUiIsInVzZXJuYW1lIjoid3V5ZCJ9.eyJzdWIiOiJMQU5fUExBWV9UT0tFTiIsImF1ZCI6IkFQUCIsImlzcyI6IlNFUlZJQ0UiLCJpZCI6MSwiZXhwIjoxNTg2MTg0NzYyLCJ1c2VyIjoiZTMwPSIsImlhdCI6MTU4NjE3NzU2Mn0.DUSRnoivwpszTWGmvz0rNK7IK4lfmQv3gKTpNbdNw88');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/room/'+username+'/room/roomMsg', onMessageReceived);
    // stompClient.subscribe('/topic/horn', onMessageReceived);

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            serverId: username,
            msg: messageInput.value,
            userId: '1',
            token:"1"
        };

        stompClient.send("/app/send.roomMsg", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message =payload.body;

    var messageElement = document.createElement('li');

        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);


    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
