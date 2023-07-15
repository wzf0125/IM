package org.quanta.im.dos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class ChatWhisperDo extends ChatDo{

    User targetUser;
}
