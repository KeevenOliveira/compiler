
public class CircularListNode {

    private int type;
    private String variable;
    private CircularListNode previous;
    private CircularListNode next;

    public CircularListNode(int type, String variable, CircularListNode previous, CircularListNode next) {
        this.type = type;
        this.variable = variable;
        this.previous = previous;
        this.next = next;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CircularListNode) {
            final CircularListNode other = (CircularListNode) obj;
            return this.type == other.getType() && this.variable.equals(other.getVariable());
        }
        return false;
    }

    public CircularListNode getPrevious() {
        return this.previous;
    }

    public void setPrevious(CircularListNode previous) {
        this.previous = previous;
    }

    public CircularListNode getNext() {
        return this.next;
    }

    public void setNext(CircularListNode next) {
        this.next = next;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

}
