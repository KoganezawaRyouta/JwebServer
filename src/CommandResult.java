
public class CommandResult {

	private final HttpResponse response;
	private final String contentType;
	private byte[] resultData = null;
	
	public CommandResult(HttpResponse response, String contentType) {
		this.response = response;
		this.contentType = contentType;
	}
	
	public HttpResponse getResponse() {
		return response;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setResultData(byte[] resultData) {
		this.resultData = resultData;
	}
	
	public byte[] getResultData() {
		return resultData;
	}
}
