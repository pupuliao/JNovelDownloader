package JNovelDownloader.UI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.management.loading.PrivateClassLoader;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;

import JNovelDownloader.Kernel.Downloader;
import JNovelDownloader.Kernel.ReadHtml;
import JNovelDownloader.Option.About;
import JNovelDownloader.Option.Option;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.regexp.internal.recompile;

public class Frame extends JFrame {

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
	private JPanel pathPanel;
	private JPanel bookNamePanel;
	private Downloader X;
	private JButton pathButton;
	private ReadHtml book;
	private JTextArea resultTextArea;
	private JScrollPane resultScrollPane;
	private JPanel resultPanel;
	private JButton settingButton;

	public Frame(final Downloader downloader, final ReadHtml readHtml,
			final Option option) {
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
				double startTime,donTime,totTime;
		    	startTime = System.currentTimeMillis();
				if (check(pageTextField.getText(), bookNameTextField.getText(),
						authorTextField.getText())) {//確認所有該填的資料都有填寫
					downloader.setUP(Integer.parseInt(pageTextField.getText()),
							urlTextField.getText());//分析網址
					readHtml.setPage(Integer.parseInt(pageTextField.getText()));
					readHtml.bookName = bookNameTextField.getText();
					readHtml.author = authorTextField.getText();
					readHtml.setPath(option.novelPath);
					resultTextArea.append("開始下載\r\n");
					try {
						if (!downloader.downloading(option, readHtml,
								resultTextArea)) {//開始下載
							resultTextArea.append("網址有問題\r\n");//下載失敗
						} else {
							donTime=System.currentTimeMillis()-startTime;
							if (readHtml.makeBook(option)) {//開始解析所有的網頁
								resultTextArea.append("小說製作完成\r\n");
								readHtml.delTempFile();
								resultTextArea.append("清除暫存檔\r\n");
								resultTextArea.paintImmediately(resultTextArea.getBounds());
								totTime=System.currentTimeMillis()-startTime;
								resultTextArea.append("總共花費 "+ totTime+ "ms ;其中下載花費"+ donTime+ "ms \r\n");
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

		resultTextArea.append("啟動中...\r\n");
		option.printOption(resultTextArea);//印出初始訊息
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

}
