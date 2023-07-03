package org.quanta.im.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.log4j.Log4j2;
import org.quanta.im.bean.MessageResponse;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/6/24
 */
@Log4j2
public class IMClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            MessageResponse MessageResponse = (MessageResponse) msg;
            log.info("client receive msg:[{}]", MessageResponse);
            System.out.println("来自服务器:[" + msg + "]");
        } catch (Exception e) {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client caught exception", cause);
        ctx.close();
    }
}
