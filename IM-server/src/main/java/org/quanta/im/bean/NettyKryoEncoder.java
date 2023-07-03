package org.quanta.im.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/6/24
 */
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {
    private final Serializer serializer;
    private final Class<?> genericClass;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(o)) {
            // 1.将对象转换为byte
            byte[] body = serializer.serialize(o);
            // 2.读取消息的长度
            int dateLength = body.length;
            // 3.写入消息对应的字节数组长度，writeIndex
            byteBuf.writeInt(dateLength);
            // 4.将字节数组写入ByteBuf对象中
            byteBuf.writeBytes(body);
        }
    }
}
