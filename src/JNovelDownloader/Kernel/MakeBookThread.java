package JNovelDownloader.Kernel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakeBookThread extends Thread {
	private String[] html;
	private StringBuilder bookData;
	private BufferedReader reader;
	private boolean encoding;
	private String result;

	public MakeBookThread(String[] data,boolean encoding) {
		html = data;
		bookData = new StringBuilder();
		this.encoding=encoding;
	}

	public void run() {
		boolean inContent = false;
		String temp ;
		int otherTable=0;
		/*用於正規表示式的過濾，比replace all 快速準確*/
		Pattern p_html= Pattern.compile("<[^>]+>",Pattern.CASE_INSENSITIVE);
        Matcher m_html; 			
//        String regEx_html = "<[^>]+>";
        
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
			} //開啟檔案
			System.out.println(html[n] + "處理中");
			try {
				while ((temp = reader.readLine()) != null) { //一次讀取一行
					// System.out.println(temp);
					if (temp.indexOf("class=\"postmessage\">") >= 0) {// 找出 文章內容
						inContent = true;
						String[] temp2 = temp.split("class=\"postmessage\">");// 接取標題
																				// 如果有
																				// 會有內容，如果沒有是空字串
						temp = temp2[1];
						temp += "\r\n";
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
								temp += "\r\n";
							}
						}
						if (otherTable == 0) {
							//temp = temp.replace("<br />", "\r\n");// 取代換行符號
							//temp = temp.replace("&nbsp;", "");// 取代特殊字元 去掉<strong>//|<[/]?strong>|<[/]?b>|<[/]?a[^>]*>)
							temp=Replace.replace(temp, "<br />", "\r\n");
							temp = Replace.replace(temp,"&nbsp;", "");
							//temp = temp.replace("<[^>]+>", "");
					        m_html = p_html.matcher(temp);
					        temp = m_html.replaceAll("");
							bookData.append(temp);
						}

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
		Encoding encoding=new Encoding();
		if(this.encoding){
			result=encoding.StoT(bookData.toString());
		}else{
			result=encoding.TtoS(bookData.toString());
		}
	}

	public String getResult() {
		return result;
	}

}
