package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ：Chang Jiaxin
 * @date ：Created in 2019/9/29 16:49
 * @description ： SLR表
 */
@Getter
@Setter
public class SLRTable {
    private ArrayList<HashMap<String, ArrayList<Pair>>> actions; //action表
    private ArrayList<HashMap<String, ArrayList<Pair>>> gotos; //goto表
}
