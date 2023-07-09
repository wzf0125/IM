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
    private Integer type;

    private Object data;
}
