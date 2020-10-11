package pair.autoexam;

import java.util.Random;

import org.junit.jupiter.api.Test;

import pair.autoexam.calculator.StrictFraction;
import pair.autoexam.throwable.StrictFractionCalculateException;
import pair.autoexam.throwable.StrictFractionFormatException;

class ExpressionTest {

	@Test
	void testOfExpression() {
		//fail("Not yet implemented");
	}

	@Test
	void testOfExpressionArray() throws StrictFractionFormatException, StrictFractionCalculateException {
		int[] ops_count = new int[] {4*4};
		int count_fraction  = 2;
		int count = 1000;
		
		Random random = new Random(System.currentTimeMillis());
		
		long millis = System.currentTimeMillis();
		Expression[] array = Expression.ofExpressionArray(ops_count, count_fraction, 
				()->{//整数生成器
					int integer = random.nextInt(20);
					String result = "";
					try {
						result += StrictFraction.parseString(new int[] {integer,1}, true);
					} catch (StrictFractionFormatException e) {
						e.printStackTrace();
					}
					return result;
				}, 
				()->{//分数生成器
					int m = random.nextInt(100);
					int d = random.nextInt(10);
					if(0 == d)
						d=1;
					String result = "";
					try {
						result+= StrictFraction.parseString(new int[] {m,d}, true);
					} catch (StrictFractionFormatException e) {
						e.printStackTrace();
					}
					return result;
					
				},
				str->{//结果验证
					try {
						if(StrictFraction.compare(str, "0") != -1
								&& StrictFraction.compare(str, "100") != 1)
							return true;
					} catch (StrictFractionFormatException e) {
						e.printStackTrace();
						return false;
					}
					return false;
				}, count);
		
		millis = System.currentTimeMillis()-millis;
		
		for(Expression exp : array)
		{
			String str = exp.getInffix();
			System.out.println(str + " = " + exp.getResult());
		}
		System.out.println("time is :" + millis );
		System.out.println("count = " + array.length);
	}

	@Test
	void testGetInffix() {
		//fail("Not yet implemented");
	}

	@Test
	void testGetResult() {
		//fail("Not yet implemented");
	}

	@Test
	void testGetSuffix() {
		//fail("Not yet implemented");
	}

	@Test
	void testEqualsObject() {
		//fail("Not yet implemented");
	}

}
