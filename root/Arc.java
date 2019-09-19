
public class Arc {

	private int node1;
	private int node2;
	private int weight;
	private int id;
	
	public Arc(String[] p) {
		
		id = Integer.parseInt(p[0]);
		node1 = Integer.parseInt(p[1]);
		node2 = Integer.parseInt(p[2]);
		weight = Integer.parseInt(p[3]);
	}
	
	public int getNode1(){
		return node1;
	}
	
	public int getNode2(){
		return node2;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public int getId(){
		return id;
	}

	public boolean equals(Arc a){
		return (a.getId() == id);
	}
}
