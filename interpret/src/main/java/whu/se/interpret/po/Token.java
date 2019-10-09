package whu.se.interpret.po;

/**
 * @author xsy
 * @projectName interpret
 * @description: 用于记录词素的token
 * @date 2019/9/2121:23
 */
public class Token {
    public enum Symbol {
        nul, ident, number, fnumber, plus, minus, times, slash,
        eql, neq, leq, geq, lss, gtr,
        andsym, orsym, notsym, selfplus, selfminus,
        lparen, rparen, lbracket, rbracket,
        lbrace, rbrace, comma, semicolon, becomes, SingleQuote, DoubleQuotes,
        intsym, floatsym, boolsym, charsym,
        ifsym, elsesym, whilesym, readsym, writesym,
        realsym, structsym, dosym, breaksym, errorsym,
        voidsym, mainsym, returnsym, errorcomment
    };
    String name;  //该词本身字符串形式
    Symbol tokenType;  //该词枚举码
    int row;  //行号
    int value;  //整型数的值
    float fvalue;  //浮点数的值
    public Token(String name,Symbol tokenType,int value,int row)
    {
        this.name = name;
        this.tokenType = tokenType;
        this.value = value;
        this.row = row;
    }
    public Token(String name,Symbol tokenType,float fvalue,int row)
    {
        this.name = name;
        this.tokenType = tokenType;
        this.fvalue = fvalue;
        this.row = row;
    }
    public Token(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\ntoken{" +
                "name='" + name + '\'' +
                ", tokenType=" + tokenType +
                ", row=" + row +
                ", value=" + value +
                ", fvalue=" + fvalue +
                "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Symbol getTokenType() {
        return tokenType;
    }

    public void setTokenType(Symbol tokenType) {
        this.tokenType = tokenType;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getFvalue() {
        return fvalue;
    }

    public void setFvalue(float fvalue) {
        this.fvalue = fvalue;
    }
}
