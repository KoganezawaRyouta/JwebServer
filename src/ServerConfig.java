import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class ServerConfig {
	
	private static final String DEFAULT_CONFIG_PATH = "serverconf.properties";

	private static final String KEY_DOCUMENTROOT = "DocumentRoot";
	private static final String KEY_DIRECTORYINDEX = "DirectoryIndex";
	
	private final Properties configProps = new Properties();
	
	private final File documentRoot;
	private Boolean directoryIndex = false;
	
	public ServerConfig() throws Exception {
		this(DEFAULT_CONFIG_PATH);
	}
	
	public ServerConfig(String configPath) throws Exception {
		FileInputStream fis = new FileInputStream(configPath);
		configProps.load(fis);
		fis.close();
		String documentRootValue = configProps.getProperty(KEY_DOCUMENTROOT);
		if (documentRootValue == null) {
			throw new IllegalArgumentException("Unable to get documentRoot.:config=" + configPath);
		}
		File documentRoot = new File(documentRootValue);
		if (!documentRoot.isDirectory()) {
			throw new IllegalArgumentException("Invalid " + KEY_DOCUMENTROOT + " setting. (Must be directory with read permission):config=" + configPath + ", value=" + documentRootValue);
		}
		this.documentRoot = documentRoot;
		System.out.println("[HttpConfig]DocumentRoot=" + this.documentRoot.getAbsolutePath());
		String directoryIndexValue = configProps.getProperty(KEY_DIRECTORYINDEX);
		if (directoryIndexValue != null) {
			try {
				this.directoryIndex = Boolean.valueOf(directoryIndexValue);
			} catch (Exception ex) {
				throw new IllegalArgumentException("Invalid " + KEY_DIRECTORYINDEX + " setting. (Must be true or false value.:config=" + configPath + ", value=" + directoryIndexValue);
			}
		}
	}
	
	public File getDocumentRoot() {
		return documentRoot;
	}

	public Boolean getDirectoryIndex() {
		return directoryIndex;
	}
}
