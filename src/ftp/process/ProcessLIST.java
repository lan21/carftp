package ftp.process;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ftp.FTPClient;
import ftp.answer.AnswerBuilder;
/**
 * this class is used when the client asks for the list of file in the directory
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessLIST implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		File currentDirectory = new File(client.getCurrentDirectory());
		File[] fileList = currentDirectory.listFiles();
		String fileInfo = "";
		if (param.length < 2) {			
			try {
				String partialAnswer = AnswerBuilder.instance.buildAnswer(150,"");
				client.getCommandWriter().writeAnswer(partialAnswer);

				for (File file : fileList) {
					fileInfo = fileInfo+getFileInformation(file);					
				}

			} catch (IOException e) {
				return 426;
			}
		} else {
			File file = new File(client.getCurrentDirectory() + '/' + param[1]);
			try {
				fileInfo = getFileInformation(file);
			} catch (IOException e) {
				return 426;
			}
		}
		System.out.println(fileInfo);
		try {
			if(client.isPassiveMode()){
				client.setDataSocket(client.getDataServerSocket().accept());
			}
			client.getDataWriter().write(fileInfo.getBytes(Charset.forName("UTF-8")));
			client.getDataWriter().flush();
			client.closeDataSocket();
			return 226;
		} catch (IOException e) {
			return 426;
		}
	}
	
	/**
	 * This function return the string describing a file in UNIX ls -l like format
	 * @param file the file to get information
	 * @return string describing the file in UNIX ls -l like format
	 * @throws IOException
	 */
	public String getFileInformation(File file) throws IOException{
		PosixFileAttributes attr = Files.readAttributes(
				Paths.get(file.getAbsolutePath()),
				PosixFileAttributes.class);
		char dir = '-';
		if (file.isDirectory())
			dir = 'd';
		if (Files.isSymbolicLink(file.toPath()))
			dir = 'l';
		String chmod = PosixFilePermissions.toString(Files.getPosixFilePermissions(file.toPath()));
		String owner = attr.owner().getName();
		String group = attr.group().getName();
		long size = file.length();
		String lastModif = new SimpleDateFormat("MMM dd HH:mm",Locale.ENGLISH)
				.format(new Date(file.lastModified()));
		String filename = file.getName();

		String fileInfo = String.format("%c%s 1 %s %s %13d %s %s\015\012",dir,chmod,owner,group,size,lastModif,filename);
		return fileInfo;
		
	}

}
