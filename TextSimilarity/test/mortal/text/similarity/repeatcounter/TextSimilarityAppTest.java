package mortal.text.similarity.repeatcounter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import mortal.text.similarity.TextSimilarityApp;

class TextSimilarityAppTest {

	@ParameterizedTest
	@MethodSource
	void testMain(String origin, String checked, String result) {
	
		TextSimilarityApp.main(new String[]{origin, checked, result});
	}
	static List<Arguments> testMain() {
		ArrayList<Arguments> list = new ArrayList<>();
		
		Path text = Paths.get(System.getProperty("user.dir"),"resource","test");
		Path result = Paths.get(System.getProperty("user.dir"), "resource", "result");
		Path orig = text.resolve("orig.txt");
		
		String checked1 = "orig_0.8_add.txt";
		String checked2 = "orig_0.8_del.txt";
		String checked3 = "orig_0.8_dis_1.txt";
		String checked4 = "orig_0.8_dis_10.txt";
		String checked5 = "orig_0.8_dis_15.txt";
		
		list.add(Arguments.arguments(orig.toString(), text.resolve(checked1).toString(), result.resolve(checked1).toString()));
		list.add(Arguments.arguments(orig.toString(), text.resolve(checked2).toString(), result.resolve(checked2).toString()));
		list.add(Arguments.arguments(orig.toString(), text.resolve(checked3).toString(), result.resolve(checked3).toString()));
		list.add(Arguments.arguments(orig.toString(), text.resolve(checked4).toString(), result.resolve(checked4).toString()));
		list.add(Arguments.arguments(orig.toString(), text.resolve(checked5).toString(), result.resolve(checked5).toString()));
		
		return list;
	}

}
