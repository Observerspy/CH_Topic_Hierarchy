package Obe.Dao.Put;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dao.CountCossim;
import Obe.Dao.Preproccess;
import Obe.Dao.RemoveStopWords;
import Obe.Dto.Concept;
import Obe.Dto.Topic;
import Obe.Util.GetAllText;
import Obe.Util.common.GetPropertiesUtils;

public class Main2 {
	private static String dir = GetPropertiesUtils.getInputDir()+"/TestingData";
	private static List<List<Topic>> l = new ArrayList<List<Topic>>();
	private static HashMap<String,Integer> mapA = new HashMap<String, Integer>();
	private static HashMap<String,Integer> mapX;//统一词表
	private static List<Concept> lc;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("reading concepts...");
		HashMap<String,Integer> mapB = GetAllText.getNew().getMap();//concept全文本词表及频数
		lc = GetAllText.getNew().getL();//concept组

		//mapB = sort(mapB);
		//lc = GetAllText.getNew().getL();//concept组

		File file=new File(dir);
		String files[] = file.list();
		System.out.println("testingdata prepocessing...");
		for(int i=0;i<files.length;i++){
			//we need a new program to preprocess it!shit!
			Preproccess p = new Preproccess();
			List<Topic> list = p.preprocess2(dir,files[i]);
			RemoveStopWords remove = new RemoveStopWords();
			list = remove.dealStopWords(list,files[i]);
//			FreqOfWords freq = new FreqOfWords();
//			list = freq.count(list);
			l.add(list);
		}
		
		WordMap wm = new WordMap();
		mapA = wm.create(l);
		//mapA = sort(mapA);
		mapX = new HashMap<String, Integer>(mapA);
		Iterator<Entry<String,Integer>> iter = mapB.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String) entry.getKey();
			if(!mapX.containsKey(str)&&!str.equals("+")&&!str.equals("?")){
				mapX.put(str, 1);
			}
		}
		
		int totalSize = lc.size()+l.size();
		
		l = countFreqTopic(l);//tf
		l = idf(l,totalSize);
		
		for(int i=0;i<lc.size();i++){
			Concept w = lc.get(i);
        	w.initialP();
        	w = count(w);//ti
        	w = idf(w,totalSize);
        	lc.set(i, w);
		}
		
		
		for(int i=0;i<l.size();i++){
			Topic t = l.get(i).get(1);
			double max = 0.0;int flag = 0;
			for(int j=0;j<lc.size();j++){
				Concept c = lc.get(j);
				CountCossim cossim = new CountCossim();
				double val = cossim.getCos4(t, c);
				if(max<val){
					max = val;
					flag = j;
				}
				//System.out.println(val);

			}
			System.out.println(max);
			System.out.println(i+" : "+t.getTopic()+" is similar to "+lc.get(flag).getConcept());
			
		}
		
		int m;
		m=0;
	}

	private static Concept idf(Concept w, int totalSize) {
		HashMap<String, Double> wordsMap = w.getP();
		Iterator<Entry<String,Integer>> iter = mapX.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String)entry.getKey();
			int count = getIDF(str);
			double val = wordsMap.get(str);
			double idf = Math.log(totalSize)-Math.log(count+1);
			wordsMap.put(str, val*idf);
			}
		w.setP(wordsMap);
		return w;
	}

	private static List<List<Topic>> idf(List<List<Topic>> l, int totalSize) {
		for(int i=0;i<l.size();i++){
			List<Topic> a = l.get(i);
			Topic t = a.get(1);
			HashMap<String, Double> wordsMap = t.getM();
			Iterator<Entry<String,Integer>> iter = mapX.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String str = (String)entry.getKey();
				int count = getIDF(str);
				double val = wordsMap.get(str);
				double idf = Math.log(totalSize)-Math.log(count+1);
				wordsMap.put(str, val*idf);
				}
			t.setM(wordsMap);
			a.set(1, t);
			l.set(i, a);
		}
		return l;
	}

	private static HashMap<String, Integer> sort(HashMap<String, Integer> map) {


List<Entry<String,Integer>> list = new ArrayList<Entry<String,Integer>>(map.entrySet());   
  
Collections.sort(list, new Comparator<Object>(){   
          public int compare(Object e1, Object e2){   
        int v1 = Integer.parseInt(((Entry<String,Integer>)e1).getValue().toString());   
        int v2 = Integer.parseInt(((Entry)e2).getValue().toString());   
        return v2-v1;   
           
    }   
});   
  
//for (Entry<String, Integer> e: list){   
//    System.out.println(e.getKey()+"  "+e.getValue());   
//}
        map = new HashMap<String, Integer>();
      for (int i=0;i<50;i++){   
      map.put(list.get(i).getKey(), list.get(i).getValue());   
  }
		return map;
	}

	private static Concept count(Concept w) {
		int textLength = GetAllText.getNew().getLength();//文本长
		String concept = w.getText();//组描述
		int length = concept.split(" ").length;//描述长		
		String[] text = concept.split(" ");
		//求每一个concept的map
		HashMap<String,Integer> Map = new HashMap<String,Integer>();
		for(int k=0;k<text.length;k++){
			if(Map.containsKey(text[k])){
				int val = Map.get(text[k]);
				val++;
				Map.put(text[k], val);
			}
			else{
				Map.put(text[k], 1);
			}
		}
		w.setMaps(Map);
		//求该concept在全词表上的tf
		Iterator<Entry<String,Integer>> iter = mapX.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String)entry.getKey();
			//System.out.println(str);
			concept += "*";str =" "+str+" ";
    		int count = 0;
			try{ count = concept.split(str.trim()).length-1;
    		//对应单词在描述中频数
    		}catch(Exception e){
    			System.out.println(str);
    		}
    		double val = (double)(count)/(double)(length);
    		w.getP().put(str.trim(), val);
		}
		return w;
	}
	
	private static int getIDF(String str) {
		int count = 0;
		//新文档
		for(int i=0;i<l.size();i++){
			List<Topic> a = l.get(i);
			Topic t = a.get(1);
			HashMap<String,Integer> map = t.getMaps();
			if(map.containsKey(str))
				count++;
		}
		//标注集
		for(int i=0;i<lc.size();i++){
			Concept w = lc.get(i);
			HashMap<String,Integer> map = w.getMaps();
			if(map.containsKey(str))
				count++;
		}
		return count;
	}

	private static List<List<Topic>> countFreqTopic(List<List<Topic>> l) {
		for(int i=0;i<l.size();i++){
			List<Topic> a = l.get(i);
			Topic t = a.get(1);
			HashMap<String, Double> wordsMap = t.getM();
			HashMap<String,Integer> Map = new HashMap<String,Integer>();
			String content = t.getText() + "*";
			//每一个topic的map
			String[] text = t.getText().split(" ");
			for(int k=0;k<text.length;k++){
				if(Map.containsKey(text[k])){
					int val = Map.get(text[k]);
					val++;
					Map.put(text[k], val);
				}
				else{
					Map.put(text[k], 1);
				}
			}
			t.setMaps(Map);
			//每一个topic在全词表上的分布
			Iterator<Entry<String,Integer>> iter = mapX.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String str = (String) entry.getKey();
	    		int count = 0;
				try{ count = content.split(str.trim()).length-1;
	    		//对应单词在描述中频数
	    		}catch(Exception e){
	    			System.out.println(str);
	    		}
				wordsMap.put(str, (double)count/(double)(text.length));
			}
			t.setM(wordsMap);
			a.set(1, t);
			l.set(i, a);
		}
		return l;
	}

}
