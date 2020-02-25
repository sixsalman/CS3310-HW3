package hw3cs3310;

import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.IOException;

/**
 * This program reads details of items (separated by commas) from an input file, stores the items in an array and then
 * in bags of capacity 125 (randomly choosing an item from original array each time) and searches them for random items
 * chosen from the original array - The bags act as hashing tables and multiple hashing functions and collision
 * handling techniques along with open hashing have been implemented.
 *
 * @author M. Salman Khan
 */
class Main {

    private static int[] permutation = new int[199];

    /**
     * Main method takes inputs, calls other methods to accomplish certain tasks and outputs
     * @param args not used
     * @throws IOException can be thrown if input file is missing in directory
     */
    public static void main(String[] args) throws IOException {

        List[][][] openHashedBags;
        Item[][][][] closeHashedBags;
        Scanner items = new Scanner(new File("items.txt"));
        items.nextLine();
        Item[] itemsArray;
        String[] itemData;
        Random rand = new Random();
        Item toSearch;
        long stTime, endTime;

        // STEP # 4 - see hashCode method of Item class for implementation
        System.out.println("Here are my three hashing functions:\nFunction1: uses string folding method and " +
                "considers name, rarity and current strength\nFunction2: uses simple hash function for strings " +
                "and considers name, rarity and current strength\nFunction3: uses simple hash function for " +
                "strings and considers name and current strength\n");

        // creating the permutation array for pseudo-random probe
        for(int i = 1; i < 199; i++) {
            int toAdd;
            boolean existsAlready;
            do {
                toAdd = rand.nextInt(198) + 1;
                existsAlready = false;
                for (int j = 1; j < 199 && permutation[j] != 0; j++) {
                    if (permutation[j] == toAdd) {
                        existsAlready = true;
                        break;
                    }
                }
            } while(existsAlready);
            permutation[i] = toAdd;
        }


        // STEP # 1
        int count = 0;
        while(items.hasNext()) {
            items.nextLine();
            count++;
        }
        itemsArray = new Item[count];
        items = new Scanner(new File("items.txt"));
        items.nextLine();
        for (int i = 0; i < itemsArray.length; i++) {
            itemData = items.nextLine().split(",");
            itemsArray[i] = new Item(itemData[0], Integer.parseInt(itemData[1]), Integer.parseInt(itemData[2]),
                    itemData[3]);
        }

        // STEP # 7
        for (int n = 1; n <= 10000; n *= 10) {

            System.out.printf("n=%d\n", n);
            openHashedBags = new List[3][n][199];
            closeHashedBags = new Item[3][3][n][199];


            // STEPS # 2 & # 3
            for(int i = 0; i < 3; i++){
                for (int j = 0; j < n; j ++) {
                    for (int k = 0; k < 199; k++) {
                        openHashedBags[i][j][k] = new List(null);
                    }
                }
            }

            // open hashing
            for (int k = 0; k < 3; k++){
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < 125; j++) {
                        Item toAdd = itemsArray[rand.nextInt(itemsArray.length)];
                        if(toAdd.getCurrentStrength() == 0)
                            toAdd.randAssignCurrStrength();
                        int hCode = toAdd.hashCode(k, 199);
                            openHashedBags[k][i][hCode].append(new List(new Node(toAdd, null, null)));
                    }
                }
            }

            // closed hashing
            for (int l = 0; l < 3; l++) {
                for (int k = 0; k < 3; k++) {
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < 125; j++) {
                            Item toAdd = itemsArray[rand.nextInt(itemsArray.length)];
                            if(toAdd.getCurrentStrength() == 0)
                                toAdd.randAssignCurrStrength();
                            int hCode = toAdd.hashCode(k, 199);
                            if (closeHashedBags[l][k][i][hCode] == null) {
                                closeHashedBags[l][k][i][hCode] = toAdd;
                            } else {
                                int collisionHandledIndex = resolveCollision(l, closeHashedBags[l][k][i], hCode);
                                closeHashedBags[l][k][i][collisionHandledIndex] = toAdd;
                            }
                        }
                    }
                }
            }


            // printing contents of bags
            if (n <= 10) {
                for (int i = 0; i < n; i++) {
                    for (int k = 0; k < 3; k++){
                        System.out.printf("Bag %d using Open Hashing with my Hashing Function%d:\n", (i + 1), (k + 1));
                        for (int j = 0; j < 5; j++) {
                            System.out.printf("\tSlot %d:\n", j);
                            if (openHashedBags[k][i][j].length() != 0) {
                                for (int l = 0; l < openHashedBags[k][i][j].length(); l++)
                                    System.out.printf("\t\t%s", openHashedBags[k][i][j].getNth(l));
                            } else {
                                System.out.println("\t\tEMPTY");
                            }
                        }
                        System.out.print("\t\t...\n\n");
                    }
                    for (int l = 0; l < 3; l++) {
                        for (int k = 0; k < 3; k++) {
                            if (l == 0)
                                System.out.printf("Bag %d using Linear-Probing Closed Hashing with my Hashing " +
                                                "Function%d:\n", (i + 1), (k + 1));
                            if (l == 1)
                                System.out.printf("Bag %d using Pseudo-Random-Probing Closed Hashing with my Hashing " +
                                        "Function%d:\n", (i + 1), (k + 1));
                            if (l == 2)
                                System.out.printf("Bag %d using Double-Hashing-Probing Closed Hashing with my " +
                                        "Hashing Function%d:\n", (i + 1), (k + 1));
                            for (int j = 0; j < 5; j++) {
                                System.out.printf("\tSlot %d:\n", j);
                                if (closeHashedBags[l][k][i][j] != null) {
                                    System.out.printf("\t\t%s", closeHashedBags[l][k][i][j]);
                                } else {
                                    System.out.println("\t\tEMPTY");
                                }
                            }
                            System.out.print("\t\t...\n\n");
                        }
                    }
                    if (i != (n - 1)) {
                        System.out.println(". . .");
                    }
                }
            }



            // STEP # 5 & # 6
            int hCode;

            // open hashing
            List hashTableList;
            StringBuilder toPrint;
            for (int l = 0; l < 3; l++) {
                stTime = System.nanoTime();
                for (int k = 0; k < 100; k++) {
                    do {
                        hashTableList = openHashedBags[l][rand.nextInt(n)][rand.nextInt(199)];
                    } while (hashTableList.length() == 0);
                    toSearch = hashTableList.getNth(rand.nextInt(hashTableList.length()));
                    // to find in openHashedBags[l]
                    hCode = toSearch.hashCode(l, 199);
                    if (k == 0)
                        System.out.printf("Searching for %s %s...\t(Using Open Hashing with my Hashing Function%d)\n",
                                toSearch.getRarity(), toSearch.getName(), (l + 1));
                    toPrint = new StringBuilder("");
                    for (int i = 0; i < n; i++) {
                        if (openHashedBags[l][i][hCode].length() != 0) {
                            if (openHashedBags[l][i][hCode].length() == 1 &&
                                    openHashedBags[l][i][hCode].getNth(0).equals(toSearch)) {
                                if (toPrint.toString().equals(""))
                                    toPrint.append("Found in ");
                                if (toPrint.length() > 9)
                                    toPrint.append(", ");
                                toPrint.append(String.format("(bag %d, slots %d. Strengths: %d)", i + 1, hCode,
                                        openHashedBags[l][i][hCode].getNth(0).getCurrentStrength()));
                            } else {
                                for (int j = 0; j < openHashedBags[l][i][hCode].length(); j++) {
                                    if (openHashedBags[l][i][hCode].getNth(j).equals(toSearch)) {
                                        if (toPrint.toString().equals("") ||
                                                toPrint.charAt(toPrint.length() - 1) == ')') {
                                            if (toPrint.toString().equals(""))
                                                toPrint.append("Found in ");
                                            if (toPrint.length() > 9)
                                                toPrint.append(", ");
                                            toPrint.append(String.format("(bag %d, slots %d. Strengths: %d",
                                                    i + 1, hCode,
                                                    openHashedBags[l][i][hCode].getNth(j).getCurrentStrength()));
                                        } else {
                                            toPrint.append(String.format(", %d",
                                                    openHashedBags[l][i][hCode].getNth(j).getCurrentStrength()));
                                        }
                                    }
                                }
                                if (!toPrint.toString().equals("") &&
                                        Character.isDigit(toPrint.toString().charAt(toPrint.length() - 1)))
                                    toPrint.append(")");
                            }
                        }
                    }
                    if (k == 0) {
                        if (toPrint.toString().equals("")) {
                            System.out.println("Not Found.");
                        } else {
                            System.out.println(toPrint.toString());
                        }
                    }
                }
                endTime = System.nanoTime();
                System.out.printf("Average search time: %d nanoseconds.\n", ((endTime - stTime) / 100));
                System.out.print("\n");
            }

            // closed hashing
            boolean searchEnd;
            StringBuilder slots;
            StringBuilder strengths;

            // linear probe
            for (int i = 0; i < 3; i++) {
                stTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    do {
                        toSearch = closeHashedBags[0][i][rand.nextInt(n)][rand.nextInt(199)];
                    } while (toSearch == null);
                    hCode = toSearch.hashCode(i, 199);
                    if (l == 0)
                        System.out.printf("Searching for %s %s...\t(Using Linear-Probing Closed Hashing with my " +
                                "Hashing Function%d)\n", toSearch.getRarity(), toSearch.getName(), (i + 1));
                    toPrint = new StringBuilder("");
                    for (int j = 0; j < n; j++) {
                        slots = new StringBuilder("");
                        strengths = new StringBuilder("");
                        searchEnd = false;
                        if (closeHashedBags[0][i][j][hCode] != null &&
                                closeHashedBags[0][i][j][hCode].equals(toSearch)) {
                            slots.append(hCode);
                            strengths.append(closeHashedBags[0][i][j][hCode].getCurrentStrength());

                            for (int k = hCode + 1; k < 199; k++) {
                                if (closeHashedBags[0][i][j][k] != null &&
                                        closeHashedBags[0][i][j][k].equals(toSearch)) {
                                    if (!slots.toString().equals(""))
                                        slots.append(", ");
                                    if (!strengths.toString().equals(""))
                                        strengths.append(", ");
                                    slots.append(k);
                                    strengths.append(closeHashedBags[0][i][j][k].getCurrentStrength());

                                } else {
                                    searchEnd = true;
                                    break;
                                }
                            }
                            if (!searchEnd) {
                                for (int k = 0; k < hCode; k++) {
                                    if (closeHashedBags[0][i][j][k] != null &&
                                            closeHashedBags[0][i][j][k].equals(toSearch)) {
                                        if (!slots.toString().equals(""))
                                            slots.append(", ");
                                        if (!strengths.toString().equals(""))
                                            strengths.append(", ");
                                        slots.append(k);
                                        strengths.append(closeHashedBags[0][i][j][k].getCurrentStrength());

                                    } else {
                                        break;
                                    }
                                }
                            }
                        } else {
                            boolean firstFound = false;
                            for (int k = hCode + 1; k < 199; k++) {
                                if (closeHashedBags[0][i][j][k] != null &&
                                        closeHashedBags[0][i][j][k].equals(toSearch)) {
                                    if (!slots.toString().equals(""))
                                        slots.append(", ");
                                    if (!strengths.toString().equals(""))
                                        strengths.append(", ");
                                    slots.append(k);
                                    strengths.append(closeHashedBags[0][i][j][k].getCurrentStrength());

                                    firstFound = true;
                                } else if (firstFound) {
                                    searchEnd = true;
                                    break;
                                }
                            }
                            if (!searchEnd) {
                                for (int k = 0; k < hCode; k++) {
                                    if (closeHashedBags[0][i][j][k] != null &&
                                            closeHashedBags[0][i][j][k].equals(toSearch)) {
                                        if (!slots.toString().equals(""))
                                            slots.append(", ");
                                        if (!strengths.toString().equals(""))
                                            strengths.append(", ");
                                        slots.append(k);
                                        strengths.append(closeHashedBags[0][i][j][k].getCurrentStrength());

                                        firstFound = true;
                                    } else if (firstFound) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (!slots.toString().equals("")) {
                            if (toPrint.toString().equals(""))
                                toPrint.append("Found in ");
                            if (!(toPrint.toString().equals("") || toPrint.toString().equals("Found in ")))
                                toPrint.append(", ");
                            toPrint.append(String.format("(bag %d, slots %s. Strengths: %s)", j + 1, slots,
                                    strengths));
                        }
                    }
                    if (l == 0) {
                        if (toPrint.toString().equals("")) {
                            System.out.println("Not Found.");
                        } else {
                            System.out.println(toPrint.toString());
                        }
                    }
                }
                endTime = System.nanoTime();
                System.out.printf("Average search time: %d nanoseconds.\n", ((endTime - stTime) / 100));
                System.out.print("\n");
            }

            // pseudo-random probe
            for (int i = 0; i < 3; i++) {
                stTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    do {
                        toSearch = closeHashedBags[1][i][rand.nextInt(n)][rand.nextInt(199)];
                    } while (toSearch == null);
                    hCode = toSearch.hashCode(i, 199);
                    if (l == 0)
                        System.out.printf("Searching for %s %s...\t(Using Pseudo-Random-Probing Closed Hashing " +
                                "with my Hashing Function%d)\n", toSearch.getRarity(), toSearch.getName(), (i + 1));
                    toPrint = new StringBuilder("");
                    for (int j = 0; j < n; j++) {
                        slots = new StringBuilder("");
                        strengths = new StringBuilder("");
                        int toChk = (hCode + permutation[1]) % 199;
                        if (closeHashedBags[1][i][j][hCode] != null &&
                                closeHashedBags[1][i][j][hCode].equals(toSearch)) {
                            slots.append(hCode);
                            strengths.append(closeHashedBags[1][i][j][hCode].getCurrentStrength());
                            for(int m = toChk, o = 2; closeHashedBags[1][i][j][toChk] != null &&
                                    closeHashedBags[1][i][j][toChk].equals(toSearch) && o < 199; m++, o++) {
                                if (!slots.toString().equals(""))
                                    slots.append(", ");
                                if (!strengths.toString().equals(""))
                                    strengths.append(", ");
                                slots.append(toChk);
                                strengths.append(closeHashedBags[1][i][j][toChk].getCurrentStrength());
                                toChk = (hCode + permutation[o]) % 199;
                            }
                        } else {
                            boolean firstFound = false;
                            for(int m = toChk, o = 2; closeHashedBags[1][i][j][toChk] != null &&
                                    o < 199; m++, o++) {
                                if (closeHashedBags[1][i][j][toChk].equals(toSearch)) {
                                    if (!slots.toString().equals(""))
                                        slots.append(", ");
                                    if (!strengths.toString().equals(""))
                                        strengths.append(", ");
                                    slots.append(toChk);
                                    strengths.append(closeHashedBags[1][i][j][toChk].getCurrentStrength());
                                    firstFound = true;
                                } else if (firstFound) {
                                    break;
                                }
                                toChk = (hCode + permutation[o]) % 199;
                            }
                        }
                        if (!slots.toString().equals("")) {
                            if (toPrint.toString().equals(""))
                                toPrint.append("Found in ");
                            if (!(toPrint.toString().equals("") || toPrint.toString().equals("Found in ")))
                                toPrint.append(", ");
                            toPrint.append(String.format("(bag %d, slots %s. Strengths: %s)", j + 1, slots,
                                    strengths));
                        }
                    }
                    if (l == 0) {
                        if (toPrint.toString().equals("")) {
                            System.out.println("Not Found.");
                        } else {
                            System.out.println(toPrint.toString());
                        }
                    }
                }
                endTime = System.nanoTime();
                System.out.printf("Average search time: %d nanoseconds.\n", ((endTime - stTime) / 100));
                System.out.print("\n");
            }

            // double hashing probe
            for (int i = 0; i < 3; i++) {
                stTime = System.nanoTime();
                for (int l = 0; l < 100; l++) {
                    do {
                        toSearch = closeHashedBags[2][i][rand.nextInt(n)][rand.nextInt(199)];
                    } while (toSearch == null);
                    hCode = toSearch.hashCode(i, 199);
                    int step = 1 + (hCode % (198));
                    if (l == 0)
                        System.out.printf("Searching for %s %s...\t(Using Double-Hashing-Probing Closed Hashing " +
                                "with my Hashing Function%d)\n", toSearch.getRarity(), toSearch.getName(), (i + 1));
                    toPrint = new StringBuilder("");
                    for (int j = 0; j < n; j++) {
                        slots = new StringBuilder("");
                        strengths = new StringBuilder("");
                        int toChk = (hCode + step) % 199;
                        if (closeHashedBags[2][i][j][hCode] != null &&
                                closeHashedBags[2][i][j][hCode].equals(toSearch)) {
                            slots.append(hCode);
                            strengths.append(closeHashedBags[2][i][j][hCode].getCurrentStrength());
                            for(int m = hCode + step, o = 1; closeHashedBags[2][i][j][toChk] != null &&
                                    closeHashedBags[2][i][j][toChk].equals(toSearch) && o < 199; m += step, o++) {
                                if (!slots.toString().equals(""))
                                    slots.append(", ");
                                if (!strengths.toString().equals(""))
                                    strengths.append(", ");
                                slots.append(toChk);
                                strengths.append(closeHashedBags[2][i][j][toChk].getCurrentStrength());
                                toChk = (hCode + m) % 199;
                            }
                        } else {
                            boolean firstFound = false;
                            for(int m = hCode + step, o = 1; closeHashedBags[2][i][j][toChk] != null &&
                                    o < 199; m+= step, o++) {
                                if (closeHashedBags[2][i][j][toChk].equals(toSearch) ) {
                                    if (!slots.toString().equals(""))
                                        slots.append(", ");
                                    if (!strengths.toString().equals(""))
                                        strengths.append(", ");
                                    slots.append(toChk);
                                    strengths.append(closeHashedBags[2][i][j][toChk].getCurrentStrength());
                                    firstFound = true;
                                } else if (firstFound) {
                                    break;
                                }
                                toChk = (hCode + m) % 199;
                            }
                        }
                        if (!slots.toString().equals("")) {
                            if (toPrint.toString().equals(""))
                                toPrint.append("Found in ");
                            if (!(toPrint.toString().equals("") || toPrint.toString().equals("Found in ")))
                                toPrint.append(", ");
                            toPrint.append(String.format("(bag %d, slots %s. Strengths: %s)", j + 1, slots,
                                    strengths));
                        }
                    }
                    if (l == 0) {
                        if (toPrint.toString().equals("")) {
                            System.out.println("Not Found.");
                        } else {
                            System.out.println(toPrint.toString());
                        }
                    }
                }
                endTime = System.nanoTime();
                System.out.printf("Average search time: %d nanoseconds.\n", ((endTime - stTime) / 100));
                System.out.print("\n");
            }

        }

    }

    /**
     * In case of a collision, finds the next appropriate index (slot) for the item with a certain hashCode
     * @param probingTech receives an integer indicating which collision handling handling is to be used - 0: Linear
     *                    1: Pseudo-Random    2: Double-Hashing
     * @param hashTable receives the hash table (array) in which collision has occurred
     * @param hCode receives the hash code which has experienced collision
     * @return next appropriate index to store the item
     */
    private static int resolveCollision(int probingTech, Item[] hashTable, int hCode) {
        int toRet = hCode;
        switch (probingTech) {
            case 0:
                // linear probe
                for (int i = hCode + 1; i < hashTable.length; i++) {
                    if(hashTable[i] == null)
                        return i;
                }
                for (int i = 0; i < hCode; i++) {
                    if(hashTable[i] == null)
                        return i;
                }
                break;
            case 1:
                // pseudo-random probe
                for(int i = 1; i < 199 && hashTable[toRet] != null; i++) {
                    toRet = (hCode + permutation[i]) % 199;
                }
                return toRet;
            case 2:
                // double-hashing probe
                int step = 1 + (hCode % (198)); // used because size of hashTable i.e. 199 is prime
                for(int i = hCode + step, j = 1; hashTable[toRet] != null && j < 199; i+= step, j++) {
                    toRet = (hCode + i) % 199;
                }
                return toRet;
        }
        return -1;
    }

}