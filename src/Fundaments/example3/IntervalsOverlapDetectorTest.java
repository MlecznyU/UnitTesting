package Fundaments.example3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class IntervalsOverlapDetectorTest {
IntervalsOverlapDetector SUT;
    @Before
    public void setUp() throws Exception {
    SUT= new IntervalsOverlapDetector();
    }
    @Test
    public void isOverlap_interval1BeforeInterval2_falseReturned(){
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(11,22);
        boolean result = SUT.isOverlap(interval1,interval2);
        assertThat(result, is(false));
    }
    @Test
    public void isOverlap_interval1IsContainedWithinInterval2_trueReturned(){
        Interval interval1 = new Interval(4,5);
        Interval interval2 = new Interval(3,22);
        boolean result = SUT.isOverlap(interval1,interval2);
        assertThat(result, is(true));
    }
    @Test
    public void isOverlap_interval1ContainsInterval2_trueReturned(){
        Interval interval1 = new Interval(2,5);
        Interval interval2 = new Interval(3,4);
        boolean result = SUT.isOverlap(interval1,interval2);
        assertThat(result, is(true));
    }
    @Test
    public void isOverlap_interval1OverlapsInterval2OnEnd_falseReturned(){
        Interval interval1 = new Interval(6,22);
        Interval interval2 = new Interval(3,5);
        boolean result = SUT.isOverlap(interval1,interval2);
        assertThat(result, is(false));
    }
    @Test
    public void isOverlap_interval1IsAfterInterval2_falseReturned(){
        Interval interval1 = new Interval(6,7);
        Interval interval2 = new Interval(8,22);
        boolean result = SUT.isOverlap(interval1,interval2);
        assertThat(result, is(false));
    }
    @Test
    public void isOverlap_interval1OverlapsInterval2OnStart_trueReturned(){
        Interval interval1 = new Interval(6,8);
        Interval interval2 = new Interval(7,12);
        boolean result = SUT.isOverlap(interval1,interval2);
        assertThat(result, is(true));
    }

}