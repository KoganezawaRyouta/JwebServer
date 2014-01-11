
public class Test {

	public static void main(String[] args) {
		String props = System.getProperties().toString();
		String[] proparrs = props.split(",");
		for (int i=0; i<proparrs.length; i++) {
			System.out.println(proparrs[i]);
		}
		
		String lf = System.getProperty("line.separator");
	}
}
