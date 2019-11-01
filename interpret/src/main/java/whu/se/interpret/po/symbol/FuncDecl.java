/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:21:18 PM
 */

@Getter
@Setter
public class FuncDecl extends NoTerminal{

	private String funcName;//函数名

	/**
	 * @param name
	 */
	public FuncDecl(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}
