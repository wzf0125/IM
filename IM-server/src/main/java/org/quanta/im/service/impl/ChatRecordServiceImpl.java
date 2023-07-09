package org.quanta.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quanta.im.entity.ChatRecord;
import org.quanta.im.mapper.ChatRecordMapper;
import org.quanta.im.service.ChatRecordService;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements ChatRecordService {
}
