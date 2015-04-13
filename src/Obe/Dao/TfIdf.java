package Obe.Dao;

import java.util.HashMap;
import java.util.List;

import Obe.Dto.Topic;

public class TfIdf {
	private int size = 0;
	
	public List<Topic> count(List<Topic> list){
		size = list.size();
		for(int i=1;i<list.size();i++){
			Topic t = list.get(i);
			List<String> map = t.getMap();
			List<Double> f = t.getP();
			for(int j=0;j<map.size();j++){
				int count = getIDF(map.get(j),list);
				double idf = Math.log(size)-Math.log(count+1);
				double tf = f.get(j);
				f.set(j, tf*idf);
			}
		}
		return list;
		
	}

	private int getIDF(String str, List<Topic> list) {
		int count = 0;
		for(int i=1;i<list.size();i++){
			Topic t = list.get(i);
			HashMap<String,Integer> map = t.getMaps();
			if(map.containsKey(str))
				count++;
		}
		return count;
	}

}
