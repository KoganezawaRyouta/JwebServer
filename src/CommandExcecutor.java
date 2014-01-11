import java.util.ArrayList;

public interface CommandExcecutor {	
	public CommandResult run(ServerConfig config, HttpRequest request, ArrayList<String> headers);
}
