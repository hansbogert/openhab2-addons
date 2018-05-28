package org.openhab.binding.omnikinverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.apache.commons.lang.ArrayUtils;

public class OmnikInverter {
    int serialNumber;
    String host;
    int port;

    public OmnikInverter(String host, int port, int serialNumber) {
        this.host = host;
        this.port = port;
        this.serialNumber = serialNumber;
    }

    public OmnikInverterMessage pullCurrentStats() throws UnknownHostException, IOException {
        byte[] magicPacket = generateMagicPacket();
        byte[] returnMessage = new byte[1024];

        Socket socket = new Socket(host, port);
        socket.getOutputStream().write(magicPacket);
        socket.getInputStream().read(returnMessage);
        socket.close();
        return new OmnikInverterMessage(returnMessage);

    }

    private byte[] generateMagicPacket() {
        Byte[] byteArr = new Byte[5];

        byteArr[0] = 123;
        byteArr[1] = 45;
        byteArr[2] = 56;
        byteArr[3] = 67;
        byteArr[4] = 89;

        byte[] magic = { 0x68, 0x02, 0x40, 0x30 };

        //
        ByteBuffer serialByteBuffer = ByteBuffer.allocate(8).putInt(serialNumber).putInt(serialNumber);
        byte[] serialBytes = serialByteBuffer.array();
        // Reverse serialBytes in a very mutable way.
        ArrayUtils.reverse(serialBytes);

        byte checksumCount = 115;
        for (byte b : serialBytes) {
            checksumCount += (char) b;
        }

        byte[] checksum = ByteBuffer.allocate(1).put(checksumCount).array();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(magic);
            outputStream.write(serialBytes);
            outputStream.write(0x01);
            outputStream.write(0x00);
            outputStream.write(checksum);
            outputStream.write(0x16);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
