import java.util.Objects;

public class CircularLinkedList {

    private CircularListNode sentinel;

    public CircularLinkedList() {
        this.sentinel = new CircularListNode(0, "", null, null);
        this.sentinel.setPrevious(this.sentinel);
        this.sentinel.setNext(this.sentinel);
    }

    public boolean isEmpty() {
        return getTail() == this.sentinel && getHead() == this.sentinel;
    }

    public void addFirst(int type, String variable) {
        if (isEmpty()) {
            CircularListNode newCircularNode = new CircularListNode(type, variable, this.sentinel, this.sentinel);
            this.sentinel.setNext(newCircularNode);
            this.sentinel.setPrevious(newCircularNode);
        } else {
            CircularListNode newCircularNode = new CircularListNode(type, variable, this.sentinel,
                    this.sentinel.getNext());
            this.sentinel.getNext().setPrevious(newCircularNode);
            this.sentinel.setNext(newCircularNode);
        }
    }

    public CircularListNode getTail() {
        return this.sentinel.getPrevious();
    }

    public int size() {
        if (isEmpty()) {
            return 0;
        } else {
            CircularListNode currentNode = this.sentinel;
            int size = 0;
            do {
                size++;
                currentNode = currentNode.getNext();
            } while (currentNode.getNext() != this.sentinel);
            return size;
        }
    }

    public void addLast(int type, String variable) {
        if (isEmpty()) {
            addFirst(type, variable);
        } else {
            CircularListNode newCircularNode = new CircularListNode(type, variable, this.sentinel.getPrevious(),
                    this.sentinel);
            this.sentinel.getPrevious().setNext(newCircularNode);

            this.sentinel.setPrevious(newCircularNode);
        }
    }

    public void reverse() {
        CircularListNode currentNode = getHead();
        CircularListNode nextNode = currentNode;
        CircularListNode previousNode = this.sentinel;

        do {
            nextNode = nextNode.getNext();
            currentNode.setPrevious(currentNode.getNext());
            currentNode.setNext(previousNode);
            previousNode = currentNode;
            currentNode = nextNode;

        } while (currentNode != this.sentinel);
        currentNode.setPrevious(currentNode.getNext());
        currentNode.setNext(previousNode);
        this.sentinel.setNext(previousNode);
    }

    public CircularListNode getHead() {
        return this.sentinel.getNext();
    }

    public CircularListNode search(String variable) {
        CircularListNode currentNode = getHead();
        do {
            if (Objects.equals(currentNode.getVariable(), variable)) {
                return currentNode;
            }
            currentNode = currentNode.getNext();
        } while (currentNode != this.sentinel);

        return null;
    }

    public void delete(CircularListNode nodeDelete) {
        CircularListNode currentNode = getHead();

        do {

            if (currentNode == nodeDelete) {
                if (currentNode.getPrevious() == this.sentinel && currentNode.getNext() == this.sentinel) {
                    this.sentinel.setNext(this.sentinel);
                    this.sentinel.setPrevious(this.sentinel);
                } else if (currentNode.getPrevious() == this.sentinel && currentNode.getNext() != this.sentinel) {
                    this.sentinel.setNext(currentNode.getNext());
                    this.sentinel.getNext().setPrevious(this.sentinel);
                } else if (currentNode.getNext() == this.sentinel && currentNode.getPrevious() != this.sentinel) {
                    currentNode.getPrevious().setNext(this.sentinel);
                    this.sentinel.setPrevious(currentNode.getPrevious());
                } else if (currentNode.getNext() != this.sentinel && currentNode.getPrevious() != this.sentinel) {
                    currentNode.getPrevious().setNext(currentNode.getNext());
                    currentNode.getNext().setPrevious(currentNode.getPrevious());
                }
                return;
            }

            currentNode = currentNode.getNext();
        } while (currentNode != this.sentinel);

    }

}
