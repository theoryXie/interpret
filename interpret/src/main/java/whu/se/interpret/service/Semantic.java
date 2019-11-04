package whu.se.interpret.service;

import org.springframework.stereotype.Service;
import whu.se.interpret.po.*;
import whu.se.interpret.po.symbol.*;
import whu.se.interpret.service.impl.SemanticImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xsy
 * @description: 语义分析方法的实现类
 * @date 2019/9/22 8:23
 */

@Service
public class Semantic implements SemanticImpl {

    private List<String> executeName = new ArrayList<>();

    private boolean judgeExecute(String name){
        if(executeName.contains(name)){
            return true;
        }
        return false;
    }

    private int operationType(Token token1, Token token2){
        Token.Symbol tokenSymbol_1 = token1.getTokenType();
        Token.Symbol tokenSymbol_2 = token2.getTokenType();
        if(tokenSymbol_1 == Token.Symbol.intsym && tokenSymbol_2 == Token.Symbol.intsym){
            return 1;
        }
        else if(tokenSymbol_1 == Token.Symbol.intsym && tokenSymbol_2 == Token.Symbol.floatsym){
            return 2;
        }
        else if(tokenSymbol_1 == Token.Symbol.floatsym && tokenSymbol_2 == Token.Symbol.intsym){
            return 3;
        }
        else{
            return 4;
        }

    }

    //语义分析
    @Override
    public void semantic_analysis(ParserResult parserResult, ArrayList<Node> grammar) throws Exception {
        ArrayList<ArrayList<Object>> symbols_object = parserResult.getSymbols_Object();//过程集合
        ArrayList<Pair> pairs = parserResult.getPairs();
        ArrayList<SymbolTable> symbolTables = new ArrayList<>();//符号表序列
        SymbolTable wholeTable = new SymbolTable("全局","void");//全局符号表
        symbolTables.add(wholeTable);
        SymbolTable nowTablePointer = wholeTable;//当前指针指向全局符号表
        //向可执行函数数组里添加全局main
        executeName.add("全局");
        executeName.add("main");
        //T的下标
        int T_index = 0;
        int F_index = 0;
        //全局返回值
        Object returnValue;
        int pair_size = pairs.size();
        for(int i = 0; i < pair_size; i++){
            Pair pair = pairs.get(i);//当前动作
            char c = pair.getC();//r || s
            int num = pair.getNum();
            ArrayList<Object> pre = symbols_object.get(i-1);//i不会为0，因为第一步必定是移进
            ArrayList<Object> post = symbols_object.get(i);
            int pre_size = pre.size();
            int post_size = post.size();
            if(c == 's'){
                //判断当前移进的是否是‘{’
                Terminal terminal = (Terminal) post.get(post_size-1);
                if(terminal.getToken().getName().equals("{")){
                    //如果前面是funcDecl，则不创建新符号表
                    if(num==92){
                        continue;
                    }

                    SymbolTable symbolTable = new SymbolTable("__F"+F_index);//新建函数符号表
                    symbolTable.setParamNum(0);
                    //记录他的上级来确定这个新符号表是否要执行
//                    if(judgeExecute(nowTablePointer.getName()))
//                    {
//                        executeName.add(symbolTable.getName());
//                    }else {
//                        FiveParam fiveParam = new FiveParam("Call","_","_",symbolTable.getName(),terminal.getToken().getRow());
//                    }
                    //2019.11.4 20:48后  全部生成五元式后再统一执行
                    FiveParam fiveParam = new FiveParam("Call","_","_",symbolTable.getName(),terminal.getToken().getRow());
                    nowTablePointer.getFiveParams().add(fiveParam);
                    //*2019.11.4 20:48
                    symbolTable.setPrePointer(nowTablePointer);
                    nowTablePointer = symbolTable;
                    symbolTables.add(symbolTable);
                }
            }
            else if(c == 'r'){
                if(num == 0){
                    //TODO acc
                }
                else{
                    if(num >=27 && num <=30){
                        //<Type> → int | char | float | void
                        Type type = (Type)post.get(post_size-1);
                        Terminal terminal = (Terminal) pre.get(pre_size-1);//int | char | float | void
                        type.setType(terminal.getToken().getName());//Type.type = int.type...
                    }
                    else if(num == 70){
                        //<factor>-><loc>
                        Loc loc = (Loc) pre.get(pre_size-1);
                        Factor factor = (Factor) post.get(post_size-1);
                        factor.setToken(loc.getToken());//factor.token = loc.token
                    }

                    else if(num == 71 || num == 72){
                        //<Factor>→num | real
                        Factor factor = (Factor)post.get(post_size-1);
                        Terminal terminal = (Terminal) pre.get(pre_size-1);//num | real
                        factor.setToken(terminal.getToken());//factor.token = num.token...
                    }
                    else if(num == 68){
                        //<Unary> → <Factor>
                        Unary unary = (Unary)post.get(post_size-1);
                        Factor factor = (Factor)pre.get(pre_size-1);
                        unary.setToken(factor.getToken());//unary.token = factor.token
                    }
                    else if(num == 65){
                        //<Term>→<Unary>
                        Term term = (Term) post.get(post_size-1);
                        Unary unary = (Unary) pre.get(pre_size-1);
                        term.setToken(unary.getToken());//term.token = unary.token
                    }
                    else if(num == 62){
                        //<Expr>→<Term>
                        Expr expr = (Expr) post.get(post_size-1);
                        Term term = (Term) pre.get(pre_size-1);
                        expr.setToken(term.getToken());//expr.token = term.token
                    }
                    else if(num == 60){
                        //<Expr1> → <Expr2> + <Term>
                        Term term = (Term) pre.get(pre_size-1);
                        Expr expr2 = (Expr) pre.get(pre_size-3);
                        Expr expr1 = (Expr) post.get(post_size-1);
                        String name = nowTablePointer.getName();   //当前符号表的名字
                        //执行
                        Token token1 = expr1.getToken();
                        Token token2 = expr2.getToken();
                        int type_ans = operationType(expr1.getToken(),expr2.getToken());
                        Token token;
                        //2019.11.4  20：51  将要修改
                        if(judgeExecute(name)){
                                switch (type_ans){
                                    case 1:
                                        token = new Token("", Token.Symbol.intsym,token1.getValue()+token2.getValue(),token1.getRow());
                                        break;
                                    case 2:
                                        token = new Token("", Token.Symbol.floatsym,token1.getValue()+token2.getFvalue(),token1.getRow());
                                        break;
                                    case 3:
                                        token = new Token("", Token.Symbol.floatsym,token1.getFvalue()+token2.getValue(),token1.getRow());
                                        break;
                                    case 4:
                                        token = new Token("", Token.Symbol.floatsym,token1.getFvalue()+token2.getFvalue(),token1.getRow());
                                        break;
                                    default:
                                        throw new IllegalStateException("Unexpected value: " + type_ans);
                                }
                                expr1.setToken(token);
                        }
                        else{
                            //生成五元式
                            FiveParam fiveParam = new FiveParam("+",token1.getName(),token2.getName(),"0T"+T_index,token1.getRow());
                            expr1.getToken().setName("0T"+T_index);
                            TableItem item = null;
                            if(type_ans==1){
                                item = new TableItem(expr1.getToken().getName(),"int",null,false);
                            }else {
                                item = new TableItem(expr1.getToken().getName(),"float",null,false);
                            }
                            nowTablePointer.getTableItems().add(item);
                            T_index++;
                            nowTablePointer.getFiveParams().add(fiveParam);
                        }
                    }
                    else if(num == 59){
                        //<Rel>→<Expr>
                        Rel rel = (Rel) post.get(post_size-1);
                        Expr expr = (Expr) pre.get(pre_size-1);
                        rel.setToken(expr.getToken());//rel.token = expr.token
                    }
                    else if(num == 54){
                        //<Equality> → <Rel>
                        Equality equality = (Equality) post.get(post_size-1);
                        Rel rel = (Rel) pre.get(pre_size-1);
                        equality.setToken(rel.getToken());//rel.token = expr.token
                    }
                    else if(num == 51){
                        //<Join> → <Equality>
                        Join join = (Join) post.get(post_size-1);
                        Equality equality = (Equality) pre.get(pre_size-1);
                        join.setToken(equality.getToken());//rel.token = expr.token
                    }
                    else if(num == 49){
                        //<Bool>→<Join>
                        Bool bool = (Bool) post.get(post_size-1);
                        Join join = (Join) pre.get(pre_size-1);
                        bool.setToken(join.getToken());//rel.token = expr.token
                    }
                    else if(num == 46){
                        //<loc>->id
                        Terminal id = (Terminal) pre.get(pre_size-1);
                        Loc loc = (Loc) post.get(post_size-1);
                        loc.setToken(id.getToken());
                    }
                    else if(num == 41){
                        //<Stmt> -> <Block>
                    }
                    else if(num == 40){
                        //<Stmt> -> return <Bool> ;
                        Bool bool = (Bool)pre.get(pre_size-2);
                        String funcName = nowTablePointer.getName();
                        if(judgeExecute(funcName)){
                            //TODO 直接结束吧
                        }else{
                            FiveParam fiveParam = new FiveParam("ret","_","_",bool.getName(),bool.getToken().getRow());
                            nowTablePointer.getFiveParams().add(fiveParam);
                        }
                    }
                    else if(num == 33){
                        //<stmt> -> <Decl>
                    }
                    else if(num == 32){
                        //<stmts> -> <stmt>
                    }
                    else if(num == 31){
                        //<Stmts> → <Stmt> <Stmts>
                    }
                    else if(num == 26){
                        //<Block>->{ <stmts> }
                        //TODO 可能会有return的问题
                        nowTablePointer = nowTablePointer.getPrePointer();
                    }
                    else if(num == 25){
                        //<Bools> → <Bool>
                        Bools bools = (Bools) post.get(post_size-1);
                        Bool bool = (Bool)pre.get(pre_size-1);
                        bools.getTokens().add(bool.getToken());//<Bools>.token = Bool.token
                    }
                    else if(num == 24){
                        //<Bools1> → <Bool> , <Bools2>
                        Bools bools_1 = (Bools) post.get(post_size-1);
                        Bools bools_2 = (Bools) pre.get(pre_size-1);
                        Bool bool = (Bool) pre.get(pre_size-3);
                        bools_1.getTokens().add(bool.getToken());
                        bools_1.getTokens().addAll(bools_2.getTokens());
                    }
                    else if(num == 22){
                        //<FuncUse> → id ( <Bools> )
                        Terminal id = (Terminal) pre.get(pre_size-4);
                        Bools bools = (Bools) pre.get(pre_size-2);
                        String FunName = id.getToken().getName();
                        if (judgeExecute(nowTablePointer.getName())){
                             //当前代码正在运行
                            boolean is_declare = false;

                            //TODO  执行当前FunName下的五元式    没有进行同名函数的处理
                            for (SymbolTable symbolTable : symbolTables) {
                                //遍历符号表序列
                                if(symbolTable.getName().equals(FunName)){
                                    //若符号表名字与当前函数名字相同
                                    is_declare = true;
                                    executeName.add(FunName);//加入可执行函数名
                                    nowTablePointer=symbolTable; //将指针指向当前符号表
                                    if(symbolTable.getParamNum() != bools.getTokens().size())
                                        throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数参数个数不匹配");
                                    for (TableItem tableItem : nowTablePointer.getTableItems()) {
                                        //遍历符号表参数
                                        if(tableItem.isParam()){
                                            //若为函数参数
                                            int temp_tableItemIndex = nowTablePointer.getTableItems().indexOf(tableItem);  //记录参数第几个
                                            Token paramToken = bools.getTokens().get(temp_tableItemIndex);
                                            if(paramToken.getTokenTypeString().equals(tableItem.getType())){
                                                //对比参数类型后
                                                tableItem.setData(paramToken.getObjectValue());//因为当前函数是立即执行的所以前面的已经计算过了，可以直接取值
                                            }else {
                                                throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数第" + (temp_tableItemIndex+1) + "参数" + tableItem.getName() + "类型不匹配");
                                            }
                                            //2019.11.4 16:42
                                        }else {
                                            break;
                                        }
                                    }
                                    //TODO 在这里执行当前符号表的五元式
                                    //TODO  执行完五元式要跳回原来执行的地方  并且返回返回值   此处要赋值并且要对比返回值类型是否正确？
                                    break;
                                }
                            }
                            if (!is_declare)
                                throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数未声明");
                        }
                        else{
                            boolean is_declare = false;
                            for (SymbolTable symbolTable : symbolTables) {
                                if(symbolTable.getName().equals(FunName)){
                                    is_declare = true;
                                    if(symbolTable.getParamNum() != bools.getTokens().size())
                                        throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数参数个数不匹配");
                                    //遍历符号表序列(寻找参数)
                                    for (TableItem tableItem : symbolTable.getTableItems()) {
                                        if(tableItem.isParam()){
                                            //是参数
                                            int paramIndex = symbolTable.getTableItems().indexOf(tableItem);//获取参数位置
                                            Token paramToken = bools.getTokens().get(paramIndex);
                                            //对比参数类型
                                            if(paramToken.getTokenTypeString().equals(tableItem.getType())){
                                                FiveParam temp_five = new FiveParam("param","_","_", paramToken.getName(),paramToken.getRow());
                                                nowTablePointer.getFiveParams().add(temp_five);
                                            }
                                        }
                                        else
                                            break;
                                    }
                                }
                            }
                            //生成调用函数的五元式
                            FiveParam fiveParam = new FiveParam("Call","_","_",id.getToken().getName(),id.getToken().getRow());
                            nowTablePointer.getFiveParams().add(fiveParam);
                            if(!is_declare)
                                throw new Exception("第"+id.getToken().getRow()+"行"+FunName + "函数未声明");
                        }
                    }
                    else if(num == 21){
                        //<ParamDecl>→<Type>id
                        Terminal id = (Terminal) pre.get(pre_size-1);
                        Type type = (Type) pre.get(pre_size-2);
                        ParamDecl paramDecl = (ParamDecl) post.get(post_size-1);
                        paramDecl.setParamName(id.getToken().getName());
                        paramDecl.setType(type.getType());
                    }
                    else if(num == 20){
                        //<ParamDecls>→<ParamDecl>
                        ParamDecl paramDecl = (ParamDecl) pre.get(pre_size-1);
                        ParamDecls paramDecls = (ParamDecls) post.get(post_size-1);
                        paramDecls.getParams().add(paramDecl);
                    }
                    else if(num == 19){
                        //<ParamDecls>1→<ParamDecl>,<ParamDecls>2
                        ParamDecl paramDecl = (ParamDecl) pre.get(pre_size-3);
                        ParamDecls paramDecls2 = (ParamDecls) pre.get(pre_size-1);
                        ParamDecls paramDecls1 = (ParamDecls) post.get(post_size-1);
                        paramDecls1.getParams().add(paramDecl);
                        paramDecls1.getParams().addAll(paramDecls2.getParams());
                    }
                    else if(num == 16){
                        //<FuncDecl>→id()
                        Terminal id = (Terminal) pre.get(pre_size-3);
                        FuncDecl funcDecl = (FuncDecl) post.get(post_size-1);
                        funcDecl.setFuncName(id.getToken().getName());
                        SymbolTable symbolTable = new SymbolTable(id.getToken().getName());//新建函数符号表
                        symbolTable.setParamNum(0);
                        symbolTable.setPrePointer(nowTablePointer);
                        nowTablePointer = symbolTable;
                        symbolTables.add(symbolTable);
                    }
                    else if(num == 17){
                        //<FuncDecl>→id(<ParamDecls>)
                        ParamDecls paramDecls = (ParamDecls) pre.get(pre_size-2);
                        Terminal id = (Terminal) pre.get(pre_size-4);
                        FuncDecl funcDecl = (FuncDecl) post.get(post_size-1);
                        funcDecl.setFuncName(id.getToken().getName());
                        SymbolTable symbolTable = new SymbolTable(id.getToken().getName());//新建函数符号表
                        symbolTable.setParamNum(paramDecls.getParams().size());
                        for(ParamDecl paramDecl : paramDecls.getParams()){
                            TableItem tableItem = new TableItem(paramDecl.getParamName(),paramDecl.getType(),null,true);
                            symbolTable.getTableItems().add(tableItem);
                        }
                        symbolTable.setPrePointer(nowTablePointer);
                        nowTablePointer = symbolTable;
                        symbolTables.add(symbolTable);
                    }
                    else if(num == 16){
                        //<Function> → <Type> <FuncDecl> <Block>
                        FuncDecl funcDecl = (FuncDecl) pre.get(pre_size-2);
                        Type type = (Type) pre.get(pre_size-3);
                        String funcName = funcDecl.getFuncName();//函数名
                        for(SymbolTable st : symbolTables){
                            //遍历符号表序列
                            if(st.getName().equals(funcName)){
                                //若当前符号表的名字和归约句的函数名字相同
                                //设置当前符号表的返回值类型
                                st.setReturnType(type.getType());
                                for(FiveParam fiveParam: st.getFiveParams()){
                                    //遍历当前符号表的五元式序列
                                    if(fiveParam.getOp().equals("ret")){
                                        //若当前五元式是ret
                                        //则获取ret五元式的返回的变量名
                                        String returnParaName = fiveParam.getParam_3();
                                        for(TableItem tableItem: st.getTableItems()){
                                            //遍历当前符号表
                                            if(returnParaName.equals(tableItem.getName())){
                                                //从符号表找到对应五元式返回的变量名
                                                if(!tableItem.getType().equals(type.getType())){
                                                    //若类型不匹配
                                                    throw new Exception(st.getName()+"函数返回值类型与函数内第"+fiveParam.getRow()+"返回值不同");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(num == 11){
                        //<Decl>→<Type>id=<Bool>;
                        Bool bool = (Bool) pre.get(pre_size-2);
                        Terminal id = (Terminal) pre.get(pre_size-4);
                        Type type = (Type) pre.get(pre_size-5);
                        TableItem tableItem = null;
                        //判断能否执行
                        String name = nowTablePointer.getName();
                        //能执行
                        if(judgeExecute(name)){
                            //记入符号表
                            if(bool.getToken().getTokenType() == Token.Symbol.intsym)
                                tableItem = new TableItem(id.getToken().getName(), type.getType(), bool.getToken().getValue());
                            else if(bool.getToken().getTokenType() == Token.Symbol.floatsym)
                                tableItem = new TableItem(id.getToken().getName(), type.getType(), bool.getToken().getFvalue());
                            nowTablePointer.getTableItems().add(tableItem);
                        }
                        //不能执行
                        else{
                            FiveParam fiveParam = new FiveParam("=",bool.getToken().getName(),"_",id.getToken().getName(),bool.getToken().getRow());
                            //T_index++;
                            nowTablePointer.getFiveParams().add(fiveParam);
                        }
                    }
                    else if(num == 6){
                        //<Base> → <Function>
                    }
                    else if(num == 4){
                        //<Base> -><Decl>
                    }
                }
            }
        }
    }
}
