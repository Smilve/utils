package com.lvboaa.utils.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.TableCharset;
import com.gitee.sunchenbin.mybatis.actable.annotation.TableEngine;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlCharsetConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlEngineConstant;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: lv.bo
 * @create: 2023-08-25 14:01
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "user")
@TableCharset(MySqlCharsetConstant.UTF8)
@TableEngine(value = MySqlEngineConstant.InnoDB)
public class User implements Serializable {

    /**
     * 主键
     */
    @IsKey
    @Column(value = "id", length = 11)
    private Integer id;

    @Column(value = "name", type = MySqlTypeConstant.LONGTEXT)
    private String name;

    @Column(value = "sex")
    private String sex;
}
