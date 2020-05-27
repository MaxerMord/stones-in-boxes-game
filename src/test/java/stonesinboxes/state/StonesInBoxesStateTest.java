package stonesinboxes.state;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StonesInBoxesStateTest {

//    @Test
//    void testSetInitial() {
//        StonesInBoxesState stonesInBoxesState = new StonesInBoxesState();
//        assertEquals(new StonesInBoxesState(new int[15])
//                ,stonesInBoxesState);
//    }

    @Test
    void testPickBox(){
        StonesInBoxesState stonesInBoxesState = new StonesInBoxesState();
        int[] test = new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,0};
        stonesInBoxesState.setTray(test);

        stonesInBoxesState.pickBox(5);
        assertArrayEquals(new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,0},stonesInBoxesState.getTray());
        stonesInBoxesState.pick2Box(4);
        assertArrayEquals(new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,0},stonesInBoxesState.getTray());
        stonesInBoxesState.pickBox(1);
        assertArrayEquals(new int[]{1,0,0,0,1,0,0,0,1,0,0,0,0,0,0},stonesInBoxesState.getTray());
        stonesInBoxesState.pick2Box(4);
        assertArrayEquals(new int[]{1,0,0,0,1,0,0,0,1,0,0,0,0,0,0},stonesInBoxesState.getTray());
        stonesInBoxesState.pick2Box(14);
        assertArrayEquals(new int[]{1,0,0,0,1,0,0,0,1,0,0,0,0,1,1},stonesInBoxesState.getTray());
    }

    @Test
    void testSetInitial(){
        StonesInBoxesState stonesInBoxesState = new StonesInBoxesState();
        assertEquals(15,stonesInBoxesState.getTray().length);
        assertEquals(int[].class,new StonesInBoxesState().getTray().getClass());

    }

    @Test
    void testOfInitialAndNearGoal() {
        int[] a = new int[15];
        assertArrayEquals(a, StonesInBoxesState.INITIAL);
        assertEquals(int[].class, StonesInBoxesState.NEAR_GOAL.getClass());
        int[] b = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        assertArrayEquals(b,StonesInBoxesState.NEAR_GOAL);

    }


    @Test
    void testIsFinished() {
        assertFalse(new StonesInBoxesState().isFinished());
        assertTrue(new StonesInBoxesState(new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}).isFinished());
        assertFalse(new StonesInBoxesState(new int[]{1,1,1,1,1,1,1,1,1,1,1,1}).isFinished());
        assertFalse(new StonesInBoxesState(new int[]{0,1,1,1,1,1,1,0,1,1,1,1,1,1,1}).isFinished());

    }

}