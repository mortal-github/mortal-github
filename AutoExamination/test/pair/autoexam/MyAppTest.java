package pair.autoexam;

import org.junit.jupiter.api.Test;

class MyAppTest {

	@Test
	void testMain() {
		int n = 10000;
		int r = 30;
		MyApp.main(new String[] {"-n", n+"", "-r", r+""} );
	}

	@Test
	void testCreateExpressionArray() {
		
//		//以下配置才能在22秒内生成10000到算术题
//		int expression_count = 10000;
//		int[] operators_count = new int[] {3};
//		int fraction_count = 0;
//		int min = 0;
//		int max = 100;
//		int unit = 100;
		
//以下配置在2秒内生成1000道算术题
		int expression_count = 1000;
		int[] operators_count = new int[] {3};
		int fraction_count = 0;
		int min = 0;
		int max = 10;
		int unit = 100;
//以下配置只能生成1000多道题(运行2~3秒)
//int expression_count = 10000;
//int[] operators_count = new int[] {3};
//int fraction_count = 0;
//int min = 0;
//int max = 10;
//int unit = 100;
		
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
