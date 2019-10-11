package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class ParserResult {

    boolean passed;//语法分析通过为true，未通过为false
    Token curToken;//语法分析出错时当前token
    String description;//错误描述

    public void print(){
        if (!passed){
            if(curToken!=null){
                System.out.print(curToken);
            }
        }
        System.out.print(description);


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
