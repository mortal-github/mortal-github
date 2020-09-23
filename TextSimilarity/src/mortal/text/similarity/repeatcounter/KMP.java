package mortal.text.similarity.repeatcounter;

public class KMP {
	
	public static int getFirstIndex(char[] pattern, char[] text)
	{
		int i=0;//主串的位置
		int j=0;//模式串的位置
		int[] next = KMP.getBackIndex(pattern);
		
		while(i < text.length && j < pattern.length)
		{
			if(j == -1 || text[i] == pattern[j])
			{//当j=-1,要移动i，j回0.
				i++;
				j++;
			}
			else
				j = next[j]; //回到指定位置。
		}
		
		if(j == pattern.length)
			return i-j;
		else 
			return -1;
		
	}
	
	private static int[] getBackIndex(char[] pattern)
	{
		int[] next = new int[pattern.length];
		next[0] = -1;
		int k = -1;
		
		int j=0;
		while(j < pattern.length-1)
		{

			if(k == -1 || pattern[k] == pattern[j])
				next[++j] = ++k;//求chs{circle+1]的前缀
			else
				k = next[k];
			//前缀的前缀必然等于后缀的后缀
			//故，若前缀的前缀的后继 与 chs[circle]相等，则可以确定chs[circle]的前缀，即下一次循环的if条件
			//若，一直不满足条件，使得index最后变为-1,则应该重新开始，故+1.
				
		}
		
		for(j=0; j < pattern.length; j++)
		{
			k=next[j];
			while(k > -1 && pattern[k] == pattern[j])
			{
				next[j] = next[k];
				k=next[j];
			}
			//在实际上的子串匹配中，
			//若chs[k] == chs[j],那么即使移动子串指针j到k的位置，依然不能与原串i指针所指字符匹配
			//故，在此优化算法。
		}
		
		return next;
	}

}
