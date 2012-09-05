package JNovelDownloader;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import JNovelDownloader.Kernel.Downloader;
import JNovelDownloader.Kernel.ReadHtml;
import JNovelDownloader.Option.About;
import JNovelDownloader.Option.Option;
import JNovelDownloader.UI.Frame;

public class main {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Option option=new Option();
		Downloader downloader=new Downloader();
		ReadHtml readHtml =new ReadHtml();
		Frame frame =new Frame(downloader,readHtml,option);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,350);
		frame.setVisible(true);
		
		String test1 ="<div id=\"postmessage_81898396\" class=\"postmessage\"><font color=\"DarkOrchid\">第九百五十四節大結局<br />";
		String[] temp2 = test1.split("class=\"postmessage\">");// 接取標題
		// 如果有
		// 會有內容，如果沒有是空字串
		for(int n=0;n<temp2.length;n++){
			System.out.println(n+"-"+temp2[n]+"\r\n");
		}
//		test1 = test1.replace("<[^>]+>", "");
//		String[] test2=test1.split("/");
//		System.out.println(test1);
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