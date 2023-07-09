package org.quanta.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quanta.im.entity.GroupUser;
import org.quanta.im.mapper.GroupUserMapper;
import org.quanta.im.service.GroupUserService;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Service
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser> implements GroupUserService {
}
