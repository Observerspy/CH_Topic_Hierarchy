package Obe.Dao.Incre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dao.Chu_Liu.Node;
import Obe.Dto.Concept;
import Obe.Util.Cnm;
import Obe.Util.GetAllText;
import Obe.Util.LoadBigFile;
import Obe.Util.ReadIncre;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class RandIndex {
	private static HashMap<Integer,List<String>> result = new HashMap<Integer,List<String>>();
	private static HashMap<Integer,List<String>> real = new HashMap<Integer,List<String>>();
	private static List<List<String>> realL = new ArrayList<List<String>>();
	private static HashMap<String,Integer> realF = new HashMap<String,Integer>();

	public static void main(String[] args) throws Exception {
		// for(int i=1;i<10;i++){
		result.clear();real.clear();realL.clear();realF.clear();
		ReadReal();//read real
		ReadIncre r = new ReadIncre();
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  
		//String d = df.format(0.05*i+0.05);
		String d = df.format(0.25);

		String str = "/IncreMean/IncreCluster_"+Double.valueOf(d)+".txt";
		//System.out.println(str);
		r.ReadIncre(str);//read result
		result = r.getMap();
		//write();
		
		double TPandFP = countTPandFP();
		double TPandFN = countTPandFN();
		double FPandTN = countFPandTN();
		double TP = countTP();
		
		double FP = TPandFP - TP;
		double FN = TPandFN - TP;
		double TN = FPandTN - FP;
		
		double P = TP/TPandFP;
		double R = TP/TPandFN;
		double F1 = 2*P*R/(P+R);
		double RI = (TP+TN)/(TPandFP+FN+TN);
		System.out.println(str);
		System.out.println("P: "+P+"\rR: "+R+"\rF1: "+F1+"\rRI: "+RI);
		 //}
	}


	private static void write() throws IOException {
		file f = new file();
		int count = 0;
		f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/IncreClusterAll.txt");f.delete();
		Iterator<Entry<Integer, List<String>>> iterx = result.entrySet().iterator();
		while (iterx.hasNext()) {
			Map.Entry entry = (Map.Entry) iterx.next();
			List<String> l = (List<String>) entry.getValue();
			for(int i=0;i<l.size();i++){
				f.write("cluster id: "+count+"concept: \r\n");
				f.write(l.get(i)+"#\r\n");
				count++;
			}
		}
	}


	private static double countTP() {
		double sum = 0.0;
    	Cnm nm = new Cnm();
		Iterator<Entry<Integer, List<String>>> iterx = result.entrySet().iterator();
		while (iterx.hasNext()) {
			Map.Entry entry = (Map.Entry) iterx.next();
			List<String> l = (List<String>) entry.getValue();
			HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
			for(int i=0;i<l.size();i++){
				if(realF.containsKey(l.get(i))){
					Integer id = realF.get(l.get(i));
					if(!map.containsKey(id))
						map.put(id, Integer.valueOf(1));
					else{
						int num = map.get(id);
						map.put(id, num+1);
					}
				}
			}
			Iterator<Entry<Integer, Integer>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry1 = (Map.Entry) iter.next();
				int num = (Integer) entry1.getValue();
				if(num>1){
					sum += nm.c(num, 2);
				}
			}
		}
		return sum;
	}


	private static double countFPandTN() {
		double sum = 0.0;
		for(int i=0;i<realL.size();i++){
			for(int j=i+1;j<realL.size();j++){
				sum += realL.get(i).size()*realL.get(j).size();
			}
		}
		return sum;
	}


	private static double countTPandFN() {
		double sum = 0.0;
    	Cnm nm = new Cnm();
		Iterator<Entry<Integer, List<String>>> iterx = real.entrySet().iterator();
		while (iterx.hasNext()) {
			Map.Entry entry = (Map.Entry) iterx.next();
			List<String> l = (List<String>) entry.getValue();
			sum += nm.c(l.size(), 2);
		}
		return sum;
	}


	private static double countTPandFP() {
		double sum = 0.0;
    	Cnm nm = new Cnm();
		Iterator<Entry<Integer, List<String>>> iterx = result.entrySet().iterator();
		while (iterx.hasNext()) {
			Map.Entry entry = (Map.Entry) iterx.next();
			List<String> l = (List<String>) entry.getValue();
			sum += nm.c(l.size(), 2);
		}
		return sum;
	}


	static void ReadReal() throws Exception {
		LoadBigFile l = new LoadBigFile();
		l.setPath(GetPropertiesUtils.getoutPutDir()+"/New_cluster_map_node.txt");
		String str = l.Load();
		String[] text = str.split("\r\n");
		for(int i=0;i<text.length;i++){		
			String[] t = text[i].split(" ");
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
