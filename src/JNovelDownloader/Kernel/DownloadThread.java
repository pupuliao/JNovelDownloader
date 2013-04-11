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
	private int threatNember;

	public DownloadThread(String[] from, String[] to,int t) {
		this.from = from;
		this.to = to;
		this.threatNember = t;
		downloadstate = true;
	}
	
	public DownloadThread(String from, String to,int t) {
		this.from= new String[1];
		this.to= new String[1];
		this.from[0] = from;
		this.to[0] = to;
		this.threatNember = t;
		downloadstate = true;
	}

	public void run() {
		for (int n = 0; n < this.to.length; n++) {
	        StringBuffer total = new StringBuffer();;
	        
	        try{
	        	System.out.print("開始下載檔案: " + from[n]);
	            URL url = new URL(from[n]);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//	            connection.setDoOutput(true);//
	            switch (threatNember) {
				case 0:
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.0.3; zh-tw; HTC_Sensation_Z710e Build/IML74K)AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30" );
					break;
				case 1:
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0; SAMSUNG; OMNIA7)　" );
					break;
				case 2:
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A5313e Safari/7534.48.3" );
					break;
				case 3:
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 4.2.2; Nexus 7 Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19" );
					break;
				default:
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 4.2.2; Nexus 7 Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19" );
					break;
				}
	            
	            
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
