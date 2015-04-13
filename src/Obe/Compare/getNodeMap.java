package Obe.Compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dao.Chu_Liu.Node;
import Obe.Util.LoadBigFile;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class getNodeMap {
	private  HashMap<String,String> nMapb = new HashMap<String,String>();
	private  HashMap<String,Node> nMap = new HashMap<String,Node>();
	private  List<Node> nL = new ArrayList<Node>();
	private  List<String> edge = new ArrayList<String>();

	public HashMap<String,String> getNode(String path) throws Exception{
		//读点
				LoadBigFile l = new LoadBigFile();
				//String temp = GetPropertiesUtils.getoutPutDir()+"/cluster_map_node.txt";
				l.setPath(GetPropertiesUtils.getoutPutDir()+"/com/"+path);
				String str = l.Load();
				String[] text = str.split("\r\n");
				for(int i=0;i<text.length;i++){
					String[] s = text[i].split(" ",2);
					//int ID = Integer.valueOf(s[0].split(":")[1].trim());
					Node n = new Node();
					String[] name = s[1].split("#",2);
					//name[1] = name[1].substring(0, name[1].length()-1);
					name[1] = name[1].substring(0, name[1].length());

					if(name[1].equals("root"))
						n.setID(0);
					else
						n.setID(i+1);
					n.setConcept(name[1].toLowerCase());
					nMap.put(name[1].toLowerCase(),n);
				}
				for(int i=0;i<nMap.size();i++)
					nL.add(null);
				Iterator<Entry<String, Node>> iterx = nMap.entrySet().iterator();
				while (iterx.hasNext()) {
					Map.Entry entry = (Map.Entry) iterx.next();
					Node t = (Node) entry.getValue();
					nL.set(t.getID(),t);
				}
				for(int i=0;i<nL.size();i++)
					nMapb.put(String.valueOf(nL.get(i).getID()+1), nL.get(i).getConcept().replaceAll(" ", "_"));
				
		return nMapb;
		
	}
	
	public List<String> getEdge(String path){
		file f = new file();
		f.setfilepath(GetPropertiesUtils.getoutPutDir()+"/com/"+path);f.read();
		String textC = f.returnS();
		String[] t = textC.split("###");
		for(int i=1;i<t.length;i++){
			//String[] tr = t[i].split(" "); 
			edge.add(t[i].trim());
		}
		return edge;
		
	}
}
