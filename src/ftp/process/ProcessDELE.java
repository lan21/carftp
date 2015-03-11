package ftp.process;

import java.io.File;

import ftp.FTPClient;

public class ProcessDELE implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		if(client.hasWriteAccess()){
			String path = client.getCurrentDirectory()+"/"+param[1];
			File file = new File(path);
			if (file.isDirectory()){
				client.setAdditionalAnswer(param[1]+"is a directory");
				return 550;
			}
			if (file.delete()){
				client.setAdditionalAnswer("File "+param[1]+" deleted");
				return 250;
			}
			else {
				client.setAdditionalAnswer("Unable to delete file "+param[1]);
				return 550;
			}
		}
		else{
			client.setAdditionalAnswer("Deleting file is not allowed");
			return 532;
		}
	}

}
