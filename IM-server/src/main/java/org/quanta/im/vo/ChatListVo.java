package org.quanta.im.vo;

import lombok.Builder;
import lombok.Data;
import org.quanta.im.dos.ChatDo;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Builder
@Data
public class ChatListVo {
    List<ChatDo> chatList;
}
