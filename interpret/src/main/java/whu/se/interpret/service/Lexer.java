package whu.se.interpret.service;

import org.springframework.stereotype.Service;
import whu.se.interpret.exception.LexerException;
import whu.se.interpret.po.Token;
import whu.se.interpret.result.Result;
import whu.se.interpret.service.impl.LexerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xsy
 * @description: 词法分析接口
 * @date 2019/9/2522:15
 */

@Service
public class Lexer implements LexerImpl {
    int codePoint;
    String temp = "";
    //此数组用于保留 关键字 与 识别的符合进行比较
    String KeyWord[] =
            {"int","float","bool","char","if",
                    "else","while","read","write",
                    "struct","do","break",
                    "void","main","return"};
    //关键字枚举数组
    Token.Symbol KeyWordValues[] =
            {Token.Symbol.intsym, Token.Symbol.floatsym, Token.Symbol.boolsym,
                    Token.Symbol.charsym, Token.Symbol.ifsym, Token.Symbol.elsesym,
                    Token.Symbol.whilesym, Token.Symbol.readsym, Token.Symbol.writesym,
                    Token.Symbol.structsym, Token.Symbol.dosym,
                    Token.Symbol.breaksym, Token.Symbol.voidsym, Token.Symbol.mainsym,
                    Token.Symbol.returnsym};

    Token.Symbol SingleSymbolEnumArr[] = new Token.Symbol[255];

    /*  这一段是测试函数*/
//    public static void main(String[] args) {
//        System.out.println("helloWorld");
//        String a = "b=a\'\"\'//\n" +
//                "b=a*c;//4" +
//                "";
//        parser parser = new parser();
//        List<Token> TokenList = parser.lexer(a);
//        for(int i = 0;i<TokenList.size();i++)
//        {
//            System.out.println(TokenList.get(i).toString());
//        }
//    }


    @Override
    public List<Token> lexer(String code) throws LexerException {
        code = code + " ";
        ArrayList<Token> TokenList = new ArrayList<Token>();
        codePoint = 0;
        int row = 1;
        initSingleSymbolEnumArr(); //初始化单字符枚举数组

        while(codePoint!=code.length())  //当codePoint不会越界时 即字符串没有访问完
        {
            //ch 为当前code指针所指位置的字符
            char ch = code.charAt(codePoint);
            temp = "";
            if(isFilter(ch))  //筛除所有的分隔符
            {
                if(ch=='\n')  //如遇回车则增加行号
                    row++; //可能访问不到 可能需要解决 2019年9月25日18:31:01
                codePoint++;
                continue;
            }
            if (isAlpha(ch)||ch=='_')  //第一个字符是字母或者_的有关键字和标识符
            {
                temp += ch;
                codePoint++;
                ch = code.charAt(codePoint);
                while (isAlpha(ch)||isDigit(ch)||ch=='_')
                {
                    temp += ch;
                    codePoint++;
                    ch = code.charAt(codePoint);
                }
                if(isKeyword(temp))
                {
                    Token mToken = new Token(temp,getKeywordSymbolType(temp),0,row);
                    TokenList.add(mToken);
                }else {
                    Token mToken = new Token(temp, Token.Symbol.ident,0,row);
                    TokenList.add(mToken);
                }
            }
            else if(isDigit(ch))  //第一个字符是数字的有整型和浮点型
            {
                temp += ch;
                codePoint++;
                ch = code.charAt(codePoint);
                boolean firstDot = true;
                while (isDigit(ch)||(ch=='.'&&firstDot))
                {
                    if (ch=='.')
                        firstDot = false;
                    temp += ch;
                    codePoint++;
                    ch = code.charAt(codePoint);
                }
                if(temp.contains("."))
                {
                    Token mToken = new Token(temp, Token.Symbol.fnumber, Float.valueOf(temp), row);
                    TokenList.add(mToken);
                }else {
                    Token mToken = new Token(temp, Token.Symbol.number, Integer.valueOf(temp), row);
                    TokenList.add(mToken);
                }
            }
            else switch (ch)
                {
                    case '+':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='+')
                        {
                            Token mToken = new Token(
                                    "++", Token.Symbol.selfplus, 0, row);
                            TokenList.add(mToken);
                            codePoint++;
                        }else
                        {
                            Token mToken = new Token("+", Token.Symbol.plus, 0, row);
                            TokenList.add(mToken);
                        }
                        break;
                    case '-':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='-')
                        {
                            Token mToken = new Token("--", Token.Symbol.selfminus, 0, row);
                            TokenList.add(mToken);
                            codePoint++;
                        }else
                        {
                            Token mToken = new Token("-", Token.Symbol.minus, 0, row);
                            TokenList.add(mToken);
                        }
                        break;
                    case '/':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='*')
                        {
                            codePoint++;
                            while ( (codePoint!=code.length()) )  //当codePoint不会越界时 即字符串没有访问完
                            {
                                ch = code.charAt(codePoint);
                                if(ch!='*')
                                {
                                    if(ch=='\n')
                                    {
                                        row++;
                                    }
                                    codePoint++;
                                    if(codePoint==code.length()-2)  //如果没有两位可以访问(因为code在导入的时候添加了一位空格 所以此处减2)
                                    {
                                        Token mToken = new Token("", Token.Symbol.errorcomment, 0, row);
                                        TokenList.add(mToken);
                                        codePoint++;
                                        break; //退出
                                    }
                                    continue;
                                }else  //当遇到*号时
                                {
                                    if(codePoint==code.length()-1)  //如果*号为最后一位  这一段可能在上一块代码中已经排除了
                                    {
                                        Token mToken = new Token("", Token.Symbol.errorcomment, 0, row);
                                        TokenList.add(mToken);
                                        break; //退出
                                    }
                                    codePoint++;
                                    ch = code.charAt(codePoint);
                                    if(ch=='/')
                                    {
                                        codePoint++;
                                        break;  //注释部分源码  跳过
                                    }else
                                    {
                                        continue;
                                    }
                                }
                            }
                        }else if(ch=='/')
                        {
                            codePoint++;
                            while (codePoint!=code.length())  //数组没有越界
                            {
                                ch = code.charAt(codePoint);
                                if(ch!='\n')
                                {
                                    codePoint++;
                                    continue;
                                }else
                                {
                                    codePoint++;
                                    row++;
                                    break;
                                }
                            }
                        }else
                        {
                            //codePoint++;
                            Token mToken = new Token("/", SingleSymbolEnumArr['/'], 0, row);
                            TokenList.add(mToken);
                            if(ch=='\n')  //当除号后面是回车时 行号加一
                                row++;
                        }
                        break;
                    case '*':
                        codePoint++;
                        Token mToken = new Token(String.valueOf(ch), SingleSymbolEnumArr[ch], 0, row);
                        TokenList.add(mToken);
                        break;
                    case '<':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='=')
                        {
                            mToken = new Token("<=", Token.Symbol.leq, 0, row);
                            TokenList.add(mToken);
                            codePoint++;
                        }else
                        {
                            mToken = new Token("<", Token.Symbol.lss, 0, row);
                            TokenList.add(mToken);
                        }
                        break;
                    case '>':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='=')
                        {
                            mToken = new Token(">=", Token.Symbol.geq, 0, row);
                            TokenList.add(mToken);
                            codePoint++;
                        }else
                        {
                            mToken = new Token(">", Token.Symbol.gtr, 0, row);
                            TokenList.add(mToken);
                        }
                        break;
                    case '!':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='=')
                        {
                            mToken = new Token("!=", Token.Symbol.neq, 0, row);
                            TokenList.add(mToken);
                            codePoint++;
                        }else
                        {
                            mToken = new Token("!", Token.Symbol.notsym, 0, row);
                            TokenList.add(mToken);
                        }
                        break;
                    case '=':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='=')
                        {
                            mToken = new Token("==", Token.Symbol.eql, 0, row);
                            TokenList.add(mToken);
                            codePoint++;
                        }else
                        {
                            mToken = new Token("=", Token.Symbol.becomes, 0, row);
                            TokenList.add(mToken);
                        }
                        break;
                    case '&':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='&')
                        {
                            mToken = new Token("&&", Token.Symbol.andsym, 0, row);
                            TokenList.add(mToken);
                            codePoint++;
                        }else
                        {
                            mToken = new Token("&", Token.Symbol.errorsym, 0, row);
                            TokenList.add(mToken);
                            System.out.println(String.valueOf(row) + "出错");
                        }
                        break;
                    case '|':
                        codePoint++;
                        ch = code.charAt(codePoint);
                        if(ch=='|')
                        {
                            mToken = new Token("||", Token.Symbol.orsym, 0, row);
                            TokenList.add(mToken);
                            codePoint++;
                        }else
                        {
                            mToken = new Token("|", Token.Symbol.errorsym, 0, row);
                            TokenList.add(mToken);
                            System.out.println(String.valueOf(row) + "出错");
                        }
                        break;
                    case ';':
                    case ',':
                    case '(':
                    case ')':
                    case '[':
                    case ']':
                    case '{':
                    case '}':
                        codePoint++;
                        mToken = new Token(String.valueOf(ch), SingleSymbolEnumArr[ch], 0, row);
                        TokenList.add(mToken);
                        break;
                    case '\'':
                    case '\"':
                        codePoint++;
                        mToken = new Token(String.valueOf(ch), SingleSymbolEnumArr[ch], 0, row);
                        TokenList.add(mToken);
                        break;
                    default:
                        codePoint++;
                        throw new LexerException("非法字符"+String.valueOf(ch));
                        //mToken = new Token(String.valueOf(ch), Token.Symbol.errorsym, 0, row);
                        //TokenList.add(mToken);
                        //break;
                }


        }
        return TokenList;
    }

    @Override
    public boolean isAlpha(char symbol) {
        if(isUpAlpha(symbol)||isLowAlpha(symbol))
            return true;
        else
            return false;
    }

    @Override
    public boolean isDigit(char symbol) {
        if(symbol>='0'&&symbol<='9')
            return true;
        else
            return false;
    }

    @Override
    public boolean isUpAlpha(char symbol) {
        if(symbol>='A'&&symbol<='Z')
            return true;
        else
            return false;
    }

    @Override
    public boolean isLowAlpha(char symbol) {
        if(symbol>='a'&&symbol<='z')
            return true;
        else
            return false;
    }

    @Override
    public Token.Symbol getKeywordSymbolType(String symbol) {
        for (int i = 0 ;i < KeyWord.length ;i++)
        {
            if(symbol.equals(KeyWord[i]))
                return KeyWordValues[i];
        }
        return null;
    }

    @Override
    public boolean isKeyword(String symbol) {
        for (int i = 0 ;i < KeyWord.length ;i++)
        {
            if(symbol.equals(KeyWord[i]))
                return true;
        }
        return false;
    }

    @Override
    public boolean isFilter(char symbol) {
        char Filter[] = {'\n','\t','\r',' '};
        for(int i = 0 ;i < Filter.length ;i++)
        {
            if(symbol == Filter[i])
                return true;
        }
        return false;
    }

    //初始化单字符枚举数组
    private void initSingleSymbolEnumArr() {
        SingleSymbolEnumArr['+'] = Token.Symbol.plus;
        SingleSymbolEnumArr['-'] = Token.Symbol.minus;
        SingleSymbolEnumArr['*'] = Token.Symbol.times;
        SingleSymbolEnumArr['/'] = Token.Symbol.slash;

        SingleSymbolEnumArr['('] = Token.Symbol.lparen;
        SingleSymbolEnumArr[')'] = Token.Symbol.rparen;
        SingleSymbolEnumArr['='] = Token.Symbol.eql;
        SingleSymbolEnumArr[','] = Token.Symbol.comma;
        SingleSymbolEnumArr[';'] = Token.Symbol.semicolon;
        SingleSymbolEnumArr['['] = Token.Symbol.lbracket;
        SingleSymbolEnumArr[']'] = Token.Symbol.rbracket;
        SingleSymbolEnumArr['{'] = Token.Symbol.lbrace;
        SingleSymbolEnumArr['}'] = Token.Symbol.rbrace;
        SingleSymbolEnumArr['\''] = Token.Symbol.SingleQuote;
        SingleSymbolEnumArr['\"'] = Token.Symbol.DoubleQuotes;
    }


    /**
     * 处理词法分析生成的Token序列，生成传给前台的Result包
     *
     * @author xsy
     * @Param Token序列
     * @return Result包
     **/
    @Override
    public Result analysisTokens(List<Token> Tokens){
        boolean isSuccess = true;//是否成功
        StringBuilder wrongMessage = new StringBuilder();//错误信息
        //遍历Token序列，检查有无错误类型
        for(int i = 0; i < Tokens.size(); i++){
            Token temp = Tokens.get(i);//当前Token
            if(temp.getTokenType().toString().equals("errorsym")){ //错误词素
                int row = temp.getRow();//错误词素的行号
                String name = temp.getName();//错误词素的名字  比如@
                isSuccess = false;
                wrongMessage.append("error: 第"+row+"行"+name+"是非法字符\n");
            }
        }
        if(isSuccess){ // 词法分析正确
            String TokenString = Tokens.toString();
            return new Result("词法分析Token序列如下："+TokenString.substring(1,TokenString.length()-1));
        }else{
            return new Result(wrongMessage.toString());
        }

    }





}
