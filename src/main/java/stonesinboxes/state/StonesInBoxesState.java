package stonesinboxes.state;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * State class
 */
@Data
@Slf4j
public class StonesInBoxesState implements Cloneable{
    /**
     * Initial state, all boxes with a stone except 1 that choose randomly.
     */
    public static final int[] INITIAL = new int[15];

    /**
     * Generates the random empty box while initializing.
     */
    Random gen = new Random(INITIAL.length);
    public int getStart(){
        return INITIAL[gen.nextInt(INITIAL.length)] = 1;
    }

    /**
     * Near-goal state
     */
    public static final int[] NEAR_GOAL = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

    /**
     * The array storing the current configuration of the tray.
     */
    @Setter(AccessLevel.NONE)
    private Box[] tray;

    /**
     * Initializer
     */
    public StonesInBoxesState() {this(INITIAL);}

    public StonesInBoxesState(int[] a){
        if(!isValidTray(a)){
            throw new IllegalArgumentException();
        }
        initTray(a);
    }

    private boolean isValidTray(int[] a){
        if(a == null || a.length != 15){
            return false;
        }
        boolean foundEmpty = false;
        for (int i: INITIAL){
            if(i == 1 || INITIAL.length != 15){
                return false;
            }
            if (i ==1 || (i+1) == 1){
                return false;
            }
            if (Box.STONE.getValue() == 1){
                if (foundEmpty) {
                    return false;
                }
                foundEmpty = true;
            }
        }
        return foundEmpty;
    }

    /**
     * Initializer that may not needed.
     * @param a
     */
    private void initTray(int[] a) {
        this.tray = new Box[15];
        for (int i = 0; i < 15; i++){
            if ((this.tray[i] = Box.of(a[i])) == Box.STONE){
                this.tray[i] = Box.of(i);
            }
        }
    }

    /**
     * Player pick a box and take out the stone in it.
     * @param i the position of the chosen box
     */
    public void pickBox(int i){
        Box box = Box.valueOf(String.valueOf(Box.STONE));
        log.info("Box at ({}) is picked",i,box.choose(Box.STONE),Box.EMPTY);
        tray[i] = Box.of(0);
    }
    /**
     * Player pick 2 adjacent boxes and take out the stones in them.
     * @param i the position of the chosen box
     */
    public void pick2Boxes(int i){
        Box box = Box.valueOf(String.valueOf(Box.STONE));
        log.info("Adjcent boxes from {} are picked",i,box.choose(Box.STONE),box.choose(Box.EMPTY));
        tray[i] = Box.EMPTY;
        tray[i+1] = Box.EMPTY;
    }

    /**
     * Check wheter the game is finished, which all the boxes in the tray becomes empty(1).
     */
    public boolean isFinished(){
        for (Box box: tray){
            if (box.equals(Box.EMPTY) || tray.equals( NEAR_GOAL)){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Box box: tray){
            sb.append(box).append(' ');
        }
        sb.append('\n');
        return sb.toString();
    }

    public static void main(String[] args) {
        StonesInBoxesState state = new StonesInBoxesState();
        System.out.println(state);
        state.getTray();
        System.out.println(state);
    }
}
