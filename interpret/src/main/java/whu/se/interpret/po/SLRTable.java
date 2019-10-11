package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

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

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> terminators = new HashSet<>();
        for (HashMap<String, ArrayList<Pair>> map : actions) {
            terminators.addAll(map.keySet());
        }
        Set<String> nonTerminators = new HashSet<>();
        for (HashMap<String, ArrayList<Pair>> map : gotos) {
            nonTerminators.addAll(map.keySet());
        }
        stringBuilder.append(String.format("%4s",""));
        for (String s : terminators) {
            stringBuilder.append(String.format("%10s",s));
        }
        for (String s : nonTerminators) {
            stringBuilder.append(String.format("%10s",s));
        }
        stringBuilder.append("\n");
        for (int i = 0; i < actions.size(); i++){
            stringBuilder.append(String.format("%4s",i));
            for (String terminator : terminators) {
                if (actions.get(i).containsKey(terminator)){
                    if (actions.get(i).get(terminator).size() == 1)
                        stringBuilder.append(String.format("%10s",actions.get(i).get(terminator).get(0).toString()));
                    else {
                        String ss = actions.get(i).get(terminator).get(0).toString()
                                + "/" + actions.get(i).get(terminator).get(1).toString();
                        stringBuilder.append(String.format("%10s",ss));
                    }
                } else stringBuilder.append(String.format("%10s",""));
            }
            for (String nonTerminator : nonTerminators) {
                if (gotos.get(i).containsKey(nonTerminator)){
                    stringBuilder.append(String.format("%10s",gotos.get(i).get(nonTerminator).get(0).toString()));
                } else stringBuilder.append(String.format("%10s",""));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
