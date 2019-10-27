package whu.se.interpret.po.symbol;

import whu.se.interpret.po.Token;

/**
 * @author xsy
 * @description: 函数类
 * @date 10/24/2019 11:08 PM
 */
public class FunctionKey {
    private String name;//函数名
    private Token.Symbol type;//返回类型
    //TODO 参数


    public FunctionKey(String name, Token.Symbol type) {
        this.name = name;
        this.type = type;
    }
}
