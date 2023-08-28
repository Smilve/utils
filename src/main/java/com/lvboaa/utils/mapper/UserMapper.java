package com.lvboaa.utils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lvboaa.utils.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 *
 * @author: lv.bo
 * @create: 2023-08-25 14:07
 */
@Mapper
@Repository
@Transactional
public interface UserMapper extends BaseMapper<User> {
}
