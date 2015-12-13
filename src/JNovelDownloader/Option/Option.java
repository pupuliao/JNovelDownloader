package JNovelDownloader.Option;

import java.io.*;

import javax.swing.JTextArea;

public class Option {

	public String tempPath;// 暫存區
	public boolean encoding;// 1-繁體 0=簡體
	public boolean replace;// 是否要做替換
	public String novelPath;// 小說存放位置
	public String outputEncode; // Unicode, UTF-8
	private File file;
	public int threadNumber;

	public Option() throws IOException {
		file = new File("option.ini");
		File tempFile;
		String temp;
		String []temp2;
		if (ifNoSetUp()) {
			try {
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
				temp=reader.readLine();
				temp2=temp.split("_");
				outputEncode=temp2[1];
				reader.close();				
			} catch(Exception e) {
				setUp();
				creatOptionfile();
			}

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
		String fileSeparator=System.getProperty("file.separator");
		tempPath=temp.getAbsolutePath()+fileSeparator+"temp"+fileSeparator;
		novelPath=temp.getAbsolutePath()+fileSeparator+"down"+fileSeparator;
		encoding = true; //預設繁體
		replace = false; //預設不處理
		outputEncode = "UTF-8"; //預設 Unicode
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
		String lineSeparator=System.getProperty("line.separator");
		writer.write("tempPath-"+tempPath+lineSeparator);
		writer.write("novelPath-"+novelPath+lineSeparator);
		writer.write("encoding-"+String.valueOf(encoding)+lineSeparator);
		writer.write("replace-"+String.valueOf(replace)+lineSeparator);
		writer.write("threadNumber-"+String.valueOf(threadNumber)+lineSeparator);
		writer.write("outputEncode_"+outputEncode+lineSeparator);
		writer.flush();
		writer.close();
	}
	
	public void saveOption() throws IOException{
		file.delete();
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		String lineSeparator=System.getProperty("line.separator");
		writer.write("tempPath-"+tempPath+lineSeparator);
		writer.write("novelPath-"+novelPath+lineSeparator);
		writer.write("encoding-"+String.valueOf(encoding)+lineSeparator);
		writer.write("replace-"+String.valueOf(replace)+lineSeparator);
		writer.write("threadNumber-"+String.valueOf(threadNumber)+lineSeparator);
		writer.write("outputEncode_"+outputEncode+lineSeparator);
		writer.flush();
		writer.close();
		/**檢查檔案路徑是否存在***/
		File tempFile=new File(tempPath);
		if(!tempFile.exists()){
			tempFile.mkdir();
		}
		tempFile= new File(novelPath);
		if(!tempFile.exists()){
			tempFile.mkdir();
		}
	}
	
	public void printOption(JTextArea resultTextArea){
		String lineSeparator=System.getProperty("line.separator");
		resultTextArea.append("暫存檔位置：" + tempPath + lineSeparator);
		resultTextArea.append("小說存放位置：" + novelPath + lineSeparator);
		if(encoding) resultTextArea.append("正體中文"+lineSeparator);
		else resultTextArea.append("簡體中文"+lineSeparator);
		resultTextArea.append("多執行序數目："+threadNumber+lineSeparator);
		resultTextArea.append("輸出編碼："+outputEncode+lineSeparator);
		resultTextArea.setCaretPosition(resultTextArea.getDocument().getLength());
		
	}
	
	public boolean checkPath(){
		boolean result =true;
		String fileSeparator=System.getProperty("file.separator");
		File file =new File(this.tempPath);
		if(!file.exists()){
			File temp1 =new File("");
			this.tempPath=temp1.getAbsolutePath()+fileSeparator+"temp"+fileSeparator;
			result =false;
		}
		file =new File(this.novelPath);
		if(!file.exists()){
		//	File temp1 =new File("");
		//	this.tempPath=temp1.getAbsolutePath()+"\\temp\\";
			result =false;
		}

		return result;
	}
	
	//private void creatOption

}
