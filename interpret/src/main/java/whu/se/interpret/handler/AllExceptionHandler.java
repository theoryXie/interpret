package whu.se.interpret.handler;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import whu.se.interpret.exception.LexerException;
import whu.se.interpret.exception.ParserException;
import whu.se.interpret.exception.SemanticException;
import whu.se.interpret.result.Result;


//全局异常处理
@ControllerAdvice
public class AllExceptionHandler {



    //类型转化异常
    @ResponseBody
    @ExceptionHandler(value= ClassCastException.class)
    public Result handleClassCastException(ClassCastException ce){
        Result result = new Result("数据类型不匹配！！！");
        result.setFinished(true);
        return result;
    }

    //语义动作异常统一处理
    @ResponseBody
    @ExceptionHandler(value= SemanticException.class)
    public Result handleSemanticException(SemanticException se){
        Result result = new Result(se.getMessage());
        result.setFinished(true);
        return result;
    }

    //词法分析异常统一处理
    @ResponseBody
    @ExceptionHandler(value= LexerException.class)
    public Result handleLexerException(LexerException le){
        Result result = new Result(le.getMessage());
        result.setFinished(true);
        return result;
    }


    //语法分析异常统一处理
    @ResponseBody
    @ExceptionHandler(value= ParserException.class)
    public Result handleLexerException(ParserException pe){
        Result result = new Result(pe.getMessage());
        result.setFinished(true);
        return result;
    }

}
