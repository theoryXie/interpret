package whu.se.interpret.service;

import org.springframework.stereotype.Service;
import whu.se.interpret.po.Node;
import whu.se.interpret.po.Pair;
import whu.se.interpret.po.ParserResult;
import whu.se.interpret.po.symbol.*;
import whu.se.interpret.service.impl.SemanticImpl;

import java.util.ArrayList;

/**
 * @author xsy
 * @description: 语义分析方法的实现类
 * @date 2019/9/22 8:23
 */

@Service
public class Semantic implements SemanticImpl {


    //语义分析
    @Override
    public void semantic_analysis(ParserResult parserResult, ArrayList<Node> grammar) {
        ArrayList<ArrayList<Object>> symbols_object = parserResult.getSymbols_Object();//过程集合
        ArrayList<Pair> pairs = parserResult.getPairs();
        int pair_size = pairs.size();
        for(int i = 0; i < pair_size; i++){
            Pair pair = pairs.get(i);//当前动作
            char c = pair.getC();//r || s
            int num = pair.getNum();
            if(c == 'r'){
                if(num == 0){
                    //TODO acc
                }
                else{
                    ArrayList<Object> pre = symbols_object.get(i-1);//i不会为0，因为第一步必定是移进
                    ArrayList<Object> post = symbols_object.get(i);
                    int pre_size = pre.size();
                    int post_size = post.size();
                    if(num >=27 && num <=30){
                        //<Type> → int | char | float | void
                        Type type = (Type)post.get(post_size-1);
                        Terminal terminal = (Terminal) pre.get(pre_size-1);//int | char | float | void
                        type.setType(terminal.getToken().getName());//Type.type = int.type...
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
                }
            }
        }
    }
}
