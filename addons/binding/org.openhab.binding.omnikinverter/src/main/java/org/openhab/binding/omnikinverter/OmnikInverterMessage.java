package org.openhab.binding.omnikinverter;

import java.nio.ByteBuffer;

public class OmnikInverterMessage {

    byte[] bytes;

    public OmnikInverterMessage(byte[] b) {
        this.bytes = b;
    }

    public float getPower() {
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.put(bytes, 59, 2);
        buf.rewind();
        return buf.getShort() / 1.0f;
    }

    /**
     *
     * @return the total energy outputted this day in kWh
     */
    public float getEnergyToday() {
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.put(bytes, 69, 2);
        buf.rewind();
        return (buf.getShort() / 100.0f);
    }

    /**
     *
     * @return the total energy outputted in kWh
     */
    public float getTotalEnergy() {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.put(bytes, 71, 4);
        buf.rewind();
        return buf.getInt() / 10.0f;
    }
}
