package JNovelDownloader.UI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import JNovelDownloader.Option.Option;

public class OptionFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField novelPathTextField;
	private JTextField tempPathTextField;
	private JTextField threadNumberTextField;
	private JPanel novelPathPanel;
	private JButton novalPathButton;
	private JLabel encodingLabel;
	private JLabel outPutEncodeLabel;
	private JLabel threadNumberLabel;
	private JPanel encodingPanel;
	private JPanel outputEncodingPanel;
	private JPanel tempPathPanel;
	private JPanel threadNuimberPanel;
	private JButton tempPathButton;
	private ButtonGroup encoding;
	private ButtonGroup outputEncoding;
	private JRadioButton encodingTRadioButton;
	private JRadioButton encodingSRadioButton;
	private JRadioButton unicodeRadioButton;
	private JRadioButton utf_8RadioButton;
	private JCheckBox replaCheckBox;
	// private JPanel checkPanel;
	private JButton setButton;
	// private JButton exitButton;
	private boolean tempEncoding;
	private String tempoutputEncoding;

	public OptionFrame(final Option option, final JTextArea resulTextArea) {
		super("設置");
		setLayout(new FlowLayout());
		novalPathButton = new JButton("小說存放目錄");
		novelPathTextField = new JTextField(option.novelPath, 50);
		novelPathPanel = new JPanel();
		novalPathButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser pathChooser = new JFileChooser();
				pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				pathChooser.setDialogTitle("選擇檔案存放路徑");
				int result = pathChooser.showOpenDialog(OptionFrame.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					String path = pathChooser.getSelectedFile()
							.getAbsolutePath();
					novelPathTextField.setText(path + "\\");
				}
			}
		});
		novelPathPanel.add(novalPathButton);
		novelPathPanel.add(novelPathTextField);
		add(novelPathPanel);
		System.out.println(option.novelPath);

		tempPathButton = new JButton("檔案暫存目錄");
		tempPathTextField = new JTextField(option.tempPath, 50);
		tempPathPanel = new JPanel();
		tempPathButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser pathChooser = new JFileChooser();
				pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				pathChooser.setDialogTitle("選擇檔案存放路徑");
				int result = pathChooser.showOpenDialog(OptionFrame.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					String path = pathChooser.getSelectedFile()
							.getAbsolutePath();
					tempPathTextField.setText(path + "\\");
				}
			}
		});
		tempPathPanel.add(tempPathButton);
		tempPathPanel.add(tempPathTextField);
		add(tempPathPanel);

		encodingLabel = new JLabel("自動轉碼選擇");
/*		if (option.encoding) {
			encodingTRadioButton = new JRadioButton("正體中文", true);
			encodingSRadioButton = new JRadioButton("簡體中文", false);
			
		} else {
			encodingTRadioButton = new JRadioButton("正體中文", false);
			encodingSRadioButton = new JRadioButton("簡體中文", true);
		}*/
		encodingTRadioButton = new JRadioButton("正體中文", option.encoding);
		encodingSRadioButton = new JRadioButton("簡體中文", !option.encoding);
		tempEncoding=option.encoding;
		encoding = new ButtonGroup();
		encoding.add(encodingTRadioButton);
		encoding.add(encodingSRadioButton);
		encodingPanel = new JPanel();
		encodingPanel.add(encodingLabel);
		encodingPanel.add(encodingTRadioButton);
		encodingPanel.add(encodingSRadioButton);
		encodingTRadioButton.addItemListener(new RadioButtonHandler(true));
		encodingSRadioButton.addItemListener(new RadioButtonHandler(false));
		add(encodingPanel);
		
		outPutEncodeLabel = new JLabel("輸出編碼選擇");
		unicodeRadioButton = new JRadioButton("Unicode", option.outputEncode.equals("Unicode"));
		utf_8RadioButton = new JRadioButton("UTF-8", !option.outputEncode.equals("Unicode"));
		tempoutputEncoding=option.outputEncode;
		outputEncoding = new ButtonGroup();
		outputEncoding.add(unicodeRadioButton);
		outputEncoding.add(utf_8RadioButton);
		outputEncodingPanel = new JPanel();
		outputEncodingPanel.add(outPutEncodeLabel);
		outputEncodingPanel.add(unicodeRadioButton);
		outputEncodingPanel.add(utf_8RadioButton);
		unicodeRadioButton.addItemListener(new EnocdeButtonHandler(true));
		utf_8RadioButton.addItemListener(new EnocdeButtonHandler(false));
		add(outputEncodingPanel);
		
		threadNumberLabel =new JLabel("多執行序數目：");
		threadNumberTextField  =new JTextField(String.valueOf(option.threadNumber), 5);
		threadNuimberPanel =new JPanel();
		threadNuimberPanel.add(threadNumberLabel);
		threadNuimberPanel.add(threadNumberTextField);
		add(threadNuimberPanel);
		
		if (option.replace) {
			replaCheckBox = new JCheckBox("違禁語、拼音復原(最新功能)", true);
		} else {
			replaCheckBox = new JCheckBox("違禁語、拼音復原(最新功能)", false);
		}

		add(replaCheckBox);
		setButton = new JButton("確定");
		setButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				option.novelPath = novelPathTextField.getText();
				option.tempPath = tempPathTextField.getText();
				option.encoding = tempEncoding;
				option.replace = replaCheckBox.isSelected();
				option.threadNumber=Integer.parseInt(threadNumberTextField.getText());
				option.outputEncode = tempoutputEncoding;

				try {
					option.saveOption();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				option.printOption(resulTextArea);
				closeFrame();
			}
		});
		add(setButton);
	}

	private void closeFrame(){
		this.dispose();
	}
	
	private class RadioButtonHandler implements ItemListener { // 當選擇 RADIO
																// 按鈕時觸發
		private boolean encode;

		public RadioButtonHandler(boolean a) {
			encode = a;
		}

		public void itemStateChanged(ItemEvent arg0) {
			// TODO Auto-generated method stub
			tempEncoding = encode;
		}

	}
	
	private class EnocdeButtonHandler implements ItemListener { // 當選擇 RADIO
		// 按鈕時觸發
		private String encode;
		
		public EnocdeButtonHandler(boolean a) {
			if(a) encode="Unicode";
			else encode="UTF-8";
		}
		
		public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
			tempoutputEncoding = encode;
		}
	}
}
