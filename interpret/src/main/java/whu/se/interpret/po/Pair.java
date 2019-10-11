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
    private char c; //只能写大写S或者小写r,r0即为acc
    private int num; //数字

    public Pair(char c, int num){
        this.c = c;
        this.num = num;
    }

    public Pair(char c){
        this.c = c;
    }

    public Pair(int num){
        this.num = num;
    }

    @Override
    public String toString() {
        if (c == 'S' || (c == 'r' && num != 0 ))
            return ""+c+num;
        else if (c == 'r')
            return "acc";
        else return ""+num;
    }
}
