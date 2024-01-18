package com.mycompany.multithreaded_udp;

import java.io.*;
import java.net.*;

/**
UDP Client Class that connects to UDP Server 
 */
public class UDP_Client {

    public static void main(String[] args) {
        try {
            //start Buffered input stream reader for user input
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            //create client socket and set serverAddress and serverPort
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 9876;

            //continuous loop to get user input, send message to server
            //and recieve any incoming messages from server
            while (true) {

                //get console input for message content
                System.out.print("Enter a message (\"exit\" to quit): ");
                String message = inFromUser.readLine();

                //exit to break out of loop
                if (message.equals("exit")) {
                    break;
                }
                //convert message string to a byte array
                byte[] sendData = message.getBytes();

                //create DatagramPacket and send to server
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                clientSocket.send(sendPacket);

                //create new DatagramPacket to receive server messages
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                //convert received message to String and print to console
                String receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(receiveString);
            }

            //close clientSocket when exit
            clientSocket.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
