
public class ReadStdin {
	
	public static void main(String[] args) throws Exception {
		System.out.print("Input?:");
		byte[] b = new byte[256];
		System.in.read(b);
		System.out.println("Result:" + new String(b));
	}

}
