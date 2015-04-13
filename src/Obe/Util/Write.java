package Obe.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dto.Edge;
import Obe.Dto.Node;
import Obe.Dto.Topic;

public class Write {
	private String dirout;
	
	public Write(String dirout) {
		this.dirout = dirout;
	}
	
	public void writePajek(List<Edge> edge, HashMap<String, Node> node) throws IOException{
		file f = new file();
		f.setoutpath(dirout);f.delete();
		f.write("*Vertices "+node.size()+"\r\n");
		Iterator<Entry<String, Node>> iter = node.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Node t = (Node) entry.getValue();
			f.write(t.getId()+" "+t.getTopic().getRoot()+":"+t.getTopic().getTopic()+"\r\n");
		}
		f.write("*Edges"+"\r\n");
		for(int i=0;i<edge.size();i++){
			Edge e = edge.get(i);
			f.write(e.getTarget().getId()+" "+e.getSource().getId()+"\r\n");
		}
	}

	public void writeDetails(List<Topic> list, String file) throws IOException {
		file f = new file();
		String[] t = file.split("[.]");
		String path = dirout+"/"+t[0]+"_details.txt";
	    File ff = new File(path);
	    ff.mkdirs();
		f.setoutpath(path);f.delete();
		f.write("ID\tTOPIC\tLEVEL\tPRE\tCHILD\tTEXT\r\n");
	    for(int i=0;i<list.size();i++){
	    	Topic topic = list.get(i);
	    	f.write(topic.getId()+"\t"+topic.getTopic()+"\t"+topic.getLevel()+"\t"
	    			+topic.getPre()+"\t"+topic.getChi()+"\r\n"+topic.getText()+"\r\n");
	    }
	}

	public void writeMap(List<Topic> list, String file) throws IOException {
		file f = new file();
		String[] t = file.split("[.]");
		String path = dirout+"/"+t[0]+"_map.txt";
	    File ff = new File(path);
	    ff.mkdirs();
		f.setoutpath(path);f.delete();
		f.write("TOPIC\tSIZE:"+list.size()+"\r\n");
	    for(int i=0;i<list.size();i++){
	    	Topic topic = list.get(i);
	    	f.write(i+1+":"+topic.getTopic()+"\r\n");
	    }
	}

	public void writeRoot(List<Topic> tlist, String file) throws IOException {
		file f = new file();
		String[] t = file.split("[.]");
		String path = dirout+"/"+t[0]+"_root.txt";
	    File ff = new File(path);
	    ff.mkdirs();
		f.setoutpath(path);f.delete();
		f.write("TOPIC\tSIZE:"+tlist.size()+"\r\n");
	    for(int i=0;i<tlist.size();i++){
	    	Topic topic = tlist.get(i);
	    	f.write(topic.getTopic()+" -AND- "+topic.getRoot()+"\r\n");
	    }
	}
}
