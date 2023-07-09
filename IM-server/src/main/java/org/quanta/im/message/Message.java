package org.quanta.im.message;

import lombok.*;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/8
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@Setter
@ToString
public class Message {
    private Long chatId;

    private Object data;
}
