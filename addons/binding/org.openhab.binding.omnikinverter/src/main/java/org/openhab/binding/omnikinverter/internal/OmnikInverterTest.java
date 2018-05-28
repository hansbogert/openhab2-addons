package org.openhab.binding.omnikinverter.internal;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;
import org.openhab.binding.omnikinverter.OmnikInverter;
import org.openhab.binding.omnikinverter.OmnikInverterMessage;

public class OmnikInverterTest {

    @Test
    public void testPullCurrentStats() {
        OmnikInverter inverter = new OmnikInverter("192.168.1.100", 8899, 604455290);
        OmnikInverterMessage message;
        try {
            message = inverter.pullCurrentStats();
            System.out.println(message.getTotalEnergy());
            System.out.println(message.getPower());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
