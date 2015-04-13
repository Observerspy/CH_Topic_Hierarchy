package Obe.Dao.Weight;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dto.Concept;
import Obe.Util.GetAllText;

public class KLCount {
	public double countKLWeight(Concept concept,Concept concept2){
		HashMap<String,Integer> map = GetAllText.getNew().getMap();//全文本词表及频数
		Iterator<Entry<String,Integer>> iter = map.entrySet().iterator();
		int i=0;double sum = 0.0;
		while (iter.hasNext()) {
			i++;
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String)entry.getKey();
			
			double val = concept.getP().get(str)*(Math.log(concept.getP().get(str))-Math.log(concept2.getP().get(str)));
			sum += val;
		}
		return sum;
	}
}
