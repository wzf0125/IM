<template>
  <head>
    <title>WebSocket Chat</title>
  </head>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  created() {

  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
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
