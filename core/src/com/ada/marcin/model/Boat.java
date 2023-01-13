package com.ada.marcin.model;

/**    This class is used during reading data from the adaship_config.ini file
 *      Objects of this type are   stored in a singleton  object. They will be heavily reused .
 *
 */
public class Boat {
    private final String name;
    private final int length;
    private final int boatIdx;

    /**
     *
     * @param boatIdx  unique identifier of the ship
     * @param name     name of the ship
     * @param length   length of the ship
     */
    public Boat(int boatIdx,
                String name,
                int length) {
        this.name = name;
        this.length = length;
        this.boatIdx = boatIdx;
    }

    public int getBoatIdx() {
        return boatIdx;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }
}
