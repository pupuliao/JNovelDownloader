package JNovelDownloader.Kernel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JTextArea;

import JNovelDownloader.Option.Option;

public class ReadHtml {
	private String[][] fileName;
	public String bookName;
	public String author;
	private int havethread;
//	private BufferedReader reader;
	// private String path;
	private OutputStreamWriter writer;
	private String domain;
	private JTextArea resultTextArea;
	
	public ReadHtml() {
		// TODO Auto-generated constructor stub

	}

	public ReadHtml(int page) {
		fileName = new String[page][];
		havethread = 0;
		bookName = null;
		// path = "";
	}

	public void setUp(int threadNumber, String bookName, String author,UrlData urlData,JTextArea resultTextArea) {
		fileName = new String[threadNumber][];
		this.bookName = bookName;
		this.author = author;
		havethread = 0;
		this.domain=urlData.domain;
		this.resultTextArea=resultTextArea;
	}

	public void setPage(int page) {
		fileName = new String[page][];
		havethread = 0;
	}

//	public void openFile(String name) throws UnsupportedEncodingException {// 輸入完整檔案路徑
//		try {
//			reader = new BufferedReader(new InputStreamReader(
//					new FileInputStream(name), "UTF-8"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
/*這是舊版的*/
//	public boolean makeBook(Option option) throws IOException {// 小說生成 
//		// class="postmessage">
//		// ----></div>
//		StringBuilder tempLineBuilder = null;
//		String temp;
//		boolean inContent = false;
//		int otherTable = 0;
//		writer = new OutputStreamWriter(new FileOutputStream(option.novelPath
//				+ bookName + ".txt"), "UTF-8");
//		writer.write(bookName + "\r\n" + author + "\r\n");
//		Encoding encoding = new Encoding();
//		for (int n = 0; n < havethread; n++) {
//			this.openFile(fileName[n]);
//			System.out.println(fileName[n]);
//			System.out.println(fileName[n] + "處理中");
//			tempLineBuilder = new StringBuilder();
//			while ((temp = reader.readLine()) != null) {
//				// System.out.println(temp);
//
//				if (temp.indexOf("class=\"postmessage\">") >= 0) {// 找出 文章內容
//					inContent = true;
//					String[] temp2 = temp.split("class=\"postmessage\">|<");// 接取標題
//																			// 如果有
//																			// 會有內容，如果沒有是空字串
//					temp = temp2[2];
//					temp += "\r\n";
//				}
//				if (inContent) {
//					if (temp.indexOf("<div ") >= 0) // 避免碰到下一階層
//						otherTable++;
//					if (temp.indexOf("</div>") >= 0) {
//						if (otherTable > 0)
//							otherTable--;
//						else {
//							temp = temp.replace("</div>", " ");
//							inContent = false;
//							temp += "\r\n";
//						}
//					}
//					if (otherTable == 0) {
//						temp = temp.replace("<br />", "\r\n");// 取代換行符號
//						temp = temp.replace("&nbsp;", "");// 取代特殊字元
//						tempLineBuilder.append(temp);
//					}
//
//				}
//
//			}
//			tempLineBuilder.append("\r\n");
//			if (option.encoding) {
//				writer.write(encoding.StoT(tempLineBuilder.toString()));
//			} else {
//				writer.write(encoding.TtoS(tempLineBuilder.toString()));
//			}
//			tempLineBuilder = null;
//			reader.close();
//		}
//		writer.flush();
//		writer.close();
//		System.out.println("小說製作完成");
//		return true;
//	}

	// public void setPath(String data) {
	// path = data;
	// }
	
	public boolean makeBook(Option option)throws IOException {
		writer = new OutputStreamWriter(new FileOutputStream(option.novelPath
				+ bookName + ".txt"), "UTF-8");
		writer.write(bookName + "\r\n" + author + "\r\n");
		resultTextArea.append("\r\n開始分析網頁\r\n");
		resultTextArea.setCaretPosition(resultTextArea.getText()
				.length());
		int type;
		if(domain.indexOf("eyny") >= 0){
			type=1;
		}else type=0;
//		Encoding encoding=new Encoding();
		MakeBookThread [] makeBookThreads =new MakeBookThread[option.threadNumber];
		for (int n=0;n<option.threadNumber;n++){
			makeBookThreads[n]=new MakeBookThread(fileName[n], option.encoding, option.replace,type,resultTextArea);
			makeBookThreads[n].start();
		}
		try {//等全部跑完才繼續
			for(int x=0 ; x<option.threadNumber;x++){
				makeBookThreads[x].join(); 
			}
        } catch (InterruptedException e) {}
        for (int n=0;n<option.threadNumber;n++){
        	System.out.println("小說製作完成");
			writer.write(makeBookThreads[n].getResult());
		}
        writer.flush();
        writer.close();
        System.out.println("小說製作完成");
		return true;
	}

	public void addFileName(String []temp) {// 輸入完整檔案路徑 改成一次一個array
		fileName[havethread++] = temp;
	}

	public void setBookName(String data) {
		bookName = data;
	}

	public void delTempFile() {
		System.out.println("刪除暫存檔中..");
		File temp;
		for (int n = 0; n < havethread; n++) {
			for (int m = 0; m < fileName[n].length; m++) {
				temp = new File(fileName[n][m]);
				if (temp.exists())
					temp.delete();
			}

		}

		System.out.println("刪除完畢");
	}

}
