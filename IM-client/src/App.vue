<template>
  <div id="chat-container">
    <h2>WebSocket Chat</h2>
    <ul id="message-list"></ul>
    <div>
      <input type="text" id="username" placeholder="用户名">
      <button id="login-button">登陆</button>
    </div>
    <div>
      <input type="text" id="message-input" placeholder="输入消息">
      <button id="send-button" >发送</button>
    </div>
  </div>
</template>

<script>

export default {
  name: 'App'
  ,created() {
    const username = document.getElementById('username');
    const messageList = document.getElementById('message-list');
    const messageInput = document.getElementById('message-input');
    const sendButton = document.getElementById('send-button');
    const loginButton = document.getElementById('login-button');

    let socket; // 声明 WebSocket 变量

    // 登录按钮点击事件处理
    loginButton.addEventListener('click', function () {
      const enteredUsername = username.value.trim();
      if (enteredUsername !== '') {
        // 建立 WebSocket 连接
        socket = new WebSocket('ws://localhost:8080/chat');

        // 连接建立时的回调函数
        socket.onopen = function () {
          console.log('WebSocket连接已建立');
        };

        // 接收到消息时的回调函数
        socket.onmessage = function (event) {
          console.log('收到服务器消息:', event.data);

          // 在消息列表中显示收到的消息
          const li = document.createElement('li');
          li.textContent = event.data;
          messageList.appendChild(li);
        };

        // 连接关闭时的回调函数
        socket.onclose = function () {
          console.log('WebSocket连接已关闭');
        };

        // 发生错误时的回调函数
        socket.onerror = function (error) {
          console.error('WebSocket发生错误:', error);
        };
      }
    });

    // 发送消息按钮点击事件处理
    sendButton.addEventListener('click', function () {
      const message = messageInput.value.trim();
      if (message !== '') {
        if (socket && socket.readyState === WebSocket.OPEN) {
          // 发送消息给服务器
          const request = {
            username: username.value,
            message: message
          };
          socket.send(JSON.stringify(request));

          // 清空输入框
          messageInput.value = '';
        } else {
          console.log('WebSocket连接尚未建立');
        }
      }
    });

    // 按下回车键时发送消息
    messageInput.addEventListener('keydown', function (event) {
      if (event.key === 'Enter') {
        sendButton.click();
      }
    });
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

#chat-container {
  width: 400px;
  margin: 0 auto;
  padding: 20px;
}

#message-list {
  list-style-type: none;
  padding: 0;
}

#message-list li {
  margin-bottom: 10px;
}
</style>
