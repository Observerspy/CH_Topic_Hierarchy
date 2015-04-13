package Obe.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class file {

	private String filePath;
	private  String outPath;
	public static String text="";
	
	    public void setfilepath(String filepath){
	    	this.filePath = filepath;
	    	text = "";
	    }
	    
	    public void setoutpath(String filepath){
	    	this.outPath = filepath;
	    	text = "";
	    }
	    
	    public String returnS(){
	    	return this.text;
	    }

	    public void read(){
			try {
				this.loadFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void loadFile() throws IOException{
			File file = new File(filePath);
			
			if(!file.exists()){
				file.createNewFile();
			}
			
			FileInputStream input=new FileInputStream(filePath);
			InputStreamReader isr; 
			isr = new InputStreamReader(input, "utf-8");
			BufferedReader read = new BufferedReader(isr);
			String s=null;
			int count = 0;
			while((s=read.readLine())!=null)
			{
			//System.out.println(s);
				if(s.trim().length()>1){
					text=text.concat("###"+s.trim());
					count++;
				} 
			}
			
			
			input.close();
		}
		
		public void delete(){
			File ff = new File(outPath);
			if(ff.exists()){
				ff.delete();
			}
		}
		
		public void write(String t) throws IOException{
			
			
			File stuFile = new File(outPath);
			FileOutputStream write=new FileOutputStream(outPath,true);
			if(!stuFile.exists()){
				stuFile.createNewFile();
			}
			PrintStream print = new PrintStream(write);
			print.print(t.toString());
	        write.close();
		}
}
