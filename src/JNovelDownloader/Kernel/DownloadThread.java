package JNovelDownloader.Kernel;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadThread extends Thread {
	
	String [] from;
	String [] to;
	
	public DownloadThread(String [] from ,String [] to ){
		this.from=from;
		this.to=to;
	}
	
	public void run(){
		for(int n=0;n<this.to.length;n++){
			URL url = null;
			try {
				url = new URL(from[n]);
			} catch (MalformedURLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} // 建立URL物件

			DataInputStream in = null;
			try {
				in = new DataInputStream(url.openStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			RandomAccessFile out = null;
			try {
				out = new RandomAccessFile(to[n], "rw");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {

				System.out.print("開始下載檔案: " + from[n]);
				byte data;
				// 複製檔案
				while (true) {
					data = (byte) in.readByte();
					out.writeByte(data);
				}
			} catch (EOFException e) {
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			resultTextArea.append("...檔案下載成功...\r\n");
//			resultTextArea.paintImmediately(resultTextArea.getBounds());
//			resultTextArea.setCaretPosition(resultTextArea.getDocument()
//					.getLength());
			System.out.println("下載成功");

			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 關閉串流
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
