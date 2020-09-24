package mortal.text.similarity.repeatcounter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RepeatWordsTest {


	@ParameterizedTest
	@MethodSource
	void testCalculateTotalRepeatChar(String[] words1, String[] words2, int min, int expect) {
		RepeatWords repeat = new RepeatWords(words1,words2, min);
		assertEquals(expect, repeat.calculate().getTotal1());
	}
	static ArrayList<Arguments> testCalculateTotalRepeatChar()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		
		list.add(Arguments.arguments(//完全重复
				new String[]{"软件工程","是","一门","非常","重要","的","课程"},
				new String[]{"软件工程","是","一门","非常","重要","的","课程"},
				2,
				14));
		
		list.add(Arguments.arguments(//开头不重复
				new String[]{"软件工程","是","一门","非常","重要","的","学科"},
				new String[]{"软件设计","是","一门","非常","重要","的","学科"},
				2,
				10));
		list.add(Arguments.arguments(//中间不重复
				new String[]{"软件工程","是","一门","非常","重要","的","学科"},
				new String[]{"软件工程","是","一门","十分","重要","的","学科"},
				2,
				12));
		list.add(Arguments.arguments(//末尾不重复
				new String[]{"软件工程","是","一门","非常","重要","的","课程"},
				new String[]{"软件工程","是","一门","非常","重要","的","学科"},
				2,
				12));
		

		list.add(Arguments.arguments(//开头不重复，调换字符数组参数顺序
				new String[]{"软件设计","是","一门","非常","重要","的","学科"},
				new String[]{"软件工程","是","一门","非常","重要","的","学科"},
				2,
				10));
		list.add(Arguments.arguments(//中间不重复，调换字符数组参数顺序。
				new String[]{"软件工程","是","一门","十分","重要","的","学科"},
				new String[]{"软件工程","是","一门","非常","重要","的","学科"},
				2,
				12));
		list.add(Arguments.arguments(//末尾不重复，调换字符数组参数顺序
				new String[]{"软件工程","是","一门","非常","重要","的","学科"},
				new String[]{"软件工程","是","一门","非常","重要","的","课程"},
				2,
				12));
		
		
		
		list.add(Arguments.arguments(//多段重复
				new String[]{"钟景文","是","广东工业大学","计算机学院","信息安全","2018","年级","的","一个","三好学生"},
				new String[]{"钟景文","是","广东工业大学","计算机    ", "信息安全","2018","级别","的","一个","四号青年"},
				2,
				21));
		list.add(Arguments.arguments(//多段重复，雕黄字符数组参数顺序
				new String[]{"钟景文","是","广东工业大学","计算机    ", "信息安全","2018","级别","的","一个","四号青年"},
				new String[]{"钟景文","是","广东工业大学","计算机学院","信息安全","2018","年级","的","一个","三好学生"},
				2,
				21));
		
		list.add(Arguments.arguments(////用重复字符床两个两个独立重复的字符数组，使其连成一个重复数组
				new String[]{"钟景文","是","广东工业大学","计算机学院","信息安全","2018","年级","的","一个","三好学生"},
				new String[]{"钟景文","是","广东工业大学","计算机     ","信息安全","2018","年级","的","一个","四号青年"},
				2,
				23));
		list.add(Arguments.arguments(////用重复字符床两个两个独立重复的字符数组，使其连成一个重复数组
				new String[]{"钟景文","是","广东工业大学","计算机    ","信息安全","2018","年级","的","一个","四号青年"},
				new String[]{"钟景文","是","广东工业大学","计算机学院","信息安全","2018","年级","的","一个","三好学生"},
				2,
				23));
	
		
		return list;
	}

}
