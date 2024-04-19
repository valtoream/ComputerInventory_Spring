import Classes.AuthenticationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {
    private static final String URL = "jdbc:mysql://localhost:3306/ComputerInventoryDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "007682Abv";

    // Database connection
    private static Connection connection;
    // Initialize the database connection
    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final int PORT = 8081;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                String tableInputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    out.println("Echo: " + inputLine);

                    // Insert data into MySQL
                    insertDataIntoMySQL(inputLine);
                }

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    // Display MySQL table contents
                    System.out.println("Input the table ");
    tableInputLine = in.readLine();
    displayTableContents(tableInputLine);
    */
    // Inside your Server class



    //HANDLE CLIENT LOGIN
    private static void handleClient(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println("Enter username:");
            String username = in.readLine();

            out.println("Enter password:");
            String password = in.readLine();

            boolean isAuthenticated = AuthenticationManager.authenticate(username, password);
            if (isAuthenticated) {
                out.println("Login successful");
                // Proceed with further communication with the authenticated client
            } else {
                out.println("Invalid username or password");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //INSERT INTO DATABASE
    private static void insertDataIntoMySQL(String data) {
        // SQL query to insert data
        String sql = "INSERT INTO categories (name) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameter values
            preparedStatement.setString(1, data);

            // Execute the query
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted into MySQL successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //DISPLAY FROM DATABASE
    private static void displayTableContents(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Table contents:");

            // Print column names
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                System.out.print(resultSet.getMetaData().getColumnName(i) + "\t");
            }
            System.out.println();

            // Print table data
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
