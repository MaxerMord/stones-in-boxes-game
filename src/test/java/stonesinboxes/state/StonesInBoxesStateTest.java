package stonesinboxes.state;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StonesInBoxesStateTest {

    @Test
    void testSetInitial(){
        StonesInBoxesState stonesInBoxesState = new StonesInBoxesState();
        assertEquals(15,stonesInBoxesState.getTray().length);
        assertEquals(int[].class,new StonesInBoxesState().getTray().getClass());

    }

    @Test
    void testInitialAndNearGoal() {
        int[] a = new int[15];
        assertArrayEquals(a, StonesInBoxesState.INITIAL);
        assertEquals(int[].class, StonesInBoxesState.NEAR_GOAL.getClass());
        int[] b = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        assertArrayEquals(b,StonesInBoxesState.NEAR_GOAL);

}

    @Test
    void testPickBox(){
        StonesInBoxesState stonesInBoxesState = new StonesInBoxesState();
        int[] test = new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,0};
        stonesInBoxesState.setTray(test);
        stonesInBoxesState.pickBox(4);
        assertArrayEquals(new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,0},stonesInBoxesState.getTray());
        stonesInBoxesState.pick2Box(3);
        assertArrayEquals(new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,0},stonesInBoxesState.getTray());
        stonesInBoxesState.pickBox(0);
        assertArrayEquals(new int[]{1,0,0,0,1,0,0,0,1,0,0,0,0,0,0},stonesInBoxesState.getTray());
        stonesInBoxesState.pick2Box(3);
        assertArrayEquals(new int[]{1,0,0,0,1,0,0,0,1,0,0,0,0,0,0},stonesInBoxesState.getTray());
        stonesInBoxesState.pick2Box(13);
        assertArrayEquals(new int[]{1,0,0,0,1,0,0,0,1,0,0,0,0,1,1},stonesInBoxesState.getTray());
        int[] test1 = new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,1};
        assertFalse(new StonesInBoxesState(new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,1}).canPick2(13));
        assertFalse(new StonesInBoxesState(new int[]{0,0,0,0,1,0,0,0,1,0,0,0,0,0,1}).canPick2(14));
    }

    @Test
    void testIsFinished() {
        assertFalse(new StonesInBoxesState().isFinished());
        assertTrue(new StonesInBoxesState(new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}).isFinished());
        assertFalse(new StonesInBoxesState(new int[]{1,1,1,1,1,1,1,1,1,1,1,1}).isFinished());
        assertFalse(new StonesInBoxesState(new int[]{0,1,1,1,1,1,1,0,1,1,1,1,1,1,1}).isFinished());

    }

}