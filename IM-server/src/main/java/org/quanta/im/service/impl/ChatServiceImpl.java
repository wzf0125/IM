package org.quanta.im.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.quanta.im.channel.IMChannel;
import org.quanta.im.dos.ChatDo;
import org.quanta.im.dos.ChatGroupDo;
import org.quanta.im.dos.ChatWhisperDo;
import org.quanta.im.dto.SendMessageDTO;
import org.quanta.im.entity.Chat;
import org.quanta.im.entity.ChatRecord;
import org.quanta.im.entity.Group;
import org.quanta.im.entity.GroupUser;
import org.quanta.im.exception.ApiException;
import org.quanta.im.mapper.ChatMapper;
import org.quanta.im.mapper.GroupMapper;
import org.quanta.im.message.Message;
import org.quanta.im.service.ChatRecordService;
import org.quanta.im.service.ChatService;
import org.quanta.im.service.GroupUserService;
import org.quanta.im.vo.ChatListVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Service
@AllArgsConstructor
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {
    private final ChatRecordService chatRecordService;
    private final GroupMapper groupMapper;
    private final GroupUserService groupUserService;
    private final ChatMapper chatMapper;

    @Override
    public void sendMessage(Long uid, SendMessageDTO param) {
        // 单聊
        if (param.getType().equals(0)) {
            // 先判断聊天窗口是否已经创建
            handleWhisper(uid, param);
        } else {// 群聊
        }
    }

    /**
     * 创建私聊
     */
    @Override
    public void createWhisper(Long uid, Long targetUid) {
        save(Chat.builder()
                .type(0)
                .userId(uid)
                .targetId(targetUid)
                .build());
    }

    /**
     * 创建聊天群
     */
    @Override
    public void createGroup(Long uid, String groupName, List<Long> targetList) {
        // 先创建群组
        Group group = Group.builder()
                .userId(uid)
                .name(groupName)
                .build();
        groupMapper.insert(group);
        // 保存聊天窗口
        save(Chat.builder()
                .type(1)
                .targetId(group.getId())
                .userId(uid)
                .build());
        // 构建群组用户列表
        List<GroupUser> groupUserList = targetList.stream()
                .map(t -> GroupUser.builder()
                        .groupId(group.getId())
                        .userId(t)
                        .build())
                .collect(Collectors.toList());
        // 添加自己
        groupUserList.add(GroupUser.builder()
                .groupId(group.getId())
                .userId(uid)
                .build());
        groupUserService.saveBatch(groupUserList);
    }

    @Override
    public ChatListVo getChatList(Long uid) {
        // 先查询单聊
        List<ChatWhisperDo> whisperList = chatMapper.getWhisperList(uid);
        // 然后查询群聊
        List<ChatGroupDo> chatGroupList = chatMapper.getChatGroupList(uid);
        // 根据活跃时间排序后返回
        List<ChatDo> res = new ArrayList<>(whisperList);
        res.addAll(chatGroupList);
        // 按照时间排序
        res.sort((v1, v2) -> DateUtil.compare(v1.getActiveTime(), v2.getActiveTime()));
        return ChatListVo.builder().chatList(res).build();
    }

    /**
     * 处理私聊逻辑
     */
    public void handleWhisper(Long uid, SendMessageDTO param) {
        // 先判断聊天是否存在
        Chat chat = getOne(new LambdaQueryWrapper<Chat>()
                .eq(Chat::getId, param.getChatId())
        );
        // 聊天不存在则视为非法发送
        if (chat == null) {
            throw new ApiException("非法请求");
        }

        // 判断目标用户是否在线 在线则需要通知聊天列表有更新
        if (IMChannel.isOnline(chat.getTargetId())) {

        }

        // 发送消息(用户在线才能收到)
        IMChannel.toUser(chat.getTargetId(), Message.builder()
                .chatId(chat.getId())
                .data(param.getMessage())
                .build());

        // 异步保存聊天记录
        saveChatRecord(ChatRecord.builder()
                .chatId(chat.getId())
                .userId(uid)
                .type(0)
                .message(param.getMessage())
                .build());
    }

    @Async("taskExecutor")
    public void saveChatRecord(ChatRecord chatRecord) {
        chatRecordService.save(chatRecord);
    }

}
