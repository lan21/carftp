package ftp.process;

import java.io.File;
import java.io.IOException;

import ftp.FTPClient;
import ftp.answer.AnswerBuilder;

public class ProcessLIST implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		File directory = new File(client.getDirectory());
		File[] fileList = directory.listFiles();
		String result = "";		
		try {
			String partialAnswer = AnswerBuilder.instance.buildAnswer(150,"");
			client.getCommandWriter().writeAnswer(partialAnswer);
			
			for (File f : fileList){
				result = f.getName()+"\r\n";
				client.getDataWriter().writeBytes(result);
				client.getDataWriter().flush();
			}
			
			client.getDataSocket().close();
			return 226;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 226;
	}

}
