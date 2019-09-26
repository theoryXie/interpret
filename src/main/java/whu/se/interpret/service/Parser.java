package whu.se.interpret.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import whu.se.interpret.po.Node;
import whu.se.interpret.service.impl.ParserImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author xsy
 * @description: 语法分析方法的实现类
 * @date 2019/9/228:21
 */

@Service
public class Parser implements ParserImpl {

    ArrayList<Node> grammar = new ArrayList<>();//产生式序列
    HashMap<String, HashSet<String>> firstSet = new HashMap<>();//所有非终结符号的first集
    HashMap<String, HashSet<String>> followSet = new HashMap<>();//所有非终结符号的follow集


    @Override
    public boolean isTerm(String s) {
        //s为非终结符号
        if(s.charAt(0) == '<' && s.charAt(s.length()-1) == '>')
            return false;
        //s为空串
        else if(s.equals("ε"))
            return false;
        //s为终结符号
        return true;
    }

    @Override
    public ArrayList<Node> getGrammar(String filename) throws FileNotFoundException {
        //获取资源文件夹static
        File path = new File(ResourceUtils.getURL("classpath:static").getPath().replace("%20"," ").replace('/', '\\'));
        //获取资源文件夹下的文法文件
        File grammarFile = new File(path.getAbsolutePath(),filename);
        //最终的文法序列
        ArrayList<Node> grammar = new ArrayList<>();
        //逐行读取文法
        try(
                FileReader fr = new FileReader(grammarFile);
                BufferedReader br = new BufferedReader(fr);
                )
        {
            String line = "";

            while ((line = br.readLine())!=null){
                Node node = new Node();//产生式
                String[] splits = line.split(" ");//根据空格分割产生式字符串
                node.setLeft(splits[0]);//产生式左部
                ArrayList<String> right = new ArrayList<>();//产生式右部
                //将文法单元添加到产生式右部
                for(int i = 2; i < splits.length; i++){
                    right.add(splits[i]);
                }
                node.setRight(right);
                grammar.add(node);
            }
            return grammar;
        }catch (IOException ioe){
            ioe.printStackTrace();
            return null;
        }

    }


    public HashSet<String> getFirst(String target) {
        HashSet<String> first_set = new HashSet<>();
        //遍历产生式序列
        for(int i = 0; i < grammar.size(); i++){
            //如果产生式左边等于target
            if(grammar.get(i).getLeft().equals(target)){
                //遍历产生式右部
                ArrayList<String> right = grammar.get(i).getRight();//产生式右部
                int right_size = right.size();
                for(int j = 0; j < right_size; j++){
                    //当前节点
                    String temp = right.get(j);

                    //当前节点为终结符号
                    if(isTerm(temp)){
                        first_set.add(temp);//直接加入target的first集
                        break;
                    }

                    //当前节点为空串
                    else if(temp.equals("ε")){
                        //如果ε是产生式右部的最后一个元素，直接将ε加入到target的first集
                        if(j == right_size-1){
                            first_set.add("ε");
                        }
                    }

                    //当前节点为非终结符号
                    else{
                        //当右部当前元素等于左部时，直接跳过，避免左递归造成死循环
                        if(!temp.equals(grammar.get(i).getLeft())){
                            //求当前非终结符号的first集
                            HashSet<String> tempFirstSet = getFirst(temp);
                            //如果temp的first集包含空串
                            if(tempFirstSet.contains("ε")){
                                tempFirstSet.remove("ε");
                                first_set.addAll(tempFirstSet);//temp的first集 和 target的first集取并集
                            }else{
                                first_set.addAll(tempFirstSet);//temp的first集 和 target的first集取并集
                                break;
                            }
                        }else
                            break;
                    }
                }
            }
        }
        return first_set;
    }


    public HashSet<String> getFollow(String target) {
        return null;
    }

    @Override
    public void init() throws FileNotFoundException {
        this.grammar = getGrammar("grammar.txt");
        for(int i = 0; i < grammar.size(); i++){
            String left = grammar.get(i).getLeft();//产生式左部
            if(!firstSet.containsKey(left)) {
                firstSet.put(left, getFirst(left));//将此产生式的first集放入总first集中
            }
        }
    }

    @Override
    public HashMap<String, HashSet<String>> getAllFirst() {
        return firstSet;
    }

    @Override
    public HashMap<String, HashSet<String>> getAllFollow() {
        return null;
    }
}
