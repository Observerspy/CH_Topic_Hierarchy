package Obe.Main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Obe.Util.GetTotalMap;
import Obe.Util.ReadAtomSim;
import Obe.Util.ReadCossine;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class Main3 {

	/**
	 * @param args
	 * 这回是只有507个的
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		 List<String> l = GetTotalMap.getNew().getL();
		 file f = new file();
	     f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/new_sim.txt");f.delete();
	     System.out.println("counting...");
		 f.write("similarity:\r\n");
		 for(int i=0;i<l.size();i++){
			 String a = l.get(i);
			 for(int j=i+1;j<l.size();j++){
				 String b = l.get(j);
				 double cos = getcosine(a,b);
				 double atom = getatom(a,b);
				 double val = 0.3*atom+0.7*cos;
				 f.write(i+":"+a+" -AND- "+j+":"+b+" sim:"+val+"\r\n");
			 }
		 }
		 System.out.println("finished!");
	}

	 public static double getcosine(String a, String b)  {
		 HashMap<String,Double> map = ReadCossine.getNew().getMap();

		 double val = 0.0;
		 String str = a +"+" +b;
		 String str2 = b +"+" +a;
		 if(map.containsKey(str))
			 val =  map.get(str);
		 else if(map.containsKey(str2))
			 val =  map.get(str2);
		 return val;
	}
	 
	 public static double getatom(String a, String b)  {
		 HashMap<String,Double> map = ReadAtomSim.getNew().getMap();

		 double val = 0.0;
		 String str = a +"+" +b;
		 String str2 = b +"+" +a;
		 if(map.containsKey(str))
			 val =  map.get(str);
		 else if(map.containsKey(str2))
			 val =  map.get(str2);
		 return val;
	}
}
