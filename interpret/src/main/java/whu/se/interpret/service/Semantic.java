package whu.se.interpret.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.se.interpret.po.*;
import whu.se.interpret.po.symbol.*;
import whu.se.interpret.service.impl.LexerImpl;
import whu.se.interpret.service.impl.SemanticImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author xsy
 * @description: 语义分析方法的实现类
 * @date 2019/9/22 8:23
 */

@Service
public class Semantic implements SemanticImpl {


    @Autowired
    LexerImpl lexerImpl;

    private List<String> executeName = new ArrayList<>();

    private boolean judgeExecute(String name) {
        if (executeName.contains(name)) {
            return true;
        }
        return false;
    }



    private int operationType(Token token1, Token token2,SymbolTable symbolTable) {
        Token.Symbol tokenSymbol_1 = token1.getTokenType();
        Token.Symbol tokenSymbol_2 = token2.getTokenType();
        if(tokenSymbol_1 == Token.Symbol.ident){
            String idTokenType = checkIdType(token1.getName(),symbolTable);
            if (idTokenType.equals("int")){
                tokenSymbol_1 = Token.Symbol.number;
            }else if(idTokenType.equals("float")){
                tokenSymbol_1 = Token.Symbol.fnumber;
            }
        }
        if(tokenSymbol_2 == Token.Symbol.ident){
            String idTokenType = checkIdType(token2.getName(),symbolTable);
            if (idTokenType.equals("int")){
                tokenSymbol_2 = Token.Symbol.number;
            }else if(idTokenType.equals("float")){
                tokenSymbol_2 = Token.Symbol.fnumber;
            }
        }
        if (tokenSymbol_1 == Token.Symbol.number && tokenSymbol_2 == Token.Symbol.number) {
            return 1;
        } else if (tokenSymbol_1 == Token.Symbol.number && tokenSymbol_2 == Token.Symbol.fnumber) {
            return 2;
        } else if (tokenSymbol_1 == Token.Symbol.fnumber && tokenSymbol_2 == Token.Symbol.number) {
            return 3;
        } else {
            return 4;
        }

    }

    private boolean isIdentDeclare(String name,SymbolTable symbolTable){
        String ans = checkIdType(name,symbolTable);
        if (!ans.equals("变量未声明"))
            return true;
        return false;
    }
    private String checkIdType(String name,SymbolTable symbolTable){
        String ans = "";
        SymbolTable now = symbolTable;
        boolean isFind = false;
        do{
            for (TableItem tableItem : now.getTableItems()) {
                if(tableItem.getName().equals(name)){
                    ans = tableItem.getType();
                    isFind = true;
                    break;
                }
            }
            if(isFind){
                break;
            }
            if(now.getName().equals("全局"))
                break;
            now = now.getPrePointer();
        }   while (true);

        if(isFind){
            return ans;
        }else {
            return "变量未声明";
        }
    }
    //语义分析
    @Override
    public List<FiveParam> semantic_analysis(ParserResult parserResult, ArrayList<Node> grammar) throws Exception {
        ArrayList<ArrayList<Object>> symbols_object = parserResult.getSymbols_Object();//过程集合
        ArrayList<Pair> pairs = parserResult.getPairs();
        ArrayList<SymbolTable> symbolTables = new ArrayList<>();//符号表序列
        SymbolTable wholeTable = new SymbolTable("全局", "void");//全局符号表
        symbolTables.add(wholeTable);
        SymbolTable nowTablePointer = wholeTable;//当前指针指向全局符号表
        //向可执行函数数组里添加全局main
        executeName.add("全局");
        executeName.add("main");
        //总的五元式序列
        ArrayList<FiveParam> wholeFiveParams = new ArrayList<>();
        //T的下标
        int T_index = 0;
        int F_index = 0;
        //全局返回值
        Object returnValue;
        int pair_size = pairs.size();
        for (int i = 0; i < pair_size; i++) {
            Pair pair = pairs.get(i);//当前动作
            char c = pair.getC();//r || s
            int num = pair.getNum();
            ArrayList<Object> post = symbols_object.get(i);
            int post_size = post.size();
            if (c == 'S') {
                //判断当前移进的是否是‘{’
                Terminal terminal = (Terminal) post.get(post_size - 1);
                if (terminal.getToken().getName().equals("{")) {
                    //如果前面是funcDecl，则不创建新符号表
                    if (post.get(post_size-2) instanceof FuncDecl) {
                        continue;
                    }
                    SymbolTable symbolTable = new SymbolTable("__F" + F_index);//新建函数符号表
                    F_index++;
                    symbolTable.setParamNum(0);
                    //记录他的上级来确定这个新符号表是否要执行
//                    if(judgeExecute(nowTablePointer.getName()))
//                    {
//                        executeName.add(symbolTable.getName());
//                    }else {
//                        FiveParam fiveParam = new FiveParam("Call","_","_",symbolTable.getName(),terminal.getToken().getRow());
//                    }
                    //2019.11.4 20:48后  全部生成五元式后再统一执行
//                    FiveParam fiveParam = new FiveParam("Call", "_", "_", symbolTable.getName(), terminal.getToken().getRow());
//                    nowTablePointer.getFiveParams().add(fiveParam);
//                    wholeFiveParams.add(fiveParam);
//                    fiveParam.setPointer(nowTablePointer);
                    //*2019.11.4 20:48
                    symbolTable.setPrePointer(nowTablePointer);
                    nowTablePointer = symbolTable;
                    symbolTables.add(symbolTable);
                }
            } else if (c == 'r') {
                ArrayList<Object> pre = symbols_object.get(i - 1);//i不会为0，因为第一步必定是移进
                int pre_size = pre.size();
                if (num == 0) {
                    //TODO acc
                } else {
                    if (num >= 27 && num <= 30) {
                        //<Type> → int | char | float | void
                        Type type = (Type) post.get(post_size - 1);
                        Terminal terminal = (Terminal) pre.get(pre_size - 1);//int | char | float | void
                        type.setType(terminal.getToken().getName());//Type.type = int.type...
                    } else if (num == 77) {
                        //<N>-> ε
                        N n = (N)post.get(post_size-1);
                        n.getNextList().add(wholeFiveParams.size());
                        FiveParam fiveParam = new FiveParam("j","_","_","0",0);
                        wholeFiveParams.add(fiveParam);
                        nowTablePointer.getFiveParams().add(fiveParam);
                        fiveParam.setPointer(nowTablePointer);
                    } else if (num == 76) {
                        //<M>-> ε
                        M m = (M)post.get(post_size-1);
                        m.setQuad(wholeFiveParams.size());
                    } else if (num == 73){
                        //<Factor> → <FuncUse>
                        Factor factor = (Factor) post.get(post_size - 1);
                        FuncUse funcUse = (FuncUse)pre.get(pre_size-1) ;
                        FiveParam fiveParam = new FiveParam("getReturn","_","_","0T"+T_index,funcUse.getToken().getRow());

                        wholeFiveParams.add(fiveParam);
                        fiveParam.setPointer(nowTablePointer);
                        TableItem tableItem = null;
                        for (SymbolTable symbolTable : symbolTables) {
                            //遍历符号表序列
                            if (symbolTable.getName().equals(funcUse.getToken().getName())){
                                //找到funcUse的符号表
                                if (symbolTable.getReturnType().equals("int")){
                                    //利用符号表返回值类型初始化Factor
                                    tableItem = new TableItem("0T"+T_index,"int",null);
                                    factor.setToken(new Token("0T"+T_index,Token.Symbol.number,0,funcUse.getToken().getRow()) ) ;
                                }else if (symbolTable.getReturnType().equals("float")){
                                    tableItem = new TableItem("0T"+T_index,"float",null);
                                    factor.setToken(new Token("0T"+T_index,Token.Symbol.fnumber,0,funcUse.getToken().getRow()) ) ;
                                }
                            }
                        }
                        nowTablePointer.getTableItems().add(tableItem);
                        nowTablePointer.getFiveParams().add(fiveParam);
                        T_index++;
                    }
                    else if (num == 71 || num == 72) {
                        //<Factor>→num | real
                        Factor factor = (Factor) post.get(post_size - 1);
                        Terminal terminal = (Terminal) pre.get(pre_size - 1);//num | real
                        factor.setToken(terminal.getToken());//factor.token = num.token...
                    } else if (num == 70) {
                        //<factor>-><loc>
                        Loc loc = (Loc) pre.get(pre_size - 1);
                        Factor factor = (Factor) post.get(post_size - 1);
                        factor.setToken(loc.getToken());//factor.token = loc.token
                    } else if (num == 68) {
                        //<Unary> → <Factor>
                        Unary unary = (Unary) post.get(post_size - 1);
                        Factor factor = (Factor) pre.get(pre_size - 1);
                        unary.setToken(factor.getToken());//unary.token = factor.token
                    } else if (num == 65) {
                        //<Term>→<Unary>
                        Term term = (Term) post.get(post_size - 1);
                        Unary unary = (Unary) pre.get(pre_size - 1);
                        term.setToken(unary.getToken());//term.token = unary.token
                    } else if (num == 62) {
                        //<Expr>→<Term>
                        Expr expr = (Expr) post.get(post_size - 1);
                        Term term = (Term) pre.get(pre_size - 1);
                        expr.setToken(term.getToken());//expr.token = term.token
                    } else if (num == 60) {
                        //<Expr1> → <Expr2> + <Term>
                        Term term = (Term) pre.get(pre_size - 1);
                        Expr expr2 = (Expr) pre.get(pre_size - 3);
                        Expr expr1 = (Expr) post.get(post_size - 1);
                        String name = nowTablePointer.getName();   //当前符号表的名字
                        //执行
                        Token token1 = term.getToken();
                        Token token2 = expr2.getToken();
                        int type_ans = operationType(token1,token2,nowTablePointer);
                        Token token;


                        //生成五元式
                        FiveParam fiveParam = new FiveParam("+", token1.getName(), token2.getName(), "0T" + T_index, token1.getRow());
                        wholeFiveParams.add(fiveParam);
                        fiveParam.setPointer(nowTablePointer);
                        expr1.getToken().setName("0T" + T_index);
                        TableItem item = null;
                        if (type_ans == 1) {
                            item = new TableItem(expr1.getToken().getName(), "int", null, false);
                            expr1.getToken().setTokenType(Token.Symbol.number);
                        } else {
                            item = new TableItem(expr1.getToken().getName(), "float", null, false);
                            expr1.getToken().setTokenType(Token.Symbol.fnumber);
                        }
                        nowTablePointer.getTableItems().add(item);
                        T_index++;
                        nowTablePointer.getFiveParams().add(fiveParam);

//                        if(judgeExecute(name)){
//                                switch (type_ans){
//                                    case 1:
//                                        token = new Token("", Token.Symbol.number,token1.getValue()+token2.getValue(),token1.getRow());
//                                        break;
//                                    case 2:
//                                        token = new Token("", Token.Symbol.fnumber,token1.getValue()+token2.getFvalue(),token1.getRow());
//                                        break;
//                                    case 3:
//                                        token = new Token("", Token.Symbol.fnumber,token1.getFvalue()+token2.getValue(),token1.getRow());
//                                        break;
//                                    case 4:
//                                        token = new Token("", Token.Symbol.fnumber,token1.getFvalue()+token2.getFvalue(),token1.getRow());
//                                        break;
//                                    default:
//                                        throw new IllegalStateException("Unexpected value: " + type_ans);
//                                }
//                                expr1.setToken(token);
//                        }
//                        else{
//                            //生成五元式
//                            FiveParam fiveParam = new FiveParam("+",token1.getName(),token2.getName(),"0T"+T_index,token1.getRow());
//                            expr1.getToken().setName("0T"+T_index);
//                            TableItem item = null;
//                            if(type_ans==1){
//                                item = new TableItem(expr1.getToken().getName(),"int",null,false);
//                                expr1.getToken().setTokenType(Token.Symbol.number);
//                            }else {
//                                item = new TableItem(expr1.getToken().getName(),"float",null,false);
//                                expr1.getToken().setTokenType(Token.Symbol.fnumber);
//                            }
//                            nowTablePointer.getTableItems().add(item);
//                            T_index++;
//                            nowTablePointer.getFiveParams().add(fiveParam);
//                        }


                    } else if (num == 59) {
                        //<Rel>→<Expr>
                        Rel rel = (Rel) post.get(post_size - 1);
                        Expr expr = (Expr) pre.get(pre_size - 1);
                        rel.setToken(expr.getToken());//rel.token = expr.token
                    } else if (num == 55 || num == 56 || num == 57 || num == 58){
                        //<Rel> → <Expr1> < <Expr2> | ....
                        Rel rel = (Rel) post.get(post_size - 1);
                        Expr expr1 = (Expr) pre.get(pre_size - 3);
                        Expr expr2 = (Expr) pre.get(pre_size - 1);
                        Terminal op = (Terminal) pre.get(pre_size-2);
                        int next_quard = wholeFiveParams.size();//下一条五元式下标
                        rel.getTrueList().add(next_quard);
                        rel.getFalseList().add(next_quard+1);
                        FiveParam fiveParam = new FiveParam("j"+op.getName(),expr1.getToken().getName(),expr2.getToken().getName(),"0",op.getToken().getRow());
                        FiveParam fiveParam1 = new FiveParam("j","_","_","0",op.getToken().getRow());
                        wholeFiveParams.add(fiveParam);
                        fiveParam.setPointer(nowTablePointer);
                        wholeFiveParams.add(fiveParam1);
                        fiveParam1.setPointer(nowTablePointer);
                        nowTablePointer.getFiveParams().add(fiveParam);
                        nowTablePointer.getFiveParams().add(fiveParam1);

                    } else if (num == 54) {
                        //<Equality> → <Rel>
                        Equality equality = (Equality) post.get(post_size - 1);
                        Rel rel = (Rel) pre.get(pre_size - 1);
                        equality.setToken(rel.getToken());//rel.token = expr.token
                        equality.setFalseList(rel.getFalseList());
                        equality.setTrueList(rel.getTrueList());
                    } else if (num == 51) {
                        //<Join> → <Equality>
                        Join join = (Join) post.get(post_size - 1);
                        Equality equality = (Equality) pre.get(pre_size - 1);
                        join.setToken(equality.getToken());//rel.token = expr.token
                        join.setFalseList(equality.getFalseList());
                        join.setTrueList(equality.getTrueList());
                    } else if(num == 50){
                        //<Join1> → <Equality> && <M> <Join2>
                        Equality equality = (Equality) pre.get(pre_size-4);
                        Join join1 = (Join) post.get(post_size-1);
                        Join join2 = (Join) pre.get(pre_size-1);
                        M m = (M) pre.get(pre_size-2);
                        for (int integer : equality.getTrueList()) {
                            String quad = String.valueOf(m.getQuad());
                            wholeFiveParams.get(integer).setParam_3(quad);
                        }
                        join1.setTrueList(join2.getTrueList());
                        join1.getFalseList().addAll(equality.getFalseList());
                        join1.getFalseList().addAll(join2.getFalseList());
                    }
                    else if (num == 49) {
                        //<Bool>→<Join>
                        Bool bool = (Bool) post.get(post_size - 1);
                        Join join = (Join) pre.get(pre_size - 1);
                        bool.setToken(join.getToken());//rel.token = expr.token
                        bool.setTrueList(join.getTrueList());
                        bool.setFalseList(join.getFalseList());
                    } else if (num == 46) {
                        //<loc>->id
                        Terminal id = (Terminal) pre.get(pre_size - 1);
                        Loc loc = (Loc) post.get(post_size - 1);
                        loc.setToken(id.getToken());
                    } else if (num == 44) {
                        //<Asgn> → <Loc> = <Bool> ;
                        Bool bool = (Bool) pre.get(pre_size - 2);
                        Loc loc = (Loc) pre.get(pre_size - 4);
                        String locName = loc.getToken().getName();
                        boolean is_declare = false;
                        //检查变量声明有没有声明
                        is_declare=isIdentDeclare(locName,nowTablePointer);
                        if(!is_declare){
                            throw new Exception(String.format("第%d行变量%s未声明",loc.getToken().getRow(),locName));
                        }

                        FiveParam fiveParam = new FiveParam("=", bool.getToken().getName(), "_", locName, loc.getToken().getRow());
                        wholeFiveParams.add(fiveParam);
                        fiveParam.setPointer(nowTablePointer);
                        nowTablePointer.getFiveParams().add(fiveParam);

                    } else if (num == 41) {
                        //<Stmt> -> <Block>
                        //
                    } else if (num == 40) {
                        //<Stmt> -> return <Bool> ;
                        Bool bool = (Bool) pre.get(pre_size - 2);
                        String funcName = nowTablePointer.getName();
                        FiveParam fiveParam = new FiveParam("ret", "_", "_", bool.getToken().getName(), bool.getToken().getRow());
                        wholeFiveParams.add(fiveParam);
                        fiveParam.setPointer(nowTablePointer);
                        nowTablePointer.getFiveParams().add(fiveParam);
//                        if(judgeExecute(funcName)){
//                            //TODO 直接结束吧
//                        }else{
//                            FiveParam fiveParam = new FiveParam("ret","_","_",bool.getName(),bool.getToken().getRow());
//                            nowTablePointer.getFiveParams().add(fiveParam);
//                        }
                    } else if (num == 36) {
                        //<Stmt1>→if(<Bool>)<M1><Stmt2><N>else<M2><Stmt3>
                        Stmt stmt1 = (Stmt) post.get(post_size-1);
                        Stmt stmt3 = (Stmt) pre.get(pre_size-1);
                        M m2 = (M) pre.get(pre_size-2);
                        N n  =(N) pre.get(pre_size-4);
                        Stmt stmt2 = (Stmt) pre.get(pre_size-5);
                        M m1 = (M)pre.get(pre_size-6);
                        Bool bool = (Bool) pre.get(pre_size-8);
                        for (Integer integer : bool.getTrueList()) {
                            wholeFiveParams.get(integer).setParam_3(String.valueOf(m1.getQuad()));
                        }
                        for (Integer integer : bool.getFalseList()) {
                            wholeFiveParams.get(integer).setParam_3(String.valueOf(m2.getQuad()));
                        }
                        stmt1.getNextList().addAll(stmt2.getNextList());
                        stmt1.getNextList().addAll(n.getNextList());
                        stmt1.getNextList().addAll(stmt3.getNextList());
                    } else if (num == 34) {
                        //<Stmt>→<Asgn>
                    } else if (num == 33) {
                        //<stmt> -> <Decl>
                    } else if (num == 32) {
                        //<stmts> -> <stmt>
                    } else if (num == 31) {
                        //<Stmts1> → <Stmt> <M> <Stmts2>
                        M m = (M) pre.get(pre_size-2);
                        Stmt stmt = (Stmt) pre.get(pre_size-3);
                        for (Integer integer : stmt.getNextList()) {
                            wholeFiveParams.get(integer).setParam_3(String.valueOf(m.getQuad()));
                        }
                        //backpatch(stmt.next,M.quad);
                    } else if (num == 26) {
                        //<Block>->{ <stmts> }
                        //TODO 可能会有return的问题
                        nowTablePointer = nowTablePointer.getPrePointer();
                    } else if (num == 25) {
                        //<Bools> → <Bool>
                        Bools bools = (Bools) post.get(post_size - 1);
                        Bool bool = (Bool) pre.get(pre_size - 1);
                        bools.getTokens().add(bool.getToken());//<Bools>.token = Bool.token
                    } else if (num == 24) {
                        //<Bools1> → <Bool> , <Bools2>
                        Bools bools_1 = (Bools) post.get(post_size - 1);
                        Bools bools_2 = (Bools) pre.get(pre_size - 1);
                        Bool bool = (Bool) pre.get(pre_size - 3);
                        bools_1.getTokens().add(bool.getToken());
                        bools_1.getTokens().addAll(bools_2.getTokens());
                    } else if (num == 22) {
                        //<FuncUse> → id ( <Bools> )
                        Terminal bracket = (Terminal) pre.get(pre_size-1);
                        Terminal id = (Terminal) pre.get(pre_size - 4);
                        Bools bools = (Bools) pre.get(pre_size - 2);
                        FuncUse funcUse = (FuncUse)post.get(post_size-1) ;
                        funcUse.setToken(id.getToken());
                        String FunName = id.getToken().getName();
                        boolean is_declare = false;
                        for (SymbolTable symbolTable : symbolTables) {
                            //遍历符号表序列
                            if (symbolTable.getName().equals(FunName)) {
                                //找到与当前函数名名字相同的符号表
                                is_declare = true;  //函数已声明
                                if (symbolTable.getParamNum() != bools.getTokens().size())
                                    throw new Exception("第" + id.getToken().getRow() + "行" + FunName + "函数参数个数不匹配");
                                //遍历符号表(寻找参数)
                                for (TableItem tableItem : symbolTable.getTableItems()) {
                                    if (tableItem.isParam()) {
                                        //是参数
                                        int paramIndex = symbolTable.getTableItems().indexOf(tableItem);//获取参数位置
                                        Token paramToken = bools.getTokens().get(paramIndex);
                                        //对比参数类型
                                        if (paramToken.getTokenTypeString().equals(tableItem.getType())) {
                                            FiveParam temp_five = new FiveParam("param", "_", "_", paramToken.getName(), bracket.getToken().getRow());
                                            nowTablePointer.getFiveParams().add(temp_five);
                                            wholeFiveParams.add(temp_five);
                                            temp_five.setPointer(nowTablePointer);
                                        } else {
                                            throw new Exception("第" + bracket.getToken().getRow() + "行" + FunName + "函数第" + (paramIndex + 1) + "参数" + tableItem.getName() + "类型不匹配");
                                        }
                                    } else
                                        break;   //参数都访问完了
                                }
                            }
                        }
                        //生成调用函数的五元式
                        FiveParam fiveParam = new FiveParam("Call", "_", "_", id.getToken().getName(), bracket.getToken().getRow());
                        wholeFiveParams.add(fiveParam);
                        fiveParam.setPointer(nowTablePointer);
                        nowTablePointer.getFiveParams().add(fiveParam);
                        if (!is_declare)
                            throw new Exception("第" + id.getToken().getRow() + "行" + FunName + "函数未声明");

//                        if (judgeExecute(nowTablePointer.getName())){
//                             //当前代码正在运行
//                            boolean is_declare = false;
//
//                            //TODO  执行当前FunName下的五元式    没有进行同名函数的处理
//                            for (SymbolTable symbolTable : symbolTables) {
//                                //遍历符号表序列
//                                if(symbolTable.getName().equals(FunName)){
//                                    //若符号表名字与当前函数名字相同
//                                    is_declare = true;
//                                    executeName.add(FunName);//加入可执行函数名
//                                    nowTablePointer=symbolTable; //将指针指向当前符号表
//                                    if(symbolTable.getParamNum() != bools.getTokens().size())
//                                        throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数参数个数不匹配");
//                                    for (TableItem tableItem : nowTablePointer.getTableItems()) {
//                                        //遍历符号表参数
//                                        if(tableItem.isParam()){
//                                            //若为函数参数
//                                            int temp_tableItemIndex = nowTablePointer.getTableItems().indexOf(tableItem);  //记录参数第几个
//                                            Token paramToken = bools.getTokens().get(temp_tableItemIndex);
//                                            if(paramToken.getTokenTypeString().equals(tableItem.getType())){
//                                                //对比参数类型后
//                                                tableItem.setData(paramToken.getObjectValue());//因为当前函数是立即执行的所以前面的已经计算过了，可以直接取值
//                                            }else {
//                                                throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数第" + (temp_tableItemIndex+1) + "参数" + tableItem.getName() + "类型不匹配");
//                                            }
//                                            //2019.11.4 16:42
//                                        }else {
//                                            break;
//                                        }
//                                    }
//                                    //TODO 在这里执行当前符号表的五元式
//                                    //TODO  执行完五元式要跳回原来执行的地方  并且返回返回值   此处要赋值并且要对比返回值类型是否正确？
//                                    break;
//                                }
//                            }
//                            if (!is_declare)
//                                throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数未声明");
//                        }
//                        else{
//                            boolean is_declare = false;
//                            for (SymbolTable symbolTable : symbolTables) {
//                                if(symbolTable.getName().equals(FunName)){
//                                    is_declare = true;
//                                    if(symbolTable.getParamNum() != bools.getTokens().size())
//                                        throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数参数个数不匹配");
//                                    //遍历符号表(寻找参数)
//                                    for (TableItem tableItem : symbolTable.getTableItems()) {
//                                        if(tableItem.isParam()){
//                                            //是参数
//                                            int paramIndex = symbolTable.getTableItems().indexOf(tableItem);//获取参数位置
//                                            Token paramToken = bools.getTokens().get(paramIndex);
//                                            //对比参数类型
//                                            if(paramToken.getTokenTypeString().equals(tableItem.getType())){
//                                                FiveParam temp_five = new FiveParam("param","_","_", paramToken.getName(),paramToken.getRow());
//                                                nowTablePointer.getFiveParams().add(temp_five);
//                                            }
//                                        }
//                                        else
//                                            break;
//                                    }
//                                }
//                            }
//                            //生成调用函数的五元式
//                            FiveParam fiveParam = new FiveParam("Call","_","_",id.getToken().getName(),id.getToken().getRow());
//                            nowTablePointer.getFiveParams().add(fiveParam);
//                            if(!is_declare)
//                                throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数未声明");
//                        }
                    } else if (num == 21) {
                        //<ParamDecl>→<Type>id
                        Terminal id = (Terminal) pre.get(pre_size - 1);
                        Type type = (Type) pre.get(pre_size - 2);
                        ParamDecl paramDecl = (ParamDecl) post.get(post_size - 1);
                        paramDecl.setParamName(id.getToken().getName());
                        paramDecl.setType(type.getType());
                    } else if (num == 20) {
                        //<ParamDecls>→<ParamDecl>
                        ParamDecl paramDecl = (ParamDecl) pre.get(pre_size - 1);
                        ParamDecls paramDecls = (ParamDecls) post.get(post_size - 1);
                        paramDecls.getParams().add(paramDecl);
                    } else if (num == 19) {
                        //<ParamDecls>1→<ParamDecl>,<ParamDecls>2
                        ParamDecl paramDecl = (ParamDecl) pre.get(pre_size - 3);
                        ParamDecls paramDecls2 = (ParamDecls) pre.get(pre_size - 1);
                        ParamDecls paramDecls1 = (ParamDecls) post.get(post_size - 1);
                        paramDecls1.getParams().add(paramDecl);
                        paramDecls1.getParams().addAll(paramDecls2.getParams());
                    } else if (num == 18) {
                        //<FuncDecl>→id()
                        Terminal id = (Terminal) pre.get(pre_size - 3);
                        FuncDecl funcDecl = (FuncDecl) post.get(post_size - 1);
                        String FunctionName = id.getToken().getName();
                        funcDecl.setFuncName(FunctionName);

                        for (SymbolTable symbolTable : symbolTables) {
                            if (symbolTable.getName().equals(FunctionName))
                                throw new Exception(String.format("第%d行出现重名函数%s",id.getToken().getRow(),FunctionName));
                        }
                        SymbolTable symbolTable = new SymbolTable(FunctionName);//新建函数符号表
                        symbolTable.setParamNum(0);
                        symbolTable.setPrePointer(nowTablePointer);
                        nowTablePointer = symbolTable;
                        symbolTables.add(symbolTable);
                    } else if (num == 17) {
                        //<FuncDecl>→id(<ParamDecls>)
                        ParamDecls paramDecls = (ParamDecls) pre.get(pre_size - 2);
                        Terminal id = (Terminal) pre.get(pre_size - 4);
                        FuncDecl funcDecl = (FuncDecl) post.get(post_size - 1);
                        String FunctionName = id.getToken().getName();
                        funcDecl.setFuncName(FunctionName);

                        for (SymbolTable symbolTable : symbolTables) {
                            if (symbolTable.getName().equals(FunctionName))
                                throw new Exception(String.format("第%d行出现重名函数%s",id.getToken().getRow(),FunctionName));
                        }
                        SymbolTable symbolTable = new SymbolTable(FunctionName);//新建函数符号表
                        symbolTable.setParamNum(paramDecls.getParams().size());
                        for (ParamDecl paramDecl : paramDecls.getParams()) {
                            TableItem tableItem = new TableItem(paramDecl.getParamName(), paramDecl.getType(), null, true);
                            symbolTable.getTableItems().add(tableItem);
                        }
                        symbolTable.setPrePointer(nowTablePointer);
                        nowTablePointer = symbolTable;
                        symbolTables.add(symbolTable);
                    } else if (num == 16) {
                        //<Function> → <Type> <FuncDecl> <Block>
                        FuncDecl funcDecl = (FuncDecl) pre.get(pre_size - 2);
                        Type type = (Type) pre.get(pre_size - 3);
                        String funcName = funcDecl.getFuncName();//函数名
                        for (SymbolTable st : symbolTables) {
                            //遍历符号表序列
                            if (st.getName().equals(funcName)) {
                                //若当前符号表的名字和归约句的函数名字相同
                                //设置当前符号表的返回值类型
                                st.setReturnType(type.getType());
                                for (FiveParam fiveParam : st.getFiveParams()) {
                                    //遍历当前符号表的五元式序列
                                    if (fiveParam.getOp().equals("ret")) {
                                        //若当前五元式是ret
                                        //则获取ret五元式的返回的变量名
                                        String returnParaName = fiveParam.getParam_3();
                                        for (TableItem tableItem : st.getTableItems()) {
                                            //遍历当前符号表
                                            if (returnParaName.equals(tableItem.getName())) {
                                                //从符号表找到对应五元式返回的变量名
                                                if (!tableItem.getType().equals(type.getType())) {
                                                    //若类型不匹配
                                                    throw new Exception(st.getName() + "函数返回值类型与函数内第" + fiveParam.getRow() + "返回值不同");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (num == 11) {
                        //<Decl>→<Type>id=<Bool>;
                        Bool bool = (Bool) pre.get(pre_size - 2);
                        Terminal id = (Terminal) pre.get(pre_size - 4);
                        Type type = (Type) pre.get(pre_size - 5);
                        String idName = id.getToken().getName();
                        for (TableItem tableItem : nowTablePointer.getTableItems()) {
                            //检查变量声明有没有重复
                            if (tableItem.getName().equals(idName)){
                                throw new Exception(String.format("第%d行%s变量已声明",id.getToken().getRow(),idName));
                            }
                        }
                        TableItem tableItem = null;
                        if (bool.getToken().getTokenType() == Token.Symbol.number)
                            tableItem = new TableItem(idName, type.getType(), null);
                        else if (bool.getToken().getTokenType() == Token.Symbol.fnumber)
                            tableItem = new TableItem(idName, type.getType(), null);
                        nowTablePointer.getTableItems().add(tableItem);

                        FiveParam fiveParam = new FiveParam("=", bool.getToken().getName(), "_", id.getToken().getName(), bool.getToken().getRow());
                        wholeFiveParams.add(fiveParam);
                        fiveParam.setPointer(nowTablePointer);
                        nowTablePointer.getFiveParams().add(fiveParam);
//                        //判断能否执行
//                        String name = nowTablePointer.getName();
//                        //能执行
//                        if(judgeExecute(name)){
//                            //记入符号表
//                            if(bool.getToken().getTokenType() == Token.Symbol.number)
//                                tableItem = new TableItem(id.getToken().getName(), type.getType(), bool.getToken().getValue());
//                            else if(bool.getToken().getTokenType() == Token.Symbol.fnumber)
//                                tableItem = new TableItem(id.getToken().getName(), type.getType(), bool.getToken().getFvalue());
//                            nowTablePointer.getTableItems().add(tableItem);
//                        }
//                        //不能执行
//                        else{
//                            FiveParam fiveParam = new FiveParam("=",bool.getToken().getName(),"_",id.getToken().getName(),bool.getToken().getRow());
//                            //T_index++;
//                            nowTablePointer.getFiveParams().add(fiveParam);
//                        }
                    } else if (num == 6) {
                        //<Base> → <Function>
                    } else if (num == 4) {
                        //<Base> -><Decl>
                    } else if (num == 3) {
                        //<Bases> → <Base>
                    } else if (num == 2) {
                        //<Bases> → <Base> <Bases>
                    } else if (num == 1) {
                        //<Program> → <Bases>
                    }
                }
            }
        }


        return wholeFiveParams;
    }

    @Override
    public FiveParam executeFiveParam(List<FiveParam> fiveParams,int stopRow) throws Exception {
        Stack<Object> paramStack = new Stack<>();
        Stack<Object> returnValueStack = new Stack<>();
        Stack<Integer> PCs = new Stack<>();
//        executeName = new ArrayList<>();
//        //向可执行函数数组里添加全局main
//        executeName.add("全局");
//        executeName.add("main");
        boolean firstSeeMain = true;
        for (int indexOfFiveParams = 0;indexOfFiveParams < fiveParams.size() ; indexOfFiveParams++) {
            FiveParam fiveParam = fiveParams.get(indexOfFiveParams);


            if (!(fiveParam.getPointer().getName().equals("全局") || fiveParam.getPointer().getName().equals("main")) && firstSeeMain ){
                continue;
            }
            if (fiveParam.getPointer().getName().equals("main") && firstSeeMain){
                firstSeeMain = false;
            }
            if(fiveParam.getRow()==stopRow){
                return fiveParam;
            }
            SymbolTable nowSymbolTable = fiveParam.getPointer();
            ArrayList<String> abc_name = new ArrayList<>();
            abc_name.add(fiveParam.getParam_1());
            abc_name.add(fiveParam.getParam_2());
            abc_name.add(fiveParam.getParam_3());
            ArrayList<String> abc_type = new ArrayList<>();
            abc_type.add("");
            abc_type.add("");
            abc_type.add("");
            ArrayList<Object> abc = new ArrayList<>();
            abc.add(new Object());
            abc.add(new Object());
            abc.add(new Object());
            if(fiveParam.getOp().equals("+")){


                for (int i = 0; i < 2 ; i++){
                    String name = abc_name.get(i);
                    switch (checkStringIsNumberOrIdent(name)){
                        case 1:
                            abc.set(i,Integer.valueOf(name));
                            abc_type.set(i,"int");
                            break;
                        case 2:
                            abc.set(i,Float.valueOf(name));
                            abc_type.set(i,"float");
                            break;
                        case 3:
                            abc.set(i,getValueFromSymbolTable(name,nowSymbolTable));
                            abc_type.set(i,checkIdType(name,nowSymbolTable));
                            break;
                    }
                }

                if (abc_type.get(0).equals("int")&&abc_type.get(1).equals("int")){
                    abc.set(2, (Integer) abc.get(0) + (Integer)abc.get(1) );
                }else if (abc_type.get(0).equals("int")&&abc_type.get(1).equals("float")){
                    abc.set(2, (Integer) abc.get(0) + (Float)abc.get(1) );
                }else if (abc_type.get(0).equals("float")&&abc_type.get(1).equals("int")){
                    abc.set(2, (Float) abc.get(0) + (Integer)abc.get(1) );
                }else if (abc_type.get(0).equals("float")&&abc_type.get(1).equals("float")){
                    abc.set(2, (Float) abc.get(0) + (Float) abc.get(1) );
                }
                setValueToSymbolTable(fiveParam.getParam_3(),nowSymbolTable,abc.get(2));
            }else if(fiveParam.getOp().equals("=")){
                for (int i = 0; i < 1 ; i++){
                    String name = abc_name.get(i);
                    switch (checkStringIsNumberOrIdent(name)){
                        case 1:
                            abc.set(i,Integer.valueOf(name));
                            abc_type.set(i,"int");
                            break;
                        case 2:
                            abc.set(i,Float.valueOf(name));
                            abc_type.set(i,"float");
                            break;
                        case 3:
                            abc.set(i,getValueFromSymbolTable(name,nowSymbolTable));
                            abc_type.set(i,checkIdType(name,nowSymbolTable));
                            break;
                    }
                }
                abc.set(2, abc.get(0) );
                setValueToSymbolTable(fiveParam.getParam_3(),nowSymbolTable,abc.get(2));
            }else if (fiveParam.getOp().equals("ret")){
                for (int i = 2; i < 3 ; i++){
                    String name = abc_name.get(i);
                    switch (checkStringIsNumberOrIdent(name)){
                        case 1:
                            abc.set(i,Integer.valueOf(name));
                            abc_type.set(i,"int");
                            break;
                        case 2:
                            abc.set(i,Float.valueOf(name));
                            abc_type.set(i,"float");
                            break;
                        case 3:
                            abc.set(i,getValueFromSymbolTable(name,nowSymbolTable));
                            abc_type.set(i,checkIdType(name,nowSymbolTable));
                            break;
                    }
                }
                if (nowSymbolTable.getName().equals("main")){
                    break;
                }
                returnValueStack.push(abc.get(2));
                indexOfFiveParams = PCs.pop()-1;
            }else if (fiveParam.getOp().equals("param")){
                for (int i = 2; i < 3 ; i++){
                    String name = abc_name.get(i);
                    switch (checkStringIsNumberOrIdent(name)){
                        case 1:
                            abc.set(i,Integer.valueOf(name));
                            abc_type.set(i,"int");
                            break;
                        case 2:
                            abc.set(i,Float.valueOf(name));
                            abc_type.set(i,"float");
                            break;
                        case 3:
                            abc.set(i,getValueFromSymbolTable(name,nowSymbolTable));
                            abc_type.set(i,checkIdType(name,nowSymbolTable));
                            break;
                    }
                }
                paramStack.push(abc.get(2));
            }else if (fiveParam.getOp().equals("Call")){
                int nextQuad = indexOfFiveParams;
                for (int tempIndex = 0;tempIndex<fiveParams.size();tempIndex++){
                    if (fiveParams.get(tempIndex).getPointer().getName().equals(abc_name.get(2))){
                        nextQuad = tempIndex;
                        //放入参数
                        SymbolTable symbolTable = fiveParams.get(tempIndex).getPointer();
                        for (int paraIndex = 0; paraIndex < symbolTable.getParamNum();paraIndex++){
                            setValueToSymbolTable(symbolTable.getTableItems().get(paraIndex).getName(),symbolTable,paramStack.pop());
                        }
                        break;
                    }
                }
                PCs.push(indexOfFiveParams + 1);
                indexOfFiveParams = nextQuad - 1;
            }else if (fiveParam.getOp().equals("getReturn")){
                abc.set(2,returnValueStack.pop());
                setValueToSymbolTable(fiveParam.getParam_3(),nowSymbolTable,abc.get(2));
            }else if (fiveParam.getOp().equals("j<")){
                for (int i = 0; i < 2 ; i++){
                    String name = abc_name.get(i);
                    switch (checkStringIsNumberOrIdent(name)){
                        case 1:
                            abc.set(i,Integer.valueOf(name));
                            abc_type.set(i,"int");
                            break;
                        case 2:
                            abc.set(i,Float.valueOf(name));
                            abc_type.set(i,"float");
                            break;
                        case 3:
                            abc.set(i,getValueFromSymbolTable(name,nowSymbolTable));
                            abc_type.set(i,checkIdType(name,nowSymbolTable));
                            break;
                    }
                }
                boolean ans = false;
                if (abc_type.get(0).equals("int")&&abc_type.get(1).equals("int")){
                    ans = (Integer) abc.get(0) < (Integer)abc.get(1) ;
                }else if (abc_type.get(0).equals("int")&&abc_type.get(1).equals("float")){
                    ans = (Integer) abc.get(0) < (Float)abc.get(1) ;
                }else if (abc_type.get(0).equals("float")&&abc_type.get(1).equals("int")){
                    ans =  (Float) abc.get(0) < (Integer)abc.get(1);
                }else if (abc_type.get(0).equals("float")&&abc_type.get(1).equals("float")){
                    ans = (Float) abc.get(0) < (Float) abc.get(1) ;
                }
                if (ans){
                    PCs.push(indexOfFiveParams);
                    indexOfFiveParams = Integer.valueOf(abc_name.get(2))-1;
                }else {
                    continue;
                }
            }else if (fiveParam.getOp().equals("j")){
                PCs.push(indexOfFiveParams);
                indexOfFiveParams = Integer.valueOf(abc_name.get(2))-1;
            }else if (fiveParam.getOp().equals("j>")){
                for (int i = 0; i < 2 ; i++){
                    String name = abc_name.get(i);
                    switch (checkStringIsNumberOrIdent(name)){
                        case 1:
                            abc.set(i,Integer.valueOf(name));
                            abc_type.set(i,"int");
                            break;
                        case 2:
                            abc.set(i,Float.valueOf(name));
                            abc_type.set(i,"float");
                            break;
                        case 3:
                            abc.set(i,getValueFromSymbolTable(name,nowSymbolTable));
                            abc_type.set(i,checkIdType(name,nowSymbolTable));
                            break;
                    }
                }
                boolean ans = false;
                if (abc_type.get(0).equals("int")&&abc_type.get(1).equals("int")){
                    ans = (Integer) abc.get(0) > (Integer)abc.get(1) ;
                }else if (abc_type.get(0).equals("int")&&abc_type.get(1).equals("float")){
                    ans = (Integer) abc.get(0) > (Float)abc.get(1) ;
                }else if (abc_type.get(0).equals("float")&&abc_type.get(1).equals("int")){
                    ans =  (Float) abc.get(0) > (Integer)abc.get(1);
                }else if (abc_type.get(0).equals("float")&&abc_type.get(1).equals("float")){
                    ans = (Float) abc.get(0) > (Float) abc.get(1) ;
                }
                if (ans){
                    PCs.push(indexOfFiveParams);
                    indexOfFiveParams = Integer.valueOf(abc_name.get(2))-1;
                }else {
                    continue;
                }
            }

        }



        return null;
    }

    @Override
    public SymbolTable debug(ParserResult parserResult, ArrayList<Node> grammar, int row) throws Exception {
        List<FiveParam> fiveParams = semantic_analysis(parserResult,grammar);
        FiveParam fiveParam = executeFiveParam(fiveParams,row);
        if (fiveParam == null){
            SymbolTable ansTemp = new SymbolTable("程序已结束");
            return ansTemp;
        }

        return fiveParam.getPointer();
    }

    private int checkStringIsNumberOrIdent(String name){
        if(name.length() == 1){
            if (lexerImpl.isDigit(name.charAt(0))){
                return 1;//整数
            }else if (lexerImpl.isAlpha(name.charAt(0))){
                return 3;//标识符
            }
        }else{
            if (name.contains(".")){
                return 2;//浮点数
            }else if (lexerImpl.isDigit(name.charAt(0))&&lexerImpl.isDigit(name.charAt(1))){
                return 1;
            }else {
                return 3;
            }
        }
        return 4;
    }
    private Object getValueFromSymbolTable(String name,SymbolTable symbolTable){
        Object ans = null;
        SymbolTable now = symbolTable;
        boolean isFind = false;
        do{
            for (TableItem tableItem : now.getTableItems()) {
                if(tableItem.getName().equals(name)){
                    if (tableItem.getData()==null) //这个if是为了防止变量未声明前同名上级已被使用，本级变量暂时还没有数据的情况
                        break;
                    ans = tableItem.getData();
                    isFind = true;
                    break;
                }
            }
            if(isFind){
                break;
            }
            if(now.getName().equals("全局"))
                break;
            now = now.getPrePointer();
        }   while (true);

        if(isFind){
            return ans;
        }else {
            return "变量未声明";
        }
    }

    private void setValueToSymbolTable(String name,SymbolTable symbolTable,Object value) throws Exception {
        SymbolTable now = symbolTable;
        boolean isFind = false;
        do{
            for (TableItem tableItem : now.getTableItems()) {
                if(tableItem.getName().equals(name)){
                    if (tableItem.getType().equals("int")){
                        tableItem.setData((Integer) value);
                    }else {
                        tableItem.setData((Float) value);
                    }
                    isFind = true;
                    break;
                }
            }
            if(isFind){
                break;
            }
            if(now.getName().equals("全局"))
                break;
            now = now.getPrePointer();
        }   while (true);

        if(isFind){
        }else {
            throw new Exception("变量未声明");
        }
    }



}
