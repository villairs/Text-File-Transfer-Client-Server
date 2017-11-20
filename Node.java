
import java.util.ArrayList;


public class Node{

private ArrayList<Node> parent;
private ArrayList<Node> child;
private ArrayList<double[]> prob;//X, Y, y is max 2.
private String name;


public Node(String name){
this.name = name;
child = new ArrayList<Node>();
parent = new ArrayList<Node>();
}

public void makeRoot(double[] d){
double[] r = new double [2];
r[0]= d[0];
  r[1] = d[1];
}

public void addParent(Node n){
parent.add(n);
  parent.get(parent.size() -1).child.add(this);
double[] d = new double[2];
prob.add(d);
}



}