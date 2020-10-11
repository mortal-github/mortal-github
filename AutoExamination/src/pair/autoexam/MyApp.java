package pair.autoexam;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import pair.autoexam.calculator.StrictFraction;
import pair.autoexam.throwable.StrictFractionCalculateException;
import pair.autoexam.throwable.StrictFractionFormatException;

public class MyApp {

	public static void main(String[] args)
	{
		int n = 10;

		int r = 10;
		boolean bool_n = false;
		boolean bool_r = false;
		for(int i = 0 ; i < args.length; i++)
		{
			String arg = args[i];
			switch(arg){
			case "-n" :
				if(bool_n)
				{
					System.out.println("-n 重复，请重新输入参数。");
					return ;
				}
				else
				{
					i++;
					arg = args[i];
					try {
						n = Integer.valueOf(arg);
						bool_n = true;
					}catch(NumberFormatException e)
					{
						System.out.println("-n 选项的参数：" + arg + " 格式不正确。");
					}
				}
				break;
			case "-r" :
				if(bool_r)
				{
					System.out.println("-r 重复，请重新输入参数。");
					return ;
				}
				else
				{
					i++;
					arg = args[i];
					try {
						r = Integer.valueOf(arg);
						bool_r = true;
					}catch(NumberFormatException e)
					{
						System.out.println("-r 选项的参数：" + arg + " 格式不正确。");
					}
				}
				break;
			default :
				System.out.println("暂时不支持的参数。");
			}
		}
		
		if(false == bool_n)
			System.out.println("你没有提供-n选项，默认为10");
		if(false == bool_r)
			System.out.println("你没有提供-r选项，默认为10");
		createAndOutputExpressionToFile(n,r);
	}
	
	public static void createAndOutputExpressionToFile(int n, int r)
	{
//		//以下配置才能在22秒内生成10000到算术题
//		int expression_count = 10000;
//		int[] operators_count = new int[] {3};
//		int fraction_count = 0;
//		int min = 0;
//		int max = 100;
//		int unit = 100;
		
//以下配置在2秒内生成1000道算术题
//		int expression_count = 1000;
//		int[] operators_count = new int[] {3};
//		int fraction_count = 0;
//		int min = 0;
//		int max = 10;
//		int unit = 100;
//以下配置只能生成1000多道题(运行2~3秒)
//int expression_count = 10000;
//int[] operators_count = new int[] {3};
//int fraction_count = 0;
//int min = 0;
//int max = 10;
		int expression_count = n;
		int[] operators_count = new int[] {3};
		int fraction_count = 0;
		int min = 0;
		int max = r;
		int unit = 100;
				
		String dir = System.getProperty("user.dir");
		Path exercises = Paths.get(dir, "Exercises.txt");
		Path answers = Paths.get(dir, "Answers.txt");
		
		

		try {
			Files.deleteIfExists(exercises);
			System.out.println("INFO:	成功删除旧文件：" + exercises);
			Files.deleteIfExists(answers);
			System.out.println("INFO:	成功删除旧文件：" + answers);
			Files.createFile(exercises);
			System.out.println("INFO:	成功创建新文件：" + exercises);
			Files.createFile(answers);
			System.out.println("INFO:	成功创建新文件：" + answers);
			
		} catch (IOException e) {
			System.out.println( "ERROR: 文件删除和创建出现错误。\n");
			//e.printStackTrace();
		}
		
		try (PrintWriter exe = new PrintWriter(exercises.toString());
			PrintWriter ans = new PrintWriter(answers.toString());){
			System.out.println("INFO:	成功打开文件输出流。");
			
			System.out.println("INFO:	正在生成表达式。");
			long millis = System.currentTimeMillis();
			Expression[] expression = createExpressionArray(expression_count, operators_count,fraction_count,min, max,unit) ;
			millis = System.currentTimeMillis() - millis;
			System.out.println("INFO:	已经生成表达式，用时 " + millis + "毫秒。");
			
			System.out.println("INFO: 	正在将表达式输出到文件。" );
			int i = 1;
			for(Expression exp : expression)
			{
				String e = exp.getInffix();
				String a = exp.getResult();
				
				exe.println(i + ". " + e + " = ");
				ans.println(i + ". " + a);
				i++;
			}
			System.out.println("INFO: 	已经将表达式输出到文件。" );
			System.out.println("INFO: 	实际生成" + (i-1) + "个表达式，并计算答案。" );
			if(i-1 < n)
			{
				System.out.println("提示: 	如果需要更多数学表达式，请增加-r参数。");
				System.out.println("提示:	否则很难生成足够-n参数的数学表达式。");
				System.out.println("提示: 	如果需要10000个数学表达式，请使-r参数大于或等于100。");
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR:	无法打开文件输出流。");
			//e.printStackTrace();
		}
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
