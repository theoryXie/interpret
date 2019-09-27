package whu.se.interpret.po;

import java.util.ArrayList;

/**
 * @author xsy
 * @projectName interpret
 * @description: 产生式
 * @date 2019/9/268:43
 */
public class Node {

    /*
     * 产生式
     * <expr> → <expr> + <term>
     *
     * left -- 产生式左部 -- <expr>
     * right -- 产生式右部 -- <expr> + <term>
     */
    String left;
    ArrayList<String> right;

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

    @Override
    public String toString() {
        return "Node{" +
                "left='" + left + '\'' +
                ", right=" + right +
                '}';
    }
}
