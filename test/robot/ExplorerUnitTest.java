package robot;

import org.junit.Assert;
import org.junit.Test;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: fabrice
 * Date: 05/12/13
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class ExplorerUnitTest {

    @Test
    public void testLireCoordonnes() {
       Assert.assertEquals(new Coordinates(1, 1), Explorer.lireCoordonnee(new Scanner("(a, 2) \n (1,1)")));
        Assert.assertEquals(new Coordinates(0,0), Explorer.lireCoordonnee(new Scanner("0 , 0")));
    }
}
