package Obe.Main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import Obe.Util.ReadWSim;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		 HashMap<String,Double> map = ReadWSim.getNew().getMap();
		 file f = new file();
	     f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/test.txt");f.delete();
		 Iterator<Entry<String,Double>> iter = map.entrySet().iterator();
		 while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			f.write(entry.getValue()+"\r\n");
			}
	}

}
