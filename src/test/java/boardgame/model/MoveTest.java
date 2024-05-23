package boardgame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    void testToString() {
        assertEquals("(0,0)", new Move(0,0).toString());
        assertEquals("(1,3)", new Move(1,3).toString());
    }

    @Test
    void row() {
        assertEquals(0, new Move(0,0).row());
        assertEquals(1, new Move(1,3).row());
    }

    @Test
    void col() {
        assertEquals(0, new Move(0,0).col());
        assertEquals(3, new Move(1,3).col());
    }
}