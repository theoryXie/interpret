package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
public class ParserResult {

    boolean passed;//语法分析通过为true，未通过为false
    Token curToken;//语法分析出错时当前token
    String description;//错误描述

    ArrayList<Pair> pairs;//用于保存移进规约过程
    ArrayList<String> symbols;//用于保存过程中的符号栈
    ArrayList<String> states;//用于保存过程中的状态栈

    public void print(){
        if (!passed){
            System.out.print(curToken);
        }
        System.out.print(description);
    }

    @Override
    public String toString(){
        String result = "";
        if (!passed){
            result = curToken.toString();
        }
        result += description;
        return result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParserResult(Token curToken) {
        this.curToken = curToken;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Token getCurToken() {
        return curToken;
    }

    public void setCurToken(Token curToken) {
        this.curToken = curToken;
    }
}
