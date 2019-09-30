package whu.se.interpret.service.impl;

import whu.se.interpret.po.Family;
import whu.se.interpret.po.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author xsy
 * @description: 语法分析的对外接口
 * @date 2019/9/228:20
 */
public interface ParserImpl {

    //初始化，文法数据结构化，求出first，follow集
    public void init(String grammarFileName) throws IOException;

    //读取文法文件，创建文法的数据结构
    public ArrayList<Node> getGrammar(String filename) throws IOException;


    //获得first集
    public HashMap<String, HashSet<String>> getAllFirst();


    //获得follow集
    public HashMap<String, HashSet<String>> getAllFollow();

    //分析文法单元是终结符还是非终结符
    // true -- 终结符
    // false -- 非终结符
    public boolean isTerm(String s);

    //通过文法的数据结构生成I0项目集规范簇(Family)
    public Family generateFamily();


}
