package org.openhab.binding.omnikinverter.internal.test;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;
import org.openhab.binding.omnikinverter.OmnikInverter;

public class OmnstikInverterTest {

    @Test
    public void testPullCurrentStats() {
        OmnikInverter inverter = new OmnikInverter("192.168.1.100", 8899, 604455290);
        try {
            System.out.println(inverter.pullCurrentStats().getPower());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
