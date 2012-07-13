package JNovelDownloader.Option;

import java.io.*;

import javax.swing.JTextArea;

public class Option {

	public String tempPath;// 暫存區
	public boolean encoding;// 1-繁體 0=簡體
	public boolean replace;// 是否要做替換
	public String novelPath;// 小說存放位置
	private File file;
	public int threadNumber;

	public Option() throws IOException {
		file = new File("option.ini");
		File tempFile;
		String temp;
		String []temp2;
		if (ifNoSetUp()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			temp=reader.readLine();
			temp2=temp.split("-");
			tempPath=temp2[1];
			temp=reader.readLine();
			temp2=temp.split("-");
			novelPath=temp2[1];
			temp=reader.readLine();
			temp2=temp.split("-");
			encoding=Boolean.valueOf(temp2[1]);
			temp=reader.readLine();
			temp2=temp.split("-");
			replace=Boolean.valueOf(temp2[1]);
			if((temp=reader.readLine())!=null){
				temp2=temp.split("-");
				threadNumber=Integer.parseInt(temp2[1]);
			}
			else threadNumber=4;
			reader.close();
			/**檢查檔案路徑是否存在***/
			tempFile=new File(tempPath);
			if(!tempFile.exists()){
				tempFile.mkdir();
			}
			tempFile= new File(novelPath);
			if(!tempFile.exists()){
				tempFile.mkdir();
			}
		} else {
			setUp();
			creatOptionfile();
		}

	}

	private void setUp() {//設定初始化
		File temp =new File("");
		tempPath=temp.getAbsolutePath()+"\\temp\\";
		novelPath=temp.getAbsolutePath()+"\\down\\";
		encoding = true; //預設繁體
		replace = false; //預設不處理
		threadNumber=4;
		temp=new File(tempPath);
		temp.mkdir();
		temp=new File(novelPath);
		temp.mkdir();
		
	}

	private boolean ifNoSetUp() {
		return file.exists();
	}
	
	private void creatOptionfile() throws IOException{
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		writer.write("tempPath-"+tempPath+"\r\n");
		writer.write("novelPath-"+novelPath+"\r\n");
		writer.write("encoding-"+String.valueOf(encoding)+"\r\n");
		writer.write("replace-"+String.valueOf(replace)+"\r\n");
		writer.write("threadNumber-"+String.valueOf(threadNumber)+"\r\n");
		writer.flush();
		writer.close();
	}
	
	public void saveOption() throws IOException{
		file.delete();
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		writer.write("tempPath-"+tempPath+"\r\n");
		writer.write("novelPath-"+novelPath+"\r\n");
		writer.write("encoding-"+String.valueOf(encoding)+"\r\n");
		writer.write("replace-"+String.valueOf(replace)+"\r\n");
		writer.write("threadNumber-"+String.valueOf(threadNumber)+"\r\n");
		writer.flush();
		writer.close();
		
	}
	
	public void printOption(JTextArea resultTextArea){
		resultTextArea.append("暫存檔位置：" + tempPath + "\r\n");
		resultTextArea.append("小說存放位置：" + novelPath + "\r\n");
		if(encoding) resultTextArea.append("正體中文\r\n");
		else resultTextArea.append("簡體中文\r\n");
		resultTextArea.append("多執行序數目："+threadNumber+"\r\n");
		resultTextArea.setCaretPosition(resultTextArea.getDocument().getLength());
		
	}
	
	//private void creatOption

}
