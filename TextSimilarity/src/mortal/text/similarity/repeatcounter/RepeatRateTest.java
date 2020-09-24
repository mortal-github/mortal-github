package mortal.text.similarity.repeatcounter;

import static org.junit.jupiter.api.Assertions.*;

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
		repeat_rate.calculate(range, min);
		double repeat = repeat_rate.getRepeatRate2();
		
		System.out.println(repeat);
		
	
		
	}
	static List<Arguments> testRepeatRate() {
		ArrayList<Arguments> list = new ArrayList<>();
		
		Path text = Paths.get(System.getProperty("user.dir"), "resource", "test");
				
		int range = 5;
		int min = 2;
		try {
			String[] orig = new String(Files.readAllBytes(text.resolve("orig.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_add = new String(Files.readAllBytes(text.resolve("orig_0.8_add.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_del = new String(Files.readAllBytes(text.resolve("orig_0.8_del.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_dis_1 = new String(Files.readAllBytes(text.resolve("orig_0.8_dis_1.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_dis_10 = new String(Files.readAllBytes(text.resolve("orig_0.8_dis_10.txt"))).split("(?=(?<=。))");
			String[] orig_0_8_dis_15 = new String(Files.readAllBytes(text.resolve("orig_0.8_dis_15.txt"))).split("(?=(?<=。))");
			
			list.add(Arguments.arguments(orig, orig_0_8_add, range, min));
			list.add(Arguments.arguments(orig, orig_0_8_del, range, min));
			list.add(Arguments.arguments(orig, orig_0_8_dis_1, range, min));
			list.add(Arguments.arguments(orig, orig_0_8_dis_10, range, min));
			list.add(Arguments.arguments(orig, orig_0_8_dis_15, range, min));
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		
		return list;
	}


}
