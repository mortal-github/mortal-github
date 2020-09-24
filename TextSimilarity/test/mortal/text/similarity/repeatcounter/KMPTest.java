package mortal.text.similarity.repeatcounter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class KMPTest {

	@ParameterizedTest
	@MethodSource
	void testGetFirstIndex(int index, String pattern, String text) {
		assertEquals(index,KMP.getFirstIndex(pattern.toCharArray(), text.toCharArray()));
	}
	static List<Arguments> testGetFirstIndex()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		
		list.add(Arguments.arguments(3,"钟景文", "我不是钟景文"));
		list.add(Arguments.arguments(5,"钟景文", "我钟景不是钟景文"));
		list.add(Arguments.arguments(7,"钟景文", "我不钟景是景文钟景文"));
		list.add(Arguments.arguments(9,"钟景文", "我不不是钟景是景文钟景文"));	
		return list;
	}

}
