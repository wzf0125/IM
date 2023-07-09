package org.quanta.im.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToUserMessage {
    Long uid;

    String message;
}
