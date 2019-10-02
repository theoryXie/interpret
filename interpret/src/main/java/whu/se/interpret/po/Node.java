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
    private int index;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Node) {
            Node node = (Node) obj;
            if (node.left.equals(this.left) && node.index == this.index && node.right.equals(this.right)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean equalsExceptIndex (Node node) {
        if (node == null) return false;
        if (this == node) return true;
        return node.left.equals(this.left) && node.index == this.index;
    }
}
