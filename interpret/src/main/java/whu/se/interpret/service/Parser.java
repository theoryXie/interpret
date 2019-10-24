package whu.se.interpret.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import whu.se.interpret.po.*;
import whu.se.interpret.po.symbol.*;
import whu.se.interpret.result.Result;
import whu.se.interpret.service.impl.ParserImpl;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author xsy
 * @description: 语法分析方法的实现类
 * @date 2019/9/22 8:21
 */
@Getter
@Setter
@Service
public class Parser implements ParserImpl {

    private ArrayList<Node> grammar = new ArrayList<>();//产生式序列
    private HashMap<String, HashSet<String>> firstSet = new HashMap<>();//所有非终结符号的first集
    private HashMap<String, HashSet<String>> followSet = new HashMap<>();//所有非终结符号的follow集

    //初始化grammar，firstSet，followSet
    @Override
    public void init(String grammarFileName) throws IOException {
        this.grammar = getGrammar(grammarFileName);
        for (Node node : grammar) {
            String left = node.getLeft();//产生式左部
            if (!firstSet.containsKey(left)) {
                getFirst(left);
            }
        }
        followSet.put("<begin>", new HashSet<>());
        followSet.get("<begin>").add("$");
        for (int i = 1; i < grammar.size(); i++) {
            String left = grammar.get(i).getLeft();//产生式左部
            if (!followSet.containsKey(left)) {
                getFollow(left);
            }
        }

    }


    @Override
    public boolean isTerm(String s) {
        //s为非终结符号
        if (s.charAt(0) == '<' && s.charAt(s.length() - 1) == '>')
            return false;
            //s为空串
        else return !s.equals("ε");
        //s为终结符号
    }

    @Override
    public ArrayList<Node> getGrammar(String filename) throws IOException {
        //获取资源文件夹static
        Resource resource = new ClassPathResource("static/" + filename);
        //File path = new File(ResourceUtils.getURL("classpath:static").getPath().replace("%20"," ").replace('/', '\\'));
        //获取资源文件夹下的文法文件
        File grammarFile = resource.getFile();
        //最终的文法序列
        ArrayList<Node> grammar = new ArrayList<>();
        //逐行读取文法
        try (
                FileReader fr = new FileReader(grammarFile);
                BufferedReader br = new BufferedReader(fr)
        ) {
            String line;

            while ((line = br.readLine()) != null) {
                Node node = new Node();//产生式
                String[] splits = line.split(" ");//根据空格分割产生式字符串
                node.setLeft(splits[0]);//产生式左部
                ArrayList<String> right = new ArrayList<>();//产生式右部
                //将文法单元添加到产生式右部
                for (int i = 2; i < splits.length; i++) {
                    right.add(splits[i]);
                }
                node.setRight(right);
                grammar.add(node);
            }
            return grammar;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

    }


    private void getFirst(String target) {
        firstSet.put(target, new HashSet<>());
        //遍历产生式序列
        for (Node node : grammar) {
            //如果产生式左边等于target
            if (node.getLeft().equals(target)) {
                //遍历产生式右部
                ArrayList<String> right = node.getRight();//产生式右部
                for (int j = 0; j < right.size(); j++) {
                    //当前节点
                    String temp = right.get(j);

                    //当前节点为终结符号
                    if (isTerm(temp)) {
                        firstSet.get(target).add(temp);//直接加入target的first集
                        break;
                    }

                    //当前节点为空串
                    else if (temp.equals("ε")) {
                        //如果ε是产生式右部的最后一个元素，直接将ε加入到target的first集
                        if (j == right.size() - 1) {
                            firstSet.get(target).add("ε");
                        }
                    }

                    //当前节点为非终结符号
                    else {
                        //当右部当前元素等于左部时，直接跳过，避免左递归造成死循环
                        if (!temp.equals(node.getLeft())) {
                            //求当前非终结符号的first集
                            getFirst(temp);
                            //如果temp的first集包含空串
                            if (firstSet.get(temp).contains("ε")) {
                                HashSet tempSet = firstSet.get(temp);
                                tempSet.remove("ε");
                                firstSet.get(target).addAll(tempSet);//temp的first集 和 target的first集取并集
                            } else {
                                firstSet.get(target).addAll(firstSet.get(temp));//temp的first集 和 target的first集取并集
                                break;
                            }
                        } else
                            break;
                    }
                }
            }
        }
    }


    private void getFollow(String target) {
        followSet.put(target, new HashSet<>());
        for (Node node : grammar) {
            boolean flag = false;
            //遍历产生式右部，找到target
            int j;
            for (j = 0; j < node.getRight().size(); j++) {
                if (node.getRight().get(j).equals(target)) {
                    flag = true;
                    break;
                }
            }
            //在产生式右部找到了target后
            if (flag) {
                String left = node.getLeft();//产生式左部

                //target右边为空串
                if ((j == (node.getRight().size() - 1)) && !target.equals(left)) {
                    //并且target不等于产生式左部（等于左部时与自己取并集）
                    //与产生式左部的follow集取并集
                    if (!followSet.containsKey(left))
                        getFollow(left);
                    followSet.get(target).addAll(followSet.get(left));
                }

                //target右边不为空
                else {
                    //遍历右部符号
                    for (; j < node.getRight().size() - 1; j++) {
                        String temp = node.getRight().get(j + 1);
                        //右边为终结符号，直接装入
                        if (isTerm(temp)) {
                            followSet.get(target).add(temp);
                            break;
                        }
                        //右边为非终结符号，且first集不含ε
                        //则与右边非终结符号的first集求并集
                        else if (!firstSet.get(temp).contains("ε")) {
                            followSet.get(target).addAll(firstSet.get(temp));
                            break;
                        }
                        //右边为非终结符号，且first集含ε
                        //则与右边非终结符号的first集和follow集求并集
                        else {
                            followSet.get(target).addAll(firstSet.get(temp));
                            getFollow(temp);
                            followSet.get(target).addAll(followSet.get(temp));
                            followSet.get(target).remove("ε");
                        }
                    }
                }
            }
            if (node.getLeft().equals("<begin>"))
                followSet.get(target).add("$");
        }
    }


    @Override
    public HashMap<String, HashSet<String>> getAllFirst() {
        return firstSet;
    }

    @Override
    public HashMap<String, HashSet<String>> getAllFollow() {
        return followSet;
    }

    /**
     * @Description: 依据文法，来构造项目集族
     * @Params: grammar，文法；
     * @Return: family，项目集规范族
     * @Author: zhouqian
     * Date: 2019-10-04
     **/
    @Override
    public Family generateFamily(ArrayList<Node> grammar) {
        Family slrFamily = new Family(); //SLR(1)分析表的项目集规范族
        ArrayList<ProjectSet> pSets = new ArrayList<>();
        ProjectSet firstProjectSet = new ProjectSet();
        ArrayList<Node> core = new ArrayList<>(); //核心项目集
        for (Node node : grammar
        ) {
            if (node.getLeft().equals("<begin>")) {
                core.add(node); //第0个项目集的核心集只有一个产生式，就是第一个
                firstProjectSet.setCore(core);
                firstProjectSet.setProduction(getProductionSet(core));//添加非核心项目集
                pSets.add(firstProjectSet);
                break;
            }
        }
        updateProjectSets(firstProjectSet, pSets);
        slrFamily.setSets(pSets);
        return slrFamily;
    }

    /**
     * @Description: 依据DFS，来更新项目集族
     * @Params: currentSet，当前项目集；pSets，项目集族
     * @Return:
     * @Author: zhouqian
     * Date: 2019-10-04
     **/
    public void updateProjectSets(ProjectSet currentSet, ArrayList<ProjectSet> pSets) {
        ArrayList<String> afterPoints = new ArrayList<>();//所有在point后面的符号集合
        HashMap<String, Integer> pointer = new HashMap<>();//DFA映射                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        7
        for (Node node : currentSet.getCore()
        ) {
            String afterPoint = getAfterPoint(node);
            if (!afterPoint.equals("ε") && !afterPoints.contains(afterPoint))
                afterPoints.add(afterPoint);
        }
        for (Node node : currentSet.getProduction()
        ) {
            String afterPoint = getAfterPoint(node);
            if (!afterPoint.equals("ε") && !afterPoints.contains(afterPoint))
                afterPoints.add(afterPoint);
        }

        for (String after : afterPoints
        ) {
            ProjectSet nextSet = getNextProjectSet(currentSet, after);
            int slrIndex = getSLRIndex(nextSet, pSets);
            if (slrIndex != -1)
                pointer.put(after, slrIndex);
            else {
                pSets.add(nextSet);
                pointer.put(after, pSets.size() - 1);
                updateProjectSets(nextSet, pSets);
            }
        }
        currentSet.setPointer(pointer);
    }

    /**
     * @Description: 找出项目集在项目集族中的编号
     * @Params: projectSet，需要寻找的项目集；projectSets，项目集编号
     * @Return: 为负数表示不在项目集中
     * @Author: zhouqian
     * Date: 2019-10-04
     **/
    public int getSLRIndex(ProjectSet projectSet, ArrayList<ProjectSet> projectSets) {
        ArrayList<Node> pCore = projectSet.getCore();
        for (int i = 0; i < projectSets.size(); i++) {
            boolean equal = true;
            ArrayList<Node> core = projectSets.get(i).getCore();
            for (Node node : pCore
            ) {
                if (!core.contains(node))
                    equal = false;
                break;
            }
            //如果pCore中的每一个项目都在core中，并且这两个集合的大小相同，说明它们两个相同
            if (equal && core.size() == pCore.size())
                return i;
        }
        return -1;
    }

    /**
     * @Description: 根据当前项目集和弧上的标记，来推出下一个项目集
     * @Params: currentSet，当前项目集；next，弧上的标记
     * @Return: nextSet，下一个项目集
     * @Author: zhouqian
     * Date: 2019-10-04
     **/
    public ProjectSet getNextProjectSet(ProjectSet currentSet, String next) {
        ProjectSet nextSet = new ProjectSet();
        ArrayList<Node> currentCore = currentSet.getCore();
        ArrayList<Node> nextCore = new ArrayList<>();
        for (Node node : currentCore
        ) {
            if (getAfterPoint(node).equals(next)) {
                Node n = (Node) node.clone();//浅拷贝
                n.setIndex(n.getIndex() + 1);//point后移对应及时index++
                nextCore.add(n);//加入核心项目集
            }
        }
        for (Node node : currentSet.getProduction()
        ) {
            if (getAfterPoint(node).equals(next)) {
                Node n = (Node) node.clone();//浅拷贝
                n.setIndex(n.getIndex() + 1);//point后移
                nextCore.add(n);//加入核心项目集
            }
        }
        nextSet.setCore(nextCore);
        nextSet.setProduction(getProductionSet(nextCore));
        return nextSet;

    }

    /**
     * @Description: 依据核心项目集，获取非核心项目集
     * @Params: core，表示核心项目集
     * @Return: array，表示非核心项目集
     * @Author: zhouqian
     * Date: 2019-10-03
     **/
    public ArrayList<Node> getProductionSet(ArrayList<Node> core) {
        ArrayList<Node> array = new ArrayList<>();
        ArrayList<String> lefts = new ArrayList<>(); //非核心项目集产生式左部的集合
        for (Node node : core
        ) {
            //获取核心项目在.后面的第一个符号，例如A->C.By,afterPoint就是B
            String afterPoint = getAfterPoint(node);
            //将以afterPoint为左部的产生式添加进来
            if (!isTerm(afterPoint) && !afterPoint.equals("ε"))
                updateProductionSet(array, core, afterPoint, lefts);//深度优先搜索
        }
        return array;
    }

    /**
     * @Description: 将所有以特定非终结符号为产生式左部的项目添加进非核心项目集
     * @Params: array，已有的非核心项目集；core，核心项目集；left，特定的非终结符；lefts，现有非核心项目集产生式左部的集合
     * @Return:
     * @Author: zhouqian
     * Date: 2019-10-04
     **/
    public void updateProductionSet(ArrayList<Node> array, ArrayList<Node> core, String left, ArrayList<String> lefts) {
        //只有left不在lefts中，才需要添加
        if (!lefts.contains(left)) {
            lefts.add(left);
            for (Node n : grammar
            ) {
                //要排除已经在核心集当中的产生式
                if (n.getLeft().equals(left) && !core.contains(n)) {
                    array.add(n);
                    String afterPoint = getAfterPoint(n);
                    //每添加一条新的非核心项目，都有可能要将以afterPoint为左部的产生式添加进来
                    if (!isTerm(afterPoint) && !afterPoint.equals("ε"))
                        updateProductionSet(array, core, afterPoint, lefts);
                }
            }
        }
    }

    /**
     * @Description: 获取当前项目在.后面的第一个符号，例如A->C.By,返回值就是B
     * @Params: node，当前项目
     * @Return: afterPoint，point后面的第一个符号，若A->C.,返回值为ε
     * @Author: zhouqian
     * Date: 2019-10-04
     **/
    public String getAfterPoint(Node node) {
        String afterPoint = "ε";
        //首先要判断point的位置是否在末尾
        if (node.getIndex() < node.getRight().size()) {
            afterPoint = node.getRight().get(node.getIndex());
        }
        return afterPoint;
    }


    /**
     * @return : SLRTable
     * @author ：Chang Jiaxin
     * @description ：根据项目集规范组得到SLR(1)分析表
     * @date ：Created in 2019/9/30
     */
    @Override
    public SLRTable generateSLRTable(Family family) {
        SLRTable slrTable = new SLRTable();
        ArrayList<ProjectSet> sets = family.getSets();
        ArrayList<HashMap<String, ArrayList<Pair>>> actions = new ArrayList<>(); //action表
        ArrayList<HashMap<String, ArrayList<Pair>>> gotos = new ArrayList<>();
        for (int i = 0; i < sets.size(); i++) {
            HashMap<String, Integer> pointers = sets.get(i).getPointer();
            ArrayList<Node> cores = sets.get(i).getCore();
            ArrayList<Node> productions = sets.get(i).getProduction();
            HashMap<String, ArrayList<Pair>> actionRow = new HashMap<>();
            HashMap<String, ArrayList<Pair>> gotoRow = new HashMap<>();
            //如果是第一条产生式，就置为acc
//            if (cores.get(0).equalsExceptIndex(grammar.get(0))) {
//                Pair pair0 = new Pair('a');
//                ArrayList<Pair> pairs0 = new ArrayList<>();
//                pairs0.add(pair0);
//                actionRow.put("$", pairs0);
//            }
            //判断能否归约，遍历每一条产生式，先遍历核心项目集
            for (Node core : cores) {
                //如果产生式最后面是点，说明可以归约
                if (core.getRight().size() == core.getIndex()) {
                    //创建pair
                    Pair pair = new Pair('r', getProductionIndex(grammar, core));
                    // 若U→x.属于Ii，则对FOLLOW(U)中的终结符a和$
                    // 均置ACTION[i，a]=rj或ACTION[i，$]=rj
                    for (String str : followSet.get(core.getLeft())) {
                        if (actionRow.containsKey(str)) {
                            actionRow.get(str).add(pair);
                        } else {
                            ArrayList<Pair> pairs = new ArrayList<>();
                            pairs.add(pair);
                            actionRow.put(str, pairs);
                        }
                    }
                    //置终结符的rj
                    ArrayList<Pair> pairs = new ArrayList<>();
                    pairs.add(pair);
                    actionRow.put("$", pairs);
                }
            }
            //再遍历非核心项目集
            for (Node production : productions) {
                //如果产生式右部
                if (production.getRight().size() == production.getIndex()) {
                    Pair pair = new Pair('r', getProductionIndex(grammar, production));
                    for (String str : followSet.get(production.getLeft())) {
                        if (actionRow.containsKey(str)) {
                            actionRow.get(str).add(pair);
                        } else {
                            ArrayList<Pair> pairs = new ArrayList<>();
                            pairs.add(pair);
                            actionRow.put(str, pairs);
                        }
                    }
                    ArrayList<Pair> pairs = new ArrayList<>();
                    pairs.add(pair);
                    actionRow.put("$", pairs);
                }
            }
            //遍历所有pointer，写移进和goto
            for (Map.Entry<String, Integer> pointer : pointers.entrySet()) {
                //如果是终结符，就可以移进
                if (isTerm(pointer.getKey())) {
                    //移进
                    Pair pair = new Pair('S', pointer.getValue());
                    //每一次加pair都要判断是否已经有数组，没有就需要新建
                    if (actionRow.containsKey(pointer.getKey())) {
                        actionRow.get(pointer.getKey()).add(pair);
                    } else {
                        ArrayList<Pair> pairs = new ArrayList<>();
                        pairs.add(pair);
                        actionRow.put(pointer.getKey(), pairs);
                    }
                }
                //如果是非终结符，就写到GOTO表
                else {
                    ArrayList<Pair> pairs = new ArrayList<>();
                    Pair pair = new Pair(pointer.getValue());
                    pairs.add(pair);
                    gotoRow.put(pointer.getKey(), pairs);
                }
            }
            actions.add(i, actionRow);
            gotos.add(i, gotoRow);
        }
        slrTable.setActions(actions);
        slrTable.setGotos(gotos);
        return slrTable;
    }

    /**
     * @author ：Chang Jiaxin
     * @description ：获取产生式在文法中的位置，生成r几
     * @date ：Created in 2019/10/5
     */
    public int getProductionIndex(ArrayList<Node> grammar, Node production) {
        if (production == null) {
            for (int i = 0; i < grammar.size(); i++) {
                if (grammar.get(i) == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < grammar.size(); i++) {
                if (production.equalsExceptIndex(grammar.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * @param tokens :词法分析产生的单词序列
     * @return : 1.PR为空:输入的tokens为空
     * : 2.PR中passed为false：语法分析未通过，此时PR中curToken应保存当前token（其中有错误行数信息）
     * : 3.PR中passed为false且curToken为空：输入串已访问到结尾 $
     * @description :生成语法分析结果 ParserResult（以下简称PR）
     */
    public ParserResult syntaxCheck(List<Token> tokens) {

        //用于保存语法分析结果
        ParserResult parserResult = new ParserResult(tokens.get(0));
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Pair> pairs = new ArrayList<>();//用于保存移进归约过程
        ArrayList<String> symbols = new ArrayList<>();//用于保存过程中的符号栈
        ArrayList<String> states = new ArrayList<>();//用于保存过程中的状态栈

        ArrayList<ArrayList<Object>> symbols_Object = new ArrayList<>();//用于保存过程中的符号对象栈

        try {
            init("grammar/grammar.txt");
            Family family = generateFamily(getGrammar());
            SLRTable slrTable = generateSLRTable(family);

            String terminalStr;//用于将token转换为String匹配slr表
            Stack<Integer> state = new Stack<>();//状态栈
            Stack<String> symbol = new Stack<>();//符号栈
            ArrayList<Object> symbol_Object = new ArrayList<>();//符号对象栈

            ArrayList<HashMap<String, ArrayList<Pair>>> actions = slrTable.getActions();//action表
            ArrayList<HashMap<String, ArrayList<Pair>>> gotos = slrTable.getGotos();//goto表
            if (tokens.isEmpty()) return null;
            state.push(0);
            tokens.add(new Token("$"));

            //每次循环就是一次移进/归约
            for (Token token : tokens) {
                //检查下一步动作
                if (!actions.get(state.peek()).containsKey(token.getName())) {
                    if (token.getTokenType().equals(Token.Symbol.number)) {
                        terminalStr = "num";
                    } else if (token.getTokenType().equals(Token.Symbol.fnumber)) {
                        terminalStr = "real";
                    } else if (token.getTokenType().equals(Token.Symbol.mainsym)) {
                        terminalStr = "id";
                    } else if (token.getTokenType().equals(Token.Symbol.ident)) {
                        terminalStr = "id";
                    } else {
                        parserResult.setPassed(false);
                        parserResult.setCurToken(token);
                        String d = "归约失败，action表访问到空节点或表中无此终结符，当前格子：："+state.peek()+","+symbol.peek();
                        stringBuilder.append(d);
                        //return new Result(stringBuilder.toString());
                        return parserResultBuilder(parserResult,d,false,pairs,states,symbols,symbols_Object,new Result(stringBuilder.toString()));
                    }
                } else {
                    terminalStr = token.getName();
                }
                char c = actions.get(state.peek()).get(terminalStr).get(0).getC();
                int num = actions.get(state.peek()).get(terminalStr).get(0).getNum();
                //冲突解决:移进归约冲突选移进
                if (actions.get(state.peek()).get(terminalStr).size() != 1) {
                    if (actions.get(state.peek()).get(terminalStr).get(1).getC() == 'S') {
                        c = 'S';
                        num = actions.get(state.peek()).get(terminalStr).get(1).getNum();
                    }
                }

                // r 归约(在移进循环中进行归约循环)
                if (c == 'r') {
                    while(c == 'r' && num != 0) {
                        //slr表r0表示acc通过
                        ArrayList<String> right = grammar.get(num).getRight();//产生式右部
                        String left = grammar.get(num).getLeft();//产生式左部

                        for (int i = right.size() - 1; i >= 0; i--) {
                            if (symbol.empty()) {
                                parserResult.setPassed(false);
                                parserResult.setCurToken(token);
                                String d = "归约失败，可能是slr表或文法问题，当前格子：" + state.peek() + "," + symbol.peek();
                                stringBuilder.append(d);
                                //return new Result(stringBuilder.toString());
                                return parserResultBuilder(parserResult,d,false,pairs,states,symbols,symbols_Object,new Result(stringBuilder.toString()));
                            }

                            if (symbol.peek().equals(right.get(i))) {
                                //符号栈出栈
                                symbol.pop();
                                symbol_Object.remove(symbol_Object.size()-1);
                                state.pop();
                            } else {
                                parserResult.setPassed(false);
                                parserResult.setCurToken(token);
                                String d = "归约失败，符号表和产生式右部不匹配，当前格子：" + state.peek() + "," + symbol.peek();
                                stringBuilder.append(d);
                                //return new Result(stringBuilder.toString());
                                return parserResultBuilder(parserResult,d,false,pairs,states,symbols,symbols_Object,new Result(stringBuilder.toString()));
                            }
                        }
                        //保存符号对象栈
                        String leftSymbolClassString = "whu.se.interpret.po.symbol.";
                        String leftSymbolString = left.substring(1,left.length()-1);
                        leftSymbolClassString += leftSymbolString;
                        Class<?> SymbolClass = Class.forName(leftSymbolClassString);
                        Constructor<?> constructor = SymbolClass.getConstructor(String.class);
                        Object symObj = constructor.newInstance(leftSymbolString);
                        symbol_Object.add(symObj);
                        symbols_Object.add(new ArrayList<>(symbol_Object));
                        //符号栈非终结符入栈,符号栈处理完毕
                        symbol.push(left);
                        symbols.add(symbol.toString());
                        StringBuilder sym = new StringBuilder();
                        for(String s:symbol){
                            sym.append(s);
                        }
                        stringBuilder.append(String.format("%-70s", sym.toString()));
                        stringBuilder.append("归约\t").append(grammar.get(num).getLeft()).append("→");
                        for (String s : grammar.get(num).getRight()) {
                            stringBuilder.append(s);
                        }
                        stringBuilder.append("\n");
                        if (!gotos.get(state.peek()).containsKey(left)) {
                            parserResult.setPassed(false);
                            parserResult.setCurToken(token);
                            String d = "归约失败，goto表访问到空节点或表中无此非终结符，当前格子：" + state.peek() + "," + symbol.peek();
                            stringBuilder.append(d);
                            //return new Result(stringBuilder.toString());
                            return parserResultBuilder(parserResult,d,false,pairs,states,symbols,symbols_Object,new Result(stringBuilder.toString()));
                        }
                        //状态栈入栈
                        state.push(gotos.get(state.peek()).get(left).get(0).getNum());
                        states.add(state.toString());
                        pairs.add(new Pair(c, num));//保存移进或归约动作

                        //检查下一步动作
                        if (!actions.get(state.peek()).containsKey(terminalStr)) {
                            parserResult.setPassed(false);
                            parserResult.setCurToken(token);
                            String d = "归约失败，action表访问到空节点或表中无此终结符，当前格子："+state.peek()+","+symbol.peek();
                            stringBuilder.append(d);
                            //return new Result(stringBuilder.toString());
                            return parserResultBuilder(parserResult,d,false,pairs,states,symbols,symbols_Object,new Result(stringBuilder.toString()));

                        }
                        c = actions.get(state.peek()).get(terminalStr).get(0).getC();
                        num = actions.get(state.peek()).get(terminalStr).get(0).getNum();
                        //冲突解决:移进归约冲突选移进
                        if (actions.get(state.peek()).get(terminalStr).size() != 1) {
                            if (actions.get(state.peek()).get(terminalStr).get(1).getC() == 'S') {
                                c = 'S';
                                num = actions.get(state.peek()).get(terminalStr).get(1).getNum();
                            }
                        }
                    }
                }
                Terminal terminal = new Terminal(terminalStr);
                terminal.setToken(token);
                symbol_Object.add(terminal);
                symbols_Object.add(new ArrayList<>(symbol_Object));
                // S 移进
                symbol.push(terminalStr);
                symbols.add(symbol.toString());
                state.push(num);
                states.add(state.toString());
                pairs.add(new Pair(c, num));//保存移进或归约动作
                StringBuilder sym = new StringBuilder();
                for(String s:symbol){
                    sym.append(s);
                }
                stringBuilder.append(String.format("%-70s",sym));
                stringBuilder.append("移进\t").append(terminalStr).append("\n");
            }

        } catch (Exception e) {
            stringBuilder.append("\n程序出现异常:").append(e.getMessage());
            //return new Result(stringBuilder.toString());
            return parserResultBuilder(parserResult,"\n程序出现异常:"+e.getMessage(),false,pairs,states,symbols,symbols_Object,new Result(stringBuilder.toString()));
        }
        stringBuilder.append("语法分析已通过");
        //return new Result(stringBuilder.toString());
        return parserResultBuilder(parserResult,"语法分析已通过",true,pairs,states,symbols,symbols_Object,new Result(stringBuilder.toString()));
    }

    private ParserResult parserResultBuilder(ParserResult parserResult, String description, boolean passed,
                                                  ArrayList<Pair> pairs, ArrayList<String> states, ArrayList<String> symbols,
                                                  ArrayList<ArrayList<Object>> symbols_Object, Result result) {
        parserResult.setDescription(description);
        parserResult.setPassed(passed);
        parserResult.setPairs(pairs);
        parserResult.setStates(states);
        parserResult.setSymbols_String(symbols);
        parserResult.setSymbols_Object(symbols_Object);
        parserResult.setResult(result);
        return parserResult;
    }
}