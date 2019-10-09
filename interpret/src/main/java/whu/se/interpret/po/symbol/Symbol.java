package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xsy
 * @description: 所有非终结符号和终结符号的父类
 * @date 10/9/2019 2:40 PM
 */

@Getter
@Setter
public class Symbol {
    String name;
    
    public Symbol(String name) {
    	this.name = name;
    }
}































































