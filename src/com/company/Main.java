// David Crawford

package com.company;

import java.net.Socket;
import java.io.*;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) {
        Socket socket = null;
        String hostName = "localhost";
        String filePath = "/";
        int portNumber = 8000;

        try {
            // Step 1: Create a socket that connects to the above host and port number
            socket = new Socket(hostName, portNumber);
            socket.setSoTimeout(10000);
            System.out.println("Connected");

            // Step 2: Create a PrintWriter from the socket's output stream
            //         Use the autoFlush option
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Step 3: Create a BufferedReader from the socket's input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Step 4: Send an HTTP GET request via the PrintWriter.
            //         Remember to print the necessary blank line
            out.println("GET " + filePath + " HTTP/1.0\r\n");
            out.flush();

            // Step 6a: Create a FileOutputStream for storing the payload
            // Step 6b: Wrap the FileOutputStream in another PrintWriter
            String t;
            try (PrintWriter payload = new PrintWriter(new FileOutputStream("crawford-payload.txt", true))) {
            while((t = in.readLine()) != null) {
                // Step 5a: Read the status line of the response
                payload.println(t);
                // Step 5b: Read the HTTP response via the BufferedReader until
                //         you get a blank line
                if (t.isEmpty()) {
                    // Step 7: Read the rest of the input from BufferedReader and write
                    //         it to the second PrintWriter.
                    //         Hint: readLine() returns null when there is no more data
                    //         to read
                    try (PrintWriter response = new PrintWriter(new FileOutputStream("crawford-response.txt", true))) {
                        while((t = in.readLine()) != null) {
                            response.println(t);
                        }
                        response.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Step 8: Remember to close the writer
            payload.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            out.close();
            in.close();
        }
        catch (Exception e) {
            System.out.print(e);
        }
    }
}