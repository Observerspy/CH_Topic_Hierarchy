package Obe.Dao.Put;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import Obe.Dao.CountCossim;
import Obe.Dao.Preproccess;
import Obe.Dao.RemoveStopWords;
import Obe.Dto.Concept;
import Obe.Dto.Topic;
import Obe.Util.GetAllText;
import Obe.Util.common.GetPropertiesUtils;

public class Main3 {
	private static String dir = GetPropertiesUtils.getInputDir()+"/TestingData";
	private static List<List<Topic>> l = new ArrayList<List<Topic>>();
	private static HashMap<String,Integer> mapA = new HashMap<String, Integer>();
	private static HashMap<String,Integer> mapX;//统一词表
	private static List<Concept> lc;
	private static Stack<List<Integer>> S = new Stack<List<Integer>>();
	private static int iterr = 10;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("reading concepts...");
		HashMap<String,Integer> mapB = GetAllText.getNew().getMap();//concept全文本词表及频数
		lc = GetAllText.getNew().getL();//concept组

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
		
		mapX = new HashMap<String, Integer>(mapA);
		Iterator<Entry<String,Integer>> iter = mapB.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String) entry.getKey();
			if(!mapX.containsKey(str)&&!str.equals("+")&&!str.equals("?")){
				mapX.put(str, 1);
			}
		}
		
		l = countFreqTopic(l);
		List<Concept> lc = GetAllText.getNew().getL();//concept组
		for(int i=0;i<lc.size();i++){
			Concept w = lc.get(i);
        	w.initialP();
        	w = count(w);
        	lc.set(i, w);
		}
		List<Concept> lcBack = new ArrayList<Concept>(lc);//更新用
		
		System.out.println("counting....");
		double ccc = 0.0;		int time = 0;

	do{
		time++;
		List<Integer> F = new ArrayList<Integer>();
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
			}
			F.add(flag);
			System.out.println(max);
			System.out.println(i+" : "+t.getTopic()+" is similar to "+lc.get(flag).getConcept());
			
		}
		lc = update(l,F,lcBack);
		if(S.size()==0)
			ccc = 0.0;
		else
			ccc = check(F,S.peek());
		S.push(F);
	 }while(ccc<0.8);
	System.out.println("rate:"+ccc);
	System.out.println("iter times:"+time);
		int m;
		m=0;
	}

	private static double check(List<Integer> f, List<Integer> list) {
		if(S.size()==0)
			return 0.0;
		else{
			int count = 0;
			for(int i=0;i<f.size();i++){
				if(f.get(i)==list.get(i))
					count++;
			}
			return (double)count/f.size();
		}
	}

	private static List<Concept> update(List<List<Topic>> l, List<Integer> f, List<Concept> lcBack) {
		List<Concept> lc = new ArrayList<Concept>(lcBack);//记录原始分布
		for(int i=0;i<l.size();i++){
			Topic t = l.get(i).get(1);
			int flag = f.get(i);
			Concept c = lc.get(flag);
			//Concept cx = lc.get(flag);

			HashMap<String, Double> tmap = t.getM();//词分布
			HashMap<String, Double> cmap = c.getP();//原始分布
			//HashMap<String, Double> cxmap = cx.getP();//目标分布
			Iterator<Entry<String, Double>> iter = tmap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String str = (String)entry.getKey();
				double val = (Double) entry.getValue() + cmap.get(str);
				cmap.put(str, val);
			}
			
			c.setP(cmap);//原始分布更新
			lc.set(flag, c);
		}
		return lc;
	}

	private static Concept count(Concept w) {
		int textLength = GetAllText.getNew().getLength();//文本长
		String concept = w.getText();//组描述
		int length = concept.split(" ").length;//描述长		
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


	private static List<List<Topic>> countFreqTopic(List<List<Topic>> l) {
		for(int i=0;i<l.size();i++){
			List<Topic> a = l.get(i);
			Topic t = a.get(1);
			HashMap<String, Double> wordsMap = t.getM();
			String content = t.getText() + "*";
			String[] text = t.getText().split(" ");
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
