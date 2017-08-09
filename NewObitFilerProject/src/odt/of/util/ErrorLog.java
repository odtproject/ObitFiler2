package odt.of.util;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class ErrorLog
{
	public ErrorLog(Exception paramException, String paramString)
	{
		String rptPathString = DirectorySetup.getRptFilesPathString();
		Toolkit.getDefaultToolkit().beep();
		Date localDate = new Date();
		try
		{
			System.err.println("Error occurred " + localDate);
			System.err.println(paramString);
			if (paramException != null) {
				System.err.println(paramException.getMessage());
				paramException.printStackTrace();
			}
			System.err.println("----------------------------");
			
			Path rptFilePath = Paths.get(rptPathString, "error.log");
			PrintWriter localPrintWriter = new PrintWriter(new FileOutputStream(rptFilePath.toString(), true));
			localPrintWriter.println("Error occurred " + localDate);
			localPrintWriter.println(paramString);
			if (paramException != null) {
				localPrintWriter.println(paramException.getMessage());
			}
			localPrintWriter.println("----------------------------");
			localPrintWriter.close();
			return;
		}
		catch (IOException localIOException)
		{
			System.err.println("Error writing to error.log: " + localIOException.getMessage());
		}
		catch ( InvalidPathException ip ) {
			System.err.println("Error writing to error.log: " + ip.getMessage());
		}
	}
}
