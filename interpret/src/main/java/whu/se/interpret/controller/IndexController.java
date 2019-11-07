package whu.se.interpret.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whu.se.interpret.po.Code;
import whu.se.interpret.po.ParserResult;
import whu.se.interpret.po.SymbolTable;
import whu.se.interpret.po.Token;
import whu.se.interpret.result.Result;
import whu.se.interpret.service.impl.LexerImpl;
import whu.se.interpret.service.impl.ParserImpl;
import whu.se.interpret.service.impl.SemanticImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xsy
 * @description: 前后端交互得控制类
 * @date 2019/9/20 23:50
 */

@RestController
public class IndexController {

    private ArrayList<Integer> rows = new ArrayList<>();//保存debug的行号
    private int index;//当前需要debug行的下标
    private Code debug_code;//保存当前需要debug的代码


    @Autowired
    LexerImpl lexerImpl;

    @Autowired
    ParserImpl parserImpl;

    @Autowired
    SemanticImpl semanticImpl;


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
        try {
            List<Token> tokens = lexerImpl.lexer(code.getCode()); //获取token序列
            Result result = parserImpl.syntaxCheck(tokens).getResult();       //将token序列转化为发给前端的Result包
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     *
     * @Param file -- 前端传来的文件
     * @return Result -- 分析的结果
     **/
    @PostMapping(value = "api/import")
    @CrossOrigin
    public Result importData(MultipartFile file) throws Exception {
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


        List<Token> tokens = lexerImpl.lexer(code); //获取token序列
        Result result = parserImpl.syntaxCheck(tokens).getResult();
        result.setCode(code);
        return result;
    }

    @GetMapping(value = "api/_continue")
    @CrossOrigin
    public Result _continue() throws Exception {
        int row = 1;
        boolean isFinished = false;
        if(index < rows.size()-1) {
            index++;
            row = rows.get(index);
        }else{
            row = 10000;
            isFinished = true;
        }
        String code = debug_code.getCode();
        List<Token> tokens = lexerImpl.lexer(code); //获取token序列
        ParserResult parserResult = parserImpl.syntaxCheck(tokens);//语法分析结果
        //TODO 语义分析传递parserResult，grammar，row
        SymbolTable ans = semanticImpl.debug(parserResult,parserImpl.getGrammar(),row);
        Result result = new Result(ans.toString());
        result.setFinished(isFinished);
        return result;
    }

    @GetMapping(value = "api/nextStep")
    @CrossOrigin
    public Result nextStep() throws Exception {
        index++;
        int row = rows.get(index);


        return null;
    }

    @PostMapping(value = "api/debug")
    @CrossOrigin
    public Result debug(@RequestBody Code code) throws Exception {
        debug_code = code;
        String cmd = code.getCmd();//获取行号
        String[] splits = cmd.split(",");
        rows.clear();
        try {
            for (String split : splits) {
                rows.add(Integer.parseInt(split));
            }
        }catch (Exception e){
            return new Result("debug输入的行号不符合正确格式，正确格式为：1,3,4,6");
        }
        Collections.sort(rows);//行号排序
        String codeString = debug_code.getCode();

        List<Token> tokens = lexerImpl.lexer(codeString); //获取token序列
        ParserResult parserResult = parserImpl.syntaxCheck(tokens);//语法分析结果
        //TODO 语义分析传递parserResult，grammar，row
        int row = rows.get(0);
        SymbolTable ans = semanticImpl.debug(parserResult,parserImpl.getGrammar(),row);

        return new Result(ans.toString());
    }
}
