package Obe.Dao.Weight;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Obe.Dto.Concept;
import Obe.Util.GetAllText;
import Obe.Util.ReadRelation;
import Obe.Util.ReadTimes;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class GetWeight {
	private static int lamda = 10000;
	
	public static void main(String[] args) throws IOException { 
//		String content = GetAllText.getNew().getContent();//全文本
		HashMap<String,Integer> map = GetAllText.getNew().getMap();//全文本词表及频数
//		int textLength = GetAllText.getNew().getLength();//文本长
		List<Concept> lc = GetAllText.getNew().getL();//concept组
		ReadRelation rr = new ReadRelation();
		HashMap<String,Integer> relation = rr.getRelation();
		ReadTimes rrr = new ReadTimes();
		HashMap<String,Integer> r = rrr.getTimes();
//		Smooth smooth = new Smooth();
//		System.out.println("smoothing...");
//        for(int i=0;i<lc.size();i++){
//        	System.out.println(i);
//
//        	Concept w = lc.get(i);
//        	w.initialP();
//        	w = smooth.smooth(w);
//        	lc.set(i, w);
// 
//        }
//        System.out.println("counting...");
//        WeightCount wc = new WeightCount();
        
		file f = new file();
        f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/cluster_weight"+GetPropertiesUtils.getEx());f.delete();
		f.write("cluster weight:\r\n");

        for(int i=0;i<lc.size();i++){      	
        	for(int k=0;k<lc.size();k++){
        		if(i!=k&&!lc.get(i).getConcept().equals("root")){
        		//double logweight = wc.countLogWeight(lc.get(i), lc.get(k));
        		String str = lc.get(i).getConcept()+" -AND- "+lc.get(k).getConcept();
        		//need add function to find if any words is in relation
        		String s = find(lc.get(i).getConcept(),lc.get(k).getConcept(),relation);
        		int x = Integer.valueOf(s.split("#")[0]);
        		int y = Integer.valueOf(s.split("#")[1]);
        		int z = getZ(lc.get(k).getConcept(),r);
        		//double val = (double)x/2.0+1.0+(double)y;
        		double val = (double)x/(double)z;//结构
        		
        		//double logl = (logweight-20292) / (31600-20292);//Math.log(lamda)=10000;//???
        		//double l = Math.pow(Math.E, logl);
        		//double F = 0.1*logl+0.9*val;
        		//logweight = logweight+x*10000;
        		//f.write(str+":"+F+"\r\n");
        		f.write(str+":"+val+"\r\n");
        		//System.out.println(list.get(i).getWord()+"-AND-"+list.get(k).getWord()+":"+logweight);
        		}
        	}
        }
        System.out.println("finished!");
        System.exit(0);
	}

	private static int getZ(String concept, HashMap<String, Integer> r) {
		String[] str1 = concept.split("#");
		int con = 0;
		for(int i=0;i<str1.length;i++){
			if(r.containsKey(str1[i]))
				con=con+r.get(str1[i]);
		}
		return con;
	}

	private static  String find(String concept, String concept2, HashMap<String,Integer> relation) {
		String[] str1 = concept.split("#");
		String[] str2 = concept2.split("#");//父
		int val = 0;int con = 0;
		for(int i=0;i<str1.length;i++){
			for(int j=0;j<str2.length;j++){
				String str = str1[i]+" -AND- "+str2[j];
				if(relation.containsKey(str)){
					val=val+relation.get(str);
				}
				if(str1[i].contains(str2[j])){
					con++;
				}
			}
		}
		return val+"#"+con;
	}
}
