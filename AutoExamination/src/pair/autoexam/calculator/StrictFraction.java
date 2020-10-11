package pair.autoexam.calculator;

import java.util.Objects;

import pair.autoexam.throwable.StrictFractionCalculateException;
import pair.autoexam.throwable.StrictFractionFormatException;

public class StrictFraction {
	
	public static final char LINE = '/';
	public static final char COMMA = '\'';
	public static final char NEGATIVE = '-';

	//辗转相除法，要求a>=b>0
	private static int divisionAlgorithm(int a, int b)
	{
		int r0 = 0;
		int r1 = a;
		int r2 = b;
		int q;
		do {
			//切换带余除法的除数与被除数
			r0=r1;
			r1=r2;
			//计算商
			q =  r0 / r1;
			//计算余数
			r2 = r0 - q*r1;
		}while(r2 > 0);
		
		assert 0 == r2;
		
		return r1;
	}
	
	//求其正值的最大公因子，0与任何数的最大公因子为0
	public static int maxCommonFactor(int a, int b)
	{
		if( 0 == a || 0 == b)
			return 0;
		//负数处理
		@SuppressWarnings("unused")
		boolean negative_a = false;
		@SuppressWarnings("unused")
		boolean negative_b = false;
		if(a < 0)
		{
			negative_a = true;
			a = -a;
		}
		if(b < 0)
		{
			negative_b = true;
			b = -b;
		}
		//辗转相除求两正数的最大公因子
		int max = a>=b ? a : b;
		int min = a<=b ? a : b;
		int result = StrictFraction.divisionAlgorithm(max, min);
		
		return result;
	}
	
	//求其正值的最小公倍数，不能为0
	public static int minCommonMultiple(int a, int b)
	{
		if( 0 == a || 0 == b)
			throw new IllegalArgumentException("0与任何数没有最小公倍数");
		//负数处理
		@SuppressWarnings("unused")
		boolean negative_a = false;
		@SuppressWarnings("unused")
		boolean negative_b = false;
		if(a<0)
		{
			negative_a = true;
			a = -a;
		}
		if(b<0)
		{
			negative_b = true;
			b = -b;
		}
		//辗转相除法求两正数最大公因子
		int max = a>=b? a : b;
		int min = a<=b? a : b;
		int factor = StrictFraction.divisionAlgorithm(max, min);
		//最小公倍数求法[a,b]=a*b/(a,b)=a/(a,b) * b;
		max = max / factor;
		
		int result = max * min;
		return result;
	}


	/**
	 * 将字符串表示的分数转换成数组表示的分数(分母不为0)。
	 * 数组[0]是分子,数组[1]是分母。
	 * 字符串表示包括，整数，真分数，假分数，带分数。
	 * @param fraction 分数的字符串表示
	 * @return 分数的数组表示
	 * @throws StrictFractionFormatException 
	 */
	public static int[] parseArray(String fraction) throws StrictFractionFormatException
	{
		Objects.requireNonNull(fraction);
		
		int n = -1;//带数，-1表示未曾赋值
		int m = -1;//分子，-1表示未曾赋值
		int d = -1;//分母，-1表示未曾赋值
		int term = -1;//解析出来的数字，-1表示已经赋值给对应n,m,d中，表示没有可处理的数字。
		
		boolean expect_m = false;//如果检测到',显然期望有分子
		boolean expect_d = false;//如果检测到/,显然期望有分母
	
		//获取带数和分子
		for(int i = 0; i < fraction.length(); i++)
		{
			//获取一个数字
			if('0'<=fraction.charAt(i)  && fraction.charAt(i)<= '9' )
			{
				int begin = i;
				do{
					i++;
				}while(i < fraction.length() && '0'<=fraction.charAt(i)  && fraction.charAt(i)<= '9');
				term = Integer.parseInt(fraction.substring(begin, i));
				
				i--;//为了简便处理分隔符，直接跳到下一次大循环来处理分隔符。
			}
			else if( -1 != term)  //遇到非数字，且有待处理的数字
			{	
				assert fraction.charAt(i) < '0' || '9' < fraction.charAt(i) ;
			
				switch(fraction.charAt(i)) {
				case StrictFraction.COMMA:
					if(-1 == n)	//第1次赋值带数
					{
						n = term;
						term = -1;
						expect_m = true;	//期望出现分子
					}else
						throw new StrictFractionFormatException("带数重复");
					break;
				case StrictFraction.LINE :
					if(-1 == m)//第1次赋值分子
					{
						m = term;
						term = -1;
						expect_m = false;	//消除分子期望
						expect_d = true;	//期望出现分母
					}else
						throw new StrictFractionFormatException("分子重复");
					break;
				default : 
						throw new StrictFractionFormatException("出现非法分隔符");
				}
				
				term = -1;//标记数字已经被处理。
			}
			else //遇到分隔符，却没有待处理的数字
			{
				String message = null;
				if (0 == i)
					message = "第1个字符期望是数字，但是实际上却是：“" + fraction.charAt(0) + "”";
				else
				{
					assert fraction.charAt(i-1) < '0' || '9' < fraction.charAt(i-1) ;
					message = "分隔符不应该连续出现";
				}
				throw new StrictFractionFormatException(message);	
			}	
		}
		
		//处理最后一个数字
		if(expect_m)//如果期望分子,则说明是带分数，且没有检测到分子与分母的分隔符
		{	
			if(-1 != term){//有待处理的数字，说明该数字应该是分子，分数表达式缺少分母
				throw new StrictFractionFormatException("没有分母");
			}else{//没有待处理的数字，说明缺少分子，分母
				throw new StrictFractionFormatException("没有分子，分母");
			}
			
		}
		else if(expect_d)//如果期望分母，则说明是分数
		{ 
			assert false == expect_m && -1 != m && -1 == d; //已经获得分子,没获得分母
		
			if(-1 != term)//有待处理数字，说明是分母
			{
				if(0 != term)
					d = term;
				else
					throw new StrictFractionFormatException("分母不应该为0");
			}
			else//没有待处理的数字，说明没有分母
				throw new StrictFractionFormatException("没有分母");
			
		}
		else//没有期望分母，说明是整数
		{
			assert -1 == n && -1 == d;
			
			if(-1 != term)
			{
				m = term;
				d = 1;	//整数是分母为1，分子为其值的分数
			}
			else
			{
				throw new StrictFractionFormatException("没有数字");
			}	
		}
			
		//计算分数
		if(-1 == n)
			n = 0;
		m += n * d;
		
		return new int[] {m,d};
	}

	/**
	 * 将数组表示的分数转换成字符串表示的分数(分母不为0)
	 * 数组[0]是分子，数组[1]是分母。
	 * 如果cut=true,那么将约整为带分数或整数，否则以假分数或真分数的形式表示分数。
	 * @param fraction 分数的数组表示
	 * @param cut 是否约整
	 * @return 分数的字符串表示
	 * @throws StrictFractionFormatException 
	 */
	public static String parseString(int[] fraction, boolean cut) throws StrictFractionFormatException
	{
		Objects.requireNonNull(fraction);
		if(fraction.length != 2)
			throw new StrictFractionFormatException("数组长度不是2");
		if(fraction[0] < 0 )
			throw new StrictFractionFormatException("分子应该大于或等于0");
		if(fraction[1] <= 0 )
			throw new StrictFractionFormatException("分母应该大于0");
		
		int n = 0;
		int m = fraction[0];
		int d = fraction[1];
		String result = "";
			
		if(cut)
		{
			n = m/d;
			m = m - n*d;
			if(n > 0)//显然分子大于或等于分母
			{
				if(m > 0)//带分数
				{
					result += n;
					result += StrictFraction.COMMA;
					result += m;
					result += StrictFraction.LINE;
					result += d;
				}
				else//整数
				{
					assert 0 == m;
					result += n;
				}
				
			}
			else if(m > 0)//分子小于分母，且分子不为0
			{ 
				assert 0 == n;
				assert m == fraction[0];
				//真分数
				result += m;
				result += StrictFraction.LINE;
				result += d;
			}
			else//分子小于分母，且分子为0
			{
				//0
				assert 0 == n;
				assert 0 == m;
				result += 0;
			}
		}
		else
		{
			result += m;
			result += StrictFraction.LINE;
			result += d;
		}
		
		return result;
	}
	
	//约分
	private static int[] reduce(int[] fraction)
	{
		assert null != fraction;
		assert 2 == fraction.length;
		
		int factor = StrictFraction.maxCommonFactor(fraction[0], fraction[1]);
		int[] result = new int[2];
		
		if(0 == factor || 1 == factor)
		{
				result[0] = fraction[0];
				result[1] = fraction[1];
		}
		else//公因子不为0,1时候才约分
		{
			assert 0 != factor && 1 != factor;
			result[0] = fraction[0] / factor;
			result[1] = fraction[1] / factor;
			
		}

		return result;
	}
	
	
	public static String add(String a, String b, boolean cut) throws StrictFractionFormatException, StrictFractionCalculateException
	{
		//转换数据，同时也检验了参数
		int[] adder1 = StrictFraction.parseArray(a);
		int[] adder2 = StrictFraction.parseArray(b);
		//约分再计算，以减少溢出的可能性
		adder1 = StrictFraction.reduce(adder1);
		adder2 = StrictFraction.reduce(adder2);
		//分解分数
		int am = adder1[0];//分数a的分子
		int ad = adder1[1];//分数a的分母
		int bm = adder2[0];//分数b的分子
		int bd = adder2[1];//分数b的分母
		//通分
		int min_common_multiple = StrictFraction.minCommonMultiple(ad, bd);
		am *= (min_common_multiple / ad);
		bm *= (min_common_multiple / bd);
		//相加
		int[] result = new int[] {am+bm, min_common_multiple};
		//检验结果
		if(result[0] <0 )
			throw new StrictFractionCalculateException("计算溢出");
		//结果约分
		result = StrictFraction.reduce(result);
		//返回结果
		return StrictFraction.parseString(result, cut);
	}

	public static String subtract(String a, String b, boolean cut) throws StrictFractionFormatException, StrictFractionCalculateException
	{
		//转换数据，同时也检验了参数
		int[] adder1 = StrictFraction.parseArray(a);
		int[] adder2 = StrictFraction.parseArray(b);
		//约分再计算
		adder1 = StrictFraction.reduce(adder1);
		adder2 = StrictFraction.reduce(adder2);
		//分解分数
		int am = adder1[0];//分数a的分子
		int ad = adder1[1];//分数a的分母
		int bm = adder2[0];//分数b的分子
		int bd = adder2[1];//分数b的分母
		//通分
		int min_common_multiple = StrictFraction.minCommonMultiple(ad, bd);
		
		am *= (min_common_multiple / ad);
		bm *= (min_common_multiple / bd);
		//相减
		int[] result = new int[] {am-bm, min_common_multiple};
		//检验结果
		if(result[0] <0 )
			throw new StrictFractionCalculateException("结果小于0，被减数不应该小于减数");
		//结果约分
		result = StrictFraction.reduce(result);
		return StrictFraction.parseString(result, cut);
	}
	
	public static String times(String a, String b, boolean cut) throws StrictFractionFormatException, StrictFractionCalculateException
	{
		//转换数据，同时也检验了参数
		int[] adder1 = StrictFraction.parseArray(a);
		int[] adder2 = StrictFraction.parseArray(b);
		//约分再计算
		adder1 = StrictFraction.reduce(adder1);
		adder2 = StrictFraction.reduce(adder2);
		//交错约分
		int term = adder1[1];
		adder1[1] = adder2[1];
		adder2[1] = term;
		adder1 = StrictFraction.reduce(adder1);
		adder2 = StrictFraction.reduce(adder2);
		//分解分数
		int am = adder1[0];//分数a的分子
		int ad = adder2[1];//分数a的分母
		int bm = adder2[0];//分数b的分子
		int bd = adder1[1];//分数b的分母
		//乘法
		int m = am * bm;
		int d = ad * bd;
		//检验结果
		if(m < 0 || d < 0)
			throw new StrictFractionCalculateException("计算溢出");
		
		int[] result = new int[] {m,d};
		return StrictFraction.parseString(result, cut);
	}
	
	public static String divide(String a, String b, boolean cut) throws StrictFractionFormatException, StrictFractionCalculateException
	{
		//转换数据，同时也检验了参数
		int[] adder1 = StrictFraction.parseArray(a);
		int[] adder2 = StrictFraction.parseArray(b);
		if(0 == adder2[0] )
			throw new StrictFractionCalculateException("除数不应该为0");
		//除法=倒置乘法
		int term = adder2[0];
		adder2[0] = adder2[1];
		adder2[1] = term;
		
		//约分再计算
		adder1 = StrictFraction.reduce(adder1);
		adder2 = StrictFraction.reduce(adder2);
		//交错约分
		term = adder1[1];
		adder1[1] = adder2[1];
		adder2[1] = term;
		adder1 = StrictFraction.reduce(adder1);
		adder2 = StrictFraction.reduce(adder2);
		//分解分数
		int am = adder1[0];//分数a的分子
		int ad = adder2[1];//分数a的分母
		int bm = adder2[0];//分数b的分子
		int bd = adder1[1];//分数b的分母
		//乘法
		int m = am * bm;
		int d = ad * bd;
		//检验结果
		if(m < 0 || d < 0)
			throw new StrictFractionCalculateException("计算溢出");
		
		int[] result = new int[] {m,d};
		return StrictFraction.parseString(result, cut);
	}

	public static int compare(String a, String b) throws StrictFractionFormatException
	{
		//转换数据，同时也检验了参数
		int[] adder1 = StrictFraction.parseArray(a);
		int[] adder2 = StrictFraction.parseArray(b);
		//约分再计算
		adder1 = StrictFraction.reduce(adder1);
		adder2 = StrictFraction.reduce(adder2);
		//分解分数
		int am = adder1[0];//分数a的分子
		int ad = adder1[1];//分数a的分母
		int bm = adder2[0];//分数b的分子
		int bd = adder2[1];//分数b的分母
		//通分
		int min_common_multiple = StrictFraction.minCommonMultiple(ad, bd);
		am *= (min_common_multiple / ad);
		bm *= (min_common_multiple / bd);
		//比较大小
		if(am < bm)
			return -1;
		else if(am == bm)
			return 0;
		else
			return 1;
	}
}
