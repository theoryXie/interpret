package whu.se.interpret.service.impl;

import whu.se.interpret.po.token;

import java.util.List;

/**
 * @author xsy
 * @description: 词法分析给外界的接口
 * @date 2019/9/2121:30
 */
public interface wordAnalysisImpl {

    /**
     * 接收代码字符串，进行词法分析，返回token序列
     *
     * @Param code -- 代码字符串
     * @return tokens -- token序列
     **/
    public List<token> wordAnalysis(String code);

    //TODO 判断是否是数字

    //TODO 判断是否是关键字

    //TODO 判断是否是...
}
