package ftp.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ftp.FTPClient;
import ftp.answer.AnswerBuilder;
/**
 * This class is used when the client wants to transfer file on the server
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessSTOR implements ProcessCommand {

	@Override
	/**
	 * This method proccess the STOR command
	 * if the client has no write access, it does not store the file
	 * it can be used either in passive or active mode
	 * @param param the parameters of the command
	 * @param client the client who want to store file
	 * @return 250 if everthing works fine
	 * 		   451 if there is error during processing
	 * 		   532 if the client cannot store file
	 */
	public int process(String[] param, FTPClient client) {
		if (client.hasWriteAccess()){
			String fullPath = this.getFullPath(param[1], client);
			File currentFile = new File(fullPath);
			try {
				if(client.isPassiveMode()){
					client.setDataSocket(client.getDataServerSocket().accept());
				}
				
				FileOutputStream fileOutputStream = new FileOutputStream(currentFile);
				
				String partialAnswer = AnswerBuilder.instance.buildAnswer(150,"");
				client.getCommandWriter().writeAnswer(partialAnswer);
				
				int read = client.getDataReader().read();
				
				while (read != -1){
					fileOutputStream.write(read);
					fileOutputStream.flush();
					read = client.getDataReader().read();
				}
				
				fileOutputStream.close();
				client.closeDataSocket();
				
				client.setAdditionalAnswer("Transfer done");
				return 250;
				
			} catch (FileNotFoundException e) {
				try {
					client.getDataSocket().close();
				} catch (IOException e1) {}
				return 451;
			} catch (IOException e) {
				try {
					client.getDataSocket().close();
				} catch (IOException e1) {}
				return 451;
			}
		}
		else{
			client.setAdditionalAnswer("Need account for storing file.");
			return 532;
		}
	}
	
	/**
	 * This function give the full path of the file when it will be stored. If the asked path is an absolute path
	 * it will be rebuilt to match the current directory
	 * 
	 * @param filePath
	 *            the path of the file to store
	 * @param client
	 *            the client of the ftp which want to store the file
	 * @return
	 */
	public String getFullPath(String filePath, FTPClient client) {
		String[] filePathSplitted = filePath.split("/");
		String relativeFilePath = filePathSplitted[filePathSplitted.length-1];
		String fullPath = client.getCurrentDirectory() + '/' + relativeFilePath;
		return fullPath;
	}

}
