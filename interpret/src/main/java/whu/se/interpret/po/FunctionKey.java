package whu.se.interpret.po;

/**
 * @author xsy
 * @description: 函数类（结构体）
 * @date 10/24/2019 11:08 PM
 */
public class FunctionKey {
    /**
     * 1、如果函数名为“全局”，则代表全局变量
     * 2、如果返回类型为“structsym”，则代表这是个结构体，name为结构体名字
     * 3、其他则为普通函数
     **/
    private String name;//函数名
    private Token.Symbol type;//返回类型
    //TODO 参数


    public FunctionKey(String name, Token.Symbol type) {
        this.name = name;
        this.type = type;
    }
}
