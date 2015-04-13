package Obe.Dao;
/**
 * 余弦相似度接口
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import Obe.Dto.Concept;
import Obe.Dto.Topic;
import Obe.Util.GetAllText;

public class CountCossim {
	private Vector<String> map1 = new Vector<String>();//时间片1wordmap,更新后的也存在这里
	private Vector<String> map2 = new Vector<String>();//时间片2wordmap
	private Vector<Double> phi1 = new Vector<Double>();//时间片1phi，更新后的也在这里
	private Vector<Double> phi2 = new Vector<Double>();//时间片2phi
	private Vector<Double> temp;//时间片2扩展调整的phi
	
	/**
	 * 功能：计算余弦相似度方法入口
	 */
	public double getCos(Topic topic1, Topic topic2) {
		map1.clear();map2.clear();phi1.clear();phi2.clear();
		initial(topic1,topic2,1);
        //合并map为map1，调整phi
//		show();
		adjust();
		//计算phi1和temp相似度
		double val = count();
//		if(val>0.35){
//			show2();
//			int m;
//			m=0;
//		}
		return val;
	}
	private void show2() {
		System.out.println("temp");
		for(int i=0;i<temp.size();i++){
			System.out.print(map1.get(i)+" ");
			System.out.print(phi1.get(i)+" ");
			System.out.print(temp.get(i)+" ");
		}
		System.out.println("");
	}
	private void show() {
		System.out.println("map1 and map2");
		for(int i=0;i<map1.size();i++){
			System.out.print(map1.get(i)+" ");
			System.out.print(phi1.get(i)+" ");
		}
		System.out.println("");
		for(int i=0;i<map2.size();i++){
			System.out.print(map2.get(i)+" ");
			System.out.print(phi2.get(i)+" ");
		}	
		System.out.println("");
	}
	/**
	 * 功能：初始化向量
	 */
	private void initial(Topic topic1, Topic topic2,int key) {	
		if(key==1){
			map1.addAll(topic1.getMap());
		    phi1.addAll(topic1.getP());
		    map2.addAll(topic2.getMap());
		    phi2.addAll(topic2.getP());
		}
		else if(key==2){
		    phi1.addAll(topic1.getV());
		    temp.addAll(topic2.getV());
		}
	}
	private void initial2(Topic t, Concept c, List<String> lm) {
		map1.addAll(t.getMap());
	    phi1.addAll(t.getP());
	    map2.addAll(lm);
	    for(int i=0;i<lm.size();i++) {
	    	double val = c.getP().get(lm.get(i));
			phi2.add(val);
		}
	    
	}
	/**
	 * 功能：计算余弦相似度
	 */
	private double count() {
		double sum = 0.0;
		for(int i=0;i<phi1.size();i++){

			sum = sum + phi1.get(i)*temp.get(i);

		}
		double mod1 = getMod(phi1);
		double mod2 = getMod(temp);

		double val = (double)sum/(double)(mod1*mod2);
		return val;
	}
	
	/**
	 * 功能：计算向量模
	 */
	private double getMod(Vector<Double> phi){
		double sum = 0.0;
		for(int i=0;i<phi.size();i++){
			sum = sum + Math.pow(phi.get(i),2);
		}
		return Math.sqrt(sum);
	}
	/**
	 * 功能：扩展合并wordmap至map1，扩展phi1，同时phi2扩展至temp
	 */
	private void adjust() {
		temp = new Vector<Double>(phi1.size(),10);
		for(int i=0;i<phi1.size();i++)
			temp.add(0.0);
		for(int i=0;i<map2.size();i++){
			int flag = 0;
			for(int k=0;k<map1.size();k++){
				if(map2.get(i).equals(map1.get(k))){
					//map2 i单词存在于map1,将phi2 i值拷贝到temp i
					temp.set(k, phi2.get(i));
					flag = 1;
					break;
				}
			}
			//不存在，将该词添加至map1末尾，phi1增加0值末尾，将phi2 i值拷贝到temp 末尾
			if(flag==0){
				map1.add(map2.get(i));
				phi1.add(0.0);
				temp.add(phi2.get(i));	
			}
		}
	}
	
	public double getCos2(Topic t1, Topic t2) {
		phi1.clear();
		temp = new Vector<Double>();
		initial(t1,t2,2);
		double val = count();
		return val;
	}
	
	public double getCos3(Topic t, Concept c, List<String> lm){
		map1.clear();map2.clear();phi1.clear();phi2.clear();
		initial2(t,c,lm);
		adjust();
		double val = count();

		return val;

	}
	
	public double getCos4(Topic t, Concept c) {
		phi1.clear();
		temp = new Vector<Double>();
		initial1(t,c);
		double val = count();
		return val;
	}
	
	private void initial1(Topic t, Concept c) {
		HashMap<String, Double> tmap = t.getM();
		HashMap<String, Double> cmap = c.getP();
		Iterator<Entry<String, Double>> iter = tmap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String)entry.getKey();
			phi1.add( (Double) entry.getValue());
			temp.add(cmap.get(str));
		}
	}
	
	
}
