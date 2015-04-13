package Obe.Dao.Incre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Util.LoadBigFile;
import Obe.Util.ReadIncre;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class Java2Python {
	private static HashMap<String,Integer> result = new HashMap<String,Integer>();
	private static HashMap<Integer,List<String>> real = new HashMap<Integer,List<String>>();
	private static List<List<String>> realL = new ArrayList<List<String>>();
	private static HashMap<String,Integer> realF = new HashMap<String,Integer>();

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//for(int k=0;k<10;k++){
		result.clear();real.clear();realL.clear();realF.clear();
		ReadReal();//read real
		ReadIncre r = new ReadIncre();
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  
		//String d = df.format(0.05*k+0.05);
		//String d = df.format(1.0);
		String str = "/IncreMax/Kmeans.txt";
		//String str = "/IncreMax/IncreCluster_"+Double.valueOf(d)+".txt";
		r.ReadIncre(str);//read result
		result = r.getMap2();
		
		//check
		List<String> all = new ArrayList<String>();
		Iterator<Entry<String, Integer>> iter = realF.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			all.add(key);
			if(!result.containsKey(key)){
				System.out.println(key);
			}
		}
		
		file f = new file();
		f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/IncreMax/label_k.true");f.delete();
		//f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/IncreMax/label_k"+Double.valueOf(d)+".true");f.delete();
		for(int i=0;i<all.size();i++){
			int id = realF.get(all.get(i));
			f.write(id+" ");
		}
		f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/IncreMax/label_k.pred");f.delete();
		//f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/IncreMax/label_k"+Double.valueOf(d)+".pred");f.delete();
		for(int i=0;i<all.size();i++){
			int id = result.get(all.get(i));
			f.write(id+" ");
		}
		int x;
		x=0;
		//}
	}
	
	static void ReadReal() throws Exception {
		LoadBigFile l = new LoadBigFile();
		l.setPath(GetPropertiesUtils.getoutPutDir()+"/IncreMax/Final_New_cluster_map_node.txt");
		String str = l.Load();
		String[] text = str.split("\r\n");
		for(int i=0;i<text.length;i++){		
			String[] t = text[i].split(" ");
			//String id = t[0].split(":")[0].trim();
			String id = t[0].split(":")[1].trim();
			String[] a = text[i].split("#");
			List<String> con = new ArrayList<String>();
			for(int j=1;j<a.length;j++){
				con.add(a[j].toLowerCase());
				realF.put(a[j].toLowerCase(), Integer.valueOf(id));
			}
			real.put(Integer.valueOf(id), con);
			realL.add(con);
		}
	}

}
