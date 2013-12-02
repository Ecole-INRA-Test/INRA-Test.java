package robot;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class BatteryUnitTest {

    @Test
    public void testCharge() throws InterruptedException {
        Battery cell = new Battery();
        cell.setUp();
        Assert.assertEquals(100f, cell.getChargeLevel());
        Thread.sleep(1000);
        cell.charge();
        Assert.assertEquals(100f + cell.CHARGE_STEP, cell.getChargeLevel());
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
}
