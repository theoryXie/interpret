package whu.se.interpret.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whu.se.interpret.po.Code;
import whu.se.interpret.po.Token;
import whu.se.interpret.result.Result;
import whu.se.interpret.service.impl.LexerImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xsy
 * @description: 前后端交互得控制类
 * @date 2019/9/20 23:50
 */

@RestController
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
    @PostMapping(value = "api/code")
    @ResponseBody
    public Result InterpretCode(@RequestBody Code code){
        /*List<Token> tokens = lexerImpl.lexer(code.getCode()); //获取token序列
        Result result = lexerImpl.analysisTokens(tokens);     //将token序列转化为发给前端的Result包*/
        System.out.println(code.getCode());
        System.out.println(code.getInput());
        Result result = new Result("hhhhhhhhhhhhhhhhhhh");
        return result;

    }

    /**
     *
     *
     * @Param file -- 前端传来的文件
     * @return Result -- 分析的结果
     **/
    @PostMapping(value = "api/import")
    @CrossOrigin
    public Result importData(MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        Reader reader = new InputStreamReader(file.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(reader);
        String code = "";
        String line;
        while ((line = br.readLine()) != null) {
            // 一次读入一行数据
            code += line + "\n";
        }
        reader.close();
        Result result = new Result(200);
        System.out.println(code);
        result.setCode(code);
        return result;
    }
}
