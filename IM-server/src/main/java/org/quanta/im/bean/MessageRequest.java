package org.quanta.im.bean;

import lombok.*;

/**
 * Description: RPC请求
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/6/20
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@Setter
@ToString
public class MessageRequest {
    private String type;

    private String username;

    private String message;

}
