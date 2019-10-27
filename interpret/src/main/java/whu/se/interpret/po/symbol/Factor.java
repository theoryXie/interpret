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
 * @date Oct 9, 2019 8:23:10 PM
 */

@Setter
@Getter
public class Factor extends NoTerminal{

	private Token token;//?到底传啥

	/**
	 * @param name
	 */
	public Factor(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}
