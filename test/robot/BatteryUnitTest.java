package robot;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: fabrice
 * Date: 26/08/13
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class BatteryUnitTest {

    @Test
    public void testCharge() {
        Battery cell = new Battery();
        Assert.assertEquals(100f, cell.getChargeLevel());
        cell.charge();
        Assert.assertEquals(110f, cell.getChargeLevel());
    }

    @Ignore
    @Test
    public void testSetup() throws InterruptedException {
        Battery cell = new Battery();
        Assert.assertEquals(100f, cell.getChargeLevel());
        cell.setUp();
        Thread.sleep(1000);
        Assert.assertEquals(110f, cell.getChargeLevel());
        Thread.sleep(1000);
        Assert.assertEquals(120f, cell.getChargeLevel());
    }

    @Test (expected = InsufficientChargeException.class)
    public void testUseWithInsufficientCharge() throws Exception {
        Battery cell = new Battery();
        cell.use(125f);
    }

    @Test
    public void testUse() throws InsufficientChargeException {
        Battery cell = new Battery();
        cell.use(25f);
        Assert.assertEquals(75f, cell.getChargeLevel());
    }

    @Test
    public void testUseMax() throws InsufficientChargeException {
        Battery cell = new Battery();
        cell.use(cell.getChargeLevel());
        Assert.assertEquals(0f, cell.getChargeLevel());
    }

    @Test
    public void testTimeToSufficientCharge() {
        Battery cell = new Battery();
        Assert.assertEquals(10000,cell.timeToSufficientCharge(200));
    }
}
