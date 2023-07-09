package org.quanta.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.quanta.im.dos.ChatGroupDo;
import org.quanta.im.dos.ChatWhisperDo;
import org.quanta.im.entity.Chat;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    // 获取私聊列表
    List<ChatWhisperDo> getWhisperList(Long uid);

    // 获取群聊列表
    List<ChatGroupDo> getChatGroupList(Long uid);
}
