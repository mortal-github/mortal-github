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

	private ArrayList<String[]> wordsArray1 = new ArrayList<>();	//词组数组1，每个词组都是由一个句子分词得来的字符串数组
	private ArrayList<String[]> wordsArray2 = new ArrayList<>();	//词组数组2
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
	 * @parm wordsArray 分词结果储存在{@code wordsArray}
	 */
	private static void segment(String[] sentences , ArrayList<String[]> wordsArray , Consumer<String> counter)
	{
		int length = sentences.length;
		
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
					counter.accept(word);
					list.add(word);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(list.size() > 0)	//除去空句
				wordsArray.add(list.toArray(new String[list.size()]));
		}//end for
	}
	
	public RepeatRate(String[] sentences1, String[] sentences2)
	{
		Objects.requireNonNull(sentences1);
		Objects.requireNonNull(sentences2);
		
		for(int i=0; i<sentences1.length; i++)
		{
			if(sentences1[i] == null)
				throw new NullPointerException("sentences1[" + i + "] must not null!" );
			
			if(sentences1[i].length() < 1)
				throw new IllegalArgumentException("sentences1[" + i + "].length() = " + sentences1[i].length() + ". but must not less than 1.");
		}
			
		for(int i=0; i<sentences2.length; i++)
		{
			if(sentences2[i] == null)
				throw new NullPointerException("sentences2[" + i + "] must not null!" );
			if(sentences2[i].length() < 1)
				throw new IllegalArgumentException("sentences2[" + i + "].length() = " + sentences2[i].length() + ". but must not less than 1.");
		}
		
		RepeatRate.segment(sentences1, this.wordsArray1, word->{this.total_count1 += word.length();});
		RepeatRate.segment(sentences2, this.wordsArray2, word->{this.total_count2 += word.length();});
	}
		
	public void calculate(int range, int min)
	{
		int length1 = this.wordsArray1.size();
		int length2 = this.wordsArray2.size();
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
				RepeatWords words = new RepeatWords(this.wordsArray1.get(i), this.wordsArray2.get(j), min);
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
	
	public double getRepeatRate1()
	{
		return this.repeat_rate1;
	}
	public double getRepeatRate2()
	{
		return this.repeat_rate2;
	}
	
	
}