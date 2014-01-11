
public class HttpResponse {
	
	public static HttpResponse RESPONSE_200 = new HttpResponse(200, "OK");
	public static HttpResponse RESPONSE_403 = new HttpResponse(403, "Forbidden");
	public static HttpResponse RESPONSE_404 = new HttpResponse(404, "Not Found");
	public static HttpResponse RESPONSE_500 = new HttpResponse(500, "Internal Server Error");

	private static final String RESPONSE_VERSION = "HTTP/1.1";
	
	private final String version = RESPONSE_VERSION;
	private final int statusCode;
	private final String description;
	private final String responseLine;
	
	public HttpResponse(int statusCode, String description) {
		this.statusCode = statusCode;
		this.description = description;
		this.responseLine = this.version + " " + String.valueOf(this.statusCode) + " " + this.description;
	}
	
	public String getResponseLine() {
		return responseLine;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getDescription() {
		return description;
	}
}
