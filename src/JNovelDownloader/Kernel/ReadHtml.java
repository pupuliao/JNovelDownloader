package JNovelDownloader.Kernel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import JNovelDownloader.Option.Option;

public class ReadHtml {
	private String[] fileName;
	public String bookName;
	public String author;
	private int havePage;
	private BufferedReader reader;
	private String path;
	private OutputStreamWriter writer;

	public ReadHtml() {
		// TODO Auto-generated constructor stub
	}

	public ReadHtml(int page) {
		fileName = new String[page];
		havePage = 0;
		bookName = null;
		path = "";
	}
	
	public void setPage(int page){
		fileName = new String[page];
		havePage = 0;
	}

	public void openFile(String name) throws UnsupportedEncodingException {// 輸入完整檔案路徑
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(name), "UTF-8"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean makeBook(Option option) throws IOException {// 小說生成
		// class="postmessage">
		// ----></div>
		StringBuilder tempLineBuilder = null;
		String temp;
		boolean inContent = false;
		int otherTable = 0;
		writer = new OutputStreamWriter(new FileOutputStream(path + bookName
				+ ".txt"), "UTF-8");
		writer.write(bookName+"\r\n"+author+"\r\n");
		for (int n = 0; n < havePage; n++) {
			this.openFile(fileName[n]);
			System.out.println(fileName[n]);
			System.out.println(fileName[n]+"處理中");
			tempLineBuilder = new StringBuilder();
			while ((temp = reader.readLine()) != null) {
				// System.out.println(temp);

				if (temp.indexOf("class=\"postmessage\">") >= 0) {// 找出 文章內容
					inContent = true;
					String[] temp2 = temp.split("class=\"postmessage\">|<");// 接取標題
																			// 如果有
																			// 會有內容，如果沒有是空字串
					temp = temp2[2];
				}
				if (inContent) {
					if (temp.indexOf("<div ") >= 0) // 避免碰到下一階層
						otherTable++;
					if (temp.indexOf("</div>") >= 0) {
						if (otherTable > 0)
							otherTable--;
						else {
							temp = temp.replace("</div>", " ");
							inContent = false;
						}
					}
					if (otherTable == 0) {
						temp = temp.replace("<br />", "\r\n");// 取代換行符號
						temp = temp.replace("&nbsp;", "");// 取代特殊字元
						tempLineBuilder.append(temp);
					}

				}

			}
			tempLineBuilder.append("\r\n");
			writer.write(tempLineBuilder.toString());
			tempLineBuilder = null;
			reader.close();
		}
		writer.flush();
		writer.close();
		System.out.println("小說製作完成");
		return true;
	}

	public void setPath(String data) {
		path = data;
	}

	public void addFileName(String data) {// 輸入完整檔案路徑
		fileName[havePage++] = data;

	}

	public void setBookName(String data) {
		bookName = data;
	}
	
	public void delTempFile(){
		System.out.println("刪除暫存檔中..");
		File temp ;
		for (int n = 0; n < havePage; n++) {
			temp = new File(fileName[n]);
			if(temp.exists()) temp.delete();
		}
		
		System.out.println("刪除完畢");
	}

}
