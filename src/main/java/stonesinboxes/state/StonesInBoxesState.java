package stonesinboxes.state;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


import java.util.Arrays;
import java.util.Random;

/**
 * State and logic class .
 */
@Data
@Slf4j
public class StonesInBoxesState implements Cloneable{

    /**
     * The array storing the current configuration of the tray.
     */
//    @Setter(AccessLevel.NONE)
    private int[] tray;

    /**
     * 0 - box with stone, 1 - empty box.
     */

    /**
     * Initial state, all boxes with a stone in it.
     */
    public static final int[] INITIAL = new int[15];


    /**
     * Near-goal state
     */
    public static final int[] NEAR_GOAL = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};


    /**
     * Generates the random empty box while initializing.
     */

    private void setINITIAL(int[] t){
//        int[] start = t;
        int[] start = t.clone();
        Random gen = new Random();
        int rand = gen.nextInt(start.length);
        start[rand] = 1;
        tray = start;
    }

    /**
     * Constructor
     */
    public StonesInBoxesState() {this(INITIAL);}

    /**
     * Initializer
     * @param initial initial array
     */
    public StonesInBoxesState(int[] initial) {
        setINITIAL(initial);
    }

    /**
     * Player picks a box,check if available and then take the stone out.
     * @param p position of the box.
     */
    public boolean canPick1(int p){
        if (tray[p] == 0 && (p >= 0 && p <= INITIAL.length)) {
            log.info("Box {} can be picked",p);
            return true;
        }else
            log.info("Box {} is unavailable.", p);
            return false;
    }

    public void  pickBox(int p){
            if (tray[p] == 0 && (p >= 0 && p <= INITIAL.length)){
                tray[p] = 1;
                log.info("Box {} is picked and remove the stone in it",p);
            }else {
                log.info("Box {} is empty, please pick an available box.", p);
                //throw new IllegalArgumentException();
            }
    }


    /**
     * Choose 2 available adjacent boxes start from position p(for loop may needed to optimize).
     * @param p the position of first(left side) box
     */
    public boolean canPick2(int p){
        if (tray[p] == 0 && tray[p+1] == 0 && (p >= 0 && p < (INITIAL.length-1))){
            log.info("Adjacent two boxes from {} can be picked",p);
            return true;
        }else
            log.info("Adjacent two boxes from {} are unavailable.", p);
            return false;

    }

    public void  pick2Box(int p){
            if (tray[p] == 0 && tray[p+1] == 0 && (p >= 0 && p < (INITIAL.length-1))) {
                tray[p] = 1;
                tray[p+1] = 1;
                log.info("Adjacent 2 Boxes {} {} from box {} are picked and remove the stones in them",
                        p, p + 1, p);
            } else {
                log.info("Adjacent two Boxes {} {} from box {} are unavailable," +
                        " please pick again", p, p + 1, p);
                //throw new IllegalArgumentException();
            }
    }

    /**
     * Override the toString method and returns the current tray.
     * @return current tray (state array)
     */
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.insert(0,"[");

        for (int p : tray){
            stringBuilder.append(p).append(' ');
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * Check whether the game is finished, the array should be full of ones with length 15.
     * @return array equals NEAR_GOAL
     */
    public boolean isFinished(){
        if (Arrays.equals(tray, NEAR_GOAL)) {return true;}
        else {return false;}
    }

    /**
     * Testing all the methods in the class.
     * @param args Argument, p = position of picked box
     */
    public static void main(String[] args) {
        StonesInBoxesState state = new StonesInBoxesState();

        System.out.println(state.toString());
        System.out.println("Initial tray...");
        System.out.println(state);
        state.pickBox(2);
        System.out.println(state);
        state.pick2Box(4);
        state.pick2Box(5);
        state.pick2Box(6);

        System.out.println(state);
        state.pick2Box(5);
        System.out.println(state);
        System.out.println(state.isFinished());
        System.out.println(state.getTray());
        System.out.println(state.getTray().getClass());

        //testing isFinished method
        state.pickBox(1);
        state.pickBox(2);
        state.pickBox(3);
        state.pickBox(4);
        state.pickBox(5);
        state.pickBox(6);
        state.pickBox(7);
        state.pickBox(8);
        state.pickBox(9);
        state.pickBox(10);
        state.pickBox(11);
        state.pickBox(12);
        state.pickBox(13);
        state.pickBox(14);
        System.out.println(state);
        state.pickBox(15);
        System.out.println(state);
        System.out.println(state.isFinished());
        state.pick2Box(65);


    }
}
