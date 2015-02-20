package ftp.process;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import exception.UnauthorizedChangedDirectoryException;
import ftp.FTPClient;

/**
 * Class ProcessCWD
 * Allow to change directory
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessCWD implements ProcessCommand {

	@Override
	/**
	 * This method proccess the CWD command
	 * if the client has no write access, it does not store the file
	 * it can be used either in passive or active mode
	 * @param param the parameters of the command
	 * @param client the client who who execute the command
	 * @return 250 if everthing works fine
	 * 		   550 if the directory can't be found or is inaccessible
	 */
	public int process(String[] param, FTPClient client) {
		String newCurrentDirectory = this.getNewCurrentDirectory(param[1], client);
		String newApparentDirectory = this.getNewApparentDirectory(newCurrentDirectory, client);	
		if(Files.exists(Paths.get(newCurrentDirectory))){
			File currentFile = new File(newCurrentDirectory);
			if (currentFile.isDirectory()){
				try {
					client.setCurrentDirectory(newCurrentDirectory);
					client.setApparentDirectory(newApparentDirectory);
				} catch (UnauthorizedChangedDirectoryException e) {
					client.setAdditionalAnswer("Access denied");
					return 550;
				}
				client.setAdditionalAnswer("CWD command successful.");
				return 250;
			}
			else{
				client.setAdditionalAnswer("Requested path is not a directory");
				return 550;
			}
		}
		else{
			client.setAdditionalAnswer("Folder not found");
			return 550;
		}
	}
	
	/**
	 * This function give the full path of the asked path.
	 * If the asked is relative, the full path is built with the current directory
	 * If the relative path asked is ".." and the current directory is '/', it return '/'
	 * @param askedPath the path asked
	 * @param client the client of the ftp asking for the path
	 * @return
	 */
	public String getNewCurrentDirectory(String askedPath,FTPClient client){
		String newCurrentDirectory;
		if(askedPath.startsWith("/")){ /*if we have absolute link*/
			newCurrentDirectory = client.getDirectory()+askedPath;
		}		
		else if(askedPath.equals("..")){ /*if we have a relative link and ..*/
			newCurrentDirectory = new File(client.getCurrentDirectory()).getParent();
			if (newCurrentDirectory == null){
				newCurrentDirectory = client.getCurrentDirectory();
			}
		}	
		else{
			newCurrentDirectory = client.getCurrentDirectory()+"/"+askedPath;
		}
		return newCurrentDirectory;
	}
	
	/**
	 * This function give the apparent path with the help of the full path
	 * If the initial folder path of the client is equivalent new current directory path, the apparent path is "/"
	 * If not, the apparent path is the new current directory path minus the initial folder path of the client and beginning with "/"
	 * @param newCurrentDirectory the path asked
	 * @param client the client of the ftp asking for the path
	 * @return
	 */
	public String getNewApparentDirectory(String newCurrentDirectory,FTPClient client){
		try{
			String newApparentDirectory = newCurrentDirectory.substring(client.getDirectory().length());
			if(newApparentDirectory.equals(""))
				return "/";
			return newApparentDirectory;
		} catch(StringIndexOutOfBoundsException e){
			return "/";
		}
		
	}

}
