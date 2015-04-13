package Obe.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadStop {
	private static ReadStop r = null;
	private static HashMap<String,Integer> stopWords = new HashMap<String,Integer>();

	public ReadStop(){
		System.out.println("reading...");
		LoadBigFile l = new LoadBigFile();
		l.setPath("F:/work/CH_Topic_Hierarchy/etc/stopwords_cn.txt");
		String ss = "";
		try {
			ss = l.Load();
		} catch (Exception e) {
			System.out.println("load error!");
			e.printStackTrace();
		}
		String[] text = ss.split("\r\n");
		for(int i=0;i<text.length;i++){
			stopWords.put(text[i].toLowerCase().trim(),1);
		}
		System.out.println("stop load finished!");
	}
	
	public static ReadStop getNew(){
		if(r==null)
			r = new ReadStop();
		return r;
	}
	
	public HashMap<String,Integer> getStop(){
		return stopWords;
	}
}
