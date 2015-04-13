package Obe.Dto;

public class Edge {
	private Node target;
	private Node source;
	private int edge;//1有边
	
	public Node getTarget() {
		return target;
	}
	public void setTarget(Node target) {
		this.target = target;
	}
	public Node getSource() {
		return source;
	}
	public void setSource(Node source) {
		this.source = source;
	}
	public int getEdge() {
		return edge;
	}
	public void setEdge(int edge) {
		this.edge = edge;
	}

}
