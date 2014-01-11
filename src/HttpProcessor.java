import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpProcessor extends Thread {
	
	private static HashMap<HttpRequest.CommandType, CommandExcecutor> COMMAND_TABLE = new HashMap<HttpRequest.CommandType, CommandExcecutor>();
	
	static {
		COMMAND_TABLE.put(HttpRequest.CommandType.Get, new GetExecutor());
	}
	
	private HttpServer server;
	private Socket socket;
	
	public HttpProcessor(HttpServer server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	public void run() {		
		debugPrint("HttpProcessor started.");
		
		LineNumberReader lnr = null;
		PrintWriter pw = null;
		try {
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			lnr = new LineNumberReader(isr);
			
			String requestLine = lnr.readLine();
			debugPrint("Request:" + requestLine);
			
			HttpRequest request = new HttpRequest(requestLine);
			
			ArrayList<String> headers = new ArrayList<String>();
			String headerLine = null;
			do {
				headerLine = lnr.readLine();
				if (headerLine != null && !headerLine.equals("")) {
					headers.add(headerLine);
					debugPrint("Header:" + headerLine);
				}
			} while (headerLine != null && !headerLine.equals(""));
			
			CommandExcecutor executor = COMMAND_TABLE.get(request.getType());
			CommandResult result = executor.run(server.getConfig(), request, headers);
			
			OutputStream os = socket.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			pw = new PrintWriter(bos);
			pw.println(result.getResponse().getResponseLine());
			pw.println("Content-Type: " + result.getContentType());
			pw.print("\n\n");
			pw.flush();
			
			byte[] data = result.getResultData();
			if (data != null) {
				bos.write(data);
				bos.flush();
			}
			pw.close();
			pw = null;
			
			lnr.close();
			lnr = null;

			debugPrint("HttpProcessor terminated.");
		} catch (Exception ex) {
			debugPrint("Error:" + ex.toString());
		} finally {
			if (lnr != null) {
				try {
					lnr.close();
				} catch (Exception ex) {
					// ignore
				}
				lnr = null;
			}
			if (pw != null) {
				try {
					pw.close();
				} catch (Exception ex) {
					// ignore
				}
				pw = null;
			}
			try {
				socket.close();
			} catch (Exception ex) {
				// ignore
			}
			
			server.removeProcessor(this);
		}
	}
	
	private void debugPrint(String message) {
		System.out.println("[Thread-" + this.getId() + "]" + message);
	}
}
