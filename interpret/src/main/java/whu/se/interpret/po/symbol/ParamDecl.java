/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:22:26 PM
 */

@Setter
@Getter
public class ParamDecl extends NoTerminal{

	/**
	 * @param name
	 */
	public ParamDecl(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}