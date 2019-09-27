package whu.se.interpret.service.impl;

import whu.se.interpret.po.Token;
import whu.se.interpret.result.Result;

import java.util.List;

/**
 * @author xsy
 * @description: 词法分析给外界的接口
 * @date 2019/9/2121:30
 */
public interface LexerImpl {

    /**
     * 接收代码字符串，进行词法分析，返回token序列
     *
     * @author xsy
     * @Param code -- 代码字符串
     * @return tokens -- token序列
     **/
    public List<Token> lexer(String code);

    //判断是否是数字
    public boolean isDigit(char symbol);

    //判断是否是关键字
    public boolean isKeyword(String symbol);

    //返回关键字的TokenType代码
    public Token.Symbol getKeywordSymbolType(String symbol);

    //判断是否是分隔符   \n \t \r ' '
    public boolean isFilter(char symbol);

    //判断是否为大小写字母
    public boolean isAlpha(char symbol);

    //判断是否为大写字母
    public boolean isUpAlpha(char symbol);

    //判断是否为小写字母
    public boolean isLowAlpha(char symbol);


    //分析生成的token序列
    public Result analysisTokens(List<Token> tokens);
}
