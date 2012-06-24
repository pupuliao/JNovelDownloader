package JNovelDownloader.Kernel;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JTextArea;

import JNovelDownloader.Option.Option;

public class Downloader {
	private String[] urlStrings;
	private int toPage;
	private UrlData urlData;

	public Downloader() {

	}

	public void setUP(int Page, String urlString) {
		urlData = Analysis.analysisUrl(urlString);
		this.toPage = Page;
	}

	public void generateUrlList() {
		// http://ck101.com/thread-1753100-55-1.html
		urlStrings = new String[toPage - urlData.page + 1];
		String temp = "http://" + urlData.domain + "/thread-"
				+ String.valueOf(urlData.Tid) + "-";
		int m = 0;
		for (int n = urlData.page; n <= toPage; n++) {
			urlStrings[m++] = temp + n + "-1.html";
		}
	}

	public boolean downloading(Option option, ReadHtml book,JTextArea resultTextArea) throws IOException {//需要重點加速的地方
		if (urlData.wrongUrl) {
			return false;
		} else {
			generateUrlList();
			// http://ck101.com/thread-1753100-55-1.html
			int m = 0;
			String temp;
			for (int n = urlData.page; n <= toPage; n++) {
				URL url = new URL(urlStrings[m++]); // 建立URL物件

				DataInputStream in = new DataInputStream(url.openStream());
				temp = option.tempPath + "thread-" + urlData.Tid + "-" + n
						+ "-1.html";
				RandomAccessFile out = new RandomAccessFile(temp, "rw");
				try {
					resultTextArea.append("開始下載檔案: " + temp);
					resultTextArea.paintImmediately(resultTextArea.getBounds()); 
					System.out.print("開始下載檔案: " + temp);
					byte data;
					// 複製檔案
					while (true) {
						data = (byte) in.readByte();
						out.writeByte(data);
					}
				} catch (EOFException e) {
				}
				resultTextArea.append("...檔案下載成功...\r\n");
				resultTextArea.setCaretPosition(resultTextArea.getDocument().getLength());
				resultTextArea.paintImmediately(resultTextArea.getBounds()); 
				System.out.println("下載成功");
				
				book.addFileName(temp);
				in.close(); // 關閉串流
				out.close();
			}
			return true;
		}
	}

}
