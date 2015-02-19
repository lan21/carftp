package ftp.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ftp.FTPClient;
import ftp.answer.AnswerBuilder;

public class ProcessSTOR implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		if (client.hasWriteAccess()){
			String fullPath = this.getFullPath(param[1], client);
			File currentFile = new File(fullPath);
			try {
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
				client.getDataSocket().close();
				
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
		else return 532;
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
