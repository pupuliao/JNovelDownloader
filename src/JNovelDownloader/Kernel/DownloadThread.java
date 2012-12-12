package JNovelDownloader.Kernel;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadThread extends Thread {

	private String[] from;
	private String[] to;
	public boolean downloadstate;

	public DownloadThread(String[] from, String[] to) {
		this.from = from;
		this.to = to;
		downloadstate = true;
	}

	public void run() {
		for (int n = 0; n < this.to.length; n++) {
	        StringBuffer total = new StringBuffer();;
	        
	        try{
	        	System.out.print("開始下載檔案: " + from[n]);
	            URL url = new URL(from[n]);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//	            connection.setDoOutput(true);
	            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.0.3; zh-tw; HTC_Sensation_Z710e Build/IML74K)" );
	            connection.connect();
	            InputStream inStream = (InputStream) connection.getInputStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"utf8"));
	            String line="";
	            while ((line = reader.readLine()) !=null ){
	                total.append(line + "\n");
	            }
	            //System.out.println("檔案："+total);
	        }catch(Exception e){
	            e.printStackTrace();
	            System.out.println("取得網頁html時發生錯誤");
	            downloadstate=false;
	            return;
	        }
	        try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(to[n]), "UTF-8");
				writer.write(total.toString());
				writer.flush();
				writer.close();
				System.out.println("下載完成");
			} catch (Exception e) {
				// TODO: handle exception
				downloadstate=false;
	            return;
			}
	        
		}
		return ;
	}

//	public void run() { 不能用了
//		for (int n = 0; n < this.to.length; n++) {
//			URL url = null;
//			try {
//				url = new URL(from[n]);
//			} catch (MalformedURLException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			} // 建立URL物件
//
//			DataInputStream in = null;
//			try {
//				in = new DataInputStream(url.openStream());
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			RandomAccessFile out = null;
//			try {
//				out = new RandomAccessFile(to[n], "rw");
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			try {
//
//				System.out.print("開始下載檔案: " + from[n]);
//				byte data;
//				// 複製檔案
//				while (true) {
//					data = (byte) in.readByte();
//					out.writeByte(data);
//				}
//			} catch (EOFException e) {
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			// resultTextArea.append("...檔案下載成功...\r\n");
//			// resultTextArea.paintImmediately(resultTextArea.getBounds());
//			// resultTextArea.setCaretPosition(resultTextArea.getDocument()
//			// .getLength());
//			System.out.println("下載成功");
//
//			try {
//				in.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} // 關閉串流
//			try {
//				out.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}

}
