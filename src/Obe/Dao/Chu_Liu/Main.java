package Obe.Dao.Chu_Liu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Util.LoadBigFile;
import Obe.Util.file;

public class Main {
	private static HashMap<String,Node> nMap = new HashMap<String, Node>();
	private static List<Edge> eL = new ArrayList<Edge>();
	private static List<Node> nL = new ArrayList<Node>();


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//读点
		LoadBigFile l = new LoadBigFile();
		l.setPath("D:/study/crawl/crawl/etc/En/data/test_node.txt");
		String str = l.Load();
		String[] text = str.split("\r\n");
		for(int i=0;i<text.length;i++){
			String[] s = text[i].split(" ",2);
			int ID = Integer.valueOf(s[0].split(":")[1].trim());
			Node n = new Node();
			String[] name = s[1].split("#",2);
			name[1] = name[1].substring(0, name[1].length()-1);
			n.setID(ID);
//			if(name[1].equals("root"))
//				n.setID(0);
//			else
//				n.setID(i+1);
			n.setConcept(name[1].toLowerCase());
			nMap.put(name[1].toLowerCase(),n);
		}
		//读边
		HashMap<String, Double> eback1 = new  HashMap<String, Double>();

		LoadBigFile l2 = new LoadBigFile();
		l2.setPath("D:/study/crawl/crawl/etc/En/data/test.txt");
		String str2 = l2.Load();
		String[] text2 = str2.split("\r\n");
		for(int i=1;i<text2.length;i++){
			String[] s = text2[i].split("-AND-");
			String a = s[0].trim().toLowerCase();
			String b = s[1].split(":")[0].trim().toLowerCase();
			double w = Double.valueOf(s[1].split(":")[1]);
			Node n1 = nMap.get(a); 
			Node n2 = nMap.get(b);
			Edge e = new Edge();
			e.setSource(n1.getID());
			e.setTarget(n2.getID());
			e.setWeight(-w);
			eL.add(e);
			eback1.put(e.getSource()+"->"+e.getTarget(),e.getWeight());

		}
		file f = new file();
//		for(int i=0;i<nMap.size();i++)
//			nL.add(null);
		f.setoutpath("D:/study/crawl/crawl/etc/En/data/net.net");f.delete();
		
		for(int i=0;i<nMap.size();i++){
			for(int k=0;k<nMap.size();k++){
				if(eback1.containsKey(i+"->"+k)){
					f.write(eback1.get(i+"->"+k)+" ");
				}
				else
					f.write("0 ");
			}
			f.write("\r\n");
		}
		
		f.write("*Vertices "+nMap.size()+"\r\n");
		Iterator<Entry<String,Node>> iter = nMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Node t = (Node) entry.getValue();
			nL.add(t);
			f.write(t.getID()+1+" "+t.getConcept()+"\r\n");
//			nL.set(t.getID(),t);
		}
//		for(int i=0;i<nL.size();i++)
//			f.write(nL.get(i).getID()+1+" "+nL.get(i).getConcept()+"\r\n");

//		Chu_Liu cl = new Chu_Liu();
//		double val = cl.Directed_MST(nL, eL, 0);
//		System.out.println("weight: "+val);

		Chu_Liu2 cl2 = new Chu_Liu2();
		List<Edge> eee = cl2.Directed_MST(nL, eL, 0);
		for(int i=0;i<eee.size();i++){
			Edge edge = eee.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			System.out.println(s+"-->"+t);
		}
	}

}
