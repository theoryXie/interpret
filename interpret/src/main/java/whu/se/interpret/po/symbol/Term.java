/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;
import whu.se.interpret.po.Token;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:25:08 PM
 */
@Getter
@Setter
public class Term extends NoTerminal{

	private Token token;

	/**
	 * @param name
	 */
	public Term(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		token = new Token("");
	}}
