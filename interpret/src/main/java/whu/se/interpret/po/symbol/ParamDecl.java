/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:22:26 PM
 */

@Setter
@Getter
public class ParamDecl extends NoTerminal{

	private String paramName;
	private String type;



	/**
	 * @param name
	 */
	public ParamDecl(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}
