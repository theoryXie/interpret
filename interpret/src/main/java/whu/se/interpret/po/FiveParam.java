/**
 * 
 */
package whu.se.interpret.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 五元式
 * @author: theory
 * @date Oct 15, 2019 9:43:41 PM
 */

@Getter
@Setter
public class FiveParam {
	private String oper;//操作符
	private String param_1;//第一个参数
	private String param_2;//第二个参数
	private String param_3;//第三个参数
	private int row;//行号

	public FiveParam(String oper, String param_1, String param_2, String param_3, int row) {
		this.oper = oper;
		this.param_1 = param_1;
		this.param_2 = param_2;
		this.param_3 = param_3;
		this.row = row;
	}

	@Override
	public String toString() {
		String ans = "(" + oper + "," + param_1 + "," + param_2 + "," + param_3 + "," + row +")";
		return ans;
	}
	
	
}
