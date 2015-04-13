package Obe.Dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Obe.Dto.Topic;
import Obe.Util.GetTraining;
import Obe.Util.Write;
import Obe.Util.common.GetPropertiesUtils;
/**
 * 生成total
 * 60行附近可以自定义生成策略
 */
public class AllWordsMap {
	private HashMap<String,Topic> map = new HashMap<String,Topic>();//此topic只关注topic和text

	public List<Topic> create(List<List<Topic>> l) {
		for(int i=0;i<l.size();i++){
			List<Topic> topicList = l.get(i);
			for(int j=1;j<topicList.size();j++){
				String topic = topicList.get(j).getTopic();
				Topic t = topicList.get(j);
				if(map.containsKey(topic)){
					Topic t1 = map.get(topic);
					String text = t1.getText();
					text += t.getText();
					t1.setText(text);
					List<Double> lp = t1.getP();
					List<String> lm = t1.getMap();
					lp.clear();lm.clear();
					t1.setP(lp);t1.setMap(lm);
					int count = t1.getCount();
					t1.setCount(count+1);
					map.put(topic,t1);
				}
				else{
					List<Double> lp = t.getP();
					List<String> lm = t.getMap();
					lp.clear();lm.clear();
					t.setP(lp);t.setMap(lm);
					t.setCount(1);
					map.put(topic,t);
				}
			}
		}
		
		Iterator<Entry<String, Topic>> iter = map.entrySet().iterator();
		List<Topic> tlist = new ArrayList<Topic>();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Topic t = (Topic) entry.getValue();
			//加一个新的过滤方法过滤training
//			GetTraining gt = new GetTraining();
//			String text = gt.get();
//			int count = text.split(t.getTopic()).length-1;
//			if(count>=2){
//				tlist.add(t);
//			}
////			//取出现不少于两次的
//			if(t.getCount()>2){
//		        tlist.add(t);
//			}
			//完整的
			tlist.add(t);
		}

		Write w = new Write(GetPropertiesUtils.getoutPutDir());
		try {
			w.writeRoot(tlist, "topic_root");
			w.writeDetails(tlist, "total.txt");
			w.writeMap(tlist, "total.txt");
		} catch (IOException e) {
			System.out.println("write total error!");
			e.printStackTrace();
		}
		return tlist;
	}

}
