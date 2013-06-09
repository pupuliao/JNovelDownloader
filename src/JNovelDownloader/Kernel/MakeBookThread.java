package JNovelDownloader.Kernel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

public class MakeBookThread extends Thread {
	private String[] html;
	private StringBuilder bookData;
	private BufferedReader reader;
	private boolean encoding;
	private String result;
	private int type;
	private JTextArea resultTextArea;
	public MakeBookThread(String[] data, boolean encoding,int type,JTextArea resultTextArea) {
		html = data;
		bookData = new StringBuilder();
		this.encoding = encoding;
		this.type=type;
		this.resultTextArea=resultTextArea;
	}

	public void run() {
//		boolean inContent = false;
		int stage = 0; // 0=不再內文中 ,1=在<div class="pbody"> 中,
						// 2=在<div class="mes">中 ,
						// 3=<div id="postmessage_~~~~" class="mes">中 ,
		String temp;
		int otherTable = 0;
		/* 用於正規表示式的過濾，比replace all 快速準確 */
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html;
		// String regEx_html = "<[^>]+>";

		for (int n = 0; n < html.length; n++) {
			try {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(html[n]), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // 開啟檔案
			System.out.println(html[n] + "處理中");
			resultTextArea.append(html[n] + "處理中\r\n");
			resultTextArea.setCaretPosition(resultTextArea.getText()
					.length());
			try {
				while ((temp = reader.readLine()) != null) { // 一次讀取一行
					switch (stage) {
					case 0:
						if(temp.indexOf("<div class=\"pbody\">")>=0){
							stage=1;
						}
						break;
					case 1:
						if(temp.indexOf("<h")>=0){//出現標題
							temp = Replace.replace(temp, " ", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");							
							bookData.append(temp);
							bookData.append("\r\n");
						}
						if(temp.indexOf("<div class=\"mes\">")>=0){
							stage=2;
						}
						break;
					case 2:
						if (temp.indexOf("class=\"postmessage\">") >= 0){// 找出 文章內容
							stage=3;
	//						String[] temp2 = temp.split("class=\"postmessage\">");// 接取標題
					//		if(temp2.length<=0) 
	//						temp = temp2[1];
							if(temp.indexOf("<i class=\"pstatus\">")>=0){ //過慮修改時間
								temp=temp.replaceAll("<i class=\"pstatus\">[^<>]+ </i>","");
							}
							if(temp.indexOf("<div class=\"quote\">")>=0){ //過濾 引用
								otherTable++;
								temp=temp.replaceAll("<font color=\"#999999\">[^<>]+</font>","");
							}
							temp += "\r\n";
							temp = Replace.replace(temp, "<br/>", "\r\n");
							temp = Replace.replace(temp, "<br />", "\r\n");
							temp = Replace.replace(temp, "&nbsp;", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");
							bookData.append(temp);
							// 如果有
							// 會有內容，如果沒有是空字串
						}
						break;
					case 3: 
						if (temp.indexOf("<div ") >= 0) // 避免碰到下一階層
							otherTable++;
						if (temp.indexOf("</div>") >= 0) { 
							if (otherTable > 0)//從底層離開
								otherTable--;
							else {//偵測是否離開了
								temp = temp.replace("</div>", " ");
								stage=0;
								temp += "\r\n";
							}
						}
						if (otherTable == 0) {
							// 去掉<strong>//|<[/]?strong>|<[/]?b>|<[/]?a[^>]*>)
							temp = Replace.replace(temp, "<br/>", "\r\n");
							temp = Replace.replace(temp, "<br />", "\r\n");
							temp = Replace.replace(temp, "&nbsp;", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");
							bookData.append(temp);
						}

						break;
					default:
						break;
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bookData.append("\r\n");
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Encoding encoding = new Encoding();
		if (this.encoding) {
			result = encoding.StoT(bookData.toString());
		} else {
			result = encoding.TtoS(bookData.toString());
		}
	}
	
	private void runType1(){
		//PC版 給伊莉用
//		boolean inContent = false;
		int stage = 2; // 0=不再內文中 ,1=在<div class="pbody"> 中,
						// 2=在<div class="mes">中 ,
						// 3=<div id="postmessage_~~~~" class="mes">中 ,
		String temp;
		int otherTable = 0;
		/* 用於正規表示式的過濾，比replace all 快速準確 */
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html;
		// String regEx_html = "<[^>]+>";

		for (int n = 0; n < html.length; n++) {
			try {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(html[n]), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // 開啟檔案
			System.out.println(html[n] + "處理中");
			try {
				while ((temp = reader.readLine()) != null) { // 一次讀取一行
					switch (stage) {
					case 0:
						if(temp.indexOf("<div class=\"pbody\">")>=0){
							stage=1;
						}
						break;
					case 1:
						if(temp.indexOf("<h")>=0){//出現標題
							temp = Replace.replace(temp, " ", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");							
							bookData.append(temp);
							bookData.append("\r\n");
						}
						if(temp.indexOf("<div class=\"mes\">")>=0){
							stage=2;
						}
						break;
					case 2:
						if (temp.indexOf("class=\"postmessage\">") >= 0){// 找出 文章內容
							stage=3;
	//						String[] temp2 = temp.split("class=\"postmessage\">");// 接取標題
					//		if(temp2.length<=0) 
	//						temp = temp2[1];
							if(temp.indexOf("<i class=\"pstatus\">")>=0){ //過慮修改時間
								temp=temp.replaceAll("<i class=\"pstatus\">[^<>]+ </i>","");
							}
							if(temp.indexOf("<div class=\"quote\">")>=0){ //過濾 引用
								otherTable++;
								temp=temp.replaceAll("<font color=\"#999999\">[^<>]+</font>","");
							}
							temp += "\r\n";
							temp = Replace.replace(temp, "<br/>", "\r\n");
							temp = Replace.replace(temp, "<br />", "\r\n");
							temp = Replace.replace(temp, "&nbsp;", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");
							bookData.append(temp);
							// 如果有
							// 會有內容，如果沒有是空字串
						}
						break;
					case 3: 
						if (temp.indexOf("<div ") >= 0) // 避免碰到下一階層
							otherTable++;
						if (temp.indexOf("</div>") >= 0) { 
							if (otherTable > 0)//從底層離開
								otherTable--;
							else {//偵測是否離開了
								temp = temp.replace("</div>", " ");
								stage=0;
								temp += "\r\n";
							}
						}
						if (otherTable == 0) {
							// 去掉<strong>//|<[/]?strong>|<[/]?b>|<[/]?a[^>]*>)
							temp = Replace.replace(temp, "<br/>", "\r\n");
							temp = Replace.replace(temp, "<br />", "\r\n");
							temp = Replace.replace(temp, "&nbsp;", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");
							bookData.append(temp);
						}

						break;
					default:
						break;
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bookData.append("\r\n");
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Encoding encoding = new Encoding();
		if (this.encoding) {
			result = encoding.StoT(bookData.toString());
		} else {
			result = encoding.TtoS(bookData.toString());
		}
	}
	private void runType0(){
		//手機版 給卡提諾用
//		boolean inContent = false;
		int stage = 0; // 0=不再內文中 ,1=在<div class="pbody"> 中,
						// 2=在<div class="mes">中 ,
						// 3=<div id="postmessage_~~~~" class="mes">中 ,
		String temp;
		int otherTable = 0;
		/* 用於正規表示式的過濾，比replace all 快速準確 */
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html;
		// String regEx_html = "<[^>]+>";

		for (int n = 0; n < html.length; n++) {
			try {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(html[n]), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // 開啟檔案
			System.out.println(html[n] + "處理中");
			try {
				while ((temp = reader.readLine()) != null) { // 一次讀取一行
					switch (stage) {
					case 0:
						if(temp.indexOf("<div class=\"pbody\">")>=0){
							stage=1;
						}
						break;
					case 1:
						if(temp.indexOf("<h")>=0){//出現標題
							temp = Replace.replace(temp, " ", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");							
							bookData.append(temp);
							bookData.append("\r\n");
						}
						if(temp.indexOf("<div class=\"mes\">")>=0){
							stage=2;
						}
						break;
					case 2:
						if (temp.indexOf("class=\"postmessage\">") >= 0){// 找出 文章內容
							stage=3;
	//						String[] temp2 = temp.split("class=\"postmessage\">");// 接取標題
					//		if(temp2.length<=0) 
	//						temp = temp2[1];
							if(temp.indexOf("<i class=\"pstatus\">")>=0){ //過慮修改時間
								temp=temp.replaceAll("<i class=\"pstatus\">[^<>]+ </i>","");
							}
							if(temp.indexOf("<div class=\"quote\">")>=0){ //過濾 引用
								otherTable++;
								temp=temp.replaceAll("<font color=\"#999999\">[^<>]+</font>","");
							}
							temp += "\r\n";
							temp = Replace.replace(temp, "<br/>", "\r\n");
							temp = Replace.replace(temp, "<br />", "\r\n");
							temp = Replace.replace(temp, "&nbsp;", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");
							bookData.append(temp);
							// 如果有
							// 會有內容，如果沒有是空字串
						}
						break;
					case 3: 
						if (temp.indexOf("<div ") >= 0) // 避免碰到下一階層
							otherTable++;
						if (temp.indexOf("</div>") >= 0) { 
							if (otherTable > 0)//從底層離開
								otherTable--;
							else {//偵測是否離開了
								temp = temp.replace("</div>", " ");
								stage=0;
								temp += "\r\n";
							}
						}
						if (otherTable == 0) {
							// 去掉<strong>//|<[/]?strong>|<[/]?b>|<[/]?a[^>]*>)
							temp = Replace.replace(temp, "<br/>", "\r\n");
							temp = Replace.replace(temp, "<br />", "\r\n");
							temp = Replace.replace(temp, "&nbsp;", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");
							bookData.append(temp);
						}

						break;
					default:
						break;
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bookData.append("\r\n");
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Encoding encoding = new Encoding();
		if (this.encoding) {
			result = encoding.StoT(bookData.toString());
		} else {
			result = encoding.TtoS(bookData.toString());
		}
	}
	
	public String getResult() {
		return result;
	}

}
