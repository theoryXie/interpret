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





    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Node node:core
             ) {
            stringBuffer.append(node.toSLRString());
        }
        for (Node node:production
        ) {
            stringBuffer.append(node.toSLRString());
        }
        for (String key : pointer.keySet()) {
            stringBuffer.append(key).append(" "+pointer.get(key)).append("\n");
        }
        return stringBuffer.toString();
    }
}
