package hw3cs3310;

/**
 * Copied from hw2 as per instructions in point # 3.
 * char data type has been replaced with Item.
 * Irrelevant methods have been commented out.
 * Objects of this class hold a reference to the first Node in a chain and posses methods that allow performing
 * several actions on the chain
 */
class List {

    private Node head;
    private Node tempNodeHolder;

    /**
     * Creates a List object, assigning the received values to its fields
     * @param head receives reference to a node
     */
    List(Node head) {
        this.head = head;
    }

    /**
     * Calculates length of a Node chain
     * @param head receives reference of the node from which to start counting
     * @return length of the chain
     */
    static int length(Node head) {
        if (head == null)
            return 0;
        Node tempNodeHolder = head;
        int length = 1;
        while(tempNodeHolder.next != null) {
            length++;
            tempNodeHolder = tempNodeHolder.next;
        }
        return length;
    }

    /**
     * Calculates length of the calling list's head node's chain
     * @return length of the calling list
     */
    int length() {
        if (head == null)
            return 0;
        tempNodeHolder = head;
        int length = 1;
        while(tempNodeHolder.next != null) {
            length++;
            tempNodeHolder = tempNodeHolder.next;
        }
        return length;
    }

//    /**
//     * Builds a list tht contains 1, 2 and 3 as characters
//     * @return the built list
//     */
//    static List buildOneTwoThree() {
//        Node one = new Node('1', null, null);
//        one.next = new Node('2', one, null);
//        one.next.next = new Node('3', one.next, null);
//        return new List(one);
//    }

    /**
     * Adds a node with received data to head node's chain (just before another node)
     * @param lPtr reference to the node just before which the new node is to be added
     * @param newData receives the data to store in the new node
     * @return updated List object
     */
    static List push(Node lPtr, Item newData) {
        if(lPtr == null) {
            lPtr = new Node(newData, null, null);
        } else {
            lPtr = new Node(newData, null, lPtr);
            lPtr.next.prev = lPtr;
        }
        return new List(lPtr);
    }

    /**
     * Adds a node with received data to head node's chain (as the first node)
     * @param newData receives the data to add to list
     * @return updated List object
     */
    List push(Item newData) {
        if(head == null) {
            head = new Node(newData, null, null);
        } else {
            head = new Node(newData, null, head);
            head.next.prev = head;
        }
        return this;
    }

    /**
     * Counts the number of times a given Item occurs in a list
     * @param searchFor the Item for which occurrences have to be counted
     * @return result of the counting
     */
    int count(Item searchFor) {
        if (head == null)
            return 0;
        tempNodeHolder = head;
        int count = 0;
        while(tempNodeHolder.next != null) {
            if(searchFor.equals(tempNodeHolder.item))
                count++;
            tempNodeHolder = tempNodeHolder.next;
        }
        return count;
    }

    /**
     * Retrieves data stored in the node at the received index
     * @param index the index from which data is to be retrieved
     * @return data contained in the node at received index
     */
    Item getNth(int index) {
        if (length() <= index || index < 0) {
            System.out.println("Extraction of an out of bounds index has been attempted\n" +
                    "The program will therefore exit");
            System.exit(1);
        }
        tempNodeHolder = head;
        for (int i = 0; i < index; i++){
            tempNodeHolder = tempNodeHolder.next;
        }
        return tempNodeHolder.item;
    }

    /**
     * Deletes all data contained in list
     */
    void deleteList() {
        head = null;
    }

    /**
     * Retrieves data from the first node and deletes the node from list
     * @return data contained in first node
     */
    Item pop() {
        if(head == null) {
            System.out.println("Pop had been called on an empty list\n" +
                    "The program will therefore exit");
            System.exit(2);
        }
        Item toReturn = head.item;
        if(length() == 1) {
            head = null;
            return toReturn;
        } else {
            head = head.next;
            head.prev.next = null;
            head.prev = null;
            return toReturn;
        }
    }

    /**
     * Inserts data received in a node and places the node at received index
     * @param index index to insert (new node containing) data at
     * @param data data to store
     */
    void insertNth(int index, Item data) {
        if (length() <= index || index < 0) {
            System.out.println("Addition to an out of bounds index has been attempted\n" +
                    "The program will therefore exit");
            System.exit(3);
        }
        tempNodeHolder = head;
        for (int i = 0; i < index - 1; i++){
            tempNodeHolder = tempNodeHolder.next;
        }
        tempNodeHolder.next = new Node(data, tempNodeHolder, tempNodeHolder.next);
        tempNodeHolder.next.next.prev = tempNodeHolder.next;
    }

//    /**
//     * Inserts a new node into the list maintaining increasing alphabetical order
//     * @param newNode receives the node to insert
//     */
//    void sortedInsert(Node newNode) {
//        if(head == null) {
//            head = newNode;
//        } else {
//            tempNodeHolder = head;
//            if(tempNodeHolder.nucleotide >= newNode.nucleotide) {
//                newNode.next = tempNodeHolder;
//                newNode.next.prev = newNode;
//                head = newNode;
//            } else {
//                while (tempNodeHolder.nucleotide < newNode.nucleotide && tempNodeHolder.next != null) {
//                    tempNodeHolder = tempNodeHolder.next;
//                }
//                if(tempNodeHolder.nucleotide < newNode.nucleotide) {
//                    tempNodeHolder.next = newNode;
//                    newNode.prev = tempNodeHolder;
//                } else {
//                    tempNodeHolder.prev.next = newNode;
//                    newNode.next = tempNodeHolder;
//                    newNode.prev = tempNodeHolder.prev;
//                    tempNodeHolder.prev = newNode;
//                }
//            }
//        }
//    }

//    /**
//     * Rearranges nodes so they are sorted in increasing order using insertion sort
//     * @return updated List object
//     */
//    List insertSort() {
//        List temp = new List(head);
//        deleteList();
//        int loopTimes = temp.length();
//        for(int i = 0; i < loopTimes; i++)
//            sortedInsert(new Node(temp.pop(), null, null));
//        return this;
//    }

    /**
     * Adds the received list's node chain after last node in calling list's node chain
     * @param second receives the list to be added
     * @return updated list object
     */
    List append(List second) {
        if(head == null) {
            head = second.head;
        } else {
            tempNodeHolder = head;
            while(tempNodeHolder.next != null)
                tempNodeHolder = tempNodeHolder.next;
            tempNodeHolder.next = second.head;
            tempNodeHolder.next.prev = tempNodeHolder;
        }
        second.head = null;
        return this;
    }

//    /**
//     * Stores characters from a received String in calling object's node chain
//     * @param toStore receives the string, characters for which are to be stored in list
//     * @param validate if true, checks characters for validity; otherwise doesn't
//     * @return false if validate is true and toStore contains unexpected characters; true otherwise
//     */
//    boolean storeStrand(String toStore, boolean validate) {
//        if(validate) {
//            for (int i = 0; i < toStore.length(); i++) {
//                if (toStore.charAt(i) == 'A' || toStore.charAt(i) == 'C' || toStore.charAt(i) == 'G' ||
//                        toStore.charAt(i) == 'T' || toStore.charAt(i) == 'U') {
//                    append(new List(new Node(toStore.charAt(i), null, null)));
//                } else {
//                    System.out.printf("\"%s\" contains characters that do not denote DNA molecules. Skipped.\n",
//                            toStore);
//                    return false;
//                }
//            }
//        } else {
//            for (int i = 0; i < toStore.length(); i++)
//                append(new List(new Node(toStore.charAt(i), null, null)));
//        }
//        return true;
//    }

}
