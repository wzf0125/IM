package org.quanta.im.server;

import cn.hutool.json.JSONUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import lombok.extern.log4j.Log4j2;
import org.quanta.im.bean.MessageRequest;
import org.quanta.im.bean.MessageResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 消息编号
     */
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 用户列表
     */
    private static final Map<Channel, String> userMap = new HashMap<>();

    /**
     * 握手阶段
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
//        // 切换到 WebSocket 协议
//        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
//                getWebSocketLocation(frame), null, true);
        String content = frame.text();
        // Deserialize the message request
        MessageRequest request = JSONUtil.toBean(content, MessageRequest.class);
        String username = request.getUsername();
        if (!userMap.containsKey(ctx.channel())) {
            userMap.put(ctx.channel(), request.getUsername());
            broadcastMessage("Welcome user:" + request.getUsername() + "to the chat room");
        }

        if (!userMap.get(ctx.channel()).equals(request.getUsername())) {
            log.error("用户名改变:[{}] -> [{}]", username, request.getUsername());
            userMap.put(ctx.channel(), request.getUsername());
        }
        userMap.put(ctx.channel(), request.getUsername());
        String message = request.getMessage();

        // Broadcast the message to all users
        broadcastMessage(message);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String username = userMap.remove(channel);
        if (username != null) {
            log.info("User unregistered: {}", username);
            // Broadcast user exit message
            broadcastMessage("User: [" + username + "] left the chat room");
        }
        super.channelUnregistered(ctx);
    }

    private void broadcastMessage(String message) {
        MessageResponse response = new MessageResponse(message);
        String jsonResponse = JSONUtil.toJsonStr(response);

        for (Channel channel : userMap.keySet()) {
            channel.writeAndFlush(new TextWebSocketFrame(jsonResponse));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception", cause);
        Channel channel = ctx.channel();
        String username = userMap.remove(channel);
        if (username != null) {
            // Broadcast user exit message
            broadcastMessage("User: [" + username + "] left the chat room");
        }
        ctx.close();
    }

    private String extractUsernameFromQueryParameters(String uri) {
        // 提取查询参数中的用户名信息
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = decoder.parameters();
        if (parameters.containsKey("username")) {
            return parameters.get("username").get(0);
        }
        return "";
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String protocol = "ws";
        String host = req.headers().get(HttpHeaderNames.HOST);
        return protocol + "://" + host + req.uri();
    }
}
