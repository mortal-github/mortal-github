package mortal.repeatcheck.calculator;

public class RepeatNumber {

	
	
	
	
	public static int[][] getRepeatCodepoints(int[] codepoints_left, String codepoints_right)
	{
		int length_raw = codepoints_left.length;
		int length_col = codepoints_right.length();
		
	     // 构建矩阵，用于查找重复字符串
		int[][] matrix = new int[length_raw][length_col];
		int i=0;//行标
		int j=0;//列标
		
		//初始化第1行,第1列
		for(j=0; j < length_col; j++)
			matrix[0][j] = 0;
		for(i=0; i < length_raw; i++)
			matrix[i][0] = 0;
		
		//
		
		
		
		
		
		
		
		return null;
	}
}
