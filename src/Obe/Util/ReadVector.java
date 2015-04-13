package Obe.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadVector {
	private String dir = "D:/study/crawl/crawl/etc/En/vectors300.bin";
	private HashMap<String,List<Double>> table = new HashMap<String, List<Double>>();
	
	public HashMap<String,List<Double>> getVector(){
		file f = new file();
		f.setfilepath(dir);f.read();
		String text = f.returnS();
		String[] t = text.split("###");
		for(int i=2;i<t.length;i++){
			String[] vec = t[i].split(" ");
			List<Double> list = new ArrayList<Double>();
			for(int k=0;k<300;k++){
				list.add(Double.valueOf(vec[k+1]));
			}
			table.put(vec[0], list);
		}
		return table;
	}

}
