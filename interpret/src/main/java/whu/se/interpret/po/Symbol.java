package whu.se.interpret.po;

/**
 * @author xsy
 * @description: 所有非终结符号和终结符号的父类
 * @date 10/9/2019 2:40 PM
 */
class Symbol {
    String name;
}

class NoTerminal extends Symbol{}

class Terminal extends Symbol{}

class Begin extends NoTerminal{}

class Program extends NoTerminal{}

class Bases extends NoTerminal{}

class Base extends NoTerminal{}

class Decl extends NoTerminal{}

class Decls extends NoTerminal{}

class StructDef extends NoTerminal{}

class Function extends NoTerminal{}

class Type extends NoTerminal{}

class Bool extends NoTerminal{}

class FuncDecl extends NoTerminal{}

class Block extends NoTerminal{}

class ParamDecls extends NoTerminal{}

class ParamDecl extends NoTerminal{}

class FuncUse extends NoTerminal{}

class Factors extends NoTerminal{}

class Factor extends NoTerminal{}

class Stmts extends NoTerminal{}

class Stmt extends NoTerminal{}

class Asgn extends NoTerminal{}

class Loc extends NoTerminal{}

class Join extends NoTerminal{}

class Equality extends NoTerminal{}

class Rel extends NoTerminal{}

class Expr extends NoTerminal{}

class Term extends NoTerminal{}

class Unary extends NoTerminal{}

class M extends NoTerminal{}

class N extends NoTerminal{}

