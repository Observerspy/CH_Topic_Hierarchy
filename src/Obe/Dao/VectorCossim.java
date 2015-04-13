package Obe.Dao;
/**
 * W2V相似度计算
 * ways两种方法
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Obe.Dto.Topic;

public class VectorCossim {
	private int D = 300;//维数
	private int ways;//两种方法
	
	public void setWays(int ways){
		this.ways = ways;
	}

	public List<Topic> count(List<Topic> allWords, HashMap<String, List<Double>> table) {
		for(int i=1;i<allWords.size();i++){
			Topic topic = allWords.get(i);
			String concept = topic.getTopic();
			String[] t = concept.split(" ");
			List<Double> list = new ArrayList<Double>(D);
			List<List<Double>> ll = new ArrayList<List<Double>>(D);

			for(int k=0;k<t.length;k++){
				List<Double> l = table.get(t[k]);
				if(l==null){
					l = new ArrayList<Double>();
					for(int m=0;m<300;m++){
						l.add(0.0);
					}
				}
				ll.add(l);
			}
//			if(ll.size()==1) 
//				list = ll.get(0);
//			else 
				list = sum(list,ll,ways);

			topic.setV(list);
			allWords.set(i, topic);
		}
		return allWords;
	}

	private List<Double> sum(List<Double> list, List<List<Double>> ll, int ways) {
		if(ways==1){
			System.out.println("1");
			for(int k=0;k<300;k++){
				double sum = 0.0;
				for(int i=0;i<ll.size();i++){
					sum += Math.pow(ll.get(i).get(k), 2);
				}
				sum /= (double)ll.size();
				sum = Math.sqrt(sum);
				list.add(sum);
			}
		}
		else if(ways==2){
			System.out.println("2");
			for(int k=0;k<300;k++){
				double sum = 0.0;
				for(int i=0;i<ll.size();i++){
					sum += Math.abs(ll.get(i).get(k));
				}
				sum /= (double)ll.size();

				list.add(sum);
			}
		}
		return list;
	}

}
