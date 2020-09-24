package mortal.text.similarity.repeatcounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RepeatRateTest {

	@ParameterizedTest
	@MethodSource
	void testRepeatRate(String[] sentence1, String[] sentence2, int range, int min) {
		
		RepeatRate repeat_rate = new RepeatRate(sentence1, sentence2);
		repeat_rate.calculateRepeat1(range, min);
		double repeat2 = repeat_rate.getRepeatRate2();
		double repeat1 = repeat_rate.getRepeatRate1();
		
		System.out.println("repeat1 : " + repeat1 + "\nrepeat2 : " + repeat2 + "\n");
		
	}
	static List<Arguments> testRepeatRate() {
		ArrayList<Arguments> list = new ArrayList<>();
		
		Path text = Paths.get(System.getProperty("user.dir"), "resource", "test");
				
		double percentage = 0.999;
		int min = 2;
		try {
			String[] orig = new String(Files.readAllBytes(text.resolve("orig.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_add = new String(Files.readAllBytes(text.resolve("orig_0.8_add.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_del = new String(Files.readAllBytes(text.resolve("orig_0.8_del.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_dis_1 = new String(Files.readAllBytes(text.resolve("orig_0.8_dis_1.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_dis_10 = new String(Files.readAllBytes(text.resolve("orig_0.8_dis_10.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_dis_15 = new String(Files.readAllBytes(text.resolve("orig_0.8_dis_15.txt"))).split("(?=(?<=。))");
			
			list.add(Arguments.arguments(orig, orig_0_8_add, 		(int)(percentage * orig_0_8_add.length), 		min));
			list.add(Arguments.arguments(orig, orig_0_8_del, 		(int)(percentage * orig_0_8_del.length), 		min));
			list.add(Arguments.arguments(orig, orig_0_8_dis_1, 	(int)(percentage * orig_0_8_dis_1.length-1), 	min));
			list.add(Arguments.arguments(orig, orig_0_8_dis_10,	(int)(percentage * orig_0_8_dis_10.length-1), min));
			list.add(Arguments.arguments(orig, orig_0_8_dis_15, 	(int)(percentage * orig_0_8_dis_15.length-1), min));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}


}
