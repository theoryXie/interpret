/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:20:37 PM
 */

@Getter
@Setter
public class Type extends NoTerminal{


	private String type;//数据类型

	/**
	 * @param name
	 */
	public Type(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}}
