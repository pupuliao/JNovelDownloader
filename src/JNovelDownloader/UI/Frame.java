package JNovelDownloader.UI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import JNovelDownloader.Kernel.Analysis;
import JNovelDownloader.Kernel.DownloadThread;
import JNovelDownloader.Kernel.Downloader;
import JNovelDownloader.Kernel.ReadHtml;
import JNovelDownloader.Kernel.Replace;
import JNovelDownloader.Kernel.UrlData;
import JNovelDownloader.Option.About;
import JNovelDownloader.Option.Option;

public class Frame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextFiled urlTextField;
	private TextFiled authorTextField;
	private TextFiled bookNameTextField;
	private JTextField pageTextField;
	private JButton downloadButton;
	private JButton parseButton;
	private JLabel urlLabel;
	private JLabel authorLabel;
	private JLabel bookNameLabel;
	private JLabel pageLabel;
	private JPanel urlPanel;
	private JPanel downloadPanel;
	private JPanel bookNamePanel;
	private JTextArea resultTextArea;
	private JScrollPane resultScrollPane;
	private JPanel resultPanel;
	private JButton settingButton;
	private double theNewVersion;

	public Frame(final Downloader downloader, final ReadHtml readHtml,
			final Option option) throws Exception {
		super(About.tittle + "-" + About.version + "  by " + About.author);
		setLayout(new FlowLayout()); // set frame layout
		/********************** 設定 ***************************/
		settingButton = new JButton("設定");
		settingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				OptionFrame frame = new OptionFrame(option, resultTextArea);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(700, 200);
				frame.setVisible(true);
			}
		});
		add(settingButton);
		/********************** 設定書名 ***************************/
		bookNameLabel = new JLabel("小說名稱");
		String os = System.getProperty("os.name").toLowerCase();
//		if(os.indexOf("win") >= 0)
//		{
			bookNameTextField = new TextFiled("", 20);
//			
//		}
//		else
//		{
//			bookNameTextField = new JTextFieldSelf("", 20);
//		}

		bookNamePanel = new JPanel();
		bookNamePanel.add(bookNameLabel);
		bookNamePanel.add(bookNameTextField);

		authorLabel = new JLabel("作者");
//		if(os.indexOf("win") >= 0)
//		{
			authorTextField = new TextFiled("", 20);
//		}
//		else
//		{
//			authorTextField = new JTextFieldSelf("", 20);
//		}

		bookNamePanel.add(authorLabel);
		bookNamePanel.add(authorTextField);
		add(bookNamePanel);
		/********************** 網址輸入 ***************************/
		urlLabel = new JLabel("網址：");
		urlPanel = new JPanel();
		// 定義輸入框
		urlPanel.add(urlLabel);
		urlTextField = new TextFiled("", 50); // 網址輸入視窗
		urlPanel.add(urlTextField);
		add(urlPanel);
		pageLabel = new JLabel("下載到第幾頁?");
		pageTextField = new JTextField("0", 4);
		downloadPanel = new JPanel();
		downloadPanel.add(pageLabel);
		downloadPanel.add(pageTextField);

		downloadButton = new JButton("下載");
		downloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO 自動產生的方法 Stub
						double startTime, donTime, totTime;
						startTime = System.currentTimeMillis();
						try {
							if (check(option,pageTextField.getText(),
									bookNameTextField.getText(),
									authorTextField.getText(),urlTextField.getText()) ) {// 確認所有該填的資料都有填寫
								// 下載、建書兩大元件初始化
								resultTextArea.append("初始化\r\n");
								downloader.setUP(
										Integer.parseInt(pageTextField.getText()),
										urlTextField.getText(),resultTextArea);// 分析網址
								readHtml.setUp(option.threadNumber,
										bookNameTextField.getText(),
										authorTextField.getText(),
										downloader.getUrlData(),resultTextArea);
								//
								resultTextArea.append("開始下載\r\n");
								// resultTextArea.paintImmediately(resultTextArea
								// .getBounds());
								resultTextArea.setCaretPosition(resultTextArea
										.getText().length());
								try {
									if (!downloader.downloading(option, readHtml,
											resultTextArea)) {// 開始下載
										resultTextArea.append("下載失敗\r\n");// 下載失敗
										// resultTextArea
										// .paintImmediately(resultTextArea
										// .getBounds());
									} else {
										donTime = System.currentTimeMillis()
												- startTime;
										if (readHtml.makeBook(option)) {// 開始解析所有的網頁
											resultTextArea.append("小說製作完成\r\n");
											// resultTextArea
											// .paintImmediately(resultTextArea
											// .getBounds());
											resultTextArea
													.setCaretPosition(resultTextArea
															.getText().length());
											readHtml.delTempFile();
											resultTextArea.append("清除暫存檔\r\n");
											// resultTextArea
											// .paintImmediately(resultTextArea
											// .getBounds());
											resultTextArea
													.setCaretPosition(resultTextArea
															.getText().length());
											totTime = System.currentTimeMillis()
													- startTime;

											resultTextArea.append("總共花費 " + totTime
													+ "ms ;其中下載花費" + donTime
													+ "ms 資料處理花費  "
													+ (totTime - donTime)
													+ "ms \r\n");
											// resultTextArea
											// .paintImmediately(resultTextArea
											// .getBounds());
										}
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								resultTextArea.append("下載失敗");
								resultTextArea.setCaretPosition(resultTextArea.getText().length());
							}
						} catch (NumberFormatException e) {
							// TODO 自動產生的 catch 區塊
							e.printStackTrace();
						} catch (IOException e) {
							// TODO 自動產生的 catch 區塊
							e.printStackTrace();
						}

					}

				}).start();
				// TODO Auto-generated method stub //下載指令放置處
				/************* 下載指令 *********/

			}
		});
		downloadPanel.add(downloadButton);
		parseButton = new JButton("偵測書名");
		parseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!urlTextField.getText().equals("")) {
					try {
						Document doc = Jsoup.connect(urlTextField.getText())
						.header("User-Agent",
								"Mozilla/5.0 (Linux; Android 4.2.2; Nexus 7 Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19")
						.get();
						Elements title = doc.getElementsByTag("title");
						String regex = "";
						regex = "([\\[【「（《［].+[\\]】」）》］])?\\s*[【《\\[]?\\s*([\\S&&[^】》]]+).*作者[】:：︰ ]*([\\S&&[^(（《﹝【]]+)";
						Matcher matcher;
						Pattern p;
						p = Pattern.compile(regex);
						matcher = p.matcher(title.get(0).text());
						if (matcher.find()) {
							bookNameTextField.setText(matcher.group(2));
							authorTextField.setText(matcher.group(3));
						} else {
							resultTextArea.append("偵測失敗");
							resultTextArea.setCaretPosition(resultTextArea.getText().length());	
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						resultTextArea.append("偵測失敗");
						resultTextArea.setCaretPosition(resultTextArea.getText().length());			
					}
				} else {
					resultTextArea.append("因網址空白導致偵測失敗");
					resultTextArea.setCaretPosition(resultTextArea.getText().length());
				}
			}
		});
		downloadPanel.add(parseButton);
		add(downloadPanel);

		resultTextArea = new JTextArea(8, 50);// 訊息視窗
		resultTextArea.setLineWrap(true);
		resultScrollPane = new JScrollPane(resultTextArea);
		resultScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		resultScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultPanel = new JPanel();
		resultPanel.add(resultScrollPane);
		add(resultPanel);

		resultTextArea.append("啟動中...\r\n");

		option.printOption(resultTextArea);// 印出初始訊息

	}

	public void popVersionAlert(Option option) {
		try {
			theNewVersion = checkVersion(option);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (theNewVersion > About.versionNumber) {
			resultTextArea
					.append("本軟體最新版本為"
							+ String.valueOf(theNewVersion)
							+ "請至http://www.pupuliao.info 下載最新版本\r\n");
		} else {
			resultTextArea.append("目前最新版本：" + String.valueOf(theNewVersion)
					+ "\r\n");
		}
		if (theNewVersion > About.versionNumber) {
			JOptionPane.showMessageDialog(null,
					"本軟體最新版本為" + String.valueOf(theNewVersion) + "請至官網 下載最新版本",
					"有更新版本喔!!", JOptionPane.WARNING_MESSAGE);
		}else if(About.versionNumber == 4.2){
			JOptionPane.showMessageDialog(null,
					"新增錯別字、禁用語、拼音字復原功能~~請至[設定]中開啟他",
					"有新功能喔!!", JOptionPane.WARNING_MESSAGE);
		}
	}

	private boolean check(Option option,String page, String bookName, String author,String url) throws IOException {
		
		if(page.isEmpty() || bookName.isEmpty() || author.isEmpty()){
			UrlData urlData=Analysis.analysisUrl(url);
			if (urlData.wrongUrl) {
				resultTextArea.append("網址有問題 無法分析\r\n");
				return false;
			}
			else {
				int p=getPage(option, url);
				if(page.isEmpty()|| !page.matches("[1-9][0-9]*"))	{
					pageTextField.setText(String.valueOf(p));
				}
				if(bookName.isEmpty()){
					bookNameTextField.setText(getTittle(option));
				}
				if(author.isEmpty()){
					authorTextField.setText("預設作者");
				}
			}
			
		
		}
		return true;
	}

	private double checkVersion(Option option) throws Exception {
		//String targetURL = "http://code.google.com/p/jnoveldownload/downloads/list";
		String targetURL = "http://sourceforge.net/projects/jnoveldownload/files";
		String to = option.tempPath + "version.html";
		double version = 0;
		DownloadThread downloadThread = new DownloadThread(targetURL, to, 0);
		try {
			downloadThread.start();
			downloadThread.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(to), "UTF-8"));
		// <a href="detail?name=JNovelDownloader_v2_1.jar&amp;can=2&amp;q=">
		String temp;
		while ((temp = reader.readLine()) != null) {
			if (temp.indexOf("<tr title=\"JNovelDownloader_v") >= 0) {
				String temp2[] = temp.split("_");
				version = Double.parseDouble(temp2[1].charAt(1) + "."
						+ temp2[2].charAt(0));
				break;
			}
		}
		reader.close();

		return version;
	}

	public void popPathAlert() {
		JOptionPane.showMessageDialog(null, "您的小說下載路徑或是暫存路徑有問題，請選擇[設定]重新設定",
				"路徑有問題", JOptionPane.WARNING_MESSAGE);
	}
	
	private int getPage(Option option,String url) throws IOException {
		int result = 0;
		DownloadThread downloadthread = new DownloadThread(url, option.tempPath
				+ "/temp.html", 1);
		downloadthread.start();
		try {
			downloadthread.join();
		} catch (InterruptedException e) {

		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(option.tempPath + "/temp.html"), "UTF-8"));
		String temp;
		String temp2[];
		while ((temp = reader.readLine()) != null) {
			if (temp.indexOf("class=\"pg\"") >= 0) {
				if (temp.indexOf("class=\"last\"") >= 0) {
					temp2 = temp.split("class=\"last\">.. ");
					temp2 = temp2[1].split("</a>");
					result = Integer.parseInt(temp2[0]);
				}else if(temp.indexOf("class=\"nxt\"") >= 0){
					temp2 = temp.split("class=\"nxt\"");
					temp2 = temp2[0].split("<a href");
					temp2 = temp2[temp2.length-2].split("</a>");
					temp2 = temp2[0].split(">");
					result = Integer.parseInt(temp2[1]);
				}else if(temp.indexOf("<strong>")>=0){
					temp2 = temp.split("<strong>");
					temp2 = temp2[1].split("</strong>");
					result = Integer.parseInt(temp2[0]);
				}
				break;
			}
		}
		reader.close();
		return result;

	}

	private String getTittle(Option option) throws IOException {// 必須要先執行過getPage
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(option.tempPath + "/temp.html"), "UTF-8"));
		String temp;
		String temp2[];
		String result = null;
		while ((temp = reader.readLine()) != null) {
			if (temp.indexOf("<title>") >= 0) {
				temp2 = temp.split("title>");
				temp2 = temp2[1].split(" - ");
				result = temp2[0];
//				result = Replace.replace(result, "【", "[");
//				result = Replace.replace(result, "】", "]");
				result = Replace.replace(result, ":", "");
				result = Replace.replace(result, " ", "");
				break;
			}
		}
		reader.close();
		return result;
	}

}
