package Sensor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class UDPConnectionController {

    private int portNumber = 8001;
    private InetAddress receiverAddress;

    public void setIPAddress(String address){
        try {
            receiverAddress = InetAddress.getByName(address);
        }
        catch(IOException netException){
            netException.printStackTrace();
        }
    }
    public void setPortNumber(String _portNumber){
        try {
            portNumber = Integer.parseInt(_portNumber);
        }
        catch(Exception netException){
                netException.printStackTrace();
            }
    }

    public boolean sendData(String data){
        byte[] bytesToSend = data.getBytes(Charset.forName("UTF-8"));

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
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
