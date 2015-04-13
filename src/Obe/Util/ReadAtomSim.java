package Obe.Util;

import java.util.HashMap;

import Obe.Util.common.GetPropertiesUtils;

public class ReadAtomSim {
	
	private static ReadAtomSim r = null;
	private static HashMap<String,Double> map = new HashMap<String, Double>();
	//private static HashMap<Integer,String> map2 = new HashMap<Integer, String>();

	public ReadAtomSim() {
		System.out.println("reading...");
//		file f = new file();
//		f.setfilepath("D:/study/crawl/crawl/etc/En/CosSimOfTotal.txt");
//		f.read();
//		System.out.println("read finish!");
//		String[] text = f.returnS().split("###");
		LoadBigFile l = new LoadBigFile();
		l.setPath(GetPropertiesUtils.getoutPutDir()+"/AtomSimOfTotal.txt");
		String ss = "";
		try {
			ss = l.Load();
		} catch (Exception e) {
			System.out.println("load error!");
			e.printStackTrace();
		}
		String[] text = ss.split("\r\n");
		for(int i=2;i<text.length;i++){
			//System.out.println(i);
			Double val = Double.valueOf(text[i].split("sim:")[1].trim());
			String[] str = text[i].split("sim:")[0].split(" -AND- ");
			String a = str[0].split(":")[1].trim();
			String b = str[1].split(":")[1].trim();
			String s = a+"+"+b;
			map.put(s, val);
		}
		System.out.println("atom load finished!");
//		System.out.println("reading total2");
//		file f = new file();
//		f.setfilepath("D:/study/crawl/crawl/etc/En/total_map2.txt");		
//		f.read();
//		String ttt = f.returnS();
//		String t[] = ttt.split("###");
//		for(int i=1;i<t.length;i++){
//			map2.put(Integer.valueOf(t[i].split(":")[0].trim()), t[i].split(":")[1].trim());
//		}

	}
	
	public static ReadAtomSim getNew() {
		if(r==null)
			r = new ReadAtomSim();
		return r;
	}

	public  HashMap<String,Double> getMap() {
		return map;
	}

//	public static HashMap<Integer,String> getMap2() {
//		return map2;
//	}



}
