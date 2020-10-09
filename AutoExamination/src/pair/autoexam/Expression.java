package pair.autoexam;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

import pair.autoexam.calculator.StrictFraction;
import pair.autoexam.throwable.StrictFractionCalculateException;
import pair.autoexam.throwable.StrictFractionFormatException;

public class Expression {
	private String[] inffix ;
	private String[] suffix;
	private String result ;
	
	private static final long maxRepeat = 1000;//失败重试次数。
	private static final long maxRepeatMember = 100;//制造后缀表达式一个成员失败重试次数。
	private static final Random random = new Random(System.currentTimeMillis());
	public static final String ADD = "+";
	public static final String SUBTRACT = "-";
	public static final String TIMES = "×";
	public static final String DIVIDE = "÷";
	
	/*
	 * 生成操作符序列
	 * 若op_count.length = 1 ,则表示加减乘除运算符总数
	 * 若op_count.length = 2,则表示加减 与乘除 运算符分别的总数
	 * 若op_count.length = 4, 则依次表示加，减，乘，除 运算符的总数
	 */
	private static ArrayList<String> generateOperators(int[] operators_count) {
	
		int random_add = 0;
		int random_subtract = 0;
		int random_times = 0;
		int random_divide = 0;
		
		int count_add = 0;
		int count_subtract = 0;
		int count_times = 0;
		int count_divide = 0;
		int count_total = 0;
		
		int length = operators_count.length;
		if(1 == length)
		{
			random_add = operators_count[0];
			random_subtract = random_add + operators_count[0];
			random_times = random_subtract + operators_count[0];
			random_divide += random_times + operators_count[0];

			count_add = operators_count[0];
			count_subtract = operators_count[0];
			count_times = operators_count[0];
			count_divide = operators_count[0];
			
			count_total = operators_count[0];
		}
		else if( 2 == length )
		{
			random_add = operators_count[0];
			random_subtract = random_add + operators_count[0];
			random_times = random_subtract + operators_count[1];
			random_divide += random_times + operators_count[1];
			
			count_add = operators_count[0];
			count_subtract = operators_count[0];
			count_times = operators_count[1];
			count_divide = operators_count[1];
			
			count_total += operators_count[0];
			count_total += operators_count[1];
		}
		else if( 4 == length )
		{
			random_add = operators_count[0];
			random_subtract = random_add + operators_count[1];
			random_times = random_subtract + operators_count[2];
			random_divide += random_times + operators_count[3];
			
			count_add = operators_count[0];
			count_subtract = operators_count[1];
			count_times = operators_count[2];
			count_divide = operators_count[3];
			
			count_total += operators_count[0];
			count_total += operators_count[1];
			count_total += operators_count[2];
			count_total += operators_count[3];
		}
		else
		{
			throw new IllegalArgumentException("数组长度不符合要求");
		}
		
		//生成操作符序列
		ArrayList<String> operators = new ArrayList<>();
		Random random = new Random(System.currentTimeMillis());
		@SuppressWarnings("unused")
		int retry = 0;//重试次数
		String op = "";//生成的操作符
		while(count_total > 0)
		{
			//随机生成一个操作符
			int select = random.nextInt(random_divide);
			assert select >= 0;
			if(select < random_add)
			{
				if(count_add > 0)
				{
					retry = 0;
					op = Expression.ADD;
				}
				else
				{
					retry++;
					op = "";
					continue;
				}	
			}
			else if(select < random_subtract)
			{
				if(count_subtract > 0)
				{
					retry = 0;
					op = Expression.SUBTRACT;
				}
				else
				{
					retry++;
					op = "";
					continue;
				}	
				
			}
			else if(select < random_times)
			{
				if(count_times > 0)
				{
					retry = 0;
					op = Expression.TIMES;
				}
				else
				{
					retry++;
					op = "";
					continue;
				}	
				
			}
			else
			{
				assert select < random_divide;
				if(count_divide > 0)
				{
					retry = 0;
					op = Expression.DIVIDE;
				}
				else
				{
					retry++;
					op = "";
					continue;
				}	
			}//end if else if 
			
			//添加操作符到操作符序列
			if(!op.equals(""))
			{
				operators.add(op);
				if(1 == length)
				{
					count_total--;
					count_add--;
					count_subtract--;
					count_times--;
					count_divide--;
					
				}
				else if(2 == length)
				{
					count_total--;
					if(op.equals(Expression.ADD) || op.equals(Expression.SUBTRACT))
					{
						count_add--;
						count_subtract--;
					}
					else if(op.equals(Expression.TIMES) || op.equals(Expression.DIVIDE))
					{
						count_times--;
						count_divide--;
					}
				}
				else if( 4 == length)
				{
					count_total--;
					switch(op) {
					case Expression.ADD : 
						count_add--;
						break;
					case Expression.SUBTRACT : 
						count_subtract--;
						break;
					case Expression.TIMES : 
						count_times--;
						break;
					case Expression.DIVIDE : 
						count_divide--;
						break;
					default:;
					}//end switch
				}//end if else if
			}//end if
		}//end while
		return operators;
	}
	
	//按randon_number指示的概率生成操作符，整数，分数
	private static String generateMember(ArrayList<String> operators, int op_begin, 
			Supplier<String> fraction, Supplier<String> integer,
			int[] random_number)
	{
		assert null!= random_number;
		assert 3 == random_number.length;
		assert random_number[0] >= 0;
		assert random_number[1] >= 0;
		assert random_number[2] >= 0;
		
		if(random_number[0] == 1) random_number[0]++;
		if(random_number[1] == 1) random_number[1]++;
		if(random_number[2] == 1) random_number[2]++;
		
		int random_op = random_number[0]; 
		int random_fraction = random_number[1] + random_op;
		int random_integer = random_number[2] + random_fraction;
		
		
		int select = Expression.random.nextInt(random_integer);
		if(select < random_op)
		{
			return operators.get(op_begin);
		}
		else if(select < random_fraction)
		{
			String str = fraction.get();
			while(str.equals(""))
				str = fraction.get();
			return str;
		}
		else 
		{
			assert select < random_integer;
			
			String str = integer.get();
			while(str.equals(""))
				str = integer.get();
			return str;
		}
	}
	
	/*
	 * 生成后缀表达式
	 * op_count 运算符的总数，显然数值总数为op_count+1
	 * fraction_count 分数数值的总数，fraction_count <= op_count
	 * fraction 分数生成器
	 * integer 整数生成器
	 * test_middle 每生成一个运算符，就检验该运算符的运算结果
	 * 若op_count.length = 1 ,则表示加减乘除运算符总数
	 * 若op_count.length = 2,则表示加减 与乘除 运算符分别的总数
	 * 若op_count.length = 4, 则依次表示加，减，乘，除 运算符的总数
	 */
	private static String[] ofSuffix(int[] ops_count, int count_fraction,
			Supplier<String> fraction, Supplier<String> integer, 
			Predicate<String> op_enable) throws StrictFractionFormatException
	{
		//生成操作符序列，同时也检验了参数ops_count
		ArrayList<String> operators = generateOperators(ops_count);
				
		assert null != ops_count;
		assert 1 == ops_count.length || 2 == ops_count.length || 4 == ops_count.length;
		assert 0 <= count_fraction && count_fraction <= operators.size() + 1;
	
		int count_integer = operators.size() + 1 - count_fraction;
	
		long retry = 0;//记录失败重试次数
		
		int op_index = 0;
		Stack<String> calculate = new Stack<>(String.class);//运算中的后缀表达式，所有运算符都已经运算数字，只剩下结果在栈中
		ArrayList<String> suffix = new ArrayList<>();//后缀表达式
		while(op_index < operators.size()) {
			if(retry > Expression.maxRepeatMember)
				return null;
			
			String member = generateMember(operators, op_index,fraction, integer,new int[] {operators.size() - op_index, count_fraction, count_integer});
			
			if(member == Expression.ADD || member == Expression.SUBTRACT || member == Expression.TIMES || member == Expression.DIVIDE)
			{
				if(calculate.size() < 2)//没有足够的运算数
				{
					retry++;
					continue;
				}
				else//检验运算，
				{
					String right = calculate.pop();
					String left = calculate.pop();
					
					try {
						String result  = null;
						boolean success = false;
						
						switch(member) {
						case Expression.ADD:
						{
							result = StrictFraction.add(left, right, false);
							success = op_enable.test(result);
							break;
						}
						case Expression.SUBTRACT:
						{
							int com = StrictFraction.compare(left, right);
							if(com != -1)
							{
								assert 0 == com || 1 == com;
								result = StrictFraction.subtract(left, right, false);
								success = op_enable.test(result);
							}
							else
								success = false;
							
							break;
						}
						case Expression.TIMES:
						{
							result = StrictFraction.times(left, right, false);
							success = op_enable.test(result);
							break;
						}
						case Expression.DIVIDE:
						{
							int com = StrictFraction.compare(right, "0");
							if(0 == com)//除数不能等于0
								success = false;
							else
							{
								result = StrictFraction.divide(left, right, false);
								success = op_enable.test(result);
							}
							break;
						}
						default:;
						}//end switch
						
						//处理验证结果
						if(success)
						{
							suffix.add(member);//贴到后缀表达式上
							calculate.push(result);
							op_index++;
							retry = 0;//重置失败重试次数
						}
						else
						{	//清除不能满足要求的运算数
							String str1 = suffix.get(suffix.size()-1);
							String str2 = suffix.get(suffix.size()-2);
							if(str1 == Expression.ADD || str1 == Expression.SUBTRACT || str1 == Expression.TIMES || str1 == Expression.DIVIDE)
							{//如果是操作符，则说明栈顶元素是表达式求值，必须重新入栈
								calculate.push(left);
								calculate.push(right);
							}
							else if(str2 == Expression.ADD || str2 == Expression.SUBTRACT || str2 == Expression.TIMES || str2 == Expression.DIVIDE)
							{//left 是表达式求值，必须入栈，right则抛弃
								calculate.push(left);
								suffix.remove(suffix.size()-1);
								
								//抛弃一个数字就增加一个数字名额
								plus_count:
								{
									for(int i = 0 ; i < right.length(); i++)
										if('/' ==  right.charAt(i))
										{
											count_fraction++;
											break plus_count;
										}
									count_integer++;
								}
								
							}
							else
							{
								suffix.remove(suffix.size()-1);//除去left
								suffix.remove(suffix.size()-1);//除去right
								
								//抛弃一个数字就增加一个数字名额
								plus_count:
								{
									for(int i = 0 ; i < right.length(); i++)
										if('/' ==  right.charAt(i))
										{
											count_fraction++;
											break plus_count;
										}
									count_integer++;
								}
								//抛弃一个数字就增加一个数字名额
								plus_count:
								{
									for(int i = 0 ; i < left.length(); i++)
										if('/' ==  left.charAt(i))
										{
											count_fraction++;
											break plus_count;
										}
									count_integer++;
								}
							}
							retry++;
						}
					} catch (StrictFractionCalculateException e) {
						//清除不能满足要求的运算数
						String str1 = suffix.get(suffix.size()-1);
						String str2 = suffix.get(suffix.size()-2);
						if(str1 == Expression.ADD || str1 == Expression.SUBTRACT || str1 == Expression.TIMES || str1 == Expression.DIVIDE)
						{//如果是操作符，则说明栈顶元素是表达式求值，必须重新入栈
							calculate.push(left);
							calculate.push(right);
						}
						else if(str2 == Expression.ADD || str2 == Expression.SUBTRACT || str2 == Expression.TIMES || str2 == Expression.DIVIDE)
						{//left 是表达式求值，必须入栈，right则抛弃
							calculate.push(left);
							suffix.remove(suffix.size()-1);
							
							//抛弃一个数字就增加一个数字名额
							plus_count:
							{
								for(int i = 0 ; i < right.length(); i++)
									if('/' ==  right.charAt(i))
									{
										count_fraction++;
										break plus_count;
									}
								count_integer++;
							}
						}
						else
						{
							suffix.remove(suffix.size()-1);//除去left
							suffix.remove(suffix.size()-1);//除去right
							
							//抛弃一个数字就增加一个数字名额
							plus_count:
							{
								for(int i = 0 ; i < right.length(); i++)
									if('/' ==  right.charAt(i))
									{
										count_fraction++;
										break plus_count;
									}
								count_integer++;
							}
							//抛弃一个数字就增加一个数字名额
							plus_count:
							{
								for(int i = 0 ; i < left.length(); i++)
									if('/' ==  left.charAt(i))
									{
										count_fraction++;
										break plus_count;
									}
								count_integer++;
							}
						}
						retry++;
					}//end catch
				}//end else
			}//end if
			else
			{//遇到操作数
				
				boolean isfraction = false;
				//还有数字可以操作
				if(count_fraction + count_integer > 0)
				{
					for(int i = 0; i < member.length(); i++)
						if('/' == member.charAt(i))
							isfraction = true;
					if(isfraction && count_fraction > 0)
					{
						suffix.add(member);
						calculate.push(member);
						count_fraction--;
						retry = 0;//重置失败重试次数
					}
					else if(!isfraction && count_integer > 0)
					{
						suffix.add(member);
						calculate.push(member);
						count_integer--;
						retry = 0;//重置失败重试次数
					}
					else
					{
						retry++;
						continue;
					}
				}
				else {
					retry++;
				}
				
			}//end else
			
		}//end while
		return suffix.toArray(new String[suffix.size()]);
	}

	private static String[] toInfix(String[] suffix)
	{
		Stack<String> stack = new Stack<>(String.class);
		for(String str : suffix)
		{
			if(str.equals(Expression.ADD) || str.equals(Expression.SUBTRACT) || str.equals(Expression.TIMES) || str.equals(Expression.DIVIDE))
			{
				String right = stack.pop();
				String left = stack.pop();
				String next = "("+left + str + right+")";
				stack.push(next);
			}
			else
			{//数字则入栈
				stack.push(str);
			}
		}
		String str = stack.pop();
		
		for(int i = 0; i < str.length(); i++)
		{
			char ch = str.charAt(i);
			if('0' <= ch && ch <= '9' || '\'' == ch || '/' == ch)
			{
				int begin = i;
				do {
					i++;
					ch = str.charAt(i);
				}while('0' <= ch && ch <= '9' || '\'' == ch || '/' == ch);
				stack.push(str.substring(begin, i));
				i--;
			}
			else 
			{
				assert ch == '(' || ch == ')' || ch == Expression.ADD.charAt(0)
						|| ch == Expression.SUBTRACT.charAt(0)
								|| ch == Expression.TIMES.charAt(0)
										|| ch == Expression.DIVIDE.charAt(0) : ch;
				
				stack.push(new String(new  char[] {ch}));
			}
		}
		
		return stack.toArray();
	}

	@SuppressWarnings("unused")
	private static String[] toSuffix(String[] infix)
	{
		Stack<String> stack = new Stack<>(String.class);
		ArrayList<String> suffix = new ArrayList<>();
		for(String str : infix)
		{
			switch(str) {
			case "(":
			{
				stack.push(str);
				break;
			}
			case ")":
			{
				String push = null;
				do {
					push = stack.pop();
					suffix.add(push);
				}while(!push.equals("("));
				suffix.remove(suffix.size());
				break;
			}
			case Expression.ADD:
			case Expression.SUBTRACT:
			{
				String push = stack.top();
				while(str.equals(Expression.ADD) || str.equals(Expression.SUBTRACT) || str.equals(Expression.TIMES) || str.equals(Expression.DIVIDE))
				{//遇到同级别或更高级别操作符就弹栈
					push = stack.pop();
					suffix.add(push);
					push = stack.top();
				}
				//操作符入栈
				stack.push(str);
				break;
			}
			case Expression.TIMES:
			case Expression.DIVIDE:
			{
				String push = stack.top();
				while(str.equals(Expression.TIMES) || str.equals(Expression.DIVIDE))
				{//遇到同级别或更高级别操作符就弹栈
					push = stack.pop();
					suffix.add(push);
					push = stack.top();
				}
				//操作符入栈
				stack.push(str);
				break;
			}
			default://遇到数字
				suffix.add(str);
			}
		}//end for
		return suffix.toArray(new String[suffix.size()]);
	}

	private static String calculate(String[] suffix) throws StrictFractionFormatException, StrictFractionCalculateException
	{
		Stack<String> stack = new Stack<>(String.class);
		for(String str : suffix)
		{
			if(str.equals(Expression.ADD) || str.equals(Expression.SUBTRACT) || str.equals(Expression.TIMES) || str.equals(Expression.DIVIDE))
			{//遇到操作符就计算
				
				String right = stack.pop();
				String left = stack.pop();
				String result = null;
				
				switch(str) {
				case Expression.ADD:
					result = StrictFraction.add(left, right, false);
					break;
				case Expression.SUBTRACT:
					result = StrictFraction.subtract(left, right, false);
					break;
				case Expression.TIMES:
					result = StrictFraction.times(left, right, false);
					break;
				case Expression.DIVIDE:
					result = StrictFraction.divide(left, right, false);
					break;
				default:;
				}
				stack.push(result);
			}
			else
			{//遇到操作数就入栈
				stack.push(str);
			}
		}
		assert 1 == stack.size();
		return stack.top();
	}
	
	private Expression(String[] infix, String[] suffix, String result)
	{
		this.inffix = infix;
		this.suffix = suffix;
		this.result = result;
	}
			
	public static Expression ofExpression(int[] ops_count, int count_fraction,
			Supplier<String> fraction, Supplier<String> integer, Predicate<String> op_enable) throws StrictFractionFormatException, StrictFractionCalculateException
	{
		String[] suffix = Expression.ofSuffix(ops_count, count_fraction, fraction, integer, op_enable);
		if(null == suffix)
			return null;
		String[] inffix = Expression.toInfix(suffix);
		String  result = Expression.calculate(suffix);
		result = StrictFraction.parseString(StrictFraction.parseArray(result),true);
		Expression expression = new Expression(inffix, suffix, result);
		
		return  expression;
	}
	
	public static Expression[] ofExpressionArray (int[] ops_count, int count_fraction,
			Supplier<String> fraction, Supplier<String> integer, Predicate<String> op_enable,
			int count) throws StrictFractionFormatException, StrictFractionCalculateException
	{
		ArrayList<Expression> array = new ArrayList<>();
		Expression expression = null;
		long retry = 0;

		outer:
		for(int i = 0; i < count; )
		{
			if(retry > Expression.maxRepeat)
				break;
			expression = Expression.ofExpression(ops_count,count_fraction,fraction, integer, op_enable);
			if(null == expression)
			{
				retry++;
				continue;
			}
			else
			{
				for(int j = 0; j < array.size(); j++)
				{
					Expression other = array.get(j);
					if(expression.equals(other))
					{
						continue outer;
					}
				}
				array.add(expression);
				i++;
				retry=0;
			}
			
		}
		return array.toArray(new Expression[array.size()]);
	}
	
	public String getInffix()
	{
		String str = "";
		for(String s : this.inffix)
		{
			str += s;
		}
		return str;
	}
	public String getResult()
	{
		String str = this.result;
		return str;
	}
	public String getSuffix()
	{
		String str = "";
		for(String s : this.suffix)
		{
			str += s;
		}
		return str;
	}
	
	public boolean equals(Object obj)
	{
		if(null == obj)
			return false;
		if(this == obj)
			return true;
		if(obj.getClass() != this.getClass())
			return false;
		
		//if(super.equals(obj) == false)
		//	return false;
		Expression other = (Expression)obj;
		try {
			if(0 != StrictFraction.compare(this.result, other.result))
				return false;
		} catch (StrictFractionFormatException e) {
			return true;
			//e.printStackTrace();
		}
		//其他先不做比较
		return true;	
	}
}
