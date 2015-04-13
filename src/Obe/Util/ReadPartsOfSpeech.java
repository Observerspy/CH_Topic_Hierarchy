package Obe.Util;

import java.util.HashMap;

public class ReadPartsOfSpeech {

		private static ReadPartsOfSpeech r = null;
		private static HashMap<String,Integer> partsOfSpeech = new HashMap<String,Integer>();

		public ReadPartsOfSpeech(){
			System.out.println("reading...");
			LoadBigFile l = new LoadBigFile();
			l.setPath("F:/work/CH_Topic_Hierarchy/etc/partsOfSpeech.txt");
			String ss = "";
			try {
				ss = l.Load();
			} catch (Exception e) {
				System.out.println("load error!");
				e.printStackTrace();
			}
			String[] text = ss.split("\r\n");
			for(int i=0;i<text.length;i++){
				partsOfSpeech.put(text[i].toLowerCase().trim(),1);
			}
			System.out.println("stop load finished!");
		}
		
		public static ReadPartsOfSpeech getNew(){
			if(r==null)
				r = new ReadPartsOfSpeech();
			return r;
		}
		
		public HashMap<String,Integer> getParts(){
			return partsOfSpeech;
		}
	

}
