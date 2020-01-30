package Fundaments.exercise2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StringDuplicatorTest {
StringDuplicator SUT;
    @Before
    public void setUp() throws Exception {
    SUT=new StringDuplicator();
    }
    @Test
    public void duplicate_emptyString_theSameString(){
        String result = SUT.duplicate("");
        assertThat(result, is(""));
    }
    @Test
    public void duplicate_singleCharacter_twoCharacters(){
        String result = SUT.duplicate("a");
        assertThat(result, is("aa"));
    }
    @Test
    public void duplicate_string_theSameStringTwoTimes(){
        String result = SUT.duplicate("mleko ");
        assertThat(result, is("mleko mleko "));
    }
}