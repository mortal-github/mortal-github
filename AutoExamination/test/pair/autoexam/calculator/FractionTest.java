package pair.autoexam.calculator;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pair.autoexam.throwable.StrictFractionCalculateException;
import pair.autoexam.throwable.StrictFractionFormatException;

class FractionTest {

	@ParameterizedTest
	@MethodSource
	void testMaxCommonFactor(int expected, int a, int b) {
		Assertions.assertEquals(expected, StrictFraction.maxCommonFactor(a, b));
	}
	static List<Arguments> testMaxCommonFactor(){
		ArrayList<Arguments> list = new ArrayList<>();
		//2,3,5,7,11,13,17,19
		//边界条件
		list.add(Arguments.arguments(0,0,0));
		list.add(Arguments.arguments(0,11,0));
		list.add(Arguments.arguments(0,0,11));
		//互素
		list.add(Arguments.arguments(1,19,11));	
		list.add(Arguments.arguments(1,2,3));
		list.add(Arguments.arguments(1,7,19));
		//相同
		list.add(Arguments.arguments(7,7,7));
		list.add(Arguments.arguments(11,11,11));
		list.add(Arguments.arguments(19,19,19));
		list.add(Arguments.arguments(19*11,19*11,19*11));
		//包含左
		list.add(Arguments.arguments(11,11,11*17));
		list.add(Arguments.arguments(3,3,3*5));
		list.add(Arguments.arguments(17,17,17*5));
		//包含右
		list.add(Arguments.arguments(11,11*17,11));
		list.add(Arguments.arguments(3,3*5,3));
		list.add(Arguments.arguments(17,17*5,17));
		//普通
		list.add(Arguments.arguments(11*13,11*13*5,11*13*7));
		list.add(Arguments.arguments(5*7, 5*7*11, 5*7*19));
		list.add(Arguments.arguments(2*3, 2*3*7, 2*3*13));
		
		return list;
	}
	
	@ParameterizedTest
	@MethodSource
	void testMinCommonMultiple(int expected, int a, int b) {
		Assertions.assertEquals(expected, StrictFraction.minCommonMultiple(a, b));
	}
	static List<Arguments> testMinCommonMultiple(){
		ArrayList<Arguments> list = new ArrayList<>();
		//2,3,5,7,11,13,17,19
		//互素
		list.add(Arguments.arguments(19*11,19,11));	
		list.add(Arguments.arguments(2*3,2,3));
		list.add(Arguments.arguments(7*19,7,19));
		//相同
		list.add(Arguments.arguments(7,7,7));
		list.add(Arguments.arguments(11,11,11));
		list.add(Arguments.arguments(19,19,19));
		list.add(Arguments.arguments(19*11,19*11,19*11));
		//包含左
		list.add(Arguments.arguments(11*17,11,11*17));
		list.add(Arguments.arguments(3*5,3,3*5));
		list.add(Arguments.arguments(17*5,17,17*5));
		//包含右
		list.add(Arguments.arguments(11*17,11*17,11));
		list.add(Arguments.arguments(3*5,3*5,3));
		list.add(Arguments.arguments(17*5,17*5,17));
		//普通
		list.add(Arguments.arguments(11*13*5*7,11*13*5,11*13*7));
		list.add(Arguments.arguments(5*7*11*19, 5*7*11, 5*7*19));
		list.add(Arguments.arguments(2*3*7*13, 2*3*7, 2*3*13));

		return list;
	}

	@ParameterizedTest
	@MethodSource
	void testParseArray(String fraction, int[] expacted) throws StrictFractionFormatException
	{
		Assertions.assertArrayEquals(expacted, StrictFraction.parseArray(fraction));
	}
	static List<Arguments> testParseArray()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		//边界测试
		list.add(Arguments.arguments("0", new int[] {0,1}));
		list.add(Arguments.arguments("0/4", new int[] { 0, 4 }));
		
		list.add(Arguments.arguments("0'3/4", new int[] { 3, 4 }));
		list.add(Arguments.arguments("1'0/4", new int[] { 4 , 4 }));

		list.add(Arguments.arguments("0'0/4", new int[] { 0, 4 }));
		
		//整数
		list.add(Arguments.arguments("3", new int[] {3,1}));
		list.add(Arguments.arguments("34534", new int[] {34534,1}));
		list.add(Arguments.arguments("4656745", new int[] {4656745,1}));
		//真分数
		list.add(Arguments.arguments("3/4", new int[] { 3 , 4 }));
		list.add(Arguments.arguments("234/345"  , new int[] {234  ,345  }));
		list.add(Arguments.arguments("2433/457354", new int[] {  2433, 457354 }));
		//假分数
		list.add(Arguments.arguments("6/4", new int[] { 6,4 }));
		list.add(Arguments.arguments("654/234", new int[] { 654,234 }));
		list.add(Arguments.arguments("585674/2342", new int[] {585674,2342 }));
		//带分数
		list.add(Arguments.arguments("1\'6/4", new int[] { 6+4*1,4 }));
		list.add(Arguments.arguments("1\'654/234", new int[] { 654+234*1,234 }));
		list.add(Arguments.arguments("1\'585674/2342", new int[] {585674+2342*1,2342 }));
		return list;
	}


	@ParameterizedTest
	@MethodSource
	void testParseString(String expected, int[] fraction, boolean cut) throws StrictFractionFormatException
	{
		Assertions.assertEquals(expected, StrictFraction.parseString(fraction, cut));
	}
	static List<Arguments> testParseString()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		//0
		list.add(Arguments.arguments(  "0", new int[] {  0,  4}, true ));
		list.add(Arguments.arguments(  "0", new int[] { 0 ,  13}, true ));
		list.add(Arguments.arguments(  "0", new int[] { 0 ,  32342}, true ));
			//不约整
		list.add(Arguments.arguments(  "0/4", new int[] {  0,  4}, false));
		list.add(Arguments.arguments(  "0/13", new int[] { 0 ,  13}, false ));
		list.add(Arguments.arguments(  "0/32342", new int[] { 0 ,  32342}, false));
		//真分数
		list.add(Arguments.arguments(  "3/4", new int[] {  3, 4 }, true ));
		list.add(Arguments.arguments(  "24/56", new int[] { 24, 56 }, true ));
		list.add(Arguments.arguments(  "234/53543", new int[] { 234 , 53543 }, true ));
			//不约整
		list.add(Arguments.arguments(  "3/4", new int[] {  3, 4 }, false));
		list.add(Arguments.arguments(  "24/56", new int[] { 24, 56 }, false ));
		list.add(Arguments.arguments(  "234/53543", new int[] { 234 , 53543 }, false ));
		//假分数
		list.add(Arguments.arguments(  "1'1/3", new int[] {  4,3 }, true));
		list.add(Arguments.arguments(  "2'8/24", new int[] {  56,24 }, true));
		list.add(Arguments.arguments(  "228'191/234", new int[] {  53543,234  }, true));
			//不约整
		list.add(Arguments.arguments(  "4/3", new int[] {  4,3 }, false));
		list.add(Arguments.arguments(  "56/24", new int[] {  56,24 }, false));
		list.add(Arguments.arguments(  "53543/234", new int[] {  53543,234  }, false));
		//一倍
		list.add(Arguments.arguments(   "1", new int[] { 4 , 4}, true ));
		list.add(Arguments.arguments(   "1", new int[] { 23 , 23}, true ));
		list.add(Arguments.arguments(   "1", new int[] { 52355 , 52355},  true ));
			//不约整
		list.add(Arguments.arguments(   "4/4", new int[] { 4 , 4}, false ));
		list.add(Arguments.arguments(   "23/23", new int[] { 23 , 23}, false ));
		list.add(Arguments.arguments(   "52355/52355", new int[] { 52355 , 52355},  false ));
		//多倍
		list.add(Arguments.arguments(   "3", new int[] { 4*3 , 4},   true));
		list.add(Arguments.arguments(   "23", new int[] { 23*11 ,11 }, true ));
		list.add(Arguments.arguments(   "23423", new int[] { 23423*23456 , 23456}, true ));
			//不约整
		list.add(Arguments.arguments(   "12/4", new int[] { 4*3 , 4},   false));
		list.add(Arguments.arguments(   "253/11", new int[] { 23*11 ,11 }, false));
		list.add(Arguments.arguments(   "5480982/23423", new int[] { 23423*234 , 23423}, false ));
	
		return list;
	}


	@ParameterizedTest
	@MethodSource
	void testAdd(String expected, String a, String b, boolean cut) throws StrictFractionFormatException, StrictFractionCalculateException {
		Assertions.assertEquals(expected, StrictFraction.add(a, b, cut));
	}

	@ParameterizedTest
	@MethodSource
	void testSubtract(String expected, String a, String b, boolean cut) throws StrictFractionFormatException, StrictFractionCalculateException {
		Assertions.assertEquals(expected, StrictFraction.subtract(a, b, cut));
	}

	@ParameterizedTest
	@MethodSource
	void testTimes(String expected, String a, String b, boolean cut) throws StrictFractionFormatException, StrictFractionCalculateException {
		Assertions.assertEquals(expected, StrictFraction.times(a, b, cut));
	}

	@ParameterizedTest
	@MethodSource
	void testDivide(String expected, String a, String b, boolean cut) throws StrictFractionFormatException, StrictFractionCalculateException {
		Assertions.assertEquals(expected, StrictFraction.divide(a, b, cut));
	}
	
	static List<Arguments> testAdd()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		//0边界
		list.add(Arguments.arguments(   "0/1" ,    "0" ,   "0" ,   false));
		list.add(Arguments.arguments(    "1/1",     "1",    "0",   false));
		list.add(Arguments.arguments(    "1/1",     "0",    "1",   false));
		list.add(Arguments.arguments(    "3/1",     "3",    "0",   false));
		list.add(Arguments.arguments(    "3/1",     "0",    "3",   false));
		list.add(Arguments.arguments(    "13/1",     "13",    "0",   false));
		list.add(Arguments.arguments(    "13/1",     "0",    "13",   false));
		list.add(Arguments.arguments(    "23432/1",     "23432",    "0",   false));
		list.add(Arguments.arguments(    "23432/1",     "0",    "23432",   false));
			//约整
		list.add(Arguments.arguments(   "0" ,    "0" ,   "0" ,   true));
		list.add(Arguments.arguments(    "1",     "1",    "0",   true));
		list.add(Arguments.arguments(    "1",     "0",    "1",   true));
		list.add(Arguments.arguments(    "3",     "3",    "0",   true));
		list.add(Arguments.arguments(    "3",     "0",    "3",   true));
		list.add(Arguments.arguments(    "13",     "13",    "0",   true));
		list.add(Arguments.arguments(    "13",     "0",    "13",   true));
		list.add(Arguments.arguments(    "23432",     "23432",    "0",   true));
		list.add(Arguments.arguments(    "23432",     "0",    "23432",   true));
		//真分数+真分数
		list.add(Arguments.arguments(    "37/21",     "13/14",  "25/30"  ,   false));
		list.add(Arguments.arguments(    "1'16/21",     "13/14",  "25/30"  ,   true));
		//真分数+假分数
		list.add(Arguments.arguments(    "149/70",     "13/14",  "30/25"  ,   false));
		list.add(Arguments.arguments(    "2'9/70",     "13/14",  "30/25"  ,   true));
		//假分数+真分数
		list.add(Arguments.arguments(    "149/70",     "30/25"  , "13/14",  false));
		list.add(Arguments.arguments(    "2'9/70",     "30/25"  , "13/14",  true));
		//真分数+带分数
		list.add(Arguments.arguments(    "149/70",     "13/14",  "1'5/25"  ,   false));
		list.add(Arguments.arguments(    "2'9/70",     "13/14",  "1'5/25"  ,   true));
		//带分数+真分数
		list.add(Arguments.arguments(    "149/70",     "1'5/25"  , "13/14",  false));
		list.add(Arguments.arguments(    "2'9/70",     "1'5/25"  , "13/14",  true));
		//假分数+假分数
		list.add(Arguments.arguments(    "148/65",     "14/13",  "30/25"  ,   false));
		list.add(Arguments.arguments(    "2'18/65",     "14/13",  "30/25"  ,   true));
		//假分数+带分数
		list.add(Arguments.arguments(    "148/65",     "14/13",  "1'5/25"  ,   false));
		list.add(Arguments.arguments(    "2'18/65",     "14/13",  "1'5/25"  ,   true));
		//带分数+假分数
		list.add(Arguments.arguments(    "148/65",     "1'1/13",  "30/25"  ,   false));
		list.add(Arguments.arguments(    "2'18/65",     "1'1/13",  "30/25"  ,   true));
		//带分数+带分数
		list.add(Arguments.arguments(    "148/65",     "1'1/13",  "1'5/25"  ,   false));
		list.add(Arguments.arguments(    "2'18/65",     "1'1/13",  "1'5/25"  ,   true));
		//整数
		list.add(Arguments.arguments(    "2/1",     "1",  "1"  ,   false));
		list.add(Arguments.arguments(    "7/1",    "1",   "6",   false));
		list.add(Arguments.arguments(    "7/1",    "6",   "1",   false));
		list.add(Arguments.arguments(    "23/1",    "22",  "1" ,   false));
		list.add(Arguments.arguments(    "24/1",    "1",   "23",   false));
		list.add(Arguments.arguments(    "437459/1",   "437458" ,   "1",   false));
		list.add(Arguments.arguments(    "437459/1",   "1", "437458" ,  false));
		list.add(Arguments.arguments(    "7/1",     "4",   "3",   false));
		list.add(Arguments.arguments(    "36/1",     "23", "13" ,   false));
		list.add(Arguments.arguments(    "3085/1",     "2342",  "743",   false));
			//约整
		list.add(Arguments.arguments(    "2",     "1",  "1"  ,   true));
		list.add(Arguments.arguments(    "7",    "1",   "6",   true));
		list.add(Arguments.arguments(    "7",    "6",   "1",   true));
		list.add(Arguments.arguments(    "23",    "22",  "1" ,   true));
		list.add(Arguments.arguments(    "24",    "1",   "23",   true));
		list.add(Arguments.arguments(    "437459",   "437458" ,   "1",   true));
		list.add(Arguments.arguments(    "437459",   "1", "437458" ,  true));
		list.add(Arguments.arguments(    "7",     "4",   "3",   true));
		list.add(Arguments.arguments(    "36",     "23", "13" ,   true));
		list.add(Arguments.arguments(    "3085",     "2342",  "743",   true));
		return list;
	}
	
	static List<Arguments> testSubtract()
	{
		ArrayList<Arguments> list = new ArrayList<>();
//抛出计算异常的测试样例
//		list.add(Arguments.arguments(    "1/1",     "0",    "1",   false));
//		list.add(Arguments.arguments(    "3/1",     "0",    "3",   false));
//		list.add(Arguments.arguments(    "13/1",     "0",    "13",   false));
//		list.add(Arguments.arguments(    "23432/1",     "0",    "23432",   false));
//		list.add(Arguments.arguments(    "1",     "0",    "1",   true));
//		list.add(Arguments.arguments(    "3",     "0",    "3",   true));
//		list.add(Arguments.arguments(    "13",     "0",    "13",   true));
//		list.add(Arguments.arguments(    "23432",     "0",    "23432",   true));
//		list.add(Arguments.arguments(    "7/1",    "1",   "6",   false));
//		list.add(Arguments.arguments(    "24/1",    "1",   "23",   false));
//		list.add(Arguments.arguments(    "437459/1",   "1", "437458" ,  false));
		
		//0边界
		list.add(Arguments.arguments(   "0/1" ,    "0" ,   "0" ,   false));
		list.add(Arguments.arguments(    "1/1",     "1",    "0",   false));
		list.add(Arguments.arguments(    "3/1",     "3",    "0",   false));
		list.add(Arguments.arguments(    "13/1",     "13",    "0",   false));
		list.add(Arguments.arguments(    "23432/1",     "23432",    "0",   false));
			//约整
		list.add(Arguments.arguments(   "0" ,    "0" ,   "0" ,   true));
		list.add(Arguments.arguments(    "1",     "1",    "0",   true));
		list.add(Arguments.arguments(    "3",     "3",    "0",   true));
		list.add(Arguments.arguments(    "13",     "13",    "0",   true));
		list.add(Arguments.arguments(    "23432",     "23432",    "0",   true));
		//真分数-真分数
		list.add(Arguments.arguments(    "2/21",     "13/14",  "25/30"  ,   false));
		list.add(Arguments.arguments(    "2/21",     "13/14",  "25/30"  ,   true));
		//假分数-真分数
		list.add(Arguments.arguments(    "19/70",     "30/25"  , "13/14",  false));
		list.add(Arguments.arguments(    "19/70",     "30/25"  , "13/14",  true));
		//带分数-真分数
		list.add(Arguments.arguments(    "19/70",     "1'5/25"  , "13/14",  false));
		list.add(Arguments.arguments(    "19/70",     "1'5/25"  , "13/14",  true));
		//假分数-假分数
		list.add(Arguments.arguments(    "8/65",  "30/25"   ,     "14/13",  false));
		list.add(Arguments.arguments(    "8/65",  "30/25"  ,      "14/13",  true));
		//假分数-带分数
		list.add(Arguments.arguments(    "8/65",  "30/25"   ,     "1'1/13",  false));
		list.add(Arguments.arguments(    "8/65",  "30/25"  ,      "1'1/13",  true));
		//带分数-假分数
		list.add(Arguments.arguments(    "8/65",  "1'5/25"   ,     "14/13",  false));
		list.add(Arguments.arguments(    "8/65",  "1'5/25"  ,      "14/13",  true));
		//带分数+带分数
		list.add(Arguments.arguments(    "8/65",  "1'5/25"   ,     "1'1/13",  false));
		list.add(Arguments.arguments(    "8/65",  "1'5/25"  ,      "1'1/13",  true));
		//整数
		list.add(Arguments.arguments(    "0/1",     "1",  "1"  ,   false));
		list.add(Arguments.arguments(    "5/1",    "6",   "1",   false));
		list.add(Arguments.arguments(    "21/1",    "22",  "1" ,   false));
		list.add(Arguments.arguments(    "437457/1",   "437458" ,   "1",   false));
		list.add(Arguments.arguments(    "1/1",     "4",   "3",   false));
		list.add(Arguments.arguments(    "10/1",     "23", "13" ,   false));
		list.add(Arguments.arguments(    "1599/1",     "2342",  "743",   false));
			//约整
		list.add(Arguments.arguments(    "0",     "1",  "1"  ,   true));
		list.add(Arguments.arguments(    "5",    "6",   "1",   true));
		list.add(Arguments.arguments(    "21",    "22",  "1" ,   true));
		list.add(Arguments.arguments(    "437457",   "437458" ,   "1",   true));
		list.add(Arguments.arguments(    "1",     "4",   "3",   true));
		list.add(Arguments.arguments(    "10",     "23", "13" ,   true));
		list.add(Arguments.arguments(    "1599",     "2342",  "743",   true));
		return list;
	}
	
	static List<Arguments> testTimes()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		//0边界
		list.add(Arguments.arguments(   "0/1" ,    "0" ,   "0" ,   false));
		list.add(Arguments.arguments(    "0/1",     "1",    "0",   false));
		list.add(Arguments.arguments(    "0/1",     "0",    "1",   false));
		list.add(Arguments.arguments(    "0/1",     "3",    "0",   false));
		list.add(Arguments.arguments(    "0/1",     "0",    "3",   false));
		list.add(Arguments.arguments(    "0/1",     "13",    "0",   false));
		list.add(Arguments.arguments(    "0/1",     "0",    "13",   false));
		list.add(Arguments.arguments(    "0/1",     "23432",    "0",   false));
		list.add(Arguments.arguments(    "0/1",     "0",    "23432",   false));
			//约整
		list.add(Arguments.arguments(   "0" ,    "0" ,   "0" ,   true));
		list.add(Arguments.arguments(    "0",     "1",    "0",   true));
		list.add(Arguments.arguments(    "0",     "0",    "1",   true));
		list.add(Arguments.arguments(    "0",     "3",    "0",   true));
		list.add(Arguments.arguments(    "0",     "0",    "3",   true));
		list.add(Arguments.arguments(    "0",     "13",    "0",   true));
		list.add(Arguments.arguments(    "0",     "0",    "13",   true));
		list.add(Arguments.arguments(    "0",     "23432",    "0",   true));
		list.add(Arguments.arguments(    "0",     "0",    "23432",   true));
		//真分数x真分数
		list.add(Arguments.arguments(    "65/84",     "13/14",  "25/30"  ,   false));
		list.add(Arguments.arguments(    "65/84",     "13/14",  "25/30"  ,   true));
		//真分数x假分数
		list.add(Arguments.arguments(    "39/35",     "13/14",  "30/25"  ,   false));
		list.add(Arguments.arguments(    "1'4/35",     "13/14",  "30/25"  ,   true));
		//假分数x真分数
		list.add(Arguments.arguments(    "39/35",     "30/25"  ,  "13/14",   false));
		list.add(Arguments.arguments(    "1'4/35",    "30/25"  ,     "13/14", true));
		//真分数x带分数
		list.add(Arguments.arguments(    "39/35",     "13/14",  "1'5/25"  ,   false));
		list.add(Arguments.arguments(    "1'4/35",     "13/14",  "1'5/25"  ,   true));
		//带分数x真分数
		list.add(Arguments.arguments(    "39/35",     "1'5/25"  ,  "13/14",   false));
		list.add(Arguments.arguments(    "1'4/35",    "1'5/25"  ,     "13/14", true));
		//假分数x假分数
		list.add(Arguments.arguments(    "84/65",     "14/13",  "30/25"  ,   false));
		list.add(Arguments.arguments(    "1'19/65",     "14/13",  "30/25"  ,   true));
		//假分数x带分数
		list.add(Arguments.arguments(    "84/65",     "14/13",  "1'5/25"  ,   false));
		list.add(Arguments.arguments(    "1'19/65",     "14/13",  "1'5/25"  ,   true));
		//带分数x假分数
		list.add(Arguments.arguments(    "84/65",     "1'1/13",  "30/25"  ,   false));
		list.add(Arguments.arguments(    "1'19/65",     "1'1/13",  "30/25"  ,   true));
		//带分数x带分数
		list.add(Arguments.arguments(    "84/65",     "1'1/13",  "1'5/25"  ,   false));
		list.add(Arguments.arguments(    "1'19/65",     "1'1/13",  "1'5/25"  ,   true));
		
		//整数
		list.add(Arguments.arguments(    "1/1",     "1",  "1"  ,   false));
		list.add(Arguments.arguments(    "6/1",    "1",   "6",   false));
		list.add(Arguments.arguments(    "6/1",    "6",   "1",   false));
		list.add(Arguments.arguments(    "22/1",    "22",  "1" ,   false));
		list.add(Arguments.arguments(    "23/1",    "1",   "23",   false));
		list.add(Arguments.arguments(    "437458/1",   "437458" ,   "1",   false));
		list.add(Arguments.arguments(    "437458/1",   "1", "437458" ,  false));
		list.add(Arguments.arguments(    "12/1",     "4",   "3",   false));
		list.add(Arguments.arguments(    "299/1",     "23", "13" ,   false));
		list.add(Arguments.arguments(    "1740106/1",     "2342",  "743",   false));
			//约整
		list.add(Arguments.arguments(    "1",     "1",  "1"  ,   true));
		list.add(Arguments.arguments(    "6",    "1",   "6",   true));
		list.add(Arguments.arguments(    "6",    "6",   "1",   true));
		list.add(Arguments.arguments(    "22",    "22",  "1" ,   true));
		list.add(Arguments.arguments(    "23",    "1",   "23",   true));
		list.add(Arguments.arguments(    "437458",   "437458" ,   "1",   true));
		list.add(Arguments.arguments(    "437458",   "1", "437458" ,  true));
		list.add(Arguments.arguments(    "12",     "4",   "3",   true));
		list.add(Arguments.arguments(    "299",     "23", "13" ,   true));
		list.add(Arguments.arguments(    "1740106",     "2342",  "743",   true));
		return list;
	}

	
	static List<Arguments> testDivide()
	{
		ArrayList<Arguments> list = new ArrayList<>();
//除数为0的异常		
//		list.add(Arguments.arguments(    "0/1",     "1",    "0",   false));
//		list.add(Arguments.arguments(   "0/1" ,    "0" ,   "0" ,   false));
//		list.add(Arguments.arguments(    "0/1",     "3",    "0",   false));
//		list.add(Arguments.arguments(    "0/1",     "13",    "0",   false));
//		list.add(Arguments.arguments(    "0/1",     "23432",    "0",   false));
		//0边界
		list.add(Arguments.arguments(    "0/1",     "0",    "1",   false));
		list.add(Arguments.arguments(    "0/3",     "0",    "3",   false));
		list.add(Arguments.arguments(    "0/13",     "0",    "13",   false));
		list.add(Arguments.arguments(    "0/23432",     "0",    "23432",   false));
			//约整
		list.add(Arguments.arguments(    "0",     "0",    "1",   true));
		list.add(Arguments.arguments(    "0",     "0",    "3",   true));
		list.add(Arguments.arguments(    "0",     "0",    "13",   true));
		list.add(Arguments.arguments(    "0",     "0",    "23432",   true));
		//真分数x真分数
		list.add(Arguments.arguments(    "65/84",     "13/14",  "30/25"  ,   false));
		list.add(Arguments.arguments(    "65/84",     "13/14",  "30/25"  ,   true));
		//真分数x假分数
		list.add(Arguments.arguments(    "39/35",     "13/14",  "25/30"  ,   false));
		list.add(Arguments.arguments(    "1'4/35",     "13/14",  "25/30"  ,   true));
		//假分数x真分数
		list.add(Arguments.arguments(    "39/35",     "30/25"  ,  "14/13",   false));
		list.add(Arguments.arguments(    "1'4/35",    "30/25"  ,     "14/13", true));
		//真分数x带分数

		
		//带分数x真分数
		list.add(Arguments.arguments(    "39/35",     "1'5/25"  ,  "14/13",   false));
		list.add(Arguments.arguments(    "1'4/35",    "1'5/25"  ,     "14/13", true));
		//假分数x假分数
		list.add(Arguments.arguments(    "84/65",     "14/13",  "25/30"  ,   false));
		list.add(Arguments.arguments(    "1'19/65",     "14/13",  "25/30"  ,   true));

		//带分数x假分数
		list.add(Arguments.arguments(    "84/65",     "1'1/13",  "25/30"  ,   false));
		list.add(Arguments.arguments(    "1'19/65",     "1'1/13",  "25/30"  ,   true));

		//整数
		list.add(Arguments.arguments(    "1/1",     "1",  "1"  ,   false));
		list.add(Arguments.arguments(    "6/1",      "6",   "1", false));
		list.add(Arguments.arguments(    "1/6",      "1",  "6",  false));
		list.add(Arguments.arguments(    "1/22",    "1" ,   "22",  false));
		list.add(Arguments.arguments(    "23/1",      "23",  "1",  false));
		list.add(Arguments.arguments(    "1/437458",     "1",   "437458" , false));
		list.add(Arguments.arguments(    "437458/1",  "437458" ,  "1",  false));
		list.add(Arguments.arguments(    "3/4",       "3",   "4", false));
		list.add(Arguments.arguments(    "13/23",      "13" , "23",  false));
		list.add(Arguments.arguments(    "743/2342",     "743",  "2342",   false));
			//约整
		list.add(Arguments.arguments(    "1",     "1",  "1"  ,   true));
		list.add(Arguments.arguments(    "6",      "6",   "1", true));
		list.add(Arguments.arguments(    "1/6",      "1",  "6",  true));
		list.add(Arguments.arguments(    "1/22",    "1" ,   "22",  true));
		list.add(Arguments.arguments(    "23",      "23",  "1",  true));
		list.add(Arguments.arguments(    "1/437458",     "1",   "437458" , true));
		list.add(Arguments.arguments(    "437458",  "437458" ,  "1",  true));
		list.add(Arguments.arguments(    "3/4",       "3",   "4", true));
		list.add(Arguments.arguments(    "13/23",      "13" , "23",  true));
		list.add(Arguments.arguments(    "743/2342",     "743",  "2342",  true));
		return list;
	}

	@ParameterizedTest
	@MethodSource
	void testCompare(int expected, String a, String b) throws StrictFractionFormatException
	{
		Assertions.assertEquals(expected, StrictFraction.compare(a, b));
	}
	static List<Arguments> testCompare()
	{
		ArrayList<Arguments> list = new ArrayList<>();
		//整数
		list.add(Arguments.arguments(  0,  "0", "0" ));
		list.add(Arguments.arguments(  -1,  "0", "1"));
		list.add(Arguments.arguments(  1,  "1", "0" ));
		list.add(Arguments.arguments(  0,  "1", "1"));
		list.add(Arguments.arguments(  -1,  "1", "3"));
		list.add(Arguments.arguments(  1,  "3", "1"));
		list.add(Arguments.arguments(  -1,  "234", "2342"));
		list.add(Arguments.arguments(  1,  "32424", "342"));
		list.add(Arguments.arguments(  0,  "234", "234"));
		//分数
		list.add(Arguments.arguments(-1, "3/4", "4/4"));
		list.add(Arguments.arguments(0, "3/4", "3/4"));
		list.add(Arguments.arguments(1, "4/4", "3/4"));
		
		return list;
	}


}
