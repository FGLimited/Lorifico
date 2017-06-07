package Server.Game.UserObjects;

import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by fiore on 07/06/2017.
 */
public class DomesticTest {

    private Domestic testDomestic;

    @Before
    public void setUp() throws Exception {
        testDomestic = new Domestic(FamilyColor.Green, DomesticColor.Orange, 5);

        testDomestic.setInPosition(true);
    }

    @Test
    public void getValue() throws Exception {
        Assert.assertEquals(5, testDomestic.getValue().intValue());
    }

    @Test
    public void getType() throws Exception {
        Assert.assertEquals(DomesticColor.Orange, testDomestic.getType());
    }

    @Test
    public void getFamilyColor() throws Exception {
        Assert.assertEquals(FamilyColor.Green, testDomestic.getFamilyColor());
    }

    @Test
    public void isInPosition() throws Exception {
        Assert.assertTrue(testDomestic.isInPosition());
    }

}