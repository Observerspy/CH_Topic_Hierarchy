package Obe.Dao;
/**
 * 预处理，形成格式化数据
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Obe.Dto.Topic;
import Obe.Util.Write;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;

public class Preproccess {

	private  List<Topic> list = new ArrayList<Topic>();
	private  HashMap<String,Topic> table = new HashMap<String,Topic>();
	private  String dirout = GetPropertiesUtils.getoutPutDir()+"/result";//D:/study/crawl/crawl/etc/Electiondata/result";
	private  String root = "";

	public List<Topic> preprocess(String dir, String files){
			list.clear();table.clear();
			file f = new file();
			f.setfilepath(dir+"/"+files);f.read();
			String text = f.returnS();
			String[] line = text.split("###");
			setRoot(files,line);
			int count = 1;String content = "";int flag = 0;
			for(int j=0;j<line.length;j++){
				if(j==line.length-1){
					Topic t = list.get(list.size()-1);
					t.setText(content);
					list.set(list.size()-1, t);
				}
				int num = 0;
				num = check(line[j],num);
				if(num==0&&flag==1){
					if(!line[j].startsWith("File:")
							&&!line[j].startsWith("{")
							&&!line[j].startsWith("-")
							&&!line[j].startsWith("<")
							&&!line[j].startsWith("!")
							&&!line[j].startsWith("}")
							&&!line[j].startsWith("|")
							&&!line[j].startsWith("*")
							&&!line[j].startsWith("Category:")
							&&line[j].equals("")==false
							)//需要格式化掉{}之类的
						content += line[j]+"\r\n";
				}
				if(num>=2){
					if(flag!=0){
						Topic t = list.get(list.size()-1);
						t.setText(content);
						list.set(list.size()-1, t);
					}
					flag = 1;
					String rgx = "";
					for(int k=0;k<num;k++) rgx += "=";
					String[] t = line[j].split(rgx);
					String topic = t[1].trim();
					
					Topic top = new Topic();
					top.setId(count);
					top.setRoot(root);
					top.setTopic(topic);
					top.setLevel(num);
					top.setText(content);
					content = "";
					count++;
					list.add(top);
				}
			}
			//计算前驱和后继
			for(int j=1;j<list.size();j++){
				Topic t = list.get(j);
//				if(t.getLevel()==2){
//					t.setPre(0);
//				}
				{
					Topic tempT = null;int k = 0;
					for(k=j-1;k>=0;k--){
						tempT = list.get(k);
						if(tempT.getLevel()==(t.getLevel()-1)){
							break;
						}
					}
					if(k<0)
						continue;
					t.setPre(tempT.getId());
					List<Integer> l = tempT.getChi();
					l.add(t.getId());
					tempT.setChi(l);
					list.set(k, tempT);
				}
				list.set(j, t);
			}
			
			try {
				Write w = new Write(dirout);
				w.writeDetails(list,files);
				w.writeMap(list,files);
			} catch (IOException e) {
                System.out.println(files+"write file error");
				e.printStackTrace();
			}

		return list;
	}

	private void setRoot(String files, String[] line) {
		String[] t = files.split("[.]");
		Topic topic = new Topic();
		topic.setTopic(t[0]);
		root = t[0];
		topic.setId(0);
		topic.setLevel(1);
		topic.setPre(-1);
		String content = "";int num = 0;
		for(int j=0;j<line.length;j++){
			num = check(line[j],num);
			if(num>=2)
				break;
			else{
				if(!line[j].startsWith("File:")
						&&!line[j].startsWith("{")
						&&!line[j].startsWith("-")
						&&!line[j].startsWith("<")
						&&!line[j].startsWith("!")
						&&!line[j].startsWith("}")
						&&!line[j].startsWith(" }")
						&&!line[j].startsWith("|")
						&&!line[j].startsWith(" |")
						&&!line[j].startsWith("rect")
						&&!line[j].startsWith("Image")
						&&!line[j].startsWith("desc ")
						&&!line[j].startsWith("*")
						&&!line[j].startsWith("Category:")
						&&line[j].equals("")==false
						)//需要格式化掉{}之类的
					content += line[j]+"\r\n";
			}
		}
		topic.setText(content);
		list.add(topic);
	}

	private int check(String line, int num) {
		if(line.startsWith("=")){
			num++;
			num = check(line.substring(1),num);
		}
		else
			return num;
		return num;
	}

	public List<Topic> preprocess2(String dir, String files) {
		list.clear();
		Topic root = new Topic();
		root.setTopic("root");root.setText(" ");
		list.add(root);
		file f = new file();
		f.setfilepath(dir+"/"+files);f.read();
		String text = f.returnS();
		Topic t = new Topic();
		t.setText(text.replace("###", " "));
		t.setTopic(files);
		list.add(t);
		return list;
	}

}
