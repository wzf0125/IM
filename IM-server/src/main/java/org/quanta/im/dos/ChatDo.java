package org.quanta.im.dos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.quanta.im.entity.User;

import java.util.Date;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Data
public class ChatDo {
    /**
     * 聊天窗口id
     * */
    Long id;

    /**
     * 聊天类型
     * */
    Integer type;

    /**
     * 最后一次活跃时间
     * */
    Date activeTime;
}
