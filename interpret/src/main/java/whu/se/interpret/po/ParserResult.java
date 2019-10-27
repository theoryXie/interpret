package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;
import whu.se.interpret.result.Result;

import java.util.ArrayList;


@Getter
@Setter
public class ParserResult {

    boolean passed;//语法分析通过为true，未通过为false
    Token curToken;//语法分析出错时当前token
    String description;//错误描述

    ArrayList<Pair> pairs;//用于保存移进规约过程
    ArrayList<String> symbols_String;//用于保存过程中的符号栈
    ArrayList<String> states;//用于保存过程中的状态栈

    ArrayList<ArrayList<Object>> symbols_Object;//用于保存过程中的符号对象栈

    Result result;//用于保存传给前端的字符串

    public ParserResult(Token curToken, String description, boolean passed, ArrayList<Pair> pairs, ArrayList<String> states, ArrayList<String> symbols_String, ArrayList<ArrayList<Object>> symbols_Object, Result result) {
        this.curToken = curToken;
        this.passed = passed;
        this.description = description;
        this.pairs = pairs;
        this.symbols_String = symbols_String;
        this.states = states;
        this.symbols_Object = symbols_Object;
        this.result = result;
    }

    public ArrayList<Pair> getPairs() {
        return pairs;
    }

    public void setPairs(ArrayList<Pair> pairs) {
        this.pairs = pairs;
    }

    public ArrayList<String> getSymbols_String() {
        return symbols_String;
    }

    public void setSymbols_String(ArrayList<String> symbols_String) {
        this.symbols_String = symbols_String;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public void setStates(ArrayList<String> states) {
        this.states = states;
    }

    public ArrayList<ArrayList<Object>> getSymbols_Object() {
        return symbols_Object;
    }

    public void setSymbols_Object(ArrayList<ArrayList<Object>> symbols_Object) {
        this.symbols_Object = symbols_Object;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

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
