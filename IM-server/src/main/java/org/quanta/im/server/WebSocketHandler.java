package org.quanta.im.server;

import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
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

    private ChannelHandlerContext curCtx;


    /**
     * 客户端连接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.curCtx = ctx;
        log.info("客户端连接");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String uri = complete.requestUri(); // 获取请求路径，接着根据自己的业务逻辑判断以及获取自己想要的参数
//            HttpHeaders entries = complete.requestHeaders(); // 请求头

            Map<String, String> param = getParam(uri);
            // 注册用户
            if (!userMap.containsKey(ctx.channel())) {
                String username = param.getOrDefault("username", "未命名");
                userMap.put(ctx.channel(), username);
                log.info("用户登陆: [{}]", username);
                broadcastMessage("欢迎用户 [" + username + "] 进入聊天室");
            }
        }
    }

    /**
     * 处理请求后续参数
     */
    private Map<String, String> getParam(String uri) {
        Map<String, String> param = new HashMap<>();
        String[] split = uri.split("\\?");
        String[] kvs = split[1].split("&");
        for (String item : kvs) {
            String[] kv = item.split("=");
            if (kv.length != 2) {
                log.error("请求参数非法");
                throw new RuntimeException("请求参数非法");
            }
            param.put(kv[0], kv[1]);
        }
        return param;
    }

    /**
     * 客户端断开连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String username = userMap.remove(ctx.channel());
        log.info("用户 [{}] 退出登陆", username);
        broadcastMessage("用户 [" + username + "] 退出登陆");
    }

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
            // 获取消息结构
            MessageRequest content = JSONUtil.toBean(message, MessageRequest.class);
            String username = content.getUsername();

            if (!userMap.get(ctx.channel()).equals(content.getUsername())) {
                log.info("用户名更改 [{}] -> [{}]", username, content.getUsername());
                userMap.put(ctx.channel(), content.getUsername());
                broadcastMessage("用户名更新: [" + username + "] -> [" + content.getUsername() + "]");
            }
            broadcastMessage(username+" : "+content.getMessage());
        }
    }

    private void broadcastMessage(String message) {
        MessageResponse response = new MessageResponse("["+atomicInteger.getAndIncrement()+"]--> "+message);
        String jsonResponse = JSONUtil.toJsonStr(response);

        userMap.keySet().stream().filter(i-> !i.equals(curCtx.channel()))
                .forEach(e->{
                    e.writeAndFlush(new TextWebSocketFrame(jsonResponse));
                });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
