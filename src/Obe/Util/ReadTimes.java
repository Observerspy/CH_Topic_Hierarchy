package Obe.Util;

import java.util.HashMap;

import Obe.Util.common.GetPropertiesUtils;

public class ReadTimes {
	private String dir = GetPropertiesUtils.getoutPutDir()+"/times.txt";
	private HashMap<String,Integer> relation = new HashMap<String,Integer>();

	public HashMap<String,Integer> getTimes(){
		file f = new file();
		f.setfilepath(dir);f.read();
		String text = f.returnS();
		String[] t = text.split("###");
		for(int i=2;i<t.length;i++){
			String[] str = t[i].split(":");
			relation.put(str[0].trim(),Integer.valueOf(str[1].trim()));
		}
		return relation;
	}
}
