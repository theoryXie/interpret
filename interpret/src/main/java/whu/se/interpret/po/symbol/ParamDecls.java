/**
 * 
 */
package whu.se.interpret.po.symbol;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @Description:
 * @author: theory
 * @date Oct 9, 2019 8:22:07 PM
 */

@Setter
@Getter
public class ParamDecls extends NoTerminal{

	private ArrayList<ParamDecl> params;

	/**
	 * @param name
	 */
	public ParamDecls(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		params = new ArrayList<>();
	}}
