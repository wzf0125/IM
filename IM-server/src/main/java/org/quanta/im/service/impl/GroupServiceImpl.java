package org.quanta.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quanta.im.entity.Group;
import org.quanta.im.mapper.GroupMapper;
import org.quanta.im.service.GroupService;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {
}
