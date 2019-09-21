package whu.se.interpret.result;

import whu.se.interpret.po.token;

import java.util.List;

/**
 * @author xsy
 * @description: 发送给前台的结果
 * @date 2019/9/2121:13
 */

public class result {

    //提交的程序是否正确
    private boolean is_success;

    //错误的原因
    private String wrongMessage;

    //词法分析单元返回的token序列
    private List<token> tokens;



}
