package hw3cs3310;

import java.util.Random;

/**
 * Objects of this class hold info related to an item
 */
class Item implements Comparable<Item> {

    private String name;
    private int minStrength;
    private int maxStrength;
    private String rarity;
    private int currentStrength;

    /**
     * Creates an Item object storing received parameters
     * @param name receives name of the item
     * @param minStrength receives minimum strength of the item
     * @param maxStrength receives maximum strength of the item
     * @param rarity receives rarity of the item
     */
    Item(String name, int minStrength, int maxStrength, String rarity) {
        this.name = name;
        this.minStrength = minStrength;
        this.maxStrength = maxStrength;
        this.rarity = rarity;
    }

    /**
     * Assigns a random value between min and max strengths (inclusive) as current strength of the item
     */
    void randAssignCurrStrength() {
        Random rand = new Random();
        currentStrength = minStrength + rand.nextInt(maxStrength - minStrength + 1);
    }

    /**
     * Gets name of the item
     * @return name of the item
     */
    String getName() {
        return name;
    }

    /**
     * Gets rarity of the item
     * @return rarity of the item
     */
    String getRarity() {
        return rarity;
    }

    /**
     * Gets current strength of the item
     * @return current strength of the item
     */
    int getCurrentStrength() {
        return currentStrength;
    }

    /**
     * Compares names of calling and received objects and returns a value accordingly, if names are same then
     * current strengths are compared
     * @param o receives an Item object
     * @return negative if o is greater than calling object, positive if vice-versa and 0 if both are equal
     */
    @Override
    public int compareTo(Item o) {
        if (!this.name.equalsIgnoreCase(o.name))
            return name.compareToIgnoreCase(o.name);
        return currentStrength - o.currentStrength;
    }

    /**
     * Compares names and rarities of calling and received objects to determine equivalence
     * @param o receives an Item object
     * @return true if o's name and rarity are equal to calling object's; false otherwise
     */
    boolean equals (Item o) {
        return (name.equals(o.name) && rarity.equals(o.rarity));
    }

    /**
     * Compares all fields of calling and received objects to determine equivalence
     * @param o receives an Item object
     * @return true if all of o's fields are equal to calling object's; false otherwise
     */
    boolean fullyEquals (Item o) {
        return (name.equals(o.name) && rarity.equals(o.rarity) && maxStrength == o.maxStrength
                && minStrength == o.minStrength && currentStrength == o.currentStrength);
    }

    /**
     * Returns a printable string containing the objects' rarity, name and current strength
     * @return a printable string
     */
    @Override
    public String toString() {
        return String.format("%s %s, %d\n", rarity, name, currentStrength);
    }

    int hashCode(int choice, int arrayLen) { // STEP # 4
        String toHash;
        int sum = 0;
        switch (choice) {
            case 0:
                // code in this case has been copied from section 10.3 of Canvas readings
                // and represents the string folding method (name, rarity and current strength considered)
                toHash = name + rarity + currentStrength;
                long longSum = 0, mul = 1;
                for (int i = 0; i < toHash.length(); i++) {
                    mul = (i % 4 == 0) ? 1 : mul * 256;
                    longSum += toHash.charAt(i) * mul;
                }
                return (int) (longSum % arrayLen);
            case 1:
                // simple hash function for strings (name, rarity and current strength considered)
                toHash = name + rarity + currentStrength;
                for(int i = 0; i < toHash.length(); i ++) {
                    sum += toHash.charAt(i);
                }
                return sum % arrayLen;
            case 2:
                // simple hash function for strings (name and current strength considered)
                toHash = name + currentStrength;
                for(int i = 0; i < toHash.length(); i ++) {
                    sum += toHash.charAt(i);
                }
                return sum % arrayLen;
        }
        return -1;
    }
}
