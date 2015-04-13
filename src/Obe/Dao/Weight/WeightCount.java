package Obe.Dao.Weight;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.math3.special.Gamma;

import Obe.Dto.Concept;
import Obe.Util.GetAllText;

public class WeightCount {

	public double countLogWeight(Concept concept,Concept concept2){
		HashMap<String,Integer> map = GetAllText.getNew().getMap();//全文本词表及频数
		int mapSize = map.size();
		double alpha = 0.1*mapSize;
        //List<String> list = GetTotalMap.getNew().getL();
        //Z
        double sum = 0.0;double gamma = 0.0;
		Iterator<Entry<String,Integer>> iter = map.entrySet().iterator();
		int i=0;
		while (iter.hasNext()) {
			i++;
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String)entry.getKey();
			
			double val = concept2.getP().get(str)*alpha;
			sum += val;
			gamma += Gamma.logGamma(val);
			if(sum>Double.MAX_VALUE||gamma>Double.MAX_VALUE)
				System.out.println("warning! num:"+i);
		}
//        for(int i=0;i<list.size();i++){
//        	String str = list.get(i);
//        	double val = b.getP().get(str)*alpha;
//        	sum += val;
//        	gamma += Gamma.logGamma(val);
//        	if(sum>Double.MAX_VALUE||gamma>Double.MAX_VALUE)
//        		System.out.println("warning! num:"+i);
//        }
		//sum = alpha;
        sum = Gamma.logGamma(sum);
        double logZ = gamma - sum;
        //right
        double multiply = 0.0;
        Iterator<Entry<String,Integer>> iter1 = map.entrySet().iterator();
		i=0;
		while (iter1.hasNext()) {
			i++;
			Map.Entry entry = (Map.Entry) iter1.next();
			String str = (String)entry.getKey();
			//double val = Math.pow(concept.getP().get(str), concept2.getP().get(str)*alpha-1);
			//val = Math.log(val);
			double val = (concept2.getP().get(str)*alpha-1)*Math.log(concept.getP().get(str));
			multiply += val;
			if(multiply==Double.NEGATIVE_INFINITY){
				int m;
				m=0;
			}
		}
//        for(int i=0;i<list.size();i++){
//        	String str = list.get(i);
//        	double val = Math.pow(a.getP().get(str), b.getP().get(str)*alpha-1);
//		    val = Math.log(val);
//        	multiply += val;        	
//        }
        double r = multiply-logZ; 
        if(r<0)
        	System.out.println("!!!!!!!!!!!!!!!!!!!");
        return r;
	}
}
