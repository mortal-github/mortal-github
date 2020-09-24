package mortal.text.similarity;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import mortal.text.similarity.repeatcounter.RepeatRate;

public class TextSimilarityApp {
	
	public static void main(String[] args)
	{
		long millis = System.currentTimeMillis();
		if(args.length != 3)
		{
			throw new IllegalArgumentException("请输入 3 个合法的文件文件路径作为参数");
		}
		Path origin_path = Paths.get(args[0]);
		Path checked_path = Paths.get(args[1]);
		Path result_path = Paths.get(args[2]);
		
		try {
			String origin = new String(Files.readAllBytes(origin_path));
			String checked = new String(Files.readAllBytes(checked_path));
			
			String[] origin_sentence = TextSimilarityApp.getSentences(origin);
			String[] checked_sentence = TextSimilarityApp.getSentences(checked);
			
			double percentage = 0.99;
			int min = 2;
			
			
			RepeatRate repeat = new RepeatRate(origin_sentence,checked_sentence);
			repeat.calculateRepeat2((int)(percentage * checked_sentence.length ), min);
			
			
			double similar = repeat.getRepeatRate2();
			similar =((double)((int)( Math.round(((double) ((int)(similar*1000)) ))/10)))/100;//四如五入2位精度
			
		 	String answer = "Text similarity is ".toUpperCase() + similar + "\n"
					+ "origin  file is " + origin_path.toString() + "\n"
					+ "checked file is " + checked_path.toString() + "\n"
					+ "\nmore details : ".toUpperCase() + "\n"
					+ "origin  file has " + repeat.getTotalCount1() + " character.\n"
					+ "checked file has " + repeat.getTotalCount2() + " character.\n"
					+ "origin  file has " + repeat.getTotalRepeat1() + " character which Repeat in checked file!\n"
					+ "checked file has " + repeat.getTotalRepeat2() + " character which Repeat in origin  file!\n\n" ;
					
			//建立输出文件
			boolean deleted = Files.deleteIfExists(result_path);
			if(deleted)System.out.println("Delete exited file : " + result_path.toString());
			
			Files.createFile(result_path);
			System.out.println("Create   new  file : " + result_path.toString());
			
			//输出答案
			PrintWriter writer = new PrintWriter(result_path.toString());
			writer.println(answer);
			writer.close();
			System.out.println("Answer had writed in file :" + result_path.toString() +  "\n");
			
			System.out.println("文本相似度算法用时 ： " + repeat.getCalculateTimeMillis2() + " millis.");
			System.out.println("IKAnalyzer分词工具分词用时 ： " + repeat.getSegmentTimeMillis() + " millis.");
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		millis = System.currentTimeMillis() - millis;
		System.out.println("程序总用时 ： " + millis + " millis");
		
	}
	
	public static String[] getSentences(String article)
	{
		String[] sentences = article.split("(?=(?<=。))");
		return sentences;
	}

}
