package Obe.Dao.Incre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Obe.Util.ReadCossine;
import Obe.Util.ReadWSim;
import Obe.Util.Readt_r;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class IncreCluster {
	private  List<Cluster> list = new ArrayList<Cluster>();//C的集合
	private HashMap<String,String> relation = new HashMap<String, String>();
	private  double lamda = 0; 
	private  int key = 0;
	
	public void start(List<String> l) throws IOException {
		 file f = new file();
		 Readt_r r = new Readt_r();
		 relation = r.getRelation();//检查是不是在一个文件里
	     f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/IncreMax/IncreCluster_"+lamda+".txt");f.delete();
	     System.out.println("clustering...");
		 f.write("clustering:\r\n");
		 //初始化C0
		 System.out.println("initial first cluster");

		 Cluster cl = new Cluster();
		 List<String> concept = new ArrayList<String>();
		 concept.add(l.get(0));
		 cl.setConcept(concept);
		 list.add(cl);
		 //对每一个concept
		 for(int i=1;i<l.size();i++){
			 System.out.println("concept_"+i);
			 String str = l.get(i);
			 //遍历簇集合
			 List<Double> s = new ArrayList<Double>();
			 for(int j=0;j<list.size();j++){
				 System.out.println("list_"+j);
				 Cluster clu = list.get(j);
				 List<String> conceptT = clu.getConcept();
				 List<Double> sim = new ArrayList<Double>();
				 //遍历簇中每一个概念
				 for(int k=0;k<conceptT.size();k++){
					 String ct = conceptT.get(k);
					 double val = getsim(str,ct);
					 if(val==0)
					 {
						 System.out.println(str+"+"+ct);
					 }
					 sim.add(val);
				 }
				 double weight = 0;
				if(key==0)
				  weight = findMax(sim);//取最大
				 else if(key==1)
				  weight = findMaxMean(sim);//最大最小平均
				 else if(key==2)
				  weight = findAllMean(sim);//取全部平均
				 s.add(weight);

			 }
			//改成对s sort,index 也随之排序
			 HashMap<Double,Integer> x = new HashMap<Double, Integer>();
			 for(int j=0;j<s.size();j++){
				 x.put(s.get(j), j);
			 }
			 Collections.sort(s);
			 Collections.reverse(s); 
//			 double weight = findMax(s);int index = 0;
//			 for(int j=0;j<s.size();j++){
//				 if(s.get(j)==weight){
//					 index = j;break;
//				 }
//			 }
			 int k = 0;int flag = 0;
			 while(s.get(k)>lamda){
				 Cluster c = list.get(x.get(s.get(k)));
				 List<String> cT = c.getConcept();
				 if(!check(str,cT)){//root不同时才进入
					 flag = 1;
					 cT.add(str);
					 c.setConcept(cT);
					 list.set(x.get(s.get(k)),c);
					 break;
				 }
				 else
					 k++;
			 }
			 if(flag==0){
				 Cluster c = new Cluster();
				 List<String> con = new ArrayList<String>();
				 con.add(str);
				 c.setConcept(con);
				 list.add(c);
			 }
		 }
		 System.out.println("cluster finished!");
		 for(int j=0;j<list.size();j++){
			 Cluster clu = list.get(j);
			 System.out.println("cluster id: "+j+"concept: ");
			 f.write("cluster id: "+j+"concept: \r\n");
			 for(int i=0;i<clu.getConcept().size();i++){
				 System.out.print(clu.getConcept().get(i)+"#");
				 f.write(clu.getConcept().get(i)+"#");
				 }
			 System.out.println();
			 f.write("\r\n");
		 }
	}

	private boolean check(String str, List<String> cT) {
		String root = relation.get(str).trim();
		for(int i=0;i<cT.size();i++){
			 String root1 = relation.get(cT.get(i)).trim();
			 if(root.equals(root1))
				 return true;
		}
		return false;
	}

	private boolean isMax(double weight, List<Double> sim) {
		double max = Collections.max(sim);
		if(weight==max)
			return true;
		return false;
	}
	private double findAllMean(List<Double> sim) {
		double sum = 0.0;
		for(int i=0;i<sim.size();i++)
			sum += sim.get(i);
		return sum/(double)sim.size();
	}
	private double findMaxMean(List<Double> sim) {
		double max = Collections.max(sim);
		double min = Collections.min(sim);
		return (max+min)/(double)2.0;
	}
	private double findMax(List<Double> sim) {
		double max = Collections.max(sim);
		return max;
	}
	private double getsim(String a, String b) {
		 HashMap<String,Double> map = ReadWSim.getNew().getMap();

		 double val = 0.0;
		 String str = a +"+" +b;
		 String str2 = b +"+" +a;
		 if(map.containsKey(str))
			 val =  map.get(str);
		 else if(map.containsKey(str2))
			 val =  map.get(str2);
		 return val;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	public double getlamda() {
		return lamda;
	}

	public void setlamda(double lamda) {
		this.lamda = lamda;
	}
}
