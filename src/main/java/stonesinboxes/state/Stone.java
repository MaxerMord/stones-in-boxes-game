package stonesinboxes.state;

/**
 * Enum representing the box is empty or not.
 */
public enum Stone {
    EMPTY(1),
    STONE(0);

    private int status;

    private Stone(int status){
        this.status = status;
    }

    /**
     * Returns the state of a box.
     * @return
     */
    public int getStatus(){return status;}

    /**
     * Returns the state changing of the picked box.
     * @param status status
     * @return the state changing of the picked box
     */
    public static Stone of(int status){
        for (Stone stone : values()){
            if (stone.status == status){
                return stone;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the box without stone
     * @return
     */
    public Stone finalStatus(){
        this.status = 1;
        return EMPTY;
    }

}
