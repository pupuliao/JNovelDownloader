package JNovelDownloader.Kernel;

public class Replace {
	public Replace() {
		// TODO Auto-generated constructor stub
	}

	public static String replace(String str, String patten, String replacement) {
		// str 要置換的字串 把patten換成 replacement
		int pos = str.indexOf(patten);
		return pos < 0 ? str : _replace(str, patten, replacement, pos);
	}
/**演算法說明
 * 
 * @param str 資料
 * @param patten 要換掉的文字
 * @param replacement 要改成的文字
 * @param pos 那段文字在哪
 * @return 
 * 1. 從頭開始找出符合的字串，並且標示第幾個字放進pos
 * 2. 先把在pos前面的字放進結果區(newContent)
 * 3. 再放進replacement
 * 4. 再去找下一個字的位置放進pos 
 * 5. 接續步驟三直到全部找完為止
 */
	public static String _replace(String str, String patten,
			String replacement, int pos) {
		int len = str.length();
		int plen = patten.length();
		StringBuilder newContent = new StringBuilder(len);

		int lastPos = 0;

		do {
			newContent.append(str, lastPos, pos);
			newContent.append(replacement);
			lastPos = pos + plen;
			pos = str.indexOf(patten, lastPos);
		} while (pos > 0);
		newContent.append(str, lastPos, len);
		return newContent.toString();
	}
}
