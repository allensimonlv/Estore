package exercise;

/*��һ����ά�����У�ÿһ�ж����մ����ҵ�����˳������ÿһ�ж����մ��ϵ��µ�����˳������
 * �����һ������������������һ����ά�����һ���������ж��������Ƿ��и�����
 * �����㷨ʱ�临�Ӷȸߣ�O(n^2)��������
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
		int [][] array = {{1,2,3},{4,5,6}};//�������ִ�Ȧ��СȦ���ͺ�����ԥ��д��forǶ�ף�
		boolean flag = Find(target,array);
		if(flag){
			System.out.println("��ά�����а����������");
		}else{
			System.out.println("��ά������û���������");
		}
		
	}

}
