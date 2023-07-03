package org.quanta.im.bean;

import lombok.*;

/**
 * Description: RPC响应
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
public class MessageResponse {

    private String message;

}
