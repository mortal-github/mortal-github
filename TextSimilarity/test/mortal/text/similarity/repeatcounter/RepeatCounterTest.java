package mortal.text.similarity.repeatcounter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RepeatCounterTest {

	
	@ParameterizedTest
	@MethodSource
	void testContrastFile(String[] str1, String[] str2, int min, PrintWriter output)
	{
		String[][] res = RepeatCounter.contrast(str1,str2,min);
		for(int i=0; i<res.length; i++)
				output.println(Arrays.toString(res[i]) + "\n");
	}
	static List<Arguments> testContrastFile()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		String resource = Paths.get(System.getProperty("user.dir"),"resource").toString();

		try {
			Path orig_paths = Paths.get(resource,"test","orig.txt");
			String orig_article = new String(Files.readAllBytes(orig_paths));
			String[] orig_paragraph = orig_article.split("\\s\\n");
			for(String str : orig_paragraph)
				System.out.println(str + "\n----------------------------------------");
			list.add(Arguments.arguments(orig_paragraph,orig_paragraph, 3, new PrintWriter(Paths.get(resource,"testresult","result.txt").toString())));
			
		} catch (IOException e) {
	
			e.printStackTrace();
		}
		
		return list;
	}
	
	//@EnabledOnOs(OS.WINDOWS)
	@ParameterizedTest
	@MethodSource
	void testContrast(String[] str1, String[] str2, int min, String[][] result) {
		String[][] res = RepeatCounter.contrast(str1, str2, min);
		assertArrayEquals(result, res);
		
		System.out.println(Arrays.deepToString(res));
	}
	static List<Arguments> testContrast()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		list.add(Arguments.arguments(new String[]{"钟景文","是","广东工业大学","计算机学院","信息安全","2018","年级","的","学生"},
											  new String[]{"钟东小","不是","广东工业大学","计算机学院","信息安全","2023","年级","的","学生"},
											   2,
		  										new String[][]{
																	new String[] {"广东工业大学","计算机学院","信息安全"},
																	new String[] {"年级","的","学生"}
		  															}
											 ));
		
	
		list.add(Arguments.arguments(new String[]{"钟景文","是","广东工业大学","计算机学院","信息安全","2018","年级","的","学生"},
				  new String[]{"钟东小","不是","2018","年级","广东工业大学","计算机学院","信息安全","2023","年级","的","学生"},
				   2,
					new String[][]{
										new String[] {"2018","年级"},
										new String[] {"广东工业大学","计算机学院","信息安全"},
										new String[] {"年级","的","学生"}
										}
				 ));
		
		
		return list;
	}

}
