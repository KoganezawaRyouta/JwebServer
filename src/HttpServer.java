import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class HttpServer extends Thread {
	
	private volatile Boolean isRunning = true;
	private final ServerConfig serverConfig;
	private ServerSocket serverSocket;
	private final Hashtable<String, HttpProcessor> processorTable = new Hashtable<String, HttpProcessor>();
	
	public HttpServer(int port) throws Exception {
		serverConfig = new ServerConfig();
	    serverSocket = new ServerSocket();
	    serverSocket.setReuseAddress(true);
	    serverSocket.bind(new InetSocketAddress(port));
	}
	
	public void run() {
		System.out.println("Start HttpServer: Port=" + serverSocket.getLocalPort());
		try {
			Socket socket = null;
			HttpProcessor processor = null;
			do {
				socket = serverSocket.accept();
				if (!isRunning) {
					break;
				}
				processor = new HttpProcessor(this, socket);
				processorTable.put(String.valueOf(processor.getId()), processor);
				processor.start();
			} while (isRunning);
			
			while (processorTable.size() > 0) {
				Thread.sleep(500);
			}
		} catch (Exception ex) {
		} finally {
			close();
			System.out.println("HttpServer terminated.");
		}
	}
	
	public ServerConfig getConfig() {
		return serverConfig;
	}
	
	public void removeProcessor(HttpProcessor processor) {
		processorTable.remove(String.valueOf(processor.getId()));
	}
	
	public void terminate() {
		isRunning = false;
		close();
		try {
			join();
		} catch (Exception ex) {
			// ignore
		}
	}
	
	private void close() {
		if (serverSocket == null) {
			return;
		}
		try {
			serverSocket.close();
		} catch (Exception ex) {
			// ignore
		}
		serverSocket = null;
	}

	public static void main(String[] args) {
		HttpServer server = null;
		try {
			server = new HttpServer(8081);
			server.start();
			Thread.sleep(1000);
			System.out.print("Any input for Stop server:");
			byte[] b = new byte[256];
			System.in.read(b);
		} catch (Exception ex) {
			System.err.println("Error:" + ex.toString());
		} finally {
			if (server != null) {
				server.terminate();
			}
		}
	}
}
