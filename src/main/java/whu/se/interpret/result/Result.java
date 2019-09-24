package whu.se.interpret.result;

import lombok.Getter;
import lombok.Setter;
import whu.se.interpret.po.token;

import java.util.List;

//返回前端结果类
@Setter
@Getter
public class Result {

    //提交的程序是否正确 200为正确，400为错误
    private int status;

    //错误的原因
    private String wrongMessage;

    //词法分析单元返回的token序列
    private List<token> tokens;

    //前端输出
    private String output;

    //返回文件的内容
    private String code;

    public Result(int status){
        this.status = status;
    }

    public Result(){}
}
