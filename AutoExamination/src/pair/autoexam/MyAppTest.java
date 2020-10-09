package pair.autoexam;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MyAppTest {

	@Test
	void testMain() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateExpressionArray() {
		
		int expression_count = 1000;
		int[] operators_count = new int[] {3};
		int fraction_count = 0;
		int min = 0;
		int max = 100;
		int unit = 100;
		
		Expression[] expression = MyApp.createExpressionArray( expression_count, operators_count, fraction_count, min,  max, unit) ;
		
		for(Expression exp : expression)
		{
		
			System.out.println(exp.getInffix() + " = " + exp.getResult());
		}
		System.out.println(expression.length);
		//局部变量过多，内存消耗严重，
		//生成表达式成分失败概率高，导致生成一个表达式循环语句过多次。
		//比较表达式是否相同复杂度O(n*2),太高了
		//限制越多，失败次数越多，时间消耗越大
		//暂时没有限制结果为真分数
	}

}
