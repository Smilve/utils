package com.lvboaa.utils.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvboaa.utils.VO.BaseVo;
import com.lvboaa.utils.entity.User;
import com.lvboaa.utils.mapper.UserMapper;
import com.lvboaa.utils.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author: lv.bo
 * @create: 2023-08-25 14:10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public IPage<User> queryForPage(IPage<User> page, QueryWrapper<User> queryWrapper) {

        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public BaseVo getUsers() {
        List<User> users = this.baseMapper.selectList(null);
        return new BaseVo().setDatas(users);
    }
}
