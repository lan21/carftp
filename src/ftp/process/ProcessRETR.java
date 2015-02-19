package ftp.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import ftp.FTPClient;
import ftp.answer.AnswerBuilder;

public class ProcessRETR implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		String fullPath = this.getFullPath(param[1], client);
		
		if (Files.exists(Paths.get(fullPath))) {
			File currentFile = new File(fullPath);
			try {
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
				client.getDataSocket().close();
				
				client.setAdditionalAnswer("Transfer done");
				return 250;
				
			} catch (FileNotFoundException e) {
				client.setAdditionalAnswer("File not found or directory");
				try {
					client.getDataSocket().close();
				} catch (IOException e1) {}
				return 550;
			} catch (IOException e) {
				try {
					client.getDataSocket().close();
				} catch (IOException e1) {}
				return 451;
			}
		}

		else {
			client.setAdditionalAnswer("File not found");
			try {
				client.getDataSocket().close();
			} catch (IOException e) {
				return 550;
			}
			return 550;
		}
	}

	/**
	 * This function give the full path of the asked file. If the asked is
	 * relative, the full path is built with the current directory
	 * 
	 * @param file
	 *            the file asked
	 * @param client
	 *            the client of the ftp asking for the path
	 * @return
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
