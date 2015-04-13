package Obe.Main;
/**
 * 跑出一遍total就行了，除非AllWordsMap又要变
 * 这个程序读total，计算相似度
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Obe.Dao.CountCossim;
import Obe.Dao.VectorCossim;
import Obe.Dto.Topic;
import Obe.Util.ReadVector;
import Obe.Util.file;

public class Main2 {
	private static String dir = "D:/study/crawl/crawl/etc/En/englishNews_total/total_map.txt";
	private static String dirR1 = "D:/study/crawl/crawl/etc/En/result1.txt";
	private static String dirR2 = "D:/study/crawl/crawl/etc/En/result2.txt";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		List<Topic> allWords =new ArrayList<Topic>();
		file f = new file();
		f.setfilepath(dir);f.read();
		String text = f.returnS();
		String[] t = text.split("###");
		for(int i=2;i<t.length;i++){
			Topic topic = new Topic();
			topic.setTopic(t[i]);
			allWords.add(topic);
		}
		
		allWords.add(0,null);

		List<Topic> allWords2 = allWords;
		ReadVector rv = new ReadVector();
		HashMap<String, List<Double>>  table = rv.getVector();
		
		VectorCossim vc = new VectorCossim();
		vc.setWays(1);
		allWords = vc.count(allWords,table);
		//write way 1
				System.out.println("cosine similarity way1:");
				f.setoutpath(dirR1);f.delete();
				f.write("cosine similarity:\r\n");
				int num=0;
				for(int k=1;k<allWords.size();k++){
					Topic t1 = allWords.get(k);
					for(int j=k+1;j<allWords.size();j++){
						Topic t2 = allWords.get(j);
						double val = 0.0;
						CountCossim cossim = new CountCossim();
						try{
						val = cossim.getCos2(t1, t2);
						}catch(Exception e){
							System.out.println(t1.getTopic()+" AND "+t2.getTopic());
						}
						if(true){
							f.write(t1.getTopic()+" AND "+t2.getTopic()+" cossim:"+val+"\r\n");
							//System.out.println(t1.getTopic()+" AND "+t2.getTopic()+" cossim:"+val);
						}
						num++;
					}
				}
				//write way 2
				vc.setWays(2);
				allWords2 = vc.count(allWords2,table);
				
				
				System.out.println("cosine similarity way2:");
				f.setoutpath(dirR2);f.delete();
				f.write("cosine similarity:\r\n");
				num=0;
				for(int k=1;k<allWords2.size();k++){
					Topic t1 = allWords2.get(k);
					for(int j=k+1;j<allWords2.size();j++){
						Topic t2 = allWords2.get(j);
						double val =0.0;
						CountCossim cossim = new CountCossim();
						try{
							val = cossim.getCos2(t1, t2);
							}catch(Exception e){
								System.out.println(t1.getTopic()+" AND "+t2.getTopic());
							}	
						if(true){
							f.write(t1.getTopic()+" AND "+t2.getTopic()+" cossim:"+val+"\r\n");
							//System.out.println(t1.getTopic()+" AND "+t2.getTopic()+" cossim:"+val);
						}
						num++;
					}
				}
				System.out.println("finish!");

	}

}
