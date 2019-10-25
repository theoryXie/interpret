package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author xsy
 * @description: 各函数的变量表
 * @date 10/24/2019 10:59 PM
 */
@Setter
@Getter
public class ParamsTable implements Cloneable{
    private ArrayList<TableItem> tableItems;

    public ParamsTable(){
        tableItems = new ArrayList<>();
    }

    public Object clone() {
        ParamsTable o = null;
        try {
            o = (ParamsTable) super.clone();
            ArrayList<TableItem> ts = new ArrayList<>();
            for(int i = 0; i<tableItems.size();i++){
                ts.add((TableItem)tableItems.get(i).clone());
            }
            o.setTableItems(ts);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
