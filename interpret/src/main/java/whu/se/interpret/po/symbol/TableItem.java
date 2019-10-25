package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xsy
 * @description: 函数变量表的每一项
 * @date 10/24/2019 10:59 PM
 */
@Getter
@Setter
public class TableItem implements Cloneable{
    private String name;//变量名
    private String type;//变量类型
    private Object data;//数据

    public TableItem(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Object clone() {
        TableItem o = null;
        try {
            o = (TableItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

}
