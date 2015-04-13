package Obe.Dao.Weight;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Obe.Dto.Concept;
import Obe.Util.GetAllText;
import Obe.Util.ReadRelation;
import Obe.Util.file;

public class GetKLWeight {
	
	public static void main(String[] args) throws IOException {
		List<Concept> lc = GetAllText.getNew().getL();//concept组
		Smooth smooth = new Smooth();
		System.out.println("smoothing...");
        for(int i=0;i<lc.size();i++){
        	Concept w = lc.get(i);
        	w.initialP();
        	w = smooth.smooth(w);
        	lc.set(i, w);
 
        }
        System.out.println("counting...");
		file f = new file();
        f.setoutpath("D:/study/crawl/crawl/etc/En/data/KL_weight.txt");f.delete();
		f.write("KL weight:\r\n");
		KLCount kl = new KLCount();
		ReadRelation rr = new ReadRelation();
		HashMap<String,Integer> relation = rr.getRelation();
        for(int i=0;i<lc.size();i++){      	
        	for(int k=0;k<lc.size();k++){
        		if(i!=k&&!lc.get(i).getConcept().equals("root")){
        			double klweight = kl.countKLWeight(lc.get(k), lc.get(i));
        			//double klweight = kl.countKLWeight(lc.get(i), lc.get(k));
            		int x = find(lc.get(i).getConcept(),lc.get(k).getConcept(),relation);
            		double val = Math.pow(0.25, x);

            		String str = lc.get(i).getConcept()+" -AND- "+lc.get(k).getConcept();
            		f.write(str+":"+val*klweight+"\r\n");
        		}
        	}
        }
        System.out.println("finished!");

	}
	
	private static int find(String concept, String concept2, HashMap<String,Integer> relation) {
		String[] str1 = concept.split("#");
		String[] str2 = concept2.split("#");//父
		int val = 0;
		for(int i=0;i<str1.length;i++){
			for(int j=0;j<str2.length;j++){
				String str = str1[i]+" -AND- "+str2[j];
				if(relation.containsKey(str)){
					val++;
				}
			}
		}
		return val;
	}
}
