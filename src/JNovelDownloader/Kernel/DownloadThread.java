package JNovelDownloader.Kernel;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import javax.swing.JTextArea;

public class DownloadThread extends Thread {

	private String[] from;
	private String[] to;
	public boolean downloadstate;
	private int threatNember;
//	private String sessionId = null;
	private JTextArea resultTextArea = null;

	public DownloadThread(String[] from, String[] to, int t) {
		this.from = from;
		this.to = to;
		this.threatNember = t;
		downloadstate = true;
	}

	public DownloadThread(String from, String to, int t) {
		this.from = new String[1];
		this.to = new String[1];
		this.from[0] = from;
		this.to[0] = to;
		this.threatNember = t;
		downloadstate = true;
	}
/*
	public DownloadThread(String[] from, String[] to, int t, String sessionId) {
		this.from = from;
		this.to = to;
		this.threatNember = t;
		downloadstate = true;
		this.sessionId = sessionId;
	}

	public DownloadThread(String from, String to, int t, String sessionId) {
		this.from = new String[1];
		this.to = new String[1];
		this.from[0] = from;
		this.to[0] = to;
		this.threatNember = t;
		downloadstate = true;
		this.sessionId = sessionId;
	}
*/
	public DownloadThread(String[] from, String[] to, int t, String sessionId,
			JTextArea resultTextArea) {
		this.from = from;
		this.to = to;
		this.threatNember = t;
		downloadstate = true;
//		this.sessionId = sessionId;
		this.resultTextArea = resultTextArea;
	}

	public DownloadThread(String from, String to, int t, String sessionId,
			JTextArea resultTextArea) {
		this.from = new String[1];
		this.to = new String[1];
		this.from[0] = from;
		this.to[0] = to;
		this.threatNember = t;
		downloadstate = true;
//		this.sessionId = sessionId;
		this.resultTextArea = resultTextArea;
	}
	public DownloadThread(String[] from, String[] to, int t,JTextArea resultTextArea) {
		this.from = from;
		this.to = to;
		this.threatNember = t;
		downloadstate = true;
		this.resultTextArea = resultTextArea;
	}

	public DownloadThread(String from, String to, int t,JTextArea resultTextArea) {
		this.from = new String[1];
		this.to = new String[1];
		this.from[0] = from;
		this.to[0] = to;
		this.threatNember = t;
		downloadstate = true;
		this.resultTextArea = resultTextArea;
	}

	public void run() {
		int downloadmiss=0;
		for (int n = 0; n < this.to.length; n++) {
			StringBuffer total = new StringBuffer();

			try {
				System.out.print("開始下載檔案: " + from[n]);
				if (resultTextArea != null) {
					resultTextArea.append("\r\n開始下載檔案: " + from[n]);
					resultTextArea.setCaretPosition(resultTextArea.getText()
							.length());
				}
				TrustManager[] trustAllCerts = new TrustManager[]{
				    new X509TrustManager() {
				        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				            return null;
				        }
				        public void checkClientTrusted(
				            java.security.cert.X509Certificate[] certs, String authType) {
				        }
				        public void checkServerTrusted(
				            java.security.cert.X509Certificate[] certs, String authType) {
				        }
				    }
				};

					// Activate the new trust manager
				try {
				    SSLContext sc = SSLContext.getInstance("SSL");
				    sc.init(null, trustAllCerts, new java.security.SecureRandom());
				    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				} catch (Exception e) {
					
				}
				
				
				URL url = new URL(from[n]);
				System.setProperty("https.protocols", "TLSv1.2");
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				
//				if (from[n].indexOf("eyny") >= 0) {

				connection.setDoOutput(true);//
				switch (threatNember) {
				case 0:
					connection
							.setRequestProperty(
									"User-Agent",
									"Mozilla/5.0 (Linux; U; Android 6.0.1; zh-CN; SM-C7000 Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.6.2.948 Mobile Safari/537.36");
					break;
				case 1:
					connection
							.setRequestProperty(
									"User-Agent",
									"Mozilla/5.0 (Linux; Android 4.1.1; M040 Build/JRO03H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36");
					break;
				case 2:
					connection
							.setRequestProperty(
									"User-Agent",
									"Mozilla/5.0 (Linux; Android 4.2.1; M040 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36");
					break;
				case 3:
					connection
							.setRequestProperty(
									"User-Agent",
									"Mozilla/5.0 (Linux; Android 4.2.2; Nexus 7 Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
					break;
				default:
					connection
							.setRequestProperty(
									"User-Agent",
									"Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12F70 Safari/600.1.4");
					break;
				}

				connection.connect();
				// edit by markwu123 新增 "Accept-Language", "en-US,en;q=0.5" 2018/1/22
				connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				InputStream inStream = (InputStream) connection
						.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inStream, "utf8"));
				String line = "";
				String lineSeparator=System.getProperty("line.separator");
				while ((line = reader.readLine()) != null) {
					total.append(line + lineSeparator);
				}
				// System.out.println("檔案："+total);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("取得網頁html時發生錯誤....");
				if (resultTextArea != null) {
					resultTextArea.append("\r\n取得網頁html時發生錯誤");
					resultTextArea.setCaretPosition(resultTextArea.getText()
							.length());
				}
				if(downloadmiss<20)
				{
					downloadmiss++;
					System.out.println("等待一秒嘗試重新下載....");
					if (resultTextArea != null) {
						resultTextArea.append("\r\n等待一秒嘗試重新下載....");
						resultTextArea.setCaretPosition(resultTextArea.getText()
								.length());
					}
					n--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				else
				{
					System.out.println("沒救了....");
					if (resultTextArea != null) {
						resultTextArea.append("\r\n沒救了....");
						resultTextArea.setCaretPosition(resultTextArea.getText()
								.length());
					}
					downloadstate = false;
					return;
				}

			}
			try {
				OutputStreamWriter writer = new OutputStreamWriter(
						new FileOutputStream(to[n]), "UTF-8");
				writer.write(total.toString());
				writer.flush();
				writer.close();
				System.out.println("下載完成");
				if (resultTextArea != null) {
					resultTextArea.setText(resultTextArea.getText().replaceAll(from[n]+"\r\n", from[n]+"下载完成\r\n"));
					resultTextArea.setCaretPosition(resultTextArea.getText()
							.length());
				}
			} catch (Exception e) {
				// TODO: handle exception
				downloadstate = false;
				return;
			}

		}
		return;
	}

	// public void run() { 不能用了
	// for (int n = 0; n < this.to.length; n++) {
	// URL url = null;
	// try {
	// url = new URL(from[n]);
	// } catch (MalformedURLException e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// } // 建立URL物件
	//
	// DataInputStream in = null;
	// try {
	// in = new DataInputStream(url.openStream());
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// RandomAccessFile out = null;
	// try {
	// out = new RandomAccessFile(to[n], "rw");
	// } catch (FileNotFoundException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// try {
	//
	// System.out.print("開始下載檔案: " + from[n]);
	// byte data;
	// // 複製檔案
	// while (true) {
	// data = (byte) in.readByte();
	// out.writeByte(data);
	// }
	// } catch (EOFException e) {
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // resultTextArea.append("...檔案下載成功...\r\n");
	// // resultTextArea.paintImmediately(resultTextArea.getBounds());
	// // resultTextArea.setCaretPosition(resultTextArea.getDocument()
	// // .getLength());
	// System.out.println("下載成功");
	//
	// try {
	// in.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } // 關閉串流
	// try {
	// out.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// }
	
	
	
}
