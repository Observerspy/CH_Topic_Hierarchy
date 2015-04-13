package Obe.Dao.Chu_Liu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Util.LoadBigFile;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class Main2 {
	private static HashMap<String,Node> nMap = new HashMap<String, Node>();
	private static HashMap<String,Double> eMap = new HashMap<String, Double>();

	private static List<Edge> eL = new ArrayList<Edge>();
	private static List<Node> nL = new ArrayList<Node>();


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		//读点
		LoadBigFile l = new LoadBigFile();
		//String temp = GetPropertiesUtils.getoutPutDir()+"/cluster_map_node.txt";
		//l.setPath(GetPropertiesUtils.getoutPutDir()+"/Final_New_cluster_map_node.txt");
		l.setPath(GetPropertiesUtils.getoutPutDir()+"/IncreCluster_0.2.txt");

		String str = l.Load();
		String[] text = str.split("\r\n");
		for(int i=0;i<text.length;i++){
			String[] s = text[i].split(" ",2);
			String[] name = s[1].split("#",2);
			name[1] = name[1].substring(0, name[1].length());
			
			//String name = text[i+1];
			Node n = new Node();
			if(name[1].equals("root"))
				n.setID(0);
			else
				n.setID(i+1);
			n.setConcept(name[1].toLowerCase());
			nMap.put(name[1].toLowerCase(),n);
		}
		//读边
		HashMap<String, Double> eback1 = new  HashMap<String, Double>();

		LoadBigFile l2 = new LoadBigFile();
		l2.setPath(GetPropertiesUtils.getoutPutDir()+"/cluster_weight"+GetPropertiesUtils.getEx());
		String str2 = l2.Load();
		String[] text2 = str2.split("\r\n");
		for(int i=1;i<text2.length;i++){
			String[] s = text2[i].split("-AND-");
			String a = s[0].trim().toLowerCase();
			String b = s[1].split(":")[0].trim().toLowerCase();
			double w = Double.valueOf(s[1].split(":")[1]);
			eMap.put(a+" -AND- "+b, w);
			Node n1 = nMap.get(a); 
			Node n2 = nMap.get(b);
			if(n1!=null&&n2!=null){
			Edge e = new Edge();
			e.setSource(n2.getID());
			e.setTarget(n1.getID());
			e.setWeight(w);
			eL.add(e);
			eback1.put(e.getSource()+"->"+e.getTarget(),e.getWeight());
			}
		}
		for(int i=0;i<nMap.size();i++)
			nL.add(null);
		file f = new file();
		f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/netn.net");f.delete();
//		for(int i=0;i<nMap.size();i++){
//			for(int k=0;k<nMap.size();k++){
//				if(eback1.containsKey(i+"->"+k)){
//					f.write(eback1.get(i+"->"+k)+" ");
//				}
//				else
//					f.write("0 ");
//			}
//			f.write("\r\n");
//		}
		
		f.write("*Vertices "+nMap.size()+"\r\n");
		Iterator<Entry<String,Node>> iter = nMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Node t = (Node) entry.getValue();
			nL.set(t.getID(),t);
		}
		for(int i=0;i<nL.size();i++){
			if(nL.get(i).getConcept().length()<=15)
				f.write(nL.get(i).getID()+1+" _"+nL.get(i).getConcept().replaceAll(" ", "_")+"\r\n");
			else
				f.write(nL.get(i).getID()+1+" _"+nL.get(i).getConcept().substring(0,16).replaceAll(" ", "_")+"\r\n");
		}
//		Chu_Liu cl = new Chu_Liu();
//		double val = cl.Directed_MST(nL, eL, 0);
//		System.out.println("weight: "+-val);
		HashMap<String, Double> eback = new  HashMap<String, Double>();
		for(int i=0;i<eL.size();i++){
			Edge ed = eL.get(i);
			eback.put(ed.getSource()+"->"+ed.getTarget(),ed.getWeight());
		}
		Chu_Liu2 cl2 = new Chu_Liu2();
		List<Edge> eee = cl2.Directed_MST(nL, eL, 0);
		double sum = 0.0;
		f.write("*Edges"+"\r\n");
		//file f2 = new file();
		//f2.setoutpath(GetPropertiesUtils.getoutPutDir()+"/netX.net");f2.delete();
		for(int i=0;i<eee.size();i++){
			Edge edge = eee.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			f.write((s+1)+" "+(t+1)+"\r\n");
			//f2.write((s+1)+" "+(t+1)+"\r\n");
			System.out.println("ID:"+i+" "+s+"->"+t);
			sum += eback.get(s+"->"+t);
		}
		System.out.println("check:"+sum);
		

	}

}
