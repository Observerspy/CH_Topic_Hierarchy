package Obe.Dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dto.Topic;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class AllTextEX {
	private HashMap<String,Integer> relation = new HashMap<String,Integer>();
	private HashMap<Integer,Topic> map = new HashMap<Integer,Topic>();

	public List<List<Topic>> TextEX(List<List<Topic>> l){
		for(int i=0;i<l.size();i++){
			map.clear();
			List<Topic> list = l.get(i);
			for(int j=0;j<list.size();j++){
				map.put(list.get(j).getId(), list.get(j));
			}
			for(int j=0;j<list.size();j++){
				Topic t = list.get(j);
				if(t.getPre()!=-1){
					Topic pre = map.get(t.getPre());
					if(pre!=null){
						String str = "";
						if(t.getPre()!=0)
					      str = t.getTopic()+" -AND- "+pre.getTopic();
						else
						  str = t.getTopic()+" -AND- root";
					if(relation.containsKey(str)){
						int val = relation.get(str);
						val++;
						relation.put(str,val);
					}
					else{
						relation.put(str, 1);
					}
					}
				}
				t = update(t);
				list.set(j, t);
			}
			l.set(i, list);
		}
		
		try {
			write(relation);
		} catch (Exception e) {
            System.out.println("write relation error");
			e.printStackTrace();
		}
		return l;
	}

	private void write(HashMap<String, Integer> r) throws IOException {
		file f = new file();
        f.setoutpath(GetPropertiesUtils.getoutPutDir()+"/relation.txt");f.delete();
		f.write("relation:\r\n");
		Iterator<Entry<String, Integer>> iter = r.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String str = (String)entry.getKey();
			int val = (Integer)entry.getValue();
			f.write(str+": "+val+"\r\n");
		}
	}

	private Topic update(Topic t) {
		List<Integer> child = t.getChi();
		for(int i=0;i<child.size();i++){
			int ID = child.get(i);
			if(map.containsKey(ID)){
				t.setText(t.getText() +" "+ map.get(ID).getText());
			}
		}
		return t;
	}
}
