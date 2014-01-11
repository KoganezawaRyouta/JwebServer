import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;


public class GetExecutor implements CommandExcecutor {

	@Override
	public CommandResult run(ServerConfig config, HttpRequest request, ArrayList<String> headers) {
		HttpRequest.CommandType type = request.getType();
		if (type != HttpRequest.CommandType.Get) {
			throw new IllegalArgumentException("Invalid request type. type=" + type.toString());
		}

		File documentRoot = config.getDocumentRoot();
		String path = request.getPath();
		String requestPath = documentRoot.getAbsolutePath() + path;

		System.out.println("RequestPath=" + requestPath);

		HttpResponse response = HttpResponse.RESPONSE_200;
		FileInputStream fis = null;
		byte[] dataBuffer = null;

		File requestFile = new File(requestPath);
		if (!requestFile.getAbsolutePath().startsWith(documentRoot.getAbsolutePath())) {
			response = HttpResponse.RESPONSE_403;
		} else if (!requestFile.isFile()) {
			if (requestFile.isDirectory() && config.getDirectoryIndex()) {
				try {
					String directoryIndex = JDir.listFiles(requestFile);
					dataBuffer = directoryIndex.replace("\n", "<br>").getBytes("UTF-8");
				} catch (Exception ex) {
					response = HttpResponse.RESPONSE_500;
				}
			} else {
				response = HttpResponse.RESPONSE_404;
			}
		} else {
			try {
				fis = new FileInputStream(requestPath);
				dataBuffer = new byte[fis.available()];
				fis.read(dataBuffer);
				fis.close();
				fis = null;
			} catch (Exception ex) {
				response = HttpResponse.RESPONSE_500;
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception ex) {
						// ignore
					}
				}
			}
		}

		CommandResult result = new CommandResult(response, "text/html; charset=UTF-8");
		if (response.equals(HttpResponse.RESPONSE_200)) {
			if (dataBuffer != null) {
				result.setResultData(dataBuffer);
			}
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("<html><body>Status Code:");
			sb.append(response.getStatusCode());
			sb.append("<br><font color=red>");
			sb.append(response.getDescription());
			sb.append("</font></body></html>");
			try {
				result.setResultData(sb.toString().getBytes("UTF-8"));
			} catch (Exception ex) {
				// ignore
			}
		}

		return result;
	}

}
