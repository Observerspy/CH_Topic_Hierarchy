package Obe.Dao.Put;

import java.util.HashMap;
import java.util.List;

import Obe.Dto.Topic;

public class WordMap {
	private HashMap<String,Integer> map = new HashMap<String, Integer>();
	
	public HashMap<String,Integer> create(List<List<Topic>> l){
		String context = "";
		for(int i=0;i<l.size();i++){
			context += l.get(i).get(1).getText();
		}
		String[] text = context.split(" ");
		for(int i=0;i<text.length;i++){
			String str = text[i];
			if(map.containsKey(str)){
				int val = map.get(str);
				map.put(str, val+1);
			}
			else{
				if(!str.equals("?")){
					map.put(str, 1);
				}

			}
		}
		return map;
		
	}
}
