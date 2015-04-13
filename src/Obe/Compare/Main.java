package Obe.Compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
	private static HashMap<String,String> ntest = new HashMap<String,String>();
	private static HashMap<String,String> ntrue = new HashMap<String,String>();
	private static List<String> etest = new ArrayList<String>();
	private static List<String> etrue = new ArrayList<String>();


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		getNodeMap getR = new getNodeMap(); 
		ntest = getR.getNode("pred.txt");
		etest = getR.getEdge("net_p.net");
		
		getNodeMap get = new getNodeMap(); 
		ntrue = get.getNode("Final_New_cluster_map_node.txt");
		etrue = get.getEdge("netn.net");
		
		int count = 0;
		for(int i=1;i<etest.size();i++){
			String[] tr = etest.get(i).split(" ");
			String t = ntest.get(tr[1].trim());
			String s = ntest.get(tr[0].trim());

			String[] sl = s.split("#");
			String[] tl = t.split("#");
			int flag = 0;
			for(int j=0;j<sl.length;j++){
				for(int k=0;k<tl.length;k++){
					if(check(sl[j],tl[k])){
						count++;
						flag = 1;
						break;
					}
				}
				if(flag==1)
					break;
			}

		}
		System.out.println("Edge Correct:"+count);
		System.out.println("P:"+(double)count/(double)etest.size());
		int m;
		m=0;
	}


	private static boolean check(String s, String t) {
		for(int i=0;i<etrue.size();i++){
			String[] tr = etrue.get(i).split(" ");
			String s1 = ntrue.get(tr[0].trim());
			String t1 = ntrue.get(tr[1].trim());

			if(s1.contains(s)&&t1.contains(t)){
				System.out.println(s1+"->"+t1);
				System.out.println(s+"->"+t);
				System.out.println();
				return true;
				}
		}
		return false;
	}

}
