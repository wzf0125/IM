package org.quanta.im.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.quanta.im.channel.IMChannel;
import org.quanta.im.constants.ChatType;
import org.quanta.im.dos.ChatDo;
import org.quanta.im.dos.ChatGroupDo;
import org.quanta.im.dos.ChatWhisperDo;
import org.quanta.im.dto.SendMessageDTO;
import org.quanta.im.entity.*;
import org.quanta.im.exception.ApiException;
import org.quanta.im.mapper.ChatMapper;
import org.quanta.im.mapper.GroupMapper;
import org.quanta.im.mapper.GroupUserMapper;
import org.quanta.im.mapper.UserMapper;
import org.quanta.im.message.Message;
import org.quanta.im.service.ChatRecordService;
import org.quanta.im.service.ChatService;
import org.quanta.im.service.GroupUserService;
import org.quanta.im.vo.ChatListVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    private final GroupUserMapper groupUserMapper;
    private final UserMapper userMapper;

    @Override
    public void sendMessage(Long uid, SendMessageDTO param) {
        // 单聊
        if (param.getType().equals(0)) {
            // 先判断聊天窗口是否已经创建
            handleWhisper(uid, param);
        } else {// 群聊
            handleGroup(uid, param);
        }
    }

    /**
     * 创建私聊
     */
    @Override
    @Transactional
    public void createWhisper(Long uid, Long targetUid) {
        User targetUser = userMapper.selectById(targetUid);
        if (targetUser == null) {
            throw new ApiException("目标用户不存在");
        }
        if (uid.equals(targetUid)) {
            throw new ApiException("不能和自己聊天喔");
        }
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
    @Transactional
    public void createGroup(Long uid, String groupName, List<Long> targetList) {
        Chat chat = Chat.builder()
                .type(1)
                .userId(uid)
                .build();
        // 保存聊天窗口
        save(chat);
        // 创建群组
        Group group = Group.builder()
                .name(groupName)
                .chatId(chat.getId())
                .build();
        groupMapper.insert(group);
        chat.setTargetId(group.getId());
        updateById(chat);
        Set<Long> userId = new HashSet<>(targetList);
        userId.add(uid);
        // 构建群组用户列表
        List<GroupUser> groupUserList = userId.stream()
                .map(t -> GroupUser.builder()
                        .groupId(group.getId())
                        .userId(t)
                        .build())
                .collect(Collectors.toList());
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
                .eq(Chat::getType, ChatType.WHISPER.getCode())
        );

        // 聊天不存在则视为非法发送
        if (chat == null) {
            throw new ApiException("非法请求");
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

    public void handleGroup(Long uid, SendMessageDTO param) {
        // 先判断聊天是否存在
        Chat chat = getOne(new LambdaQueryWrapper<Chat>()
                .eq(Chat::getId, param.getChatId())
                .eq(Chat::getType, ChatType.GROUP.getCode())
        );

        // 聊天不存在则视为非法发送
        if (chat == null) {
            throw new ApiException("非法请求");
        }

        // 查询聊天成员
        List<Long> userId = groupUserMapper.getChatGroupUserId(chat.getId());

        // 发送消息
        IMChannel.broadcastById(param.getMessage(), userId, Collections.singletonList(uid));
    }

    @Async("taskExecutor")
    public void saveChatRecord(ChatRecord chatRecord) {
        chatRecordService.save(chatRecord);
    }

}
