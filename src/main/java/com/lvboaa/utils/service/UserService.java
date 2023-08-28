package com.lvboaa.utils.service;

import com.lvboaa.utils.VO.BaseVo;
import com.lvboaa.utils.entity.User;

/**
 * Description:
 *
 * @author: lv.bo
 * @create: 2023-08-25 14:09
 */
public interface UserService extends BaseService<User>{
    BaseVo getUsers();
}
