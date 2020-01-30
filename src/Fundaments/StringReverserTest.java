package Fundaments;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StringReverserTest {

    StringReverser SUT;
    @Before
    public void setUp() throws Exception {
        SUT=new StringReverser();
    }
    @Test
    public void reverse_emptyString_emptyStringReturned() throws Exception{
        String result = SUT.reverse("");
        assertThat(result, is(""));
    }
    @Test
    public void reverse_singleCharacter_sameStringReturned() throws Exception{
        String result = SUT.reverse("a");
        assertThat(result, is("a"));
    }
    @Test
    public void reverse_String_reversedStringReturned() throws Exception{
        String result = SUT.reverse("String milk");
        assertThat(result, is("klim gnirtS"));
    }
}