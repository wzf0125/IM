package org.quanta.im.dos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.quanta.im.entity.User;


/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ChatWhisperDo extends ChatDo{

    User targetUser;
}
