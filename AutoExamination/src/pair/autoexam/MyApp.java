package pair.autoexam;


import static org.junit.Assert.assertNotNull;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import pair.autoexam.calculator.StrictFraction;
import pair.autoexam.throwable.StrictFractionCalculateException;
import pair.autoexam.throwable.StrictFractionFormatException;

public class MyApp {
	private static String dir = System.getProperty("user.dir");
	private static String exercises = "Exercised.txt";
	private static String answer = "Answerfile.txt";
	private static String grade = "Grade.txt";
	
	public static void main(String[] args)
	{
		//参数
		int r = 10;
		int n = 10;
		Path e = null;
		Path a = null;
		//参数状态
		boolean r_meet = false;
		boolean n_meet = false;
		boolean e_meet = false;
		boolean a_meet = false;
		//信息
		String error  = "";
		String info = "";
		outer:
		for(int i = 0 ; i< args.length; i++)
		{
			switch(args[i]) {
			case "-r":
				if(r_meet)
				{
					error += "option \"-r\" is repeat!\n";
					break outer;
				}
				else
				{
					i++;
					String number = args[i];
					try {
						r = Integer.valueOf(number);
						r_meet = true;
						break;
					}catch(NumberFormatException excp)
					{
						error += "option -r parameter \""+number + "\"is not a correct integer.\n";
						break outer;
					}
					
				}
			case "-n":
				if(n_meet)
				{
					error += "option \"-n\" is repeat!\n";
					break outer;
				}
				else
				{
					i++;
					String number = args[i];
					try {
						n = Integer.valueOf(number);
						n_meet = true;
						break;
					}catch(NumberFormatException excp)
					{
						error += "option -n parameter \""+ number + "\"is not a correct integer.\n";
						break outer;
					}
					
				}
			case "-e":
				if(e_meet)
				{
					error += "option \"-e\" is repeat!\n";
					break outer;
				}
				else
				{
					i++;
					String exrcisefile = args[i];
					try {
						e = Paths.get(exrcisefile);
						e_meet = true;
						break;
					}catch(InvalidPathException exp)
					{
						error += "option -e parameter \""+ exrcisefile +"\" is invalid path.";
						break outer;
					}
				}
			case "-a":
				if(a_meet)
				{
					error += "option \"-a\" is repeat!\n";
					break outer;
				}
				else
				{
					i++;
					String answerfile = args[i];
					try {
						a = Paths.get(answerfile);
						a_meet = true;
						break;
					}catch(InvalidPathException exp)
					{
						error += "option -e parameter \""+ answerfile +"\" is invalid path.";
						break outer;
					}
				}
			default : 
				error += args[i] + " is unkown options!";
				break outer;
			}//end siwtch
		}//end for
		
		if(!error.equals(""))
		{
			//参数检查的错误信息
			System.out.println(error);
			return;
		}
		
		if(r_meet || n_meet)
		{
			//要生成题目，就必须同时提供-r,-n
			if(r_meet && n_meet)
			{
				//TODO 生成题目
				return ;
			}
			else
			{	
				error += "miss option ";
				if(r_meet == false)
					error += "\"-r\"";
				else
				{
					assert n_meet == false;
					error += "\"-n\"";
				}
			}
		}
		else//不生成题目
		{
			//要比较答案肯定需要同时提供题目文件和答案文件
			if(e_meet || a_meet)
			{
				if(e_meet && a_meet)
				{
					//TODO 比较答案
					return ;
				}
				else
				{
					error += "miss option ";
					if(e_meet == false)
						error += "\"-e\"";
				}
			}
			else
			{
				error += "你没有输入足够的选项";
			}
			
		}
		
		//选项检查的错误信息
		System.out.println(error);
	}
	
	/*
	 * 共生成expreeesion_count个不重复的表达式
	 * 数值的最小值为min,最大值为max
	 * 如果产生分数，则0<分母<=util ,分数值最小为min,最大为max
	 ** 若op_count.length = 1 ,则表示加减乘除运算符总数，op_count值数组operators_count.
	 * 若op_count.length = 2,则表示加减 、乘除 运算符分别的总数,即op_count[0]是加减运算符的总数，op_count[1]是乘除运算符的总数
	 * 若op_count.length = 4, 则依次表示加，减，乘，除 运算符的总数，op_count[0] [1] [2] [3] 一次是加减乘除运算符各自的数量
	 */
	public static Expression[] createExpressionArray(int expression_count,  int[] operators_count, int fraction_count, int min, int max, int unit) 
	{
		assert max > min && min >=0;
		assert unit>0;
		
		Random random = new Random(System.currentTimeMillis());
		
		Expression[] array = null;
		try {
			array = Expression.ofExpressionArray(operators_count, fraction_count,  
					//分数生成器
					()->{
						int d ;
						int m;
						do {
							do{
								d = random.nextInt(unit);
							}while( 0 == d);//分母不为0
							m = random.nextInt(unit*(max+1-min));
							m+=unit*min;
						}while(m/d > 0 && m %d == 0);//不要整数
						
						String result = "";
						try {
							result+= StrictFraction.parseString(new int[] {m,d}, true);
						} catch (StrictFractionFormatException e) {
							e.printStackTrace();
						}
						return result;
					},
					//整数生成器
					()->{
						int integer = random.nextInt(max+1-min);
						integer += min;
						String result = "";
						try {
							result += StrictFraction.parseString(new int[] {integer,1}, true);
						} catch (StrictFractionFormatException e) {
							e.printStackTrace();
						}
						return result;
					},
					//结果验证器
					str->{
						try {
							if(StrictFraction.compare(str, Integer.valueOf(min).toString()) != -1		//不小于最小值
							&& StrictFraction.compare(str, Integer.valueOf(max).toString()) != 1)//不大于最大值
							{
								int[] fa = StrictFraction.parseArray(str);
								if(0 == fa[1]		//分母不为0
									|| unit >= fa[1])//分母不大unit
								{
									return true;
								}
								else
									return false;
							}
						} catch (StrictFractionFormatException e) {
							e.printStackTrace();
							return false;
						}
						return false;
					}, expression_count);//end ofExpressionArray
			
		} catch (StrictFractionFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StrictFractionCalculateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return array;
	}
}
