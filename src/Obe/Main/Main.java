package Obe.Main;
/**
 * 到50行可以生成所有预处理的文件
 * 后面功能待定
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dao.AllTextEX;
import Obe.Dao.AllWordsMap;
import Obe.Dao.AtomSim;
import Obe.Dao.CountCossim;
import Obe.Dao.EditDistance;
import Obe.Dao.FreqOfWords;
import Obe.Dao.Preproccess;
import Obe.Dao.RemoveStopWords;
import Obe.Dao.TfIdf;
import Obe.Dao.TotalMergeText;
import Obe.Dao.VectorCossim;
import Obe.Dto.Edge;
import Obe.Dto.Node;
import Obe.Dto.Topic;
import Obe.Util.ReadVector;
import Obe.Util.Write;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class Main {
	
	private static String dir = GetPropertiesUtils.getInputDir();//"D:/study/crawl/crawl/etc/Election";
	private static List<List<Topic>> l = new ArrayList<List<Topic>>();
	//private static String dirR1 = GetPropertiesUtils.getoutPutDir()+"/result1.txt";//D:/study/crawl/crawl/etc/Electiondata/result1.txt";
	//private static String dirR2 = GetPropertiesUtils.getoutPutDir()+"/result2.txt";//"D:/study/crawl/crawl/etc/Electiondata/result2.txt";
	private static String dirR = GetPropertiesUtils.getoutPutDir()+"/CosSimOfTotal.txt";//"D:/study/crawl/crawl/etc/Electiondata/CosSimOfTotal.txt";
	private static String dirA = GetPropertiesUtils.getoutPutDir()+"/AtomSimOfTotal.txt";//"D:/study/crawl/crawl/etc/Electiondata/AtomSimOfTotal.txt";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file=new File(dir);
		String files[] = file.list();
		System.out.println("prepocessing...");
		for(int i=0;i<files.length;i++){
			Preproccess p = new Preproccess();
			List<Topic> list = p.preprocess(dir,files[i]);
			RemoveStopWords remove = new RemoveStopWords();
			list = remove.dealStopWords(list,files[i]);
			//FreqOfWords freq = new FreqOfWords();
			//list = freq.count(list);
			l.add(list);

		}
		count(l);
		
		//总的map统计频率分布
		System.out.println("creating totalMap...");
		AllWordsMap awm = new AllWordsMap();
		List<Topic> allWords = awm.create(l);
		allWords.add(0,null);
		FreqOfWords freq = new FreqOfWords();
		allWords = freq.count(allWords);
		TfIdf tfidf = new TfIdf();
		allWords = tfidf.count(allWords);
		
		//merge
		System.out.println("Mergeing...");
		TotalMergeText m = new TotalMergeText();
		m.setway(1);//非扩展文本
		m.Merge(allWords);
		m.MergeRoot(l);
//
//		m.setway(2);//扩展文本
		AllTextEX ate = new AllTextEX();
		l = ate.TextEX(l);
//		List<Topic> allWords2 = awm.create(l);
//		allWords2.add(0,null);
//		m.Merge(allWords2);
		//m.MergeRoot(l);
		
		file f = new file();
		file f2 = new file();

		System.out.println("counting cosine similarity...");
		f.setoutpath(dirR);f.delete();
		f.write("cosine similarity:\r\n");
		f2.setoutpath(dirA);f2.delete();
		f2.write("Atom similarity:\r\n");
		new ArrayList<Edge>();
		new HashMap<String,Node>();

		for(int k=1;k<allWords.size();k++){
			Topic t1 = allWords.get(k);
			if(!t1.getText().equals(""))
			for(int j=k+1;j<allWords.size();j++){
				Topic t2 = allWords.get(j);
				if(t2.getText().equals(""))
					continue;
				CountCossim cossim = new CountCossim();
				double val1 = cossim.getCos(t1, t2);
				if(val1==0){
					int h;
					h=0;
				}
				AtomSim as = new AtomSim();
				double val2 = as.count(t1, t2);
				f.write(k+":"+t1.getTopic()+" -AND- "+j+":"+t2.getTopic()+" sim:"+val1+"\r\n");
				f2.write(k+":"+t1.getTopic()+" -AND- "+j+":"+t2.getTopic()+" sim:"+val2+"\r\n");
//				if(!t1.getRoot().equals(t2.getRoot())){
//					if(val>=0.6){
//						f.write(t1.getRoot()+":"+t1.getTopic()+" -AND- "+t2.getRoot()+":"+t2.getTopic()+" cossim:"+val+"\r\n");
//						
//						Node node1 = new Node();
//						if(!node.containsKey(t1.getTopic())){
//							node1.setId(num);
//							node1.setTopic(t1);
//							node.put(t1.getTopic(),node1);
//							num++;
//						}
//						else{
//							node1 = node.get(t1.getTopic());
//						}
//						
//						Node node2 = new Node();
//						if(!node.containsKey(t2.getTopic())){
//							node2.setId(num);
//							node2.setTopic(t2);
//							node.put(t2.getTopic(),node2);
//							num++;
//						}
//						else{
//							node2 = node.get(t2.getTopic());
//						}
//
//						
//						Edge e = new Edge();
//						e.setSource(node1);
//						e.setTarget(node2);
//						e.setEdge(1);
//						edge.add(e);
//					}
//					//System.out.println(t1.getTopic()+" AND "+t2.getTopic()+" cossim:"+val);
//				}
			}
		}
//		System.out.println("writing Pajek file...");
//		Write w = new Write(dirNet);
//		w.writePajek(edge,node);
		System.out.println("finish!");
	}
	private static void count(List<List<Topic>> l) throws IOException {
		HashMap<String,Integer> map = new HashMap<String, Integer>();
		for(int i=0;i<l.size();i++){
			List<Topic> ll = l.get(i);
			for(int j=0;j<ll.size();j++){
				Topic t = ll.get(j);String str = "";
				if(t.getId()!=0)
					str = t.getTopic().toLowerCase().trim();
				else
					str = "root";
				if(map.containsKey(str)){
					int val = map.get(str);
					val++;
					map.put(str, val);
				}
				else
					map.put(str, 1);
			}
		}
		
		file f = new file();
		f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/times.txt");f.delete();
		f.write("topic times:\r\n");
		Iterator<Entry<String, Integer>> iterx = map.entrySet().iterator();
		while (iterx.hasNext()) {
			Map.Entry entry = (Map.Entry) iterx.next();
			String str = (String) entry.getKey();
			int val = (Integer) entry.getValue();
			f.write(str+": "+val+"\r\n");
		}
	}

}
