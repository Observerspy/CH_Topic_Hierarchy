package Obe.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dao.Chu_Liu.Node;
import Obe.Util.LoadBigFile;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class Compare {
	private static HashMap<String,Integer> map1 = new HashMap<String, Integer>();
	private static HashMap<String,Integer> map2 = new HashMap<String, Integer>();
	private static HashMap<String,Node> nMap = new HashMap<String,Node>();
	private static List<Node> nL = new ArrayList<Node>();
	private static HashMap<String,String> nMapb = new HashMap<String,String>();


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//读点
		LoadBigFile l = new LoadBigFile();
		//String temp = GetPropertiesUtils.getoutPutDir()+"/cluster_map_node.txt";
		l.setPath(GetPropertiesUtils.getoutPutDir()+"/cluster_map_node.txt");
		String str = l.Load();
		String[] text = str.split("\r\n");
		for(int i=0;i<text.length;i++){
			String[] s = text[i].split(" ",2);
			//int ID = Integer.valueOf(s[0].split(":")[1].trim());
			Node n = new Node();
			String[] name = s[1].split("#",2);
			//name[1] = name[1].substring(0, name[1].length()-1);
			name[1] = name[1].substring(0, name[1].length());

			if(name[1].equals("root"))
				n.setID(0);
			else
				n.setID(i+1);
			n.setConcept(name[1].toLowerCase());
			nMap.put(name[1].toLowerCase(),n);
		}
		for(int i=0;i<nMap.size();i++)
			nL.add(null);
		Iterator<Entry<String, Node>> iterx = nMap.entrySet().iterator();
		while (iterx.hasNext()) {
			Map.Entry entry = (Map.Entry) iterx.next();
			Node t = (Node) entry.getValue();
			nL.set(t.getID(),t);
		}
		for(int i=0;i<nL.size();i++)
			nMapb.put(String.valueOf(nL.get(i).getID()+1), nL.get(i).getConcept().replaceAll(" ", "_"));
		
		file f = new file();
		f.setfilepath(GetPropertiesUtils.getoutPutDir()+"/netC.net");f.read();
		String textC = f.returnS();
		String[] t = textC.split("###");
		for(int i=1;i<t.length;i++){
			map1.put(t[i].trim(), 0);
		}
		
		f.setfilepath(GetPropertiesUtils.getoutPutDir()+"/netX.net");f.read();
		String textCX = f.returnS();
		String[] tX = textCX.split("###");
		for(int i=1;i<tX.length;i++){
			map2.put(tX[i].trim(), 0);
		}
		
		List<String> A = new ArrayList<String>();
		Iterator<Entry<String, Integer>> iter = map1.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String strr = (String) entry.getKey();
			if(!map2.containsKey(strr))
				A.add(strr);
		}
		
		List<String> B = new ArrayList<String>();
		Iterator<Entry<String, Integer>> iter1 = map2.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry entry = (Map.Entry) iter1.next();
			String strr = (String) entry.getKey();
			if(!map1.containsKey(strr))
				B.add(strr);
		}
		
		f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/com.txt");f.delete();
		f.write("联合-父子:\r\n");
		for(int i=0;i<A.size();i++){
			String[] tr = A.get(i).split(" "); 
//			f.write(tr[0].trim()+" -AND- ");
//			f.write(tr[1].trim()+"\r\n");
			f.write(nMapb.get(tr[0].trim())+" -AND- ");
			f.write(nMapb.get(tr[1].trim())+"\r\n");
		}
		f.write("=====================华丽的分割线=====================\r\n");
		f.write("父子-联合:\r\n");
		for(int i=0;i<B.size();i++){
			String[] tr = B.get(i).split(" ");
//			f.write(tr[0].trim()+" -AND- ");
//			f.write(tr[1].trim()+"\r\n");
			f.write(nMapb.get(tr[0].trim())+" -AND- ");
			f.write(nMapb.get(tr[1].trim())+"\r\n");
		}
	}

}
