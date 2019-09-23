package whu.se.interpret.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import whu.se.interpret.po.Code;
import whu.se.interpret.result.Result;

import java.util.List;

/**
 * @author xsy
 * @description: 前后端交互得控制类
 * @date 2019/9/20 23:50
 */

@Controller
public class IndexController {

    /**
     * 
     * 
     * @Param code -- 前端传来的需要分析的代码 
     * @return result -- 分析的结果
     **/
    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result InterpretCode(@RequestBody Code code){
        System.out.println(code.getCode());
        Result result = new Result(true);
        return result;
    }

}
