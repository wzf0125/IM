package org.quanta.im.server;

import cn.hutool.json.JSONUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.log4j.Log4j2;
import org.quanta.im.message.Message;
import org.quanta.im.message.ToGroupMessage;
import org.quanta.im.message.ToUserMessage;
import org.quanta.im.channel.IMChannel;
import org.quanta.im.constans.MessageTypeConstants;
import org.quanta.im.handle.TokenHandle;
import org.quanta.im.entity.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Resource
    TokenHandle tokenHandle;

    /**
     * 消息编号
     */
    private static final AtomicInteger atomicInteger = new AtomicInteger();

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

    /**
     * 建立连接完成后会调用这个方法
     * 用于校验Token和分发具体请求
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String uri = complete.requestUri(); // 获取请求路径，接着根据自己的业务逻辑判断以及获取自己想要的参数
//            HttpHeaders entries = complete.requestHeaders(); // 请求头
            // 处理token并返回用户信息
            Map<String, String> params = getParam(uri);
            String token = params.get("token");
            User user = null;
            if (token == null || (user = tokenHandle.auth(token)) == null) {
                log.info("未认证请求[{}]", ctx.channel());
                ctx.channel().writeAndFlush(Message.builder()
                        .data("非法请求")
                        .build());
                ctx.close();
                return;
            }
            // 注册用户
            IMChannel.addChannel(ctx.channel(), user);
            // 查询聊天列表

            // 返回聊天列表
            IMChannel.toUser(user.getId(),null);
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
        User user = IMChannel.removeChanel(ctx.channel());
        log.info("用户 [{}] 退出登陆", user);
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
            String text = textFrame.text();
            // 获取消息结构
            Message message = JSONUtil.toBean(text, Message.class);
            // 根据消息类型处理不同的情况
            // 进入聊天室
            if(MessageTypeConstants.ENTER_CHAT_ROOM.getCode().equals(message.getType())){

            }// 进入单人群聊
            else if(MessageTypeConstants.ENTER_CHAT.getCode().equals(message.getType())){

            }// 发送私聊消息
            else if(MessageTypeConstants.SEND_WHISPER.getCode().equals(message.getType())){
                ToUserMessage msg = (ToUserMessage) message.getData();
                IMChannel.toUser(msg.getUid(),msg.getMessage());
            }// 发送群聊消息
            else if(MessageTypeConstants.SEND_BROADCAST.getCode().equals(message.getType())){
                ToGroupMessage msg = (ToGroupMessage) message.getData();
                // 获取群聊用户列表


                // 向群聊用户广播信息
//                IMChannel.broadcast();
            }
        }
    }




    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
