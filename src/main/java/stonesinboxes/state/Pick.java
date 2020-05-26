package stonesinboxes.state;

/**
 * Enum representing the box is empty or not.
 */
public enum Pick {
    EMPTY(0),
    STONE(1);

    private int status;

    private Pick(int status){
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
    public static Pick of(int status){
        for (Pick pick : values()){
            if (pick.status == status){
                return pick;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the
     * @return
     */
    public Pick finalStatus(){
        this.status = 0;
        return STONE;
    }

}
