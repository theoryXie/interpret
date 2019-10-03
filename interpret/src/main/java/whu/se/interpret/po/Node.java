package whu.se.interpret.po;

import java.util.ArrayList;

/**
 * @author xsy
 * @description: 产生式
 * @date 2019/9/26 8:43
 */
public class Node {

    /*
     * 产生式
     * <expr> → <expr> + <term>
     *
     * left -- 产生式左部 -- <expr>
     * right -- 产生式右部 -- <expr> + <term>
     */
    private String left;
    private ArrayList<String> right;
    private int index = 0;

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public ArrayList<String> getRight() {
        return right;
    }

    public void setRight(ArrayList<String> right) {
        this.right = right;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Node{" +
                "left='" + left + '\'' +
                ", right=" + right +
                '}';
    }

    public String toSLRString(){
        StringBuffer stringBuffer =  new StringBuffer();
        for (int i = 0;i<right.size();i++){
            if(i == index) {
                stringBuffer.append(".");
                i--;
            } else
                stringBuffer.append(right.get(i));

        }
        return left + "->"+stringBuffer.toString()+"\n";
    }

}
