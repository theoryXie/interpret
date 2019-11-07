package whu.se.interpret.service.impl;

import whu.se.interpret.po.FiveParam;
import whu.se.interpret.po.Node;
import whu.se.interpret.po.ParserResult;
import whu.se.interpret.po.SymbolTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xsy
 * @description: 语义分析方法的对外接口
 * @date 2019/9/228:23
 */
public interface SemanticImpl {

    /**
     * 语义分析
     *
     * @author xsy
     * @Param  parserResult -- 语法分析过程
     * @Param  grammar -- 文法
     * @return
     **/
    List<FiveParam> semantic_analysis(ParserResult parserResult, ArrayList<Node> grammar) throws Exception;

    FiveParam executeFiveParam(List<FiveParam> fiveParams,int stopRow) throws Exception;

    SymbolTable debug(ParserResult parserResult,ArrayList<Node> grammar,int row) throws Exception;
}
