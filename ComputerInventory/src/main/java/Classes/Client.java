package Classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static Classes.AuthenticationManager.authenticate;

public class Client {

    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int PORT = 8081;

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            // Prompt for username
           // System.out.println(in.readLine());
           // out.println("Input username");   //to fix later
            System.out.println("Username:");
            String username = stdIn.readLine();
            out.println(username);

            // Prompt for password
          //  System.out.println(in.readLine());  //to fix later
           // out.println("Input password");
            System.out.println("password:");
            String password = stdIn.readLine();
            out.println(password);

            boolean isAuthenticated = authenticate(username, password); // Call the authenticate method
            if (isAuthenticated) {
                System.out.println("Login successful.");
            } else {
                System.out.println("Login failed. Invalid username or password.");
                return; // Exit the program if authentication fails
            }





            // Display server response
            System.out.println(in.readLine());

            System.out.println("Connected to server. Type your message:");

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("Server says: " + in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
