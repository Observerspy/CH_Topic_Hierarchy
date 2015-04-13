package Obe.Dao.Incre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Obe.Util.LoadBigFile;
import Obe.Util.Readt_r;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class CheckReal {
	private static HashMap<List<String>,Integer> real = new HashMap<List<String>,Integer>();
	private static List<List<String>> realL = new ArrayList<List<String>>();
	private static HashMap<String,Integer> realF = new HashMap<String,Integer>();
	private static HashMap<String,String> relation = new HashMap<String, String>();

	public static void main(String[] args) throws Exception {
		ReadReal();
		Readt_r r = new Readt_r();
		relation = r.getRelation();//检查是不是在一个文件里
		
		file f = new file();
		f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/real.txt");f.delete();
		for(int i=0;i<realL.size();i++){
			List<String> l = realL.get(i);
			f.write(real.get(l)+": ");
			for(int k=0;k<l.size();k++){
				String str1 = l.get(k);
				for(int j=k+1;j<l.size();j++){
					String str2 = l.get(j);
					if(check(str1,str2)){
						f.write(str1+","+str2+"#");
					}
				}					
			}
			f.write("\r\n");
		}
	}
	
	public static void ReadReal() throws Exception {
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
			real.put(con, Integer.valueOf(id));
			realL.add(con);
		}
	}
	
	private static boolean check(String str, String str1) {
		if(relation.containsKey(str)&&relation.containsKey(str1)){
		String root = relation.get(str).trim();
		//for(int i=0;i<cT.size();i++){
		String root1 = relation.get(str1).trim();
		if(root.equals(root1))
			return true;
		//}
		else
			return false;
		}
		System.out.println(str+"-AND-"+str1+"can not find!!");
		return true;
	}
}
