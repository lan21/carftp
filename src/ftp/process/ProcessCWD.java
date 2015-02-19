package ftp.process;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import exception.UnauthorizedChangedDirectoryException;
import ftp.FTPClient;

public class ProcessCWD implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		String newCurrentDirectory = this.getNewCurrentDirectory(param[1], client);		
		if(Files.exists(Paths.get(newCurrentDirectory))){
			File currentFile = new File(newCurrentDirectory);
			if (currentFile.isDirectory()){
				try {
					client.setCurrentDirectory(newCurrentDirectory);
				} catch (UnauthorizedChangedDirectoryException e) {}
				client.setAdditionalAnswer("CWD successful "+client.getCurrentDirectory());
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
			newCurrentDirectory = askedPath;
		}		
		else if(askedPath.equals("..")){ /*if we have a relative link and ..*/
			newCurrentDirectory = new File(client.getCurrentDirectory()).getParent();
			if (newCurrentDirectory == null){
				newCurrentDirectory = client.getCurrentDirectory();
			}
		}	
		else{
			newCurrentDirectory = client.getCurrentDirectory()+'/'+askedPath;
		}
		return newCurrentDirectory;

		/*try {
			client.setCurrentDirectory(param[1]);
			client.setAdditionalAnswer(client.getCurrentDirectory());
			return 250;
		} catch (UnauthorizedChangedDirectoryException e) {
			return 553;
		}*/
	}

}
