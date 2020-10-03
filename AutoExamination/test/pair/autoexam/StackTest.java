package pair.autoexam;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StackTest {
	Stack<String> stack;
	static String[] elements ;
	
	@BeforeAll
	static void setElements()
	{
		StackTest.elements = new String[]{
				"z","h","o","n","g","j","i","n","g","w",
				"e","n","h","a","o","l","i","h","a","o",
				"m","e","i","n","e","n","g","c","h","a","o"};
	}
	@AfterAll
	static void clearElements()
	{
		StackTest.elements = null;
	}
	
	@BeforeEach
	void newStack()
	{
		stack = new Stack<>(String.class);
	}
	@AfterEach
	void clearStack()
	{
		stack=null;
	}
	
	@Test
	void testPushAndTop() {
		for(int i = 0 ; i < StackTest.elements.length; i++)
		{
			this.stack.Push(StackTest.elements[i]);
			String element = this.stack.Top();
			Assertions.assertEquals(StackTest.elements[i], element);
		}
	}

	@Test
	void testPop() {
		for(int i = 0 ; i < StackTest.elements.length; i++)
		{
			this.stack.Push(StackTest.elements[i]);
		}
		for(int i = StackTest.elements.length-1 ; i >=0 ;i--)
		{
			String element = this.stack.Pop();
			Assertions.assertEquals(StackTest.elements[i], element);
		}
		for(int i = 0; i< 10; i++)
		{
			String element = this.stack.Pop();
			Assertions.assertEquals(null, element);
		}
	}
}
