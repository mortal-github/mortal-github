package mortal.text.similarity.repeatcounter;

import java.util.ArrayList;
import java.util.Arrays;

public class RepeatCounter {
	
	/**
	 * 
	 * @param str1 	一个句子(由词语数组成).
	 * @param str2		一个句子(由词语数组成).
	 * @param min		标记记词语为重复时，应该至少连续重复的词语的数量。
	 * @return words	返回所有标记为重复的词语.
	 */
	public static String[][] contrast(String[] str1, String[] str2,int min)
	{
		//参数验证
		//matrix | str2[0] | str2[1] | ...	mx_raw
		//str1[0]| 矩阵示意	 |	矩阵示意  |  ...	mx_raw1
		//str1[1]| 矩阵示意	 |  矩阵示意  |  ...	mx_raw2
		//  ...  |	 ....	 |	  ....  | .... mx_raw1
		//	mx_col
		int raw = str1.length;//列用行计数
		int col = str2.length;//行用列计数
		int i=0;	//行标
		int j=0; //列标
			
		//用于计算最大公共重复序列的矩阵，由首列，行1，行2数组动态构成。
		int[] mx_col = new int[raw];	//矩阵首列
		int[] mx_raw1 = new int[col];	//通过行交替形成矩阵体
		int[] mx_raw2 = new int[col]; //通过行交替形成矩阵体
		int[] raw_length = new int[col];//用元素索引记录重复序列的结尾，元素值记录重复序列的长度。
		
		//初始化首行，首列
		for(j=0; j<col; j++)
			mx_raw1[j] = mx_raw2[j] = raw_length[j] = str2[j].equals(str1[0]) ? 1 : 0;
		for(i=0; i<raw; i++)
			mx_col[i] = str1[i].equals(str2[0]) ? 1 : 0;
		//嵌套for循环，比较二维矩阵中每个点对应行列词语是否相等，不等为0，否则为左上角矩阵元素值+1
		for(i=1; i<raw; i++)//句内每行
		{
			for(j=1; j<col; j++)//交替行
				mx_raw1[j] = mx_raw2[j];
			
			for(j=1; j<col; j++)//行内每词
			{	
				if(str1[i].equals(str2[j]))
				{
					mx_raw2[j] = mx_raw1[j-1] + 1;//重复序列计数中
				}
				else
				{
					mx_raw2[j] = 0;
					if(mx_raw1[j-1] != 0)//重复序列断裂，故记录重复序列末尾信息
						raw_length[j-1] = mx_raw1[j-1];
				}
			}
		}
		for(j=1; j<col; j++)//处理最后一行的重复序列信息
		{
			if(mx_raw2[j] != 0 )
				raw_length[j] = mx_raw2[j];
		}
		
		
		//获取可能重叠的重复字符串
		ArrayList<String[]> words = new ArrayList<>();
		int count=0;
		for(j=0; j<col; j++)
		{
			if(raw_length[j] >= min)
			{
				count = count + 1;
				String[] strs = (String[])Arrays.copyOfRange(str2, j-raw_length[j]+1, j+1);
				words.add(strs);	
			}
		}
	
		return words.toArray(new String[count][]);
	}
}
