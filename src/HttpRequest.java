
public class HttpRequest {

	enum CommandType {
		Get,
		Unknown
	};
	
	private String requestLine;
	private CommandType commandType = HttpRequest.CommandType.Unknown;
	private String requestPath;
	private String requestVersion;
	
	public HttpRequest(String requestLine) {
		this.requestLine = requestLine;
		parse();
	}
	
	public HttpRequest.CommandType getType() {
		return commandType;
	}
	
	public String getPath() {
		return requestPath;
	}
	
	public String getVersion() {
		return requestVersion;
	}
	
	private void parse() {
		String[] requests = requestLine.split(" ");
		if (requests.length != 3) {
			throw new IllegalArgumentException("Invalid request line:" + requestLine);
		}
		if ("GET".equalsIgnoreCase(requests[0])) {
			commandType = HttpRequest.CommandType.Get;
		} else {
			throw new IllegalArgumentException("Invalid request command:" + requests[0]);
		}
		requestPath = requests[1];
		requestVersion = requests[2];
	}
}
