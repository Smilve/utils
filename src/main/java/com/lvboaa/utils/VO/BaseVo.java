package com.lvboaa.utils.VO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lvboaa.utils.common.constants.CommonConstants;
import lombok.Data;

/**
 * @author zheng.huiyuan
 */
@Data
public class BaseVo<T> {

    /**
     * 状态码
     */
    private int code;

    /**
     * 备注信息
     */
    private String msg;

    /**
     * 数据域
     */
    private T datas;

    private boolean containPage;

    /**
     * 分页
     */
    private Pager pager = new Pager();

    public BaseVo() {
        this.code = CommonConstants.SUCCESSED_CODE;
        this.msg = CommonConstants.SUCCESSED_MSG;
    }


    /**
     * 快速设置接口调用错误信息
     *
     * @param msg
     */
    public void setFailed(String msg) {
        this.code = CommonConstants.FAILED_CODE;
        this.msg = msg;
    }

    /**
     * 快速设置接口未授权错误信息
     *
     * @param msg
     */
    public void setUnauthorized(String msg) {
        this.code = CommonConstants.UNAUTHORIZED_CODE;
        this.msg = msg;
    }

    /**
     * 快速设置接口调用正确信息
     *
     * @param msg
     */
    public void setSuccessed(String msg) {
        this.code = CommonConstants.SUCCESSED_CODE;
        this.msg = msg;
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSuccessed() {
        return this.getCode() == CommonConstants.SUCCESSED_CODE;
    }

    /**
     * 简单的设置返回的数据
     *
     * @param datas 返回的数据实体
     */
    public BaseVo<T> setDatas(T datas) {
        this.datas =  datas;
        this.containPage = false;
        return this;
    }

    /**
     * 设置返回数据实体，并且指定分页信息等
     *
     * @param page 分页对象
     */
    public BaseVo<T> setResult(IPage page) {
        this.datas = (T)page.getRecords();
        this.pager.setTotalCount(page.getTotal());
        this.pager.setPageNum(page.getCurrent());
        this.pager.setPageSize(page.getSize());
        this.containPage = true;
        return this;
    }

}

@Data
class Pager {
    // 分页相关信息
    /**
     * 总记录数
     */
    private long totalCount;

    /**
     * 总分页数
     */
    private long pageSize;

    /**
     * 当前页
     */
    private long pageNum;
    // 分页相关信息
}

