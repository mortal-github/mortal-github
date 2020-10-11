package pair.autoexam;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class Stack<E> {
	private E[] elements = null;				//栈元素
	private int top = 0;						//栈顶标记，下一个入栈元素的位置
	public final int MIN_SIZE = 10;			    //最小容量
	public final int DEFAULT_INCREMENT = 10;	//默认扩容大小
	
	@SuppressWarnings("unchecked")
	public Stack(Class<E> elemType) {
		Objects.requireNonNull(elemType);
		this.elements = (E[]) Array.newInstance(elemType, MIN_SIZE);
		for(int i=0; i< this.elements.length; i++)
			this.elements[i] = null;
	}

	private void realloc(int size)
	{
		assert size > 0;					//新数组长度应该大于0
		assert this.elements != null;		//原数组应该存在
		assert size >= this.top; 			//新数组长度不应该少于元素数量。
		@SuppressWarnings("unchecked")
		E[] newArray = (E[]) Array.newInstance(this.elements.getClass().getComponentType(), size);
		
		//尽可能地复制原有数组元素的引用到新数组
		int raw_length = this.elements.length;
		int min_length = raw_length <= size ? raw_length : size;
		//复制元素
		for(int i = 0 ; i <  min_length; i++ )	
			newArray[i] = this.elements[i];
		//多余空间赋值null
		for(int i = min_length; i < size; i++)
			newArray[i] = null;
		//变容量
		this.elements = newArray;
	}
	
	/**
	 * 将元素入栈.
	 * @param element
	 */
	public void push(E element)
	{
		Objects.requireNonNull(elements);
		//扩容
		if(this.top >= this.elements.length)
		{
			int new_size = this.elements.length +  DEFAULT_INCREMENT;
			this.realloc(new_size);
		}
		//入栈
		this.elements[this.top] = element;
		this.top++;
	}
	
	/**
	 * 元素出栈,失败返回null
	 * @return
	 */
	public E pop()
	{
		if(0 == this.top)
			return null;
		else
		{
			this.top--;
			return this.elements[this.top];
		}
	}
	
	/**
	 *查看栈顶元素，失败返回null。
	 * @return
	 */
	public E top()
	{
		if(0 == this.top)
			return null;
		else
			return this.elements[this.top-1];
	}
	
	public int size()
	{
		return this.top;
	}
	
	public E[] toArray()
	{
		return Arrays.copyOfRange(elements, 0, this.top);
	}
}
