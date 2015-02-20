package ftp.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import ftp.FTPClient;
import ftp.answer.AnswerBuilder;

/**
 * This class is used when the client want to get a file from the server
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessRETR implements ProcessCommand {

	@Override
	/**
	 * This method proccess the RETR command
	 * it can be used either in passive or active mode
	 * @param param the parameters of the command
	 * @param client the client who want to store file
	 * @return 226 if everything works fine
	 * 		   451 if there is error during processing
	 * 		   550 if the client cannot store file
	 * 		   426 if the connection is cut
	 */
	public int process(String[] param, FTPClient client) {
		String fullPath = this.getFullPath(param[1], client);
		
		if (Files.exists(Paths.get(fullPath))) {
			File currentFile = new File(fullPath);
			try {
				if(client.isPassiveMode()){
					client.setDataSocket(client.getDataServerSocket().accept());
				}
				FileInputStream fileInputStream = new FileInputStream(currentFile);
				
				String partialAnswer = AnswerBuilder.instance.buildAnswer(150,"");
				client.getCommandWriter().writeAnswer(partialAnswer);
				
				int read = fileInputStream.read();
				while (read != -1){					
					client.getDataWriter().write(read);
					client.getDataWriter().flush();
					read = fileInputStream.read();
				}
				
				fileInputStream.close();
				client.closeDataSocket();;
				
				return 226;
				
			} catch (FileNotFoundException e) {
				client.setAdditionalAnswer("File not found or directory");
				try {
					client.closeDataSocket();;
				} catch (IOException e1) {
					return 426;
				}
				return 550;
			} catch (IOException e) {
				try {
					client.closeDataSocket();;
				} catch (IOException e1) {
					return 426;
				}
				return 451;
			}
		}

		else {
			client.setAdditionalAnswer("File not found");
			try {
				client.closeDataSocket();
			} catch (IOException e) {
				return 426;
			}
			return 550;
		}
	}

	/**
	 * This function give the full path of the asked file. If the asked is
	 * relative, the full path is built with the current directory
	 * 
	 * @param askedPaht
	 *            the file asked
	 * @param client
	 *            the client of the ftp asking for the path
	 * @return a string representing the full path
	 */
	public String getFullPath(String askedPath, FTPClient client) {
		String newFullPath;
		if (askedPath.startsWith("/")) { /* if we have absolute link */
			newFullPath = askedPath;
		} else { /* if we have absolute link */
			newFullPath = client.getCurrentDirectory() + '/' + askedPath;
		}
		return newFullPath;
	}
}
