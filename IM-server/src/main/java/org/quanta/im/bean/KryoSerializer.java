package org.quanta.im.bean;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/6/24
 */
public class KryoSerializer implements Serializer {
    /**
     * Kryo不是线程安全的，每个线程都应该有自己的kryo，Input和Output实例
     * 使用ThreadLocal存放Kryo对象
     * */
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(MessageRequest.class);
        kryo.register(MessageResponse.class);
        kryo.setReferences(true); // 默认值为true，是否关闭注册因为，关闭之后可能存在序列化问题，一般推荐设置为true
        kryo.setRegistrationRequired(false); // 默认值为false，是否关闭循环应用，可以提高性能，但是一般不推荐设置为true
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Output output = new Output(byteArrayOutputStream);
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializeException("序列化错误");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            Input input = new Input(byteArrayInputStream);
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (Exception e) {
            throw new SerializeException("反序列化失败");
        }
    }
}
