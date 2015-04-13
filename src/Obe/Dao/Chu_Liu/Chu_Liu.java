package Obe.Dao.Chu_Liu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import Obe.Util.file;

public class Chu_Liu {
	private HashMap<Integer,Node> nMap = new HashMap<Integer, Node>();
	private HashMap<Integer,Node> nMapback = new HashMap<Integer, Node>();
	private HashMap<Integer,Integer> visit = new HashMap<Integer, Integer>();

	private Stack<HashMap<String,Integer>> stackmin = new Stack<HashMap<String,Integer>>();
	private Stack<List<String>> stackf = new Stack<List<String>>();
	private HashMap<String, Double> eback = new  HashMap<String, Double>();
	private Stack<HashMap<String,Integer>> stacke = new Stack<HashMap<String,Integer>>();
	private List<Integer> nIDback = new ArrayList<Integer>();

	//root=0;其他节点从1开始
	public double Directed_MST(List<Node> n,List<Edge> e,int root){
		for(int i=0;i<n.size();i++){
			Node ed = n.get(i);
			nIDback.add(ed.getID());
			Node no = new Node();
			no.setID(ed.getID());
			no.setConcept(ed.getConcept());
			nMapback.put(no.getID(), no);
		}
		for(int i=0;i<e.size();i++){
			Edge ed = e.get(i);
			eback.put(ed.getSource()+"->"+ed.getTarget(),ed.getWeight());
		}
	 double ret = 0.0;int time = 0;
	 while(true){
		//初始化节点哈希表
		nMap.clear();
		for(int i=0;i<n.size();i++){
			if(n.get(i).getConcept().equals("root"))
				root = n.get(i).getID();
			nMap.put(n.get(i).getID(), n.get(i));
		}
		//最小入边计算
		for(int i=0;i<e.size();i++){
			Edge edge = e.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			Node source = nMap.get(s);
			Node target = nMap.get(t);
			if(target.getID()!=root&&edge.getWeight()<target.getCost()&&s!=t){
				target.setCost(edge.getWeight());
				target.setPre(source.getID());
				nMap.put(target.getID(), target);
				//edge.setFlag(1);
				//e.set(i, edge);
			}
		}
		int circle = 0;
		//更新点列表
		n.clear();
		HashMap<String, Integer> min = new HashMap<String,Integer>();//映射记录
		Iterator<Entry<Integer,Node>> iter = nMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Node t = (Node) entry.getValue();
			n.add(t);
			if(t.getID()!=root){
				min.put(t.getPre()+"->"+t.getID(),0);
			    System.out.println("最小边集"+nMap.get(t.getPre()).getID()+"--->"+t.getID()+" "+t.getCost());
			    }
		}
		stackmin.push(min);
		//找环
		for(int i=0;i<n.size();i++){
			cleanVIS(nMap);
		    Node  node = nMap.get(n.get(i).getID());
			if(node.getID()!=root)
				ret += node.getCost();
			//System.out.println("---"+node.getID()+"---");
			while(node.getID()!=root&&node.getVisit()==0&&node.getCircle()==0){
				node.setVisit(1);
				nMap.put(node.getID(), node);
				node = nMap.get(node.getPre());
			}
			if(node.getID()!=root&&node.getCircle()==0){//在环内
				circle++;
				node.setCircle(circle);
				System.out.print("circle "+circle+" "+node.getID());
				nMap.put(node.getID(), node);
				Node nodepre = nMap.get(node.getPre());
				while(nodepre.getID()!=node.getID()){
					System.out.print("->"+nodepre.getID());
					nodepre.setCircle(circle);
					nMap.put(nodepre.getID(), nodepre);
					nodepre = nMap.get(nodepre.getPre());
				}
				System.out.print("->"+nodepre.getID());
				System.out.println();
			}
		}
		if(circle==0){
			System.out.println("迭代： "+time);
			try {
				out(time);
			} catch (Exception e1) {
				System.out.println("write error!");
				e1.printStackTrace();
			}
			break;
		}//无环
		time++;
		for(int i=0;i<n.size();i++){
			Node  node = nMap.get(n.get(i).getID());
			if(node.getCircle()==0&&node.getID()!=root){
				circle++;
				node.setCircle(circle);				
				nMap.put(node.getID(), node);
			}
		}
		
		//重新构图
	    HashMap<String, Integer> etest = new  HashMap<String, Integer>();
		for(int i=0;i<e.size();i++){
			Edge edge = e.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			if(s!=t)
			etest.put(s+"->"+t, 0);
			//System.out.println(s+"--->>"+t);
			Node source = nMap.get(s);
			Node target = nMap.get(t);
			//更新边信息
			edge.setSource(source.getCircle());
			edge.setTarget(target.getCircle());
			edge.setWeight(edge.getWeight()-target.getCost());
			edge.setFlag(0);
			e.set(i, edge);
		}
		stacke.push(etest);
		//更新节点信息
		List<String> f = new ArrayList<String>();//映射记录
		List<Node> newN = new ArrayList<Node>();
		for(int i=0;i<n.size();i++){
			Node  node = nMap.get(n.get(i).getID());
			node.setCost(Double.MAX_VALUE);
			node.setPre(0);
			node.setVisit(0);
			f.add(node.getCircle()+"->"+node.getID());
			System.out.println("映射："+node.getCircle()+"->"+node.getID());
			node.setID(node.getCircle());
			node.setCircle(0);
			newN.add(node);
		}
		stackf.push(f);
		n.clear();n=newN;
		
	 }

	 System.out.println("finish!");
	 return ret;
	}
	
	private void out(int time) throws IOException {
		file f2 = new file();
		f2.setoutpath("D:/study/crawl/crawl/etc/En/data/net.net");
		f2.write("*Edges"+"\r\n");
		//处理不迭代就出来的
		if(time==0){
			HashMap<String,Integer> min = stackmin.pop();
			Iterator<Entry<String, Integer>> iter = min.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String l = (String)entry.getKey();
				String[] str = l.split("->");
				int s = Integer.valueOf(str[0]);
				int t = Integer.valueOf(str[1]);
			f2.write((s+1)+" "+(t+1)+"\r\n");
		    System.out.println(s+"-->"+t);
		}
		}
		for(int i=0;i<time;i++){
			System.out.println(i);
			//List<String> x = new ArrayList<String>();
			HashMap<String,Integer> x = new HashMap<String,Integer>();
			HashMap<String,Integer> min = stackmin.pop();
			List<String> f = stackf.pop();
			HashMap<String,Integer> che = stackmin.pop();
			HashMap<String,Integer> e = stacke.pop();
			//for(int k=0;k<min.size();k++){
			Iterator<Entry<String, Integer>> iter = min.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String l = (String)entry.getKey();
				String[] str = l.split("->");
				int s = Integer.valueOf(str[0]);
				int t = Integer.valueOf(str[1]);
				
				List<Integer> news = check(s,f);
				List<Integer> newt = check(t,f);
				//扩展目标是环的边
				for(int p=0;p<news.size();p++)
					for(int q=0;q<newt.size();q++){
						x.put(news.get(p)+"->"+newt.get(q),0);
					}
				//定环方向
				for(int q=0;q<newt.size();q++){
					int a = newt.get(q);
					for(int p=0;p<newt.size();p++){
						if(p!=q){
						int b = newt.get(p);
						if(checkEdge(a,b,che)){
							x.put(a+"->"+b,0);
						}
						}
					}
				}
				int m;
				m=0;
			}
			//和上一次保存的图比较，留下在图内的边
			x = checkche(x,e);
			x = findnext(x,i,time,f2);
			stackmin.push(x);
		}
//		int root = 0;
//		HashMap<String,Integer> x = stackmin.pop();
//		HashMap<Integer,Integer> vis = new HashMap<Integer, Integer>();
//		int u = 0;int size = 0;
//		file f = new file();
//		f.setoutpath("D:/study/crawl/crawl/etc/En/data/net.net");
//		f.write("*Edges"+"\r\n");
//		while(!checkVIS(vis,size)){
//			//System.out.println("root"+root);
//		//for(int k=0;k<x.size();k++){
//		Iterator<Entry<String, Integer>> iter = x.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry entry = (Map.Entry) iter.next();
//			String l = (String)entry.getKey();
//			String[] str = l.split("->");
//			int s = Integer.valueOf(str[0]);
//			int t = Integer.valueOf(str[1]);
//			if(s==root&&!vis.containsKey(t)){				
//				if(checkEdge(s,t,eback)){
//					u++;
//					root = t;
//					f.write((nMapback.get(s).getID()+1)+" "+(nMapback.get(t).getID()+1)+"\r\n");
//					System.out.println(u+" :"+nMapback.get(s).getID()+"-->"+nMapback.get(t).getID());
//					vis.put(t, 1);
//					iter = x.entrySet().iterator();
//					//k=0;
//				}
//			}
//			int m;
//			m=0;
//		}
//		root++;
//		if(root>=nIDback.size())
//			root -= nIDback.size();
//	}
	}

	private HashMap<String, Integer> findnext(HashMap<String, Integer> x, int i, int time, file f2) throws IOException {
		int root = 0;double sum = 0.0;		
		HashMap<Integer,Integer> vis = new HashMap<Integer, Integer>();
		HashMap<Integer,Integer> vis2 = new HashMap<Integer, Integer>();
		vis2.put(0, 1);
		Iterator<Entry<String, Integer>> iter2 = x.entrySet().iterator();
		while (iter2.hasNext()) {
			Map.Entry entry = (Map.Entry) iter2.next();
			String l = (String)entry.getKey();
			String[] str = l.split("->");
			int s = Integer.valueOf(str[0]);
			int t = Integer.valueOf(str[1]);
			vis.put(t,0);
		}
		int size = vis.size();vis.clear();
		HashMap<String,Integer> y = new HashMap<String, Integer>();
		while(!checkVIS(vis,size)){
		Iterator<Entry<String, Integer>> iter = x.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String l = (String)entry.getKey();
			String[] str = l.split("->");
			int s = Integer.valueOf(str[0]);
			int t = Integer.valueOf(str[1]);
			//还需要检查源是否在已选树上，这个集合比vis多一个root
			if(s==root&&!vis.containsKey(t)&&vis2.containsKey(s)){				
				if(checkEdge(s,t,x)){
					//u++;
					root = t;
					if(i==time-1){
						if(s==140)
							System.out.println();
						f2.write((nMapback.get(s).getID()+1)+" "+(nMapback.get(t).getID()+1)+"\r\n");
					    System.out.println(nMapback.get(s).getID()+"-->"+nMapback.get(t).getID());
					    sum += eback.get(nMapback.get(s).getID()+"->"+nMapback.get(t).getID());
					}
					y.put(nMapback.get(s).getID()+"->"+nMapback.get(t).getID(),0);
					vis.put(t, 1);
					vis2.put(t, 1);
					//visit.put(t, 1);
					iter = x.entrySet().iterator();
					//k=0;
				}
			}
			int m;
			m=0;
		}
		//这时候有问题，可能选中了孤立点，导致环的生成！！应该在已选中的树上做？
		root++;
		if(root>size)
			root -= size;
		}
		if(i==time-1){
			System.out.println("check： "+sum);
		}
		return y;
	}

	private HashMap<String, Integer> checkche(HashMap<String, Integer> x,
			HashMap<String, Integer> che) {
		Iterator<Entry<String, Integer>> iter = x.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String l = (String)entry.getKey();
			String[] str = l.split("->");
			int s1 = Integer.valueOf(str[0]);
			int t2 = Integer.valueOf(str[1]);
			
				if(!che.containsKey(l))
					iter.remove();
			
		}
		return x;
	}

	private boolean checkVIS(HashMap<Integer, Integer> vis, int size) {
		if(vis.size()==size)
			return true;
		return false;
	}

	private boolean checkEdge(int s, int t, HashMap<String, Integer> che) {
		//for(int i=0;i<che.size();i++){
		Iterator<Entry<String, Integer>> iter = che.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String l = (String)entry.getKey();
			String[] str = l.split("->");
			int s1 = Integer.valueOf(str[0]);
			int t2 = Integer.valueOf(str[1]);
			if(s==s1&&t==t2)
				return true;
		}
		return false;
	}

	private List<Integer> check(int t, List<String> f) {
		List<Integer> ret = new ArrayList<Integer>();
		for(int i=0;i<f.size();i++){
			String[] str = f.get(i).split("->");
			int fs = Integer.valueOf(str[0]);
			if(fs==t){
				int ft = Integer.valueOf(str[1]);
				ret.add(ft);
			}		
		}
		return ret;
	}

	private void cleanVIS(HashMap<Integer, Node> nMap2) {
		Iterator<Entry<Integer,Node>> iter = nMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Node t = (Node) entry.getValue();
			t.setVisit(0);
			nMap.put(t.getID(), t);
		}
	}
}
