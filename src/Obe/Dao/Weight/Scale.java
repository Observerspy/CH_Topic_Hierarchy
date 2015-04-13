package Obe.Dao.Weight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dao.Chu_Liu.Edge;
import Obe.Dao.Chu_Liu.Node;
import Obe.Util.LoadBigFile;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class Scale {
	private static HashMap<String,Double> eL = new HashMap<String,Double>();
	private static List<Double> weight = new ArrayList<Double>();

	public static void main(String[] args) throws Exception {
		LoadBigFile l2 = new LoadBigFile();
		l2.setPath(GetPropertiesUtils.getoutPutDir()+"/cluster_weight"+GetPropertiesUtils.getEx());
		String str2 = l2.Load();
		String[] text2 = str2.split("\r\n");
		for(int i=1;i<text2.length;i++){
			//String[] s = text2[i].split("-AND-");
			double w = Double.valueOf(text2[i].split(":")[1]);
			eL.put(text2[i].split(":")[0], w);
			weight.add(w);
		}
		
		double max = Collections.max(weight);
		double min = Collections.min(weight);
		
		file f = new file();
        f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/Cluster_Scale_Weight"+GetPropertiesUtils.getEx());f.delete();
		f.write("cluster weight:\r\n");
		
		Iterator<Entry<String, Double>> iter = eL.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			double val = (Double) entry.getValue();
			val = (val - min) / (max - min);
			f.write(key+":"+val+"\r\n");
		}
		System.out.println("finish!");

		
	}
}
