package Obe.Dao.Chu_Liu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

public class Chu_Liu2 {
	private HashMap<Integer,Node> nMap = new HashMap<Integer, Node>();
	private HashMap<String, Double> eback = new  HashMap<String, Double>();
	private HashMap<String, Edge> eback2 = new  HashMap<String, Edge>();
	private Stack<HashMap<Integer,Node>> stack = new Stack<HashMap<Integer,Node>>();
	private Stack<Integer> stackC = new Stack<Integer>();
	//private Stack<String> stackInOut = new Stack<String>();

	private int time = 0;
	private HashMap<Integer, HashMap<Integer,Node>> CMap = new  HashMap<Integer, HashMap<Integer,Node>>();


	
	public List<Edge> Directed_MST(List<Node> n,List<Edge> e,int root){
		for(int i=0;i<e.size();i++){
			Edge ed = e.get(i);
			eback.put(ed.getSource()+"->"+ed.getTarget(),ed.getWeight());
			eback2.put(ed.getSource()+"->"+ed.getTarget(),ed);

		}
		nMap.clear();
		for(int i=0;i<n.size();i++){
			if(n.get(i).getConcept().equals("root"))
				root = n.get(i).getID();
			nMap.put(n.get(i).getID(), n.get(i));
		}
		//1.为图上每一个点找最大入边，形成图G_M, 
	     HashMap<Integer,Integer> viss = new HashMap<Integer, Integer>();
		 HashMap<Integer,Integer> vist = new HashMap<Integer, Integer>();

		for(int i=0;i<e.size();i++){
			Edge edge = e.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			Node source = nMap.get(s);
			Node target = nMap.get(t);
			
			if(viss.containsKey(s)){
				List<Integer> l = source.getChild();
				l.add(target.getID());
				source.setChild(l);
				List<Double> ll = source.getOutweight();
				ll.add(edge.getWeight());
				source.setOutweight(ll);
			}
			else{
				List<Integer> l = new ArrayList<Integer>();
				l.add(target.getID());
				source.setChild(l);
				List<Double> ll = new ArrayList<Double>();
				ll.add(edge.getWeight());
				source.setOutweight(ll);
				viss.put(s, 0);
			}			
			nMap.put(source.getID(), source);
			
			if(vist.containsKey(t)){
				List<Integer> l1 = target.getParent();
				l1.add(source.getID());
				target.setParent(l1);
				List<Double> ll1 = target.getInweight();
				ll1.add(edge.getWeight());
				target.setInweight(ll1);
			}
			else{
				List<Integer> l1 = new ArrayList<Integer>();
				l1.add(source.getID());
				target.setParent(l1);
				List<Double> ll1 =  new ArrayList<Double>();
				ll1.add(edge.getWeight());
				target.setInweight(ll1);
				vist.put(t, 0);
			}
			nMap.put(target.getID(), target);
			
			if(target.getID()!=root&&edge.getWeight()>=target.getCost()&&s!=t){
				target.setCost(edge.getWeight());
				target.setPre(source.getID());
				nMap.put(target.getID(), target);
			}
		}
		
		int circle = 0;
		//更新点列表
		n.clear();
		Iterator<Entry<Integer,Node>> iter = nMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Node t = (Node) entry.getValue();
			n.add(t);
			if(t.getID()!=root){
			    System.out.println("最小边集"+nMap.get(t.getPre()).getID()+"--->"+t.getID()+" "+t.getCost());
			    }
		}
		
		//检查G_M是否存在环。
		HashMap<Integer,Integer> C = new HashMap<Integer,Integer>();
		HashMap<Integer,Node> circleC = new HashMap<Integer,Node>();
		double sumC = 0.0;int key = 0;
		for(int i=0;i<n.size();i++){
			cleanVIS(nMap);
		    Node  node = nMap.get(n.get(i).getID());
			//System.out.println("---"+node.getID()+"---");
			while(node.getID()!=root&&node.getVisit()==0&&node.getCircle()==0){
				node.setVisit(1);
				nMap.put(node.getID(), node);
				node = nMap.get(node.getPre());
			}
			if(node.getID()!=root&&node.getCircle()==0){//在环内
				circle++;key = 1;
				node.setCircle(circle);
				circleC.put(node.getID(),node);
				C.put(node.getPre(), node.getID());
				System.out.print("circle "+(time+1)+" "+node.getID());
				sumC += eback.get(node.getPre()+"->"+node.getID());
				nMap.put(node.getID(), node);
				Node nodepre = nMap.get(node.getPre());
				while(nodepre.getID()!=node.getID()){
					System.out.print("->"+nodepre.getID());
					circleC.put(nodepre.getID(),nodepre);
					nodepre.setCircle(circle);
					nMap.put(nodepre.getID(), nodepre);
					C.put(nodepre.getPre(), nodepre.getID());
					sumC += eback.get(nodepre.getPre()+"->"+nodepre.getID());

					nodepre = nMap.get(nodepre.getPre());

				}
				//sumC += eback.get(nodepre.getPre()+"->"+nodepre.getID());
				C.put(nodepre.getPre(), nodepre.getID());
				System.out.println();
				break;
			}
		}
		
		HashMap<Integer,Node> nMapb = new HashMap<Integer, Node>();
		Iterator<Entry<Integer,Node>> iter1 = nMap.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry entry = (Map.Entry) iter1.next();
			Node t = (Node) entry.getValue();
			Node n1 = new Node();
			n1.setID(t.getID());
			n1.setConcept(t.getConcept());
			n1.setChild(t.getChild());
			n1.setCircle(t.getCircle());
			n1.setCost(t.getCost());
			n1.setFromC(t.getFromC());
			n1.setInweight(t.getInweight());
			n1.setOutweight(t.getOutweight());
			n1.setParent(t.getParent());
			n1.setPre(t.getPre());
			n1.setToC(t.getToC());
			n1.setVisit(t.getVisit());
			nMapb.put(n1.getID(), n1);
		}
		stack.push(nMapb);
		//2.如果不存在环，则返回G_M
		if(key==0){	
			double sum = 0.0;
			List<Edge> eL = new ArrayList<Edge>();
			for(int i=0;i<n.size();i++){
				if(n.get(i).getCost()!=-Double.MAX_VALUE)
					sum += n.get(i).getCost();
				String str = n.get(i).getPre()+"->"+n.get(i).getID();
				Edge e1 = eback2.get(str);
				if(e1!=null)
					eL.add(e1);
				
			}
			System.out.println("weight:"+sum);
			return eL;
		}


		//4.调用收缩环的函数contract（G,C,s），返回收缩环C之后的图G_C。
		//注意这里调用收缩环的时候，输入的图用调用chu-liu算法的那个图。
		e = contract(n,e,circleC,root,sumC);
		//更新点列表
		n.clear();
		Iterator<Entry<Integer,Node>> iter11 = nMap.entrySet().iterator();
		while (iter11.hasNext()) {
			Map.Entry entry = (Map.Entry) iter11.next();
			Node t = (Node) entry.getValue();
			n.add(t);
		}
		//5.递归调用Chu-liu-edmonds（G_C,s）这里的输入图是指收缩环C之后的图，
		//分数s也是已经调整过的。返回图y。
		e = Directed_MST(n,e,root);
		
		//6.从环C里面找一个点，比如说x，环C之外的一个点x’指向了x，
		//环C里面也有指向x的点，成为x’’,将这个图加上环，并且去掉环里面的x’’到x的那条边,
		//返回该图
		nMap = stack.pop();int tt = stackC.pop();
		List<Edge> eL = new ArrayList<Edge>();
		eL.addAll(e);int Cin = -Integer.MAX_VALUE;int flag = 0;List<Integer> out = null;
		//String str = stackInOut.pop();
		//String[] k = str.split(",");
		//int inC = Integer.valueOf(k[0]);Cin = inC;
		//int outC = Integer.valueOf(k[1]);
		
		for(int i=0;i<e.size();i++){ 
			Edge edge = e.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			if(t==tt){
			Node in = nMap.get(s);
			Node target = nMap.get(t);
			if(target.getConcept().equals("circel!")){
				//System.out.println("circle id:"+target.getID());
				circleC = CMap.get(target.getID());

				out = getOut(s,t,eL);
				eL = remove(s,t,eL);
				
				Edge edg = new Edge();
				edg.setSource(s);
				edg.setTarget(in.getToC());
				Cin = in.getToC();
				eL.add(edg);break;
				//circleC = CMap.get(target.getID());
//				Iterator<Entry<Integer, Node>> iter111 = circleC.entrySet().iterator();
//				int key1 = 0;
//				while (iter111.hasNext()) {
//					Map.Entry entry = (Map.Entry) iter111.next();
//					Node c = (Node) entry.getValue();
//					List<Integer> l = c.getParent();
//					for(int i1=0;i1<l.size();i1++){
//						if(s==l.get(i1)){
//							Edge edg = new Edge();
//							edg.setSource(s);
//							edg.setTarget(c.getID());
//							Cin = c.getID();
//							eL.add(edg);
//							key1 = 1;flag = 1;
//							break;
//						}
//					}
//					if(key1==1)
//						break;
//				}
//				if(flag==1)
//					break;
			}
			}
		}
		
		
		HashMap<Integer,Integer> visC = new HashMap<Integer,Integer>();
		visC.put(Cin,0);
		Iterator<Entry<Integer, Node>> iter111 = circleC.entrySet().iterator();
		while (iter111.hasNext()) {
			Map.Entry entry = (Map.Entry) iter111.next();
			Node c = (Node) entry.getValue();
			List<Integer> l = c.getParent();
			for(int i1=0;i1<l.size();i1++){
				if(Cin==l.get(i1)&&!visC.containsKey(c.getID()))
				{
					Edge edg = new Edge();
					edg.setSource(Cin);
					edg.setTarget(c.getID());
					Cin = c.getID();
					visC.put(Cin, 0);
					eL.add(edg);
					iter111 = circleC.entrySet().iterator();
					break;
				}
			}
		}
		for(int q=0;q<out.size();q++){
		if(out.get(q)!=-Integer.MAX_VALUE){
			Node outn = nMap.get(out.get(q));
			Edge edg = new Edge();
		    edg.setSource(outn.getFromC());
		    edg.setTarget(out.get(q));
		    eL.add(edg);
		    
//			int flag1 = 0;
//			Iterator<Entry<Integer, Node>> iter1111 = circleC.entrySet().iterator();
//			while (iter1111.hasNext()) {
//				Map.Entry entry = (Map.Entry) iter1111.next();
//				Node c = (Node) entry.getValue();
//				if(c.getToC()==out){
//					Edge edg = new Edge();
//				    edg.setSource(c.getID());
//				    edg.setTarget(out);
//				    eL.add(edg);
//				    break;
//				}
//				List<Integer> l = c.getChild();
//				for(int i1=0;i1<l.size();i1++){
//					if(out==l.get(i1)){
//						Edge edg = new Edge();
//					    edg.setSource(c.getID());
//					    edg.setTarget(out);
//					    eL.add(edg);
//					    flag1 = 1;
//					    break;
//					    }
//					}
//				if(flag1==1)
//					break;
//				}
		}
		}
		for(int i=0;i<eL.size();i++){
			Edge edge = eL.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			//System.out.println(s+"->"+t);
		}
		//System.out.println("============================");
		return eL;
	}
	
	private List<Integer> getOut(int s2, int t2, List<Edge> eL) {
		List<Integer> t = new ArrayList<Integer>();
		for(int i=0;i<eL.size();i++){ 
			Edge edge = eL.get(i);
			int s = edge.getSource();
			int t1 = edge.getTarget();
			if(s==t2){
				t.add(t1);
			}				
		}		
		return t;
	}

	private List<Edge> remove(int s2, int t2, List<Edge> eL) {
		for(int i=0;i<eL.size();i++){ 
			Edge edge = eL.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			if(s==s2&&t==t2){
				eL.remove(i);
				i--;
			}				
		}
		for(int i=0;i<eL.size();i++){ 
			Edge edge = eL.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			if(s==t2){
				eL.remove(i);
				i--;
			}				
		}
		return eL;
	}

	private List<Edge> contract(List<Node> n, List<Edge> e, HashMap<Integer, Node> circleC, int root, double sumC) {
		//1.把环C中的点全部剔除，包括相应的边，得到一个子图G_C
		int max_id = 0;
		for(int i=0;i<n.size();i++){
			if(circleC.containsKey(n.get(i).getID())){
				n.remove(i);
				i--;
			}
		}
		for(int i=0;i<e.size();i++){
			Edge edge = e.get(i);
			int s = edge.getSource();
			int t = edge.getTarget();
			if(circleC.containsKey(s)||circleC.containsKey(t)){
				e.remove(i);
				i--;
			}			
		}
		nMap.clear();
		for(int i=0;i<n.size();i++){
			if(max_id<n.get(i).getID())
				max_id = n.get(i).getID();
			if(n.get(i).getConcept().equals("root"))
				root = n.get(i).getID();
			nMap.put(n.get(i).getID(), n.get(i));
		}

			
		//2.在图G_C中加上一个点c，来代替那个环C。
		Node C = new Node();
		time++;
		int Cid = -1*time;
		C.setID(Cid);
		C.setConcept("circel!");
		n.add(C);
		stackC.push(Cid);
		nMap.put(C.getID(),C);
		//CMap.put(max_id+1, circleC);
		//3.对G_C进行加边，从环C中的某个点指出去V中其他点x的话，
		//加一条边从c—>x,分数取最大的从环C指出来到x的分数
		//int outC = -1;
		if(Cid==-133){
			int m;
			m=0;
		}
		for(int i=0;i<n.size()-1;i++){
			int flag = 0;Edge edge = new Edge();double max =-Double.MAX_VALUE;
			Iterator<Entry<Integer,Node>> iter = circleC.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Node node = (Node) entry.getValue();
				List<Integer> l = node.getChild();
				List<Double> ll = node.getOutweight();
				for(int j=0;j<l.size();j++){
					if(l.get(j)==n.get(i).getID()){
						if(ll.get(j)>max){
							//outC = node.getID();
							max = ll.get(j);
							flag = 1;
							edge.setSource(Cid);
							edge.setTarget(l.get(j));
							edge.setWeight(ll.get(j));
							
							Node newn = nMap.get(l.get(j));
							newn.setFromC(node.getID());
							nMap.put(newn.getID(), newn);
							//逻辑不对！怎么保证确定环出边点？
							Node nn = circleC.get(node.getID());
							nn.setToC(newn.getID());
							circleC.put(node.getID(), nn);
						}
					}
				}
			}
			if(flag==1)
				e.add(edge);
		}

		//4.顶点集合中从环C外进入环C的点x，加一条边从x—>c，
		//分数取进入环C中的最大分数。计算方法是假设x进入环C有两种途径，
		//一种是到环内C1点，一种是到C2点，那么第一种方式的分数是
		//x到C1的分数-环里面某个点到C1的分数+环的总分数。第二种同理。最后x—>c的分数取这两种最大值。
		//环C的总分数是指把环C所有边的分数加起来。
		//int inC = -1;
		for(int i=0;i<n.size()-1;i++){
			Edge edge = new Edge();double max = -Double.MAX_VALUE;int flag = 0;
			Node node = n.get(i);
			List<Integer> l = node.getChild();
			List<Double> ll = node.getOutweight();
			for(int j=0;j<l.size();j++){
				if(circleC.containsKey(l.get(j))){
					Node c = circleC.get(l.get(j));
					double val = ll.get(j)-c.getCost()+sumC;
					if(val>max){
						//inC = c.getID();
						flag = 1;
						edge.setSource(node.getID());
						edge.setTarget(Cid);
						edge.setWeight(val);
						
						Node newn = nMap.get(node.getID());
						newn.setToC(c.getID());
						nMap.put(newn.getID(), newn);
						
						
						c.setFromC(newn.getID());
						circleC.put(c.getID(), c);
					}
				}
			}
			if(flag==1)
				e.add(edge);
		}
		//stackInOut.push(inC+","+outC);
		CMap.put(Cid, circleC);
		return e;
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
