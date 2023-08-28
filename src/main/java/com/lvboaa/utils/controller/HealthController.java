package com.lvboaa.utils.controller;

import com.lvboaa.utils.VO.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 测试接口
 *
 * @author recivejt
 */
@Api(value = "健康检查接口", tags = "健康检查接口")
@RestController
@RequestMapping("/health")
public class HealthController {

    @ResponseBody
    @RequestMapping(value = "/isStart", method = RequestMethod.GET)
    @ApiOperation(value = "isStart", notes = "isStart")
    public BaseVo isStart() {
        BaseVo baseVo = new BaseVo();

        baseVo.setMsg("start UP!");

        return baseVo;
    }
}
