package org.quanta.im.channel;

import io.netty.channel.Channel;
import org.quanta.im.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
public class IMChannel {
    /**
     * 在线用户列表
     */
    private static final Map<Channel, User> userMap = new HashMap<>();
    /**
     * 在线用户列表
     * */
    private static final Map<Long,Channel> channelMap = new HashMap<>();

    /**
     * 添加用户连接
     */
    public static void addChannel(Channel channel, User user) {
        userMap.put(channel, user);
        channelMap.put(user.getId(),channel);
    }

    /**
     * 获取用户信息
     */
    public static User getUserInfo(Channel channel) {
        return userMap.get(channel);
    }

    public static Channel getChannelById(Long uid){
        return channelMap.get(uid);
    }

    /**
     * 移除连接
     */
    public static User removeChanel(Channel channel) {
        User user = userMap.remove(channel);
        channelMap.remove(user.getId());
        return user;
    }

    /**
     * 广播给所有用户
     */
    public static void broadcast(Object data) {
        userMap.keySet().forEach(channel -> channel.writeAndFlush(data));
    }

    /**
     * 发送消息
     * 只发送给receiver用户
     * 排除except用户
     */
    public static void broadcast(Object data, Channel receiver, Channel except) {
        userMap.keySet().stream()
                .filter(channel -> channel.equals(receiver))
                .filter(channel -> !channel.equals(except))
                .forEach(channel ->
                        channel.writeAndFlush(data));
    }

    /**
     * 广播消息
     * 发送给receiverList列表中的用户
     * 排除exceptList列表用户
     */
    public static void broadcast(Object data, List<Channel> receiverList, List<Channel> exceptList) {
        userMap.keySet().stream()
                .filter(receiverList::contains)
                .filter(channel -> !exceptList.contains(channel))
                .forEach(channel -> channel.writeAndFlush(data));
    }

    /**
     * 发送消息给某个用户
     * */
    public static void toUser(Long uid,Object data){
        channelMap.get(uid).writeAndFlush(data);
    }

}
