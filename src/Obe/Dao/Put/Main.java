package Obe.Dao.Put;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dao.AllWordsMap;
import Obe.Dao.CountCossim;
import Obe.Dao.FreqOfWords;
import Obe.Dao.Preproccess;
import Obe.Dao.RemoveStopWords;
import Obe.Dto.Concept;
import Obe.Dto.Topic;
import Obe.Util.GetAllText;
import Obe.Util.common.GetPropertiesUtils;

public class Main {
	private static String dir = GetPropertiesUtils.getInputDir()+"/TestingData";
	private static List<List<Topic>> l = new ArrayList<List<Topic>>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		
		System.out.println("creating totalMap...");
		AllWordsMap awm = new AllWordsMap();
		List<Topic> allWords = awm.create(l);
		allWords.add(0,null);
		FreqOfWords freq = new FreqOfWords();
		allWords = freq.count(allWords);
		
		System.out.println("reading concepts...");
		List<Concept> lc = GetAllText.getNew().getL();//concept组
		for(int i=0;i<lc.size();i++){
			Concept w = lc.get(i);
        	w.initialP();
        	w = count(w);
        	lc.set(i, w);
		}
		
		List<String> lm =new ArrayList<String>();
		HashMap<String,Integer> map = GetAllText.getNew().getMap();//全文本词表及频数
		Iterator<Entry<String,Integer>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			lm.add((String) entry.getKey());
		}
		for(int i=1;i<allWords.size();i++){
			Topic t = allWords.get(i);
			double max = 0.0;int flag = 0;
			for(int j=0;j<lc.size();j++){
				Concept c = lc.get(j);
				CountCossim cossim = new CountCossim();
				double val = cossim.getCos3(t, c,lm);
				if(max<val){
					max = val;
					flag = j;
				}
			}
			System.out.println(i+" : "+t.getTopic()+" is similar to "+lc.get(flag).getConcept());
			int m;
			m=0;
		}

	}

	private static Concept count(Concept w) {
		int textLength = GetAllText.getNew().getLength();//文本长
		String concept = w.getText();//组描述
		int length = concept.split(" ").length;//描述长		
		HashMap<String,Integer> map = GetAllText.getNew().getMap();//全文本词表及频数
		Iterator<Entry<String,Integer>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String)entry.getKey();
			//System.out.println(str);
			concept += "*";str =" "+str+" ";
    		int count = 0;
			try{ count = concept.split(str.trim()).length-1;
    		//对应单词在描述中频数
    		}catch(Exception e){
    			System.out.println(concept+"---"+str);
    		}
    		double val = (double)(count)/(double)(length);
    		w.getP().put(str.trim(), val);
		}
		return w;
	}

}
