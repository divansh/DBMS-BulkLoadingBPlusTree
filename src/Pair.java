
public class Pair implements Comparable<Pair> {
	
	 int a,b;

	public Pair(int a, int b) {
		super();
		this.a = a;
		this.b = b;
	}
	@Override
	public int compareTo(Pair arg0) {
		return Integer.compare(this.getA(), arg0.getA());
				
	}
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public static void main(String[] args) {
		for(int i=20;i>0;i--)
		{
			System.out.println(i);
		}
	}
}
