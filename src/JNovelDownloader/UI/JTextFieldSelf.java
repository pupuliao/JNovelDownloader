package JNovelDownloader.UI;

import java.awt.im.InputMethodRequests;

import javax.swing.JTextField;
 
public class JTextFieldSelf extends JTextField {
 
  /**
   * 
   */
//  private static final long serialVersionUID = 6029092769322354225;
 
  public JTextFieldSelf() {
    super();
  }
 
  public JTextFieldSelf(String a,int b) {
	    super(a,b);
	  }
  public InputMethodRequests getInputMethodRequests() {
    return null; // 由active client變為passive client
  }
 
}