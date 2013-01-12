package JNovelDownloader.UI;

import java.awt.FlowLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sun.xml.internal.ws.message.StringHeader;

import JNovelDownloader.Kernel.DownloadThread;
import JNovelDownloader.Kernel.Downloader;
import JNovelDownloader.Kernel.ReadHtml;
import JNovelDownloader.Option.About;
import JNovelDownloader.Option.Option;


public class Frame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField urlTextField;
	private JTextField authorTextField;
	private JTextField bookNameTextField;
	private JTextField pageTextField;
	private JButton downloadButton;
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
		bookNameTextField = new JTextField("", 20);
		bookNamePanel = new JPanel();
		bookNamePanel.add(bookNameLabel);
		bookNamePanel.add(bookNameTextField);

		authorLabel = new JLabel("作者");
		authorTextField = new JTextField("", 20);
		bookNamePanel.add(authorLabel);
		bookNamePanel.add(authorTextField);
		add(bookNamePanel);
		/********************** 網址輸入 ***************************/
		urlLabel = new JLabel("網址：");
		urlPanel = new JPanel();
		// 定義輸入框
		urlPanel.add(urlLabel);
		urlTextField = new JTextField("", 50); // 網址輸入視窗
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
				// TODO Auto-generated method stub //下載指令放置處
				/*************下載指令*********/
				double startTime,donTime,totTime;
		    	startTime = System.currentTimeMillis();
				if (check(pageTextField.getText(), bookNameTextField.getText(),
						authorTextField.getText())) {//確認所有該填的資料都有填寫
					downloader.setUP(Integer.parseInt(pageTextField.getText()),
							urlTextField.getText());//分析網址
					readHtml.setUp(option.threadNumber, bookNameTextField.getText(), authorTextField.getText());
					resultTextArea.append("開始下載\r\n");
					resultTextArea.paintImmediately(resultTextArea.getBounds());
					try {
						if (!downloader.downloading(option, readHtml,
								resultTextArea)) {//開始下載
							resultTextArea.append("下載失敗\r\n");//下載失敗
							resultTextArea.paintImmediately(resultTextArea.getBounds());
						} else {
							donTime=System.currentTimeMillis()-startTime;
							if (readHtml.makeBook(option)) {//開始解析所有的網頁
								resultTextArea.append("小說製作完成\r\n");
								resultTextArea.paintImmediately(resultTextArea.getBounds());
								readHtml.delTempFile();
								resultTextArea.append("清除暫存檔\r\n");
								resultTextArea.paintImmediately(resultTextArea.getBounds());
								totTime=System.currentTimeMillis()-startTime;
								
								resultTextArea.append("總共花費 "+ totTime+ "ms ;其中下載花費"+ donTime+ "ms 資料處理花費  "+(totTime-donTime)+"ms \r\n");
								resultTextArea.paintImmediately(resultTextArea.getBounds());
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					resultTextArea.append("下載失敗");
				}
			}
		});
		downloadPanel.add(downloadButton);
		add(downloadPanel);

		resultTextArea = new JTextArea(8, 50);//訊息視窗
		resultTextArea.setLineWrap(true);
		resultScrollPane = new JScrollPane(resultTextArea);
		resultScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		resultScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultPanel = new JPanel();
		resultPanel.add(resultScrollPane);
		add(resultPanel);
		theNewVersion=checkVersion(option);
		resultTextArea.append("啟動中...\r\n");
		if(theNewVersion>About.versionNumber)
		{
			resultTextArea.append("本軟體最新版本為"+String.valueOf(theNewVersion)+"請至http://code.google.com/p/jnoveldownload/downloads/list 下載最新版本\r\n");
		}else{
			resultTextArea.append("目前最新版本："+String.valueOf(theNewVersion)+"\r\n");
		}
		
		option.printOption(resultTextArea);//印出初始訊息
		if(theNewVersion>About.versionNumber){
			JOptionPane.showMessageDialog(null, "本軟體最新版本為"+String.valueOf(theNewVersion)+"請至官網 下載最新版本", "有更新版本喔!!", JOptionPane.WARNING_MESSAGE );
		}
	}

	private boolean check(String page, String bookName, String author) {
		if (!page.matches("[1-9][0-9]*")) {
			resultTextArea.append("page..有誤");
			return false;
		} else if (bookName.isEmpty()) {
			resultTextArea.append("bookName..有誤");
			return false;
		} else if (author.isEmpty()) {
			resultTextArea.append("author..有誤");
			return false;
		} else
			return true;
	}
	
	private double checkVersion(Option option) throws Exception
	{
		String targetURL="http://code.google.com/p/jnoveldownload/downloads/list";
		String to=option.tempPath+"version.html";
		double version=0;
		DownloadThread downloadThread=new DownloadThread(targetURL, to);
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
			if(temp.indexOf("detail?name=JNovelDownloader")>=0){
				String temp2[]=temp.split("_");
				version=Double.parseDouble(temp2[1].charAt(1)+"."+temp2[2].charAt(0));
				break;
			}
		}
		
		return version;
	}

}
