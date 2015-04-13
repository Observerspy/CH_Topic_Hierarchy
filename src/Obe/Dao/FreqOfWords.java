package Obe.Dao;
/**
 * 词频统计
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Obe.Dto.Topic;

public class FreqOfWords {
	private HashMap<String,Integer> map = new HashMap<String,Integer>();

	public List<Topic> count(List<Topic> list) {
		for(int i=1;i<list.size();i++){
			map.clear();
			Topic t = list.get(i);
			List<String> wordsMap = t.getMap();
			List<Double> freq = t.getP();
			String[] words = t.getText().split(" ");
			for(int k=0;k<words.length;k++){
				if(map.containsKey(words[k])){
					int val = map.get(words[k]);
					val++;
					map.put(words[k], val);
				}
				else{
					map.put(words[k], 1);
				}
			}
			HashMap<String,Integer> maps = new HashMap(map);
			t.setMaps(maps);
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry entry = (Map.Entry) iter.next();
			    wordsMap.add((String) entry.getKey());
			    double value =(double)( (Integer) entry.getValue()/(double)words.length );
			    freq.add(value);
			}
			t.setMap(wordsMap);
			t.setP(freq);
			list.set(i, t);
		}
		return list;
	}

}
