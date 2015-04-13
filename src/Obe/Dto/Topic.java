package Obe.Dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Topic {
	private int id;
	private String topic;
	private String topicWithoutStop;
	private String text;
	private String root;
	private int level;
	private int count;//wordmap时计数
	private int pre;//0为跟
	private List<Integer> chi = new ArrayList<Integer>();//孩子节点列表
	private List<Double> p = new ArrayList<Double>();
	private List<Double> v = new ArrayList<Double>();//读向量结果
	private List<String> map = new ArrayList<String>();
	private HashMap<String,Integer> maps = new HashMap<String,Integer>();
	private HashMap<String,Double> m = new HashMap<String,Double>();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPre() {
		return pre;
	}
	public void setPre(int pre) {
		this.pre = pre;
	}
	public List<Integer> getChi() {
		return chi;
	}
	public void setChi(List<Integer> chi) {
		this.chi = chi;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public List<Double> getP() {
		return p;
	}
	public void setP(List<Double> p) {
		this.p = p;
	}
	public List<String> getMap() {
		return map;
	}
	public void setMap(List<String> map) {
		this.map = map;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Double> getV() {
		return v;
	}
	public void setV(List<Double> v) {
		this.v = v;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getTopicWithoutStop() {
		return topicWithoutStop;
	}
	public void setTopicWithoutStop(String topicWithoutStop) {
		this.topicWithoutStop = topicWithoutStop;
	}
	public HashMap<String,Integer> getMaps() {
		return maps;
	}
	public void setMaps(HashMap<String,Integer> maps) {
		this.maps = maps;
	}
	public HashMap<String,Double> getM() {
		return m;
	}
	public void setM(HashMap<String,Double> m) {
		this.m = m;
	}



}
