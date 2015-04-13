package Obe.Util;

public class GetTraining {
	private String dir = "D:/study/crawl/crawl/etc/En/training.txt";
	
	public String get(){
		file f = new file();
		f.setfilepath(dir);f.read();
		String text = f.returnS();
		text =text.replace("###", " ");
		text += "*";
		return text;
	}

}
