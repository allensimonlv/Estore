package exercise;

public class ali {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		A a = new B();
		a.eat();
	}
}

class A{
	public void eat(){
		System.out.println("A eat");
	}
} 
 class B extends A{
	 public void eat(){
		 System.out.println("B eat");
	 }
 }