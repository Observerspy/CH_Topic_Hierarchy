package Obe.Dao.Weight;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dto.Concept;
import Obe.Util.GetAllText;

public class Smooth {
	
	public Concept smooth(Concept w){
		//String content = GetAllText.getNew().getContent();//全文本
		int textLength = GetAllText.getNew().getLength();//文本长
        double miu = 0.01*(double)textLength;//miu
		String concept = w.getText();//组描述
//		List<Concept> l = GetAllText.getNew().getL();//concept组
//		String text = "";//
//        for(int i=0;i<l.size();i++){
//        	Concept c = l.get(i);
//        	if(c.getConcept().contains(concept)){
//        		text = c.getText();
//        		break;
//        	}
//        }
		int length = concept.split(" ").length;//描述长		

		HashMap<String,Integer> map = GetAllText.getNew().getMap();//全文本词表及频数
		Iterator<Entry<String,Integer>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String)entry.getKey();
			//System.out.println(str);
			int num = (Integer) entry.getValue();//在全文频数
			concept += "*";str =" "+str+" ";
    		int count = 0;
			try{ count = concept.split(str).length-1;
    		//对应单词在描述中频数
    		}catch(Exception e){
    			System.out.println(concept+"---"+str);
    		}
    		double val = (double)(count + 0.01 * num )/(double)(length+miu);
    		w.getP().put(str.trim(), val);
		}	
		
 /*       List<String> list = GetTotalMap.getNew().getL();
        for(int i=0;i<list.size();i++){
        	String str = list.get(i);
        	if(word.getP().containsKey(str)){
        		int count = text.split(str+"*").length-1;//对应单词在描述中频数
        		int num = content.split(str+"*").length-1;//在全文频数
        		if(num==0)
        			num=1;
        		double val = (double)(count + 0.01 * num )/(double)(length+miu);
        		word.getP().put(str, val);
        	}
        }*/
		return w;
	}
}
