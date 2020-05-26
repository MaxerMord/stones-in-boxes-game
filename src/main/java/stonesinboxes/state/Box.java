package stonesinboxes.state;

import org.hibernate.sql.Select;

/**
 * Class representing the state of a box.
 */
public enum  Box {
    EMPTY,
    STONE;

    /**
     * The array defining a box is empty or not.
     */
    private static final int[][] B = {
            {0},
            {1}
    };

    /**
     * Returns the instance represennted by the values specified.
     *
     * @param value the value representing an instance.
     * @return the instance represented by the value specified.
     * @throws IllegalArgumentException if the values specified does not represent an instance.
     */
    public static Box of(int value){
        if (value < 0 || value >= values().length){
            throw new IllegalArgumentException();
        }
        return values()[value];
    }
    /**
     * Returns the integer value that represents this instance.
     *
     * @return the integer value that represents this instance
     */
    public int getValue(){return ordinal();}

    /**
     * Choose the available box(es) and pick the stone(s) within.
     *
     * @param pick the box(es) which were chosen
     * @return the box(es) chose by player and becomes empty
     * @throws UnsupportedOperationException if the method is invoked on the
     * {@link #EMPTY} instance
     */
    public Box choose(Pick pick){
        if (this == EMPTY) {
            throw new UnsupportedOperationException();
        }
        return values()[B[ordinal()][pick.ordinal()]];
    }


    public String toString(){return Integer.toString(ordinal());}
}
