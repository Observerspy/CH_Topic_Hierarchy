package Obe.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Obe.Util.ReadPartsOfSpeech;
import Obe.Util.ReadStop;


public class Remove {

	public String remove(String str){
	       HashMap<String,Integer> partsOfSpeech = ReadPartsOfSpeech.getNew().getParts();
	       HashMap<String,Integer> stopWords = ReadStop.getNew().getStop();
	       str = str.replaceAll("[^(\\u4e00-\\u9fa5)]", "");
	      // str = str.replaceAll("\\s+", " ");
	      
	       //System.out.println(str);
	       //String s = classifier.classifyToString(str);
	       String s = "";
		try {
			s = NlpirTest.getWords(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	       //System.out.println(s);
	       
	       String[] t = s.split(" ");
	       String content = "";
	       for(int i=1;i<t.length;i++){
	    	   if(t[i].contains("/")){
	    	   String[] text = t[i].split("/");
	    	   if(partsOfSpeech.containsKey(text[1].trim())&&!stopWords.containsKey(text[0].trim())){
	    		   content += text[0]+" ";
	    	   }
	    	   }
	       }
	       //System.out.println(content);
	       return content;
	}
}
