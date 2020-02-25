package hw3cs3310;

/**
 * Copied from hw2 as per instructions (List class requires this class) in point # 3.
 * char data type has been replaced with Item.
 * Objects of this class hold and link information in a (linked) list
 */
class Node {

    Item item;
    Node prev;
    Node next;

    /**
     * Creates a Node object, assigning the received values to its fields
     * @param item receives an Item object to store
     * @param prev receives reference to another node
     * @param next receives reference to another node
     */
    Node(Item item, Node prev, Node next) {
        this.item = item;
        this.prev = prev;
        this.next = next;
    }

}
