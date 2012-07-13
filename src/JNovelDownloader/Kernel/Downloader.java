package JNovelDownloader.Kernel;

import java.io.IOException;

import javax.swing.JTextArea;

import JNovelDownloader.Option.Option;

public class Downloader {
	private String[] urlStrings;
	private int toPage;
	private UrlData urlData;

	public Downloader() {

	}

	public void setUP(int Page, String urlString) {
		urlData = Analysis.analysisUrl(urlString);// 分析網址
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
    /*****************以下function 是舊有的downloading ***************************/
//	public boolean downloadingOld(Option option, ReadHtml book,
//			JTextArea resultTextArea) throws IOException {// 需要重點加速的地方
//		if (urlData.wrongUrl) {
//			return false;
//		} else {
//			generateUrlList();
//			// http://ck101.com/thread-1753100-55-1.html
//			int m = 0;
//			String temp;
//			for (int n = urlData.page; n <= toPage; n++) {
//				URL url = new URL(urlStrings[m++]); // 建立URL物件
//
//				DataInputStream in = new DataInputStream(url.openStream());
//				temp = option.tempPath + "thread-" + urlData.Tid + "-" + n
//						+ "-1.html";
//				RandomAccessFile out = new RandomAccessFile(temp, "rw");
//				try {
//					// resultTextArea.append("開始下載檔案: " + temp); // 在UI上顯示結果
//					// resultTextArea.paintImmediately(resultTextArea.getBounds());
//					// resultTextArea.setCaretPosition(resultTextArea.getDocument()
//					// .getLength());
//					System.out.print("開始下載檔案: " + temp);
//					byte data;
//					// 複製檔案
//					while (true) {
//						data = (byte) in.readByte();
//						out.writeByte(data);
//					}
//				} catch (EOFException e) {
//				}
//				// resultTextArea.append("...檔案下載成功...\r\n");
//				// resultTextArea.paintImmediately(resultTextArea.getBounds());
//				// resultTextArea.setCaretPosition(resultTextArea.getDocument()
//				// .getLength());
//				System.out.println("下載成功");
//
//				book.addFileName(temp); // 放入要處理檔案的清單中
//				in.close(); // 關閉串流
//				out.close();
//			}
//			return true;
//		}
//	}

	public boolean downloading(Option option, ReadHtml book,
			JTextArea resultTextArea) throws IOException {// 需要重點加速的地方
		if (urlData.wrongUrl) {
			return false;
		} else {
			generateUrlList();
			// http://ck101.com/thread-1753100-55-1.html
			int m = 0;
			//int threadNumber = 4;
			int morethread = urlStrings.length % option.threadNumber;
			int tempNumber = urlStrings.length / option.threadNumber;
			DownloadThread []downloadThread =new DownloadThread[option.threadNumber]; 
			String[] from ;
			String[] to ;
			String[] totalTo = new String[urlStrings.length];
			int number;
			for (int n = urlData.page; n <= toPage; n++) {
				// from[m]=urlStrings[m];
				totalTo[m] = option.tempPath + "thread-" + urlData.Tid + "-" + n
						+ "-1.html";
				;
//				book.addFileName(totalTo[m]);
				m++;
			}
			m=0;
			for (int x = 0; x < option.threadNumber; x++) {
				if (morethread > 0) {
					number = tempNumber + 1;
					morethread--;
				} else {
					number = tempNumber;
				}
				from= new String [number];
				to=new String [number];
				for(int y=0;y<number;y++){
					from[y]=urlStrings[m];
					to[y]=totalTo[m++];
				}
				downloadThread[x] =new DownloadThread(from, to);
				book.addFileName(to);
				downloadThread[x].start();
			}
			try {
				for(int x=0 ; x<option.threadNumber;x++){
					downloadThread[x].join(); 
				}
	        } catch (InterruptedException e) {}
			return true;
		}
	}

}
