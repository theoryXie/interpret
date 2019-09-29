package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ：Chang Jiaxin
 * @date ：Created in 2019/9/29 16:49
 * @description ： 表中的单元格内容，比如 r1 或者 S5 这种
 */
@Getter
@Setter
public class Pair {
    private char c; //只能写大写S或者小写r
    private int num; //数字
}
