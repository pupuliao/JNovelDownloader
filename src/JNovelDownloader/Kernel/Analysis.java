package JNovelDownloader.Kernel;

public class Analysis {
	public Analysis() {

	}

	public static UrlData analysisUrl(String data) {
		//ck101.com/forum.php?mod=viewthread&tid=1961368&page=1
		UrlData result = new UrlData();
		System.out.println(data);
		result.urlString = data;
		String temp[] = data.split("/");
		if (temp.length > 4) {
			System.out.println("..");
			result.wrongUrl = true;
			return result;
		}
		result.page=1; //避免網址列中沒有頁碼
		result.domain = temp[temp.length - 2];
		System.out.println(result.domain);
		String A = temp[temp.length - 1];
		if (A.indexOf("forum.php?") >=0) {
			String[] temp2 = A.split("=|&");
			for (int n = 0; n < temp2.length; n++) {
				if (temp2[n].equals("tid"))
					result.Tid = Integer.parseInt(temp2[n + 1]);
				if (temp2[n].equals("page"))
					result.page = Integer.parseInt(temp2[n + 1]);
				
			}
			result.wrongUrl = false;
		} else if (A.matches("thread-[0-9]+-[0-9]+-[0-9].html")) {
			String[] temp2 = A.split("-");
			result.Tid = Integer.parseInt(temp2[1]);
			result.page = Integer.parseInt(temp2[2]);
		} else {
			result.wrongUrl = true;
		}

		return result;
	}
}
