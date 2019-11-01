package whu.se.interpret.service.impl;

import whu.se.interpret.po.Node;
import whu.se.interpret.po.ParserResult;

import java.util.ArrayList;

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
    void semantic_analysis(ParserResult parserResult, ArrayList<Node> grammar) throws Exception;
}
