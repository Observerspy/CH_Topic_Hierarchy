package Obe.Dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Obe.Dto.Topic;
import Obe.Util.GetAllText;
import Obe.Util.file;
import Obe.Util.common.GetPropertiesUtils;
//根据聚类结果合并文本
public class TotalMergeText {
	private  String dirtotal = "";
	private  HashMap<String,Topic> map = new  HashMap<String,Topic>();
	private int key;

	public void Merge(List<Topic> allWords) throws IOException {
		for(int i=1;i<allWords.size();i++){
			map.put(allWords.get(i).getTopic(), allWords.get(i));
		}
			
		file out = new file();
		out.setoutpath(dirtotal);out.delete();
		
		//File file=new File(dir);
		//String files[] = file.list();			

		//for(int i=0;i<files.length;i++){
			file f = new file();
			f.setfilepath(GetPropertiesUtils.getoutPutDir()+"/IncreCluster_0.2.txt");f.read();//prep
			//f.setfilepath(GetPropertiesUtils.getoutPutDir()+"/Final_New_cluster_map_node.txt");f.read();//true
			String text = f.returnS();
			String[] line = text.split("###");
			for(int j=1;j<line.length;j++){
				String content = "";String name = "";
				String[] name1 = line[j].split("#");
				for(int i=1;i<name1.length;i++){
					name += name1[i]+"#";
					if(map.containsKey(name1[i].toLowerCase())){
						Topic t = map.get(name1[i].toLowerCase());
						content += t.getText();
					}
				}
				//name = line[j].split("#")[1];

				//String num = line[j].split(":")[1].split("concept")[0].trim();//prep
				String num = line[j].split(":")[1].split(" ")[0];
				out.write("ID:"+num+" #"+name+"\r\n");
				out.write("TEXT:"+content+"\r\n");
			}

		//}
		if(key==2){
			out.write("ID:"+0+" #root#"+"\r\n");
			String content1 = GetAllText.getNew().getContent();//全文本
			out.write("TEXT:"+content1+"\r\n");
		}
	}
	
	public void MergeRoot(List<List<Topic>> l) throws IOException{
		file out = new file();
		out.setoutpath(dirtotal);
		out.write("ID:"+0+" #root#"+"\r\n");
		for(int i=0;i<l.size();i++){
			out.write("TEXT:"+l.get(i).get(0).getText()+"\r\n");
		}
	}

	public void setway(int i) {
		if(i==1){
			this.key = i;
			this.dirtotal = GetPropertiesUtils.getoutPutDir()+"/F_cluster_map.txt";
		}
		else if(i==2){
			this.key = i;
			this.dirtotal = GetPropertiesUtils.getoutPutDir()+"/cluster_mapEX.txt";
		}
	}
}
