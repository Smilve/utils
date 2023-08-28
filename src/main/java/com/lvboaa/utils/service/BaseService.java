package com.lvboaa.utils.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseService<T> extends IService<T> {
    /**
     * 分页查询
     *
     * @param page         分页信息
     * @param queryWrapper 查询条件
     * @return 查询结果
     */
    IPage<T> queryForPage(IPage<T> page, QueryWrapper<T> queryWrapper);

}
