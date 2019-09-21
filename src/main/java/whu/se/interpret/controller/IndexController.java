package whu.se.interpret.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whu.se.interpret.result.result;

import java.util.List;

/**
 * @author xsy
 * @description: 前后端交互得控制类
 * @date 2019/9/20 23:50
 */

@RestController
@RequestMapping("")
public class IndexController {

    /**
     * 
     * 
     * @Param code -- 前端传来的需要分析的代码 
     * @return result -- 分析的结果
     **/
    @PostMapping("")
    public result InterpretCode(){
        return null;
    }

}
