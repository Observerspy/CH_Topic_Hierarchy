package Obe.Dao.Incre;

import java.io.IOException;
import java.util.List;

import Obe.Util.GetTotalMap;

public class Main2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		 Runtime run = Runtime.getRuntime();
//		 long max = run.maxMemory();
//		 System.out.println("max memory = " + max/1024/1024);
		 List<String> l = GetTotalMap.getNew().getL();
		 IncreCluster startCluster = new IncreCluster();
		 startCluster.setKey(0);startCluster.setlamda(0.15d);
		 startCluster.start(l);
	}

}
