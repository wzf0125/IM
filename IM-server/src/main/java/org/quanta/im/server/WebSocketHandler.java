package org.quanta.im.server;

import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.log4j.Log4j2;
import org.quanta.im.bean.MessageRequest;
import org.quanta.im.bean.MessageResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    /**
     * 消息编号
     */
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 用户列表
     */
    private static final Map<Channel, String> userMap = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // 判断是否是关闭连接的指令
        if (frame instanceof CloseWebSocketFrame) {
            // 关闭WebSocket连接
            ctx.close();
        } else if (frame instanceof TextWebSocketFrame) {
            // 处理文本消息
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String message = textFrame.text();
            FullHttpRequest request = (FullHttpRequest) ctx.channel().attr(AttributeKey.valueOf("request")).get();
            // 获取请求参数
            String username = getUsername("username", request);

            if (username == null) {
                broadcastMessage("非法请求");
            }

            // 注册用户
            if (!userMap.containsKey(ctx.channel())) {
                userMap.put(ctx.channel(), username);
                log.info("用户登陆: [{}]", username);
                broadcastMessage("欢迎用户 [" + username + "] 进入聊天室");
            }

            // 获取消息结构
            MessageRequest content = JSONUtil.toBean(message, MessageRequest.class);

            if (!userMap.get(ctx.channel()).equals(content.getUsername())) {
                log.info("用户名更改 [{}] -> [{}]", username, content.getUsername());
                broadcastMessage("用户名更新: [" + username + "] -> [" + content.getUsername() + "]");
            }
            broadcastMessage(username+" : "+content.getMessage());
        }
    }

    private String getUsername(String key, FullHttpRequest request) {
        return request.headers().get(key);
    }

    private void broadcastMessage(String message) {
        MessageResponse response = new MessageResponse("["+atomicInteger.getAndIncrement()+"]--> "+message);
        String jsonResponse = JSONUtil.toJsonStr(response);

        for (Channel channel : userMap.keySet()) {
            channel.writeAndFlush(new TextWebSocketFrame(jsonResponse));
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
