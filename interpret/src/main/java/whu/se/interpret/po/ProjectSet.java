package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ：Chang Jiaxin
 * @date ：Created in 2019/9/29 16:49
 * @description ： 项目集
 */
@Getter
@Setter
public class ProjectSet {
    private ArrayList<Node> core; //核心项目集
    private ArrayList<Node> production; //非核心项目集
    private HashMap<String, Integer> pointer; //输入某个符号（字符串），转到第几个I
}
