package JNovelDownloader;


import javax.swing.JFrame;

import JNovelDownloader.Kernel.Downloader;
import JNovelDownloader.Kernel.ReadHtml;
import JNovelDownloader.Option.Option;
import JNovelDownloader.UI.Frame;

public class main {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Option option=new Option();
		Downloader downloader=new Downloader();
		ReadHtml readHtml =new ReadHtml();
		Frame frame =new Frame(downloader,readHtml,option);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,350);
		frame.setVisible(true);
		//frame.popVersionAlert(option); //版本檢查掛了
		if(!option.checkPath()){
			frame.popPathAlert();
		}
		

//		int[][] d=new int[5][2];
		
//		String test1 ="第一章 殺手不好做，咱轉行吧";
//		System.out.println(test1.matches("第[一二三四五六七八九十百零1234567890 　]*章 [^<>]*"));

//		String[] temp2 = test1.split("class=\"postmessage\">");// 接取標題
//		// 如果有
//		// 會有內容，如果沒有是空字串
//		for(int n=0;n<temp2.length;n++){
//			System.out.println(n+"-"+temp2[n]+"\r\n");
//		}
//		String test2 = test1.replaceAll("<[^>]+>", "");
		//test1 = test1.replace("<[^>]+>", "");
//		test1=Replace.replace(test1, "<[^>]+>", "");
//		String[] test2=test1.split("/");
//		System.out.println(test1);
//		System.out.println(test2);
//		for(int n=0;n<test2.length;n++){
//			System.out.println(n+":"+test2[n]);
//		}
//		String test1 = "<title>叱吒風雲    作者:高樓大廈  (已完成) - 第55頁 - 長篇小說 -  卡提諾 - </title>";
//		String[] tempTittleString = test1.split("<title>|</title>");
//		String bookName = tempTittleString[1];
//		String test2 = test1.replaceAll("<[/]?title>", "");
//		System.out.println(test2);
//		String test2 = "<link href=\"http://ck101.com/thread-1753100-1-1.html\" rel=\"canonical\" />";
//		String test3 = "http://";
//		System.out.println(test1.indexOf("<title>"));
//		System.out.println(test2.indexOf("<title>"));
//		System.out.println(test3.equals(test1.substring(0, 7)));
//		System.out.println(test1.compareTo(test3));
	}
}