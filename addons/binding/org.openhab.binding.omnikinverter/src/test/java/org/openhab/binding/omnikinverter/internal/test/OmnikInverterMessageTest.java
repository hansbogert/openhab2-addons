package org.openhab.binding.omnikinverter.internal.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.omnikinverter.OmnikInverterMessage;

public class OmnikInverterMessageTest {

    OmnikInverterMessage message;

    @Before
    public void setUp() {
        byte[] b = new byte[1024];

        File resourcesDirectory = new File("src/test/resources/omnik.output");
        try {
            new FileInputStream(resourcesDirectory).read(b);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        message = new OmnikInverterMessage(b);
    }

    @Test
    public void testGetPower() {
        assertEquals(137.0f, message.getPower(), 0.01f);
    }

    @Test
    public void testGetTotalEnergy() {
        assertEquals(12412.7f, message.getTotalEnergy(), 0.01f);
    }

    @Test
    public void testGetEnergyToday() {
        assertEquals(11.13f, message.getEnergyToday(), 0.01f);
    }

}
