package mortal.text.similarity.repeatcounter;

import java.util.Objects;

public class RepeatWords
{
	//计算信息
	private String[]  words1;	//句子1，数组元素为词语，词语是一个字符串。
	private String[]  words2;	//句子2同上。
	private boolean[] bool1;	//句子1重复词语标记，true表示该词语重复。
	private boolean[] bool2;	//句子2同上。
	private int[]  position1;	//句子1连续重复的词语的信息，索引表示连续序列的尾段索引，元素值为连续序列长度。
	private int[]  position2;
	private int total1;	//在words1中与对方重复的字符数。注意是字符数，不是词语或句子！
	private int total2;	//在words2中与对方重复的字符数。注意是字符数，不是词语或句子！
	
	//控制信息
	private int min;	//连续重复的词语数量必须超过或等于该变量才记如重复。
	
	public RepeatWords(String[] words1, String[] words2, int min)
	{
		Objects.requireNonNull(words1);
		Objects.requireNonNull(words2);
		int length1 = words1.length;
		int length2 = words2.length;
		
		if(length1 < 1)
			throw new IllegalArgumentException("words1.length must not less than 1");
		if(length2 < 1)
			throw new IllegalArgumentException("words2.length must not less than 1");
		
		for(String word : words1)
			Objects.requireNonNull(word);
		for(String word : words2)
			Objects.requireNonNull(word);
		
		
		
		this.min = min;
		
		RepeatWords.this.words1 = words1;
		RepeatWords.this.words2 = words2;
		RepeatWords.this.bool1 = new boolean[length1];
		RepeatWords.this.bool2 = new boolean[length2];
		RepeatWords.this.position1 = new int[length1];
		RepeatWords.this.position2 = new int[length2];
		
		for(int i=0; i<length1; i++)
		{
			RepeatWords.this.bool1[i] = false;
			RepeatWords.this.position1[i] = 0;
		}
		for(int i=0; i<length2; i++)
		{
			RepeatWords.this.bool2[i] = false;
			RepeatWords.this.position2[i] = 0;
		}
			
	}
	
	
	private void calculatePosition()
	{
//		//matrix    | words2[0] | wors2[1] | ...	mx_raw
//		//wordst1[0]| 矩阵示意     |	矩阵示意  |  ...	mx_raw1
//		//wordst1[1]| 矩阵示意	    |  矩阵示意   |  ...	mx_raw2
//		//  ...     |	 ....	   |	  ....  | .... mx_raw1
//		//	mx_col
		
		int raw = this.words1.length;//列用行计数
		int col = this.words2.length;//行用列计数
		
		int[] mx_col  = new int[raw];		//矩阵列
		int[] mx_raw  = new int[col];		//矩阵行
		int[] mx_temp = new int[col];		//矩阵临时行，通过行交替形成矩阵
		
		//初始化首行、首列、及info其他信息
		for(int j=0; j<col; j++)
		{
			mx_raw[j] = mx_temp[j] = this.words2[j].equals(this.words1[0]) ? 1 : 0;
		}
		for(int i=0; i<raw; i++)
		{
			
			mx_col[i] =
					this.words1[i]
							.equals(this.words2[0]) ? 1 : 0;
			
		}
		
		//嵌套for循环，比较二维矩阵中每个点对应行列词语是否相等，不等为0，否则为左上角矩阵元素值+1
		for(int i=1; i<raw; i++)
		{
			for(int j=0; j<col; j++)
			{
				mx_raw[j] = mx_temp[j];
			}
			
			//计算重复序列位置信息
			mx_temp[0] = mx_col[i];
			for(int j=1; j<col; j++)
			{
				if(this.words1[i].equals(this.words2[j]))
				{
					mx_temp[j] = mx_raw[j-1] + 1;//重复序列计数中
				}
				else
				{	
					//assert this.words1[i].equals(this.words2[j]) == false;
					mx_temp[j] = 0;
					if(mx_raw[j-1] != 0)//重复序列断裂，故记录重复序列末尾信息
					{
						this.position2[j-1] = mx_raw[j-1];
						this.position1[i-1] = mx_raw[j-1];
					}
				}
				
			}//end for
		}//end for
		
		//处理最后一行的重复序列信息
		for(int j=0; j<col; j++)
		{
			if(mx_temp[j] != 0)
			{
				this.position2[j] = mx_temp[j];
				this.position1[raw-1] = mx_temp[j];
			}
		}//end for
	}

	private void calculateBool()
	{
		for(int i=0; i<this.position1.length; i++)
		{
			if(this.position1[i] >= this.min)
			{
				for(int k= i-this.position1[i]+1; k<i+1; k++)
				{
					this.bool1[k]=true;
				}
			}
		}
			
		for(int j=0; j<this.position2.length; j++)
		{
			if(this.position2[j] >= this.min)
			{
				for(int k= j-this.position2[j]+1; k<j+1; k++)
				{
					this.bool2[k]=true;
				}
				
			}
		}
	}

	private void calculateTotal()
	{
		int count1 = 0;
		int count2 = 0;
		for(int i=0; i<this.bool1.length; i++)
		{
			if(this.bool1[i] != false)
				count1 += this.words1[i].length();
		}
		
		for(int j=0; j<this.bool2.length; j++)
		{
			if(this.bool2[j] != false)
				count2 += this.words2[j].length();
		}
		
		this.total1 = count1;
		this.total2 = count2;
	}

	public RepeatWords calculate()
	{
		this.calculatePosition();
		this.calculateBool();
		this.calculateTotal();
		
		return this;
	}
	
	public int getTotal1()
	{
		return this.total1;
	}
	
	public int getTotal2()
	{
		return this.total2;
	}
	
}