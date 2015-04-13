package Obe.Dao;
/**
 * 去各种停用词及乱糟糟的词
 * 43行附近可自定义那些去那些不去
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Obe.Dto.Topic;
import Obe.Util.Write;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class RemoveStopWords {

	private static String dir = "F:/work/CH_Topic_Hierarchy/etc/stopwords_cn.txt";
	private static List<String> stopWords = new ArrayList<String>();
	private static String dirout =  GetPropertiesUtils.getoutPutDir()+"/result_Remove";//"D:/study/crawl/crawl/etc/Electiondata/result_Remove";

	public List<Topic> dealStopWords(List<Topic> list, String files){
		file f = new file();
		f.setfilepath(dir);f.read();
		String text = f.returnS();
		String[] line = text.split("###");
		for(int i=0;i<line.length;i++){
			stopWords.add(line[i]);
		}
		
		for(int i=0;i<list.size();i++){
			Topic topic = list.get(i);
//			if(topic.getTopic().equals("See also")||
//					topic.getTopic().equals("Notes")||
//					topic.getTopic().equals("References")||
//					topic.getTopic().equals("Further reading")||
//					topic.getTopic().equals("External links")){
//				list.remove(i);
//				i--;
//				continue;
//			}
			//选择去停用词的部分
			Remove r = new Remove();
			String str = r.remove(topic.getTopic()).toLowerCase();
			topic.setTopicWithoutStop(str);
			//topic.setTopic(remove(topic.getTopic()).toLowerCase());
			topic.setTopic(topic.getTopic());//不去停用词变小写
			topic.setText(r.remove(topic.getText()));
			list.set(i, topic);
		}
		try {
			Write w = new Write(dirout);
			w.writeDetails(list, files);
			w.writeMap(list, files);
		} catch (IOException e) {
            System.out.println(files+"write file error");
			e.printStackTrace();
		}
		return list;
	}

//	private String remove(String str) {
//		String[] s = str.split(" ");
//		str = "";
//		for(int k=0;k<s.length;k++){
//			for(int i=0;i<stopWords.size();i++){
//				if(s[k].toLowerCase().equals(stopWords.get(i).toLowerCase())){
//					s[k] = "";
//					}
//				}
//			str = updateStr(str,s[k]);
//			}
//		return str;
//	}
	
	
	
	private String updateStr(String str,String s) {
		if(!s.equals("")&&!s.endsWith("}")&&!s.startsWith("{")){
			s = s.replaceAll("[\\p{Punct}]+", "");
			String[]a = s.split("\r\n");
			if(a.length>0)
				s = a[0];
			str = str + s + " ";
		}
		return str;
	}

}
