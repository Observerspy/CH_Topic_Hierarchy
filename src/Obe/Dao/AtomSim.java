package Obe.Dao;

import java.util.HashMap;

import Obe.Dto.Topic;

public class AtomSim {

	public double count(Topic t1, Topic t2) {
		if(t1.getTopicWithoutStop().equals("")||t1.getTopicWithoutStop().equals(""))
			return 0;
		
		String[] set1 = t1.getTopicWithoutStop().split(" ");
		String[] set2 = t2.getTopicWithoutStop().split(" ");

		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(int i=0;i<set1.length;i++){
			map.put(set1[i],0);
		}
		int count = 0;
		for(int i=0;i<set2.length;i++){
			if(map.containsKey(set2[i]))
				count++;
		}
		double val = (double)(2*count)/(double)(set1.length+set2.length);
		if(val>0)
			System.out.println( t1.getTopicWithoutStop()+"----"+ t2.getTopicWithoutStop());
		return val;
	}

}
