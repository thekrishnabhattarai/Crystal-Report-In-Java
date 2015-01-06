
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.lib.*;
import com.crystaldecisions.sdk.occa.report.exportoptions.*;
import java.io.*;
import javax.servlet.http.HttpServletResponse;

public class CRConnect{

final String REPORT_NAME = "reports/myreport.rpt";
 
final String EXPORT_FILE = "myExportedReport.pdf";
final String EXPORT_LOC = "C:\\";


public void printReport(){
try {
	
	//Open report.
	ReportClientDocument reportClientDoc = new ReportClientDocument();
	reportClientDoc.open(REPORT_NAME, 0);

	//NOTE: If parameters or database login credentials are required, they need to be set before.
	//calling the export() method of the PrintOutputController.
			
	//Export report and obtain an input stream that can be written to disk.
	//See the Java Reporting Component Developer's Guide for more information on the supported export format enumerations
	//possible with the JRC.
	ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream)reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
			
	//Release report.
	reportClientDoc.close();
	
	//These utility methods below demonstrate how to use the Java I/O libraries to write the input stream content
	//directly to the browser, or the server's file system.  Note: We are now working with APIs completely outside of
	//Crystal at this point:  						
	//writeToBrowser(byteArrayInputStream, response, "application/pdf", EXPORT_FILE);
	
	//Write file to disk...
	//String EXPORT_OUTPUT = EXPORT_LOC + EXPORT_FILE
	//out.println("Exporting to " + EXPORT_OUTPUT);
	//writeToFileSystem(byteArrayInputStream, EXPORT_OUTPUT);
		
}
catch(ReportSDKException ex) {	
	   System.out.println(ex);
}
catch(Exception ex) {
	  System.out.println(ex);			
}
}


   /*
	* Utility method that demonstrates how to write an input stream to the server's local file system.  
	*/
	private void writeToFileSystem(ByteArrayInputStream byteArrayInputStream, String exportFile) throws Exception {
	
		//Use the Java I/O libraries to write the exported content to the file system.
		byte byteArray[] = new byte[byteArrayInputStream.available()];

		//Create a new file that will contain the exported result.
		File file = new File(exportFile);
		FileOutputStream fileOutputStream = new FileOutputStream(file);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteArrayInputStream.available());
		int x = byteArrayInputStream.read(byteArray, 0, byteArrayInputStream.available());

		byteArrayOutputStream.write(byteArray, 0, x);
		byteArrayOutputStream.writeTo(fileOutputStream);

		//Close streams.
		byteArrayInputStream.close();
		byteArrayOutputStream.close();
		fileOutputStream.close();
		
	}

   /*
	* Utility method that demonstrates how to write an input stream to the server's local file system.  
	*/
	private void writeToBrowser(ByteArrayInputStream byteArrayInputStream, HttpServletResponse response, String mimetype, String exportFile) throws Exception {
	
		//Create a byte[] the same size as the exported ByteArrayInputStream.
		byte[] buffer = new byte[byteArrayInputStream.available()];
		int bytesRead = 0;
		
		//Set response headers to indicate mime type and inline file.
		response.reset();
		response.setHeader("Content-disposition", "inline;filename=" + exportFile);
		response.setContentType(mimetype);
		
		//Stream the byte array to the client.
		while((bytesRead = byteArrayInputStream.read(buffer)) != -1) {
			response.getOutputStream().write(buffer, 0, bytesRead);	
		}
		
		//Flush and close the output stream.
		response.getOutputStream().flush();
		response.getOutputStream().close();
		
	}
        
}