package exercise;

/*在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
 * 请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数
 * 这种算法时间复杂度高：O(n^2)，不合适
 * */
public class Solution {
	
	public static boolean Find(int target, int [][] array) {
				
		for(int i=0;i<array.length;i++){
			for(int j=0;j<array[i].length;j++){
				if(target==array[i][j]){
					return true;
				}
			}
		}
		return false;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int target = 3;
		int [][] array = {{1,2,3},{4,5,6}};//看到这种大圈套小圈，就毫不犹豫的写上for嵌套！
		boolean flag = Find(target,array);
		if(flag){
			System.out.println("二维数组中包含这个整数");
		}else{
			System.out.println("二维数组中没有这个整数");
		}
		
	}

}
