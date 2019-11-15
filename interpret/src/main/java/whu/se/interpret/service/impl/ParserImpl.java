package whu.se.interpret.service.impl;

import whu.se.interpret.exception.ParserException;
import whu.se.interpret.po.*;
import whu.se.interpret.result.Result;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author xsy
 * @description: 语法分析的对外接口
 * @date 2019/9/228:20
 */
public interface ParserImpl {

    //初始化，文法数据结构化，求出first，follow集
    void init(String grammarFileName) throws IOException;

    //读取文法文件，创建文法的数据结构
    ArrayList<Node> getGrammar(String filename) throws IOException;


    //获得first集
    HashMap<String, HashSet<String>> getAllFirst();


    //获得follow集
    HashMap<String, HashSet<String>> getAllFollow();

    //分析文法单元是终结符还是非终结符
    // true -- 终结符
    // false -- 非终结符
    boolean isTerm(String s);

    //获取语法
    ArrayList<Node> getGrammar();

    //通过文法的数据结构生成I项目集规范族(Family)
    Family generateFamily(ArrayList<Node> grammar);

    //通过项目集规范族生成SLR(1)分析表
    SLRTable generateSLRTable(Family family);

    /**
     * @description    :生成语法分析结果 ParserResult（以下简称PR）
     * @param tokens   :词法分析产生的单词序列
     * @return         : 1.PR为空:输入的tokens为空
     *                 : 2.PR中passed为false：语法分析未通过，此时PR中curToken应保存当前token（其中有错误行数信息）
     *                 : 3.PR中passed为false且curToken为空：输入串已访问到结尾 $
     */
    public ParserResult syntaxCheck(List<Token> tokens) throws ParserException, IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

}
