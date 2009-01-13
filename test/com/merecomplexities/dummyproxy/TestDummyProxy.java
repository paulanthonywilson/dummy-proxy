package com.merecomplexities.dummyproxy;

import junit.framework.TestCase;

/**
 */
public class TestDummyProxy extends TestCase {

    private PartialSquirrelImplementation partialSquirrelImplementation;


    protected void setUp() throws Exception {
        partialSquirrelImplementation = new PartialSquirrelImplementation();
    }

    
    public void testShouldImplementTheInterface() throws Exception {
        Squirrel squirrel = DummyProxy.dummy(Squirrel.class, this);
        assertNotNull(squirrel);
    }


    public void testShouldCallTheInterfaceMethodOnThePartialImplementation() throws Exception {
        testeeProxy().climbTree();
        assertEquals("Tree climbed", true, partialSquirrelImplementation.treeClimbed);
    }


    public void testShouldDistinguishBetweenOverloadedMethods() throws Exception {
        testeeProxy().hideNuts();
        assertEquals(1, partialSquirrelImplementation.nutsHidden);
        testeeProxy().hideNuts(2);
        assertEquals(3, partialSquirrelImplementation.nutsHidden);
    }


    public void testShouldReturnTheValue() throws Exception {
        partialSquirrelImplementation.nutsHidden = 5;
        assertEquals(5, testeeProxy().getHiddentNutsCount());
    }


    public void testShouldPropogateTheThrownRuntimeException() throws Exception {
        try {
            testeeProxy().jumpToTheBlueTree();
            fail("Expecting UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e) {

        }
    }



    public void testDefaultShouldThrowUnsupportedOperationExceptionIfMethodNotImplemented() throws Exception {
        try {
            testeeProxy().jumpToTheRedTree();
            fail("Expecting UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e) {

        }
    }



    public void testShouldReturnNullIfMethodNotImplementedAndReturningNullOptionIsUsed() throws Exception {
        Squirrel testee = DummyProxy.dummyReturningNullOnUnrecognised(Squirrel.class, this);
        assertNull(testee.jumpToTheRedTree());
    }


    public void testShouldPropogateTheThrownCheckedException() throws Exception {
        try {
            testeeProxy().doSquirrelDance();
            fail("Expecting TooTiredtoDanceException");
        }
        catch (TooTiredToDanceException e) {

        }
    }



    private Squirrel testeeProxy() {
        return DummyProxy.dummy(Squirrel.class, partialSquirrelImplementation);
    }


    public static interface Squirrel {
        void climbTree();

        void hideNuts();

        void hideNuts(int count);

        int getHiddentNutsCount();

        void jumpToTheBlueTree();

        void doSquirrelDance() throws TooTiredToDanceException;

        Object jumpToTheRedTree();

    }

    public static class TooTiredToDanceException extends Exception {
    }

    public static class PartialSquirrelImplementation {
        private boolean treeClimbed;
        private int nutsHidden;

        public void jumpToTheBlueTree() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("jumpToTheBlueTree");
        }

        public void doSquirrelDance() throws TooTiredToDanceException {
            throw new TooTiredToDanceException();
        }

        public void climbTree() {
            treeClimbed = true;
        }

        public void hideNuts(int count) {
            nutsHidden += count;
        }

        public void hideNuts() {
            nutsHidden++;
        }

        public int getHiddentNutsCount() {
            return nutsHidden;
        }
    }


}
