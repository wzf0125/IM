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
import org.quanta.im.bean.*;


/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/6/24
 */
@Log4j2
public class IMServer {
    private final int port;

    public IMServer(int port) {
        this.port = port;
    }

    public void run() {
        // 用于接收客户端的链接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用于处理与客户端的数据交互
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 创建KryoSerializer对象用于对象的序列化和反序列化
        KryoSerializer kryoSerializer = new KryoSerializer();
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
//                            ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, MessageRequest.class));
//                            ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, MessageResponse.class));
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/chat"));
                            // 添加NettyServerHandler用于处理客户端发送的请求
                            ch.pipeline().addLast(new WebSocketHandler());
                        }
                    });
            // 绑定服务器端口并启动服务器
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server:", e);
        } finally {
            // 优雅地关闭线程组，释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        // 创建NettyServer对象并指定端口号，然后启动服务器
        new IMServer(8080).run();
    }
}
