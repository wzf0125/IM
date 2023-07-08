package org.quanta.im.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;
import org.quanta.im.config.IMConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Description: 单例模式创建
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/6/24
 */
@Component
@Log4j2
public class IMServer {
    @Resource
    IMConfig imConfig;
    @Resource
    WebSocketHandler webSocketHandler;

    public void run() {
        // 用于接收客户端的链接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用于处理与客户端的数据交互
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(65536));
                            // 添加NettyKryoDecoder和NettyKryoEncoder用于对RpcRequest和RpcResponse对象进行编解码
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler(imConfig.getServerName(), true));
                            // 添加NettyServerHandler用于处理客户端发送的请求
                            ch.pipeline().addLast(webSocketHandler);
                        }
                    });
            // 绑定服务器端口并启动服务器
            ChannelFuture f = b.bind(imConfig.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server:", e);
        } finally {
            // 优雅地关闭线程组，释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
