package whu.se.interpret.result;

import whu.se.interpret.po.Token;

import java.util.List;

public class Result {

    //提交的程序是否正确
    private boolean is_success;

    //错误的原因
    private String wrongMessage;

    //词法分析单元返回的token序列的字符串形式
    private String tokens;

    public Result(boolean is_success){
        this.is_success = is_success;
    }

    public Result(boolean is_success, String wrongMessage, String tokens) {
        this.is_success = is_success;
        this.wrongMessage = wrongMessage;
        this.tokens = tokens;
    }

    public boolean isIs_success() {
        return is_success;
    }

    public void setIs_success(boolean is_success) {
        this.is_success = is_success;
    }

    public String getWrongMessage() {
        return wrongMessage;
    }

    public void setWrongMessage(String wrongMessage) {
        this.wrongMessage = wrongMessage;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }
}
