package Obe.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Obe.Util.common.GetPropertiesUtils;



public class ReadIncre {
	private static ReadIncre r = null;
	private static HashMap<Integer,List<String>> map = new HashMap<Integer,List<String>>();
	private static HashMap<String,Integer> map2 = new HashMap<String,Integer>();

	public void ReadIncre(String path){
		System.out.println("reading...");
		LoadBigFile l = new LoadBigFile();
		l.setPath(GetPropertiesUtils.getoutPutDir()+path);
		String ss = "";
		try {
			ss = l.Load();
		} catch (Exception e) {
			System.out.println("load error!");
			e.printStackTrace();
		}
		String[] text = ss.split("\r\n");
		for(int i=0;i<text.length;){
			List<String> con = new ArrayList<String>();
			//String[] a = text[i+1].split("#");
			//String id = text[i].split(":")[1].split("concept")[0].trim();
			String id = text[i].split(" ")[0].split(":")[1].trim();
			String[] a = text[i].split(" ")[1].split("#");
			for(int j=0;j<a.length;j++){
				con.add(a[j]);
				map2.put(a[j].toLowerCase(), Integer.valueOf(id));
			}
			map.put(Integer.valueOf(id),con);
			i=i+1;
		}
		System.out.println(" load finished!");

	}
	
	public static ReadIncre getNew() {
		if(r==null)
			r = new ReadIncre();
		return r;
	}
	
	public  HashMap<Integer, List<String>> getMap() {
		return map;
	}

	public  HashMap<String,Integer> getMap2() {
		return map2;
	}
}
