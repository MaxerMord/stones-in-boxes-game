package stonesinboxes.state;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


import java.util.Arrays;
import java.util.Random;

/**
 * State class and controll logic.
 */
@Data
@Slf4j
public class StonesInBoxesState implements Cloneable{

    /**
     * The array storing the current configuration of the tray.
     */
    @Setter(AccessLevel.NONE)
    private int[] tray;



    /**
     * Initial state, all boxes with a stone in it.
     */
    public static int[] INITIAL = new int[15];


    /**
     * Near-goal state
     */
    public static final int[] NEAR_GOAL = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

//    /**
//     * SIB = stonesinboxes
//     * @param t
//     */
//    public static SIB

    /**
     * Generates the random empty box while initializing.
     */

    private void setINITIAL(int[] t){
        int[] start = INITIAL;
        Random gen = new Random();
        int rand = gen.nextInt(INITIAL.length);
        start[rand] = 1;
        tray = start;
    }

    /**
     * Initializer
     */
    public StonesInBoxesState() {this(INITIAL);}

    public StonesInBoxesState(int[] initial) {
        setINITIAL(initial);
    }

    /**
     * Player picks box(es),check if available and then take the stone(s) out.
     * @param p position of the first box.
     */
    public void  pickBox(int p){
//        int[] temp = tray;
//        temp[p] = 1;
        if (tray[p-1] == 0){
            tray[p-1] = 1;
            log.info("Box {} is picked and remove the stone in it",p);
        }else {
            log.info("Box {} is empty, please pick an available box",p);
        }
    }

    /**
     * Choose 2 boxes start from position p(for loop may needed to optimize).
     * @param p the position of first(left side) box
     */
    public void  pick2Box(int p){
        if (tray[p-1] == 0 && tray[p] ==0){
            tray[p-1] = 1;
            tray[p] =1;
            log.info("Adjacent 2 Boxes {} {} are picked and remove the stones in them",
                    p,p+1);
        }else {
            log.info("Adjacent two Boxes {} {} from box {} are unavailable," +
                    " please",p,p+1,p);
        }

    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.insert(0,"[");

        for (int p : tray){
            stringBuilder.append(p).append(' ');
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public boolean isFinished(){
        if (Arrays.equals(tray, NEAR_GOAL)) {return true;}
        else {return false;}
    }

    public static void main(String[] args) {
        StonesInBoxesState state = new StonesInBoxesState();
        System.out.println("Initial tray...");
        System.out.println(state);
        state.pickBox(2);
        System.out.println(state);
        state.pick2Box(5);
        System.out.println(state);
        System.out.println(state.isFinished());

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
        state.pickBox(15);
        System.out.println(state);
        System.out.println(state.isFinished());


    }
}
