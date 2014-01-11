import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JDir {

  public static String listFiles(File parent) {
	  StringBuilder sb = new StringBuilder();
	  sb.append("               " + parent.getAbsoluteFile() + " のディレクトリ\n");
	  
	  File[] listFiles = parent.listFiles();
	  DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  int totalSize = 0;
	  int fileCounts = 0;
	  for (int i=0; i<listFiles.length; i++) {
		  File f = listFiles[i];
		  if (!f.isDirectory()) {
			  totalSize += f.length();
			  ++fileCounts;
		  }
		  sb.append(df.format(new Date(f.lastModified())) + "    " + (f.isDirectory() ? "<DIR>": "     ") + (f.isDirectory() ? "     ": String.valueOf(f.length())) + "     " + f.getName() + "\n");
	  }
	  sb.append("                    " + String.valueOf(fileCounts) + "個のファイル   " + String.valueOf(totalSize) + "バイト\n");
	  sb.append("                    " + String.valueOf(listFiles.length-fileCounts) + "個のディレクトリ\n");
	  return sb.toString();
  }
}