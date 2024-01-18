package com.mycompany.multithreaded_udp;

import java.io.*;
import java.net.*;

/**
 * UDP_Server class that runs a continuous loop of receiving any incoming
 * packets from a Datagram Server Socket
 */
public class UDP_Server {

    public static void main(String[] args) {

        try {
            //create new datagram socket
            DatagramSocket serverSocket = new DatagramSocket(9876);

            //infinite loop of receiving and processing datagram packets
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                //creates a new PacketHandler thread to process current message
                new PacketHandler(serverSocket, receivePacket);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}

/**
PacketHandler class that creates a new thread to process the incoming client packet
and send back the message count to the client
 */
class PacketHandler implements Runnable {

    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;

    /*
    Constructor to set serverSocket and receivePacket for current instance
    and starts a new thread
     */
    public PacketHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
        new Thread(this).start();
    }

    //run method for the PacketHandler threads
    @Override
    public void run() {
        //get client address and port number so a message can be sent back
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        byte[] receiveData = new byte[1024];

        //process and print messsage output as well as sender info
        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Received message from " + clientAddress + " on port " + clientPort + ":  " + message);

        //send back server message count to client
        String returnMessage = "Server message count: " + MessageCounter.getCount();
        byte[] sendData = returnMessage.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
        try {
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
MessageCounter class to get synchronized message server message count
 */
class MessageCounter {

    private static int count = 0;

    //increments count and then returns it
    public static synchronized int getCount() {
        count++;
        return count;
    }
}
