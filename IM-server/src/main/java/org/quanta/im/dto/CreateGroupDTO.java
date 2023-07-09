package org.quanta.im.dto;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Data
public class CreateGroupDTO {
    /**
     * 群聊名称
     */
    String groupName;

    /**
     * 群聊头像
     */
    String avatar;
    List<Long> ids;
}
