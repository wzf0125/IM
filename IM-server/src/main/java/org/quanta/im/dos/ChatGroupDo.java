package org.quanta.im.dos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.quanta.im.entity.Group;
import org.quanta.im.entity.User;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatGroupDo extends ChatDo{
    List<Group> groupInfo;
}
