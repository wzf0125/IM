package org.quanta.im.client;

import org.quanta.im.bean.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.extern.log4j.Log4j2;
import org.quanta.im.bean.MessageRequest;
import org.quanta.im.bean.MessageResponse;


/**
 * Description: Netty客户端类
 * Param: host - 服务器主机名
 * port - 服务器端口号
 * return:
 * Author: wzf
 * Date: 2023/6/20
 */
@Log4j2
public class IMClient {
    private final String host;
    private final int port;
    private static final Bootstrap b;

    private ChannelFuture f;

    private String username;

    public IMClient(String host, int port) {
        this.host = host;
        this.port = port;

    }

    public void login(String username) {
        try {
            f = b.connect(host, port).sync();
            this.username = username;
            log.info("client connect {}", host + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static {
        // 创建NIO事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        b = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 添加NettyKryoDecoder和NettyKryoEncoder用于对RpcResponse和MessageRequest对象进行编解码
                        ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, MessageResponse.class));
                        ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, MessageRequest.class));
                        // 添加NettyClientHandler用于处理服务端返回的消息
                        ch.pipeline().addLast(new IMClientHandler());
                    }
                });
    }

    /**
     * 发送消息给服务器并接收返回的结果
     *
     * @param message - 要发送的MessageRequest对象
     * @return - 服务端返回的RpcResponse对象
     */
    public void sendMessage(String message) {
        try {
            Channel futureChannel = f.channel();
            log.info("send message");
            if (futureChannel != null) {
                futureChannel.writeAndFlush(MessageRequest.builder()
                                .username(this.username)
                                .message(message)
                                .build())
                        .addListener(future -> {
                            if (future.isSuccess()) {
                                log.info("client send message: [{}]", message);
                            } else {
                                log.error("Send failed:", future.cause());
                            }
                        });
            }
        } catch (Exception e) {
            log.error("occur exception when connect serve:", e);
        }
    }

}
