package odt.of.util;


import java.nio.file.Path;
import java.nio.file.Paths;


/* This class contains all sorts of wonderful things about the ODT
 * directory structure.  There are methods to set it up and get folder
 * information.
 */
public class DirectorySetup {
	
	private static String odtFilePathString;
	private static String libFilesPathString;
	private static String appFilesPathString;
	private static String rptFilesPathString;
	private boolean setupComplete;	
		
	public DirectorySetup () {
		
		setupComplete = false;
		
		Path odtFilePath;
		Path libFilesPath;
		Path appFilesPath;
		Path rptFilesPath;
		
		try {
			/* Get the path for ODT files */
			odtFilePath = Paths.get(System.getProperty("user.home"),"ODT");
			if ( ! odtFilePath.toFile().isDirectory() ) {
				/* try to make the folder */
				odtFilePath.toFile().mkdir();
				
				if ( ! odtFilePath.toFile().isDirectory() ) {
					/* We can't use the folder we're used to, so default to the user's
					 * current folder.
					 */
					System.err.println("Unable to use ODT path: " + odtFilePath.toString());
					System.err.println("Cannot proceed");
					return;
				}
			}
			odtFilePathString = odtFilePath.toString();
			
			/* Set up library files path */
			libFilesPath = Paths.get(odtFilePath.toString(), "lib");
			if ( ! libFilesPath.toFile().isDirectory() ) {
				
				/* try to make the folder */
				libFilesPath.toFile().mkdir();
				
				if ( ! libFilesPath.toFile().isDirectory() ) {			
					/* We can't work without the library files path.
					 * We need the files in that folder.				
					 */
					System.err.println("Unable to find ODT library path: " + libFilesPath.toString());
					System.err.println("Cannot proceed");
					return;
				}
			}
			libFilesPathString = libFilesPath.toString();
			
			/* Set up library files path */
			appFilesPath = Paths.get(odtFilePath.toString(), "appdata");
			if ( ! appFilesPath.toFile().isDirectory() ) {
				
				/* try to make the folder */
				appFilesPath.toFile().mkdir();
				
				if ( ! appFilesPath.toFile().isDirectory() ) {			
					/* We can't work without the library files path.
					 * We need the files in that folder.				
					 */
					System.err.println("Unable to find appdata path: " + appFilesPath.toString());
					System.err.println("Cannot proceed");
					return;
				}
			}
			appFilesPathString = appFilesPath.toString();
			
			/* Set up library files path */
			rptFilesPath = Paths.get(odtFilePath.toString(), "Reports");
			if ( ! rptFilesPath.toFile().isDirectory() ) {
				
				/* try to make the folder */
				rptFilesPath.toFile().mkdir();
				
				if ( ! rptFilesPath.toFile().isDirectory() ) {			
					/* We can't work without the library files path.
					 * We need the files in that folder.				
					 */
					System.err.println("Unable to find Reports path: " + rptFilesPath.toString());
					System.err.println("Cannot proceed");
					return;
				}
			}
			rptFilesPathString = rptFilesPath.toString();

			setupComplete = true;

		} catch ( Exception e ) {
			System.err.println(e.getMessage());
		}
		
		return;
	
	} /* end of DirectorySetup constructor */
	
	/* Get the requested sub-folder under the ODT main path */
	public static String getODTSubFolderString ( String subFolderName ) {
		
		String folderPathString;
		try {
			Path folderPath = Paths.get(odtFilePathString, subFolderName);
			if ( ! folderPath.toFile().isDirectory() ) {
				/* try to make the folder */
				folderPath.toFile().mkdir();
				
				if ( ! folderPath.toFile().isDirectory() ) {
					/* We can't use the folder we're used to, so default to the user's
					 * current folder.
					 */
					System.err.println("Unable to use ODT subfolder: " + folderPath.toString());
					return("Invalid");
				}				
			}
			
			folderPathString = folderPath.toString();
			return (folderPathString);
			
		} catch ( Exception e ) {
		System.err.println(e.getMessage() );
		return("Invalid");
		}
		
	} /* end of getODTSubFolderString */	
	
	/* Get the requested sub-folder under the ODT main path */
	public String getLibFileString ( String fileName ) {
		
		String filePathString;
		try {
			Path filePath = Paths.get(libFilesPathString, fileName);
			if ( ! filePath.toFile().exists() ) {
					System.err.println("Lib file not found: " + filePath.toString());
					return("Invalid");
			}
			
			filePathString = filePath.toString();
			
			return (filePathString);

		} catch ( Exception e ) {
			System.err.println(e.getMessage() );
			return("Invalid");
		}
		
	} /* end of getODTSubFolderString */	
	
	/* Get the requested sub-folder under the ODT main path */
	public static String getAppSubFolderString ( String subFolderName ) {
		
		String folderPathString;
		try {
			
			Path folderPath = Paths.get(getAppFilesPathString(), subFolderName);
			if ( ! folderPath.toFile().isDirectory() ) {
				/* try to make the folder */
				folderPath.toFile().mkdir();
				
				if ( ! folderPath.toFile().isDirectory() ) {
					/* We can't use the folder */
					System.err.println("Unable to use appdata subfolder: " + folderPath.toString());
					return("Invalid");
				}
			}
			folderPathString = folderPath.toString();
			
			return (folderPathString);

		} catch ( Exception e ) {
			System.err.println(e.getMessage() );
			return("Invalid");
		}		
		
	} /* end of getAppSubFolderString */	

	/* Get the requested sub-folder under the ODT main path */
	public static String getAppFileString ( String fileName ) {
		
		String filePathString;
		try {
			
			Path filePath = Paths.get(getAppFilesPathString(), fileName);
			filePathString = filePath.toString();
			
			return (filePathString);

		} catch ( Exception e ) {
			System.err.println(e.getMessage() );
			return("Invalid");
		}		
		
	} /* end of getAppFileString */	

	public String getOdtFilePathString() {
		return odtFilePathString;
	}

	public String getLibFilesPathString() {
		return libFilesPathString;
	}

	public static String getAppFilesPathString() {
		return appFilesPathString;
	}

	public boolean isSetupComplete() {
		return setupComplete;
	}

	public static String getRptFilesPathString() {
		return rptFilesPathString;
	}
		
} /* end of class DirectorySetup */
