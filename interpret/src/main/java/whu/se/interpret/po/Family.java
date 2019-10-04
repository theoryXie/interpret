package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author ：Chang Jiaxin
 * @date ：Created in 2019/9/29 16:49
 * @description ： 项目集规范族
 */
@Getter
@Setter
public class Family {
    private ArrayList<ProjectSet> sets; //所有项目集

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sets.size(); i++) {
            stringBuilder.append("I"+i).append(":\n").append(sets.get(i)+"\n");
        }
        return stringBuilder.toString();
    }
}
