package org.quanta.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.quanta.im.entity.GroupUser;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Mapper
public interface GroupUserMapper extends BaseMapper<GroupUser> {
    public List<Long> getChatGroupUserId(Long chatId);
}
