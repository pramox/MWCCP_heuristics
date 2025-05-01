package entities;

public class Constraint {
    Node node1;
    Node node2;

    int node1Number;
    int node2Number;

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public int getNode1Number() {
        return node1Number;
    }

    public void setNode1Number(int node1Number) {
        this.node1Number = node1Number;
    }

    public int getNode2Number() {
        return node2Number;
    }

    public void setNode2Number(int node2Number) {
        this.node2Number = node2Number;
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "node1=" + node1 +
                ", node2=" + node2 +
                '}';
    }

    public boolean containsNode(int number) {
        return number == node1Number || number == node2Number;
    }

    public Constraint(int node1, int node2) {
        this.node1Number = node1;
        this.node2Number = node2;
    }
}
