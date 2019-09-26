package whu.se.interpret.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import whu.se.interpret.po.Code;
import whu.se.interpret.po.Token;
import whu.se.interpret.result.Result;
import whu.se.interpret.service.impl.LexerImpl;

import java.util.List;

/**
 * @author xsy
 * @description: 前后端交互得控制类
 * @date 2019/9/20 23:50
 */

@Controller
public class IndexController {

    @Autowired
    LexerImpl lexerImpl;


    /**
     * 
     * 
     * @Param code -- 前端传来的需要分析的代码 
     * @return result -- 分析的结果
     **/
    @CrossOrigin
    @PostMapping(value = "")
    @ResponseBody
    public Result InterpretCode(@RequestBody Code code){
        List<Token> tokens = lexerImpl.lexer(code.getCode()); //获取token序列
        Result result = lexerImpl.analysisTokens(tokens);     //将token序列转化为发给前端的Result包
        return result;
    }

}
