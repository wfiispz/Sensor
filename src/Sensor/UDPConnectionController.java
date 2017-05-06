package Sensor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;

/**
 * Created by jak12 on 05.05.2017.
 */
public class UDPConnectionController {

    final static int portNumber = 80;

    public UDPConnectionController(){
    }

    public boolean sendData(String data){
        byte[] bytesToSend = data.getBytes(Charset.forName("UTF-8"));

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress receiverAddress = InetAddress.getLocalHost();
            DatagramPacket packet = new DatagramPacket(bytesToSend, bytesToSend.length, receiverAddress, portNumber);
            datagramSocket.send(packet);
            return true;
        }
        catch(IOException netException){
            netException.printStackTrace();
        }
        return false;
    }
}
