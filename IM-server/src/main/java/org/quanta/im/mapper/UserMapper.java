package org.quanta.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.quanta.im.user.User;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/8
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
