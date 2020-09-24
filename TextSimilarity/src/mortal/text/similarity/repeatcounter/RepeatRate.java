package mortal.text.similarity.repeatcounter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class RepeatRate {

	private String[][] wordsArray1;	//词组数组1，每个词组都是由一个句子分词得来的字符串数组
	private String[][] wordsArray2;	//词组数组2
	private int total_count1 = 0;		//词组数组1总字符数
	private int total_count2 = 0;		//词组数组2总字符数
	private int total_repeat1 = 0;	//词组数组1中被标记为重复的词语的总字符数
	private int total_repeat2 = 0;	//词组数组2中被标记为重复的词语的总字符数
	
	private double repeat_rate1 = 0;
	private double repeat_rate2 = 0;
	
	/**
	 * 
	 * @param sentences 句子数组，将句子数组中的每一个句子分词成一个词组(即词语数组)。
	 * @param counter 接受一个参数，在分词过程中对每一个词语调用该方法。主要用来计数字符总数
	 * @return String[][] 词组数组，句子数组分词的结果，即词语数组的数组
	 */
	private static String[][] segment(String[] sentences, Consumer<String> counter)
	{
		int length = sentences.length;
		String[][] wordsArray = new String[length][];
		
		for(int i=0; i<length; i++)
		{
			List<String> list = new ArrayList<>();
			StringReader reader = new StringReader(sentences[i]);
			IKSegmenter ik = new IKSegmenter(reader,true);
			Lexeme lex = null;
			
			try {
				while((lex = ik.next()) != null)
				{
					String word = lex.getLexemeText();
					
					if(word != null && word.length() > 0)//避免分出空串
					{
						counter.accept(word);
						list.add(word);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			wordsArray[i] = list.toArray(new String[list.size()]);
		}//end for
		
		return wordsArray;
	}
	
	public RepeatRate(String[] sentences1, String[] sentences2)
	{
		Objects.requireNonNull(sentences1);
		Objects.requireNonNull(sentences2);
		for(int i=0; i<sentences1.length; i++)
			if(sentences1[i] == null)
				throw new NullPointerException("sentences1[" + i + "] must not null!" );
		for(int i=0; i<sentences2.length; i++)
			if(sentences2[i] == null)
				throw new  NullPointerException("sentences2[" + i + "] must not null!" );
	

		this.wordsArray1 = RepeatRate.segment(sentences1, word->{this.total_count1 += word.length();});
		this.wordsArray2 = RepeatRate.segment(sentences2, word->{this.total_count2 += word.length();});
	}
		
	public void calculate(int range, int min)
	{
		int length1 = this.wordsArray1.length;
		int length2 = this.wordsArray2.length;
		int i=0;
		int j=0;
		
		//计算前先清零
		this.total_repeat1=0;
		this.total_repeat2=0;
		
		for(i=0; i<length1; i++)
		{
			int max_repeat1=0;
			int max_repeat2=0;
			
			int end =  i+range < length2 ? i+range : length2;
			for(j = i-range > 0 ? j-range : 0 ; j<end; j++)
			{
				RepeatWords words = new RepeatWords(this.wordsArray1[i], this.wordsArray2[j], min);
				words.calculate();
				int repeat1 = words.getTotal1();
				int repeat2 = words.getTotal2();
				
				max_repeat1 = repeat1 > max_repeat1 ? repeat1 : max_repeat1;
				max_repeat2 = repeat2 > max_repeat2 ? repeat2 : max_repeat2;
			}
			this.total_repeat1 += max_repeat1;
			this.total_repeat2 += max_repeat2;
		}
		
		//计算相似率
		this.repeat_rate1 = ((double)this.total_repeat1) / ((double)this.total_count1); 
		this.repeat_rate2 = ((double)this.total_repeat2) / ((double)this.total_count2); 
	}
	
	public String[][] getWordArray1()
	{
		//将来修改为返回深度克隆的副本，现在暂时不理会。
		return this.wordsArray1;
	}
	public String[][] getWordArray2()
	{
		//将来修改为返回深度克隆的副本，现在暂时不理会。
		return this.wordsArray2;
	}

	public double getRepeatRate1()
	{
		return this.repeat_rate1;
	}
	public double getRepeatRate2()
	{
		return this.repeat_rate2;
	}
	
	
}
