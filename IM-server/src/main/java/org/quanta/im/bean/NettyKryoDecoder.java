package org.quanta.im.bean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Description: 自定义解码器
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/6/24
 */
@AllArgsConstructor
@Log4j2
public class NettyKryoDecoder extends ByteToMessageDecoder {
    private final Serializer serializer;
    private final Class<?> genericClass;

    /**
     * Netty传输的消息长度也就是对象序列化后对应的字节数组的大小，存储在ByteBuf头部
     */
    private static final int BODY_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1. byteBuf中写入的消息长度所占的字节数已经是4了，所以byteBuf的可读字节必须大于4
        if (in.readableBytes() >= BODY_LENGTH) {
            // 2.标记当前readIndex的位置，以便后面重置readIndex的时候使用
            in.markReaderIndex();
            // 3.读取消息的长度
            // 注意: 消息长度是encode的时候我们自己写入的
            int dateLength = in.readInt();
            // 4.遇到不合理的情况直接return
            if (dateLength < 0 || in.readableBytes() < 0) {
                log.error("data length or byteBuf readableBytes is not valid");
                return;
            }
            // 5. 如果可读字节小于消息长度的话，说明是不完整的消息，重置readIndex
            if (in.readableBytes() < dateLength) {
                in.readableBytes();
                return;
            }
            // 6.序列化
            byte[] body = new byte[dateLength];
            in.readBytes(body);
            Object obj = serializer.deserialize(body, genericClass);
            out.add(obj);
            log.info("successful decode ByteBuf to Object");
        }
    }
}
