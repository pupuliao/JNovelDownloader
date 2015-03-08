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
	private String lineSeparator;

	public MakeBookThread(String[] data, boolean encoding, int type,
			JTextArea resultTextArea) {
		html = data;
		bookData = new StringBuilder();
		this.encoding = encoding;
		this.type = type;
		this.resultTextArea = resultTextArea;
		this.lineSeparator=System.getProperty("line.separator");
	}

	public void run() {
		System.out.println(type);
		if (type == 1) {
			this.runType1();
		} else {
			this.runType0();
		}
	}

	private void runType1() {
		// archiver給伊莉用
		// 參考 網友D.A.R.K
		// http://uneedanapple.blogspot.tw/2013/09/android-app-noveldroid.html
		// 的程式碼
		// boolean inContent = false;
		int stage = 0; // 0=不再內文中 ,1=在<div class="pbody"> 中,
						// 2=在<div class="mes">中 ,
						// 3=<div id="postmessage_~~~~" class="mes">中 ,
		/**
		 * 0: not in article 主題開頭 1: in author section 處理作者 2: in title section
		 * 處理標題 3: in article section 處理內文
		 */
		int end;
		String temp;
		// int otherTable = 0;
		/* 用於正規表示式的過濾，比replace all 快速準確 */
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Pattern pTitle = Pattern.compile("<h3>(.+)?</h3>");
		Pattern pModStamp = Pattern
				.compile(" 本帖最後由 \\S+ 於 \\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2} (\\S{2} )?編輯 ");
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
						if (temp.indexOf("<p class=\"author\">") >= 0) {
							stage = 1;
						}
						break;
					case 1:
						if (temp.indexOf("</p>") >= 0) {//
							stage = 2;
						}
						break;
					case 2: // 應該是處理標題
						m_html = pTitle.matcher(temp);
						if (m_html.find()) {
							if ((temp = m_html.group(1)) != null)// 分組0
																	// <h3>1</h3>2
								bookData.append(temp + lineSeparator);
							stage = 3;
						}
						break;
					case 3:
						m_html = pModStamp.matcher(temp);
						if (m_html.find())
							break;
						if (temp.indexOf("<p class=\"author\">") >= 0) {
							stage = 1;
//							break;
						}
						if ((end = temp.indexOf("...&lt;div class='locked'")) == 0) {
							if (temp.indexOf("<p class=\"author\">") >= 0) {
								stage = 1;
							}
							else {
								stage = 0;
							}
							break;
						}

						if (end > 0) {
							if (temp.indexOf("<p class=\"author\">") >= 0) {
								stage = 1;
							}
							else {
								stage = 0;
							}
							temp = temp.substring(0, end);
							temp=temp+lineSeparator;
						}
						
						temp = Replace.replace(temp, "&nbsp;", "");
						temp = Replace.replace(temp, "<br/>", lineSeparator);
						temp = Replace.replace(temp, "<br />", lineSeparator);
						m_html = p_html.matcher(temp);
						temp = m_html.replaceAll("");
						temp = temp.replaceAll("^[ \t　]+", "");
						// if (temp.length() > 2)
						// temp = "　　" + temp;
						bookData.append(temp);
						break;
					default:
						break;
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bookData.append(lineSeparator);
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

	private void runType0() {
		// boolean inContent = false;
		int stage = 0; // 0=不再內文中 ,1=在<div class="pbody"> 中,
						// 2=在<div class="mes">中 ,
						// 3=<div id="postmessage_~~~~" class="mes">中 ,
		String temp;
		boolean flag = false;
		int otherTable = 0;
		/* 用於正規表示式的過濾，比replace all 快速準確 */
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html;
		// String regEx_html = "<[^>]+>";
		Pattern pModStamp = Pattern
				.compile(" 本帖最後由 \\S+ 於 \\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2} (\\S{2} )?編輯 ");
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
			resultTextArea.append(html[n] + "處理中"+lineSeparator);
			resultTextArea.setCaretPosition(resultTextArea.getText().length());
			try {
				while ((temp = reader.readLine()) != null) { // 一次讀取一行
					switch (stage) {
					case 0:
						if (temp.indexOf("class=\"pbody") >= 0) {
							stage = 1;
						}
						break;
					case 1:
						if (temp.indexOf("<h") >= 0) {// 出現標題
							temp = Replace.replace(temp, " ", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");
							
							// if (flag == false
							// &&
							// temp.matches("第[一二三四五六七八九十百零1234567890 　]*章 [^<>]*"))
							// // for
							// // Calibre
							// // 轉檔
							// {
							// String headLineString = "<floor>"
							// + Replace.replace(temp, "\r\n", "")
							// + "</floor>";
							// bookData.append(headLineString);
							// bookData.append("\r\n");
							// flag = true;
							// }
							bookData.append(temp);
							bookData.append(lineSeparator);
						}
						if (temp.indexOf("<div class=\"mes") >= 0) {
						//	System.out.println(temp);
							stage = 2;
						}
						break;
					case 2:
						if (temp.indexOf("class=\"postmessage\">") >= 0) {// 找出
																			// 文章內容
					//		System.out.println(temp);
							stage = 3;
							// String[] temp2 =
							// temp.split("class=\"postmessage\">");// 接取標題
							// if(temp2.length<=0)
							// temp = temp2[1];
							if (temp.indexOf("<i class=\"pstatus\">") >= 0) { // 過慮修改時間
								temp = temp.replaceAll(
										"<i class=\"pstatus\">[^<>]+ </i>", "");
							}
							if (temp.indexOf("<div class=\"quote\">") >= 0) { // 過濾
																				// 引用
								otherTable++;
								temp = temp
										.replaceAll(
												"<font color=\"#999999\">[^<>]+</font>",
												"");
							}
							m_html=pModStamp.matcher(temp);
							temp = m_html.replaceAll("");
							temp += lineSeparator;
							temp = Replace.replace(temp, "<br/>", lineSeparator);
							temp = Replace.replace(temp, "<br />", lineSeparator);
							temp = Replace.replace(temp, "&nbsp;", "");
							m_html = p_html.matcher(temp);
							temp = m_html.replaceAll("");
							temp = temp.replaceAll("^[ \t　]+", ""); // 過濾凸排
							// if(flag==false &&
							// temp.matches("第[一二三四五六七八九十百零1234567890 　]*章 [^<>]*"))
							// //for Calibre 轉檔
							// {
							//
							// String
							// headLineString="<floor>"+Replace.replace(temp,
							// "\r\n", "")+"</floor>";
							// bookData.append(headLineString);
							// bookData.append("\r\n");
							// flag=true;
							// }
							bookData.append(temp);
							// 如果有
							// 會有內容，如果沒有是空字串
						}
						break;
					case 3:
						if (temp.indexOf("<div ") >= 0) // 避免碰到下一階層
							otherTable++;
						if (temp.indexOf("</div>") >= 0) {
							if (otherTable > 0)// 從底層離開
								otherTable--;
							else {// 偵測是否離開了
								temp = temp.replace("</div>", " ");
								stage = 0;
								flag = false;
								temp += lineSeparator+lineSeparator+lineSeparator;
							}
						}
						if (true) {
							// 去掉<strong>//|<[/]?strong>|<[/]?b>|<[/]?a[^>]*>)
							m_html=pModStamp.matcher(temp);
							temp = m_html.replaceAll("");
							temp = Replace.replace(temp, "<br/>", lineSeparator);
							temp = Replace.replace(temp, "<br />", lineSeparator);
							temp = Replace.replace(temp, "&nbsp;", "");
							m_html = p_html.matcher(temp);
							// if(flag==false &&
							// temp.matches("第[一二三四五六七八九十百零1234567890 　]*章 [^<>]*"))
							// //for Calibre 轉檔
							// {
							// String
							// headLineString="<floor>"+Replace.replace(temp,
							// "\r\n", "")+"</floor>";
							// bookData.append(headLineString);
							// bookData.append("\r\n");
							// flag=true;
							// }
							temp = m_html.replaceAll("");
							temp = temp.replaceAll("^[ \t　]+", ""); // 過濾凸排
							bookData.append(temp);
							//System.out.println(temp);
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
			bookData.append(lineSeparator);
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Encoding encoding = new Encoding();
	//	System.out.println(bookData.toString());
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
