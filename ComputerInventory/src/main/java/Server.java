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
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    out.println("Echo: " + inputLine);

                    // Insert data into MySQL
                    insertDataIntoMySQL(inputLine);
                    // Display MySQL table contents
                    displayTableContents();
                }

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertDataIntoMySQL(String data) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/ComputerInventoryDB";
        String username = "root";
        String password = "007682Abv";

        // SQL query to insert data
        String sql = "INSERT INTO categories (name) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
    private static void displayTableContents() {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/ComputerInventoryDB";
        String username = "root";
        String password = "007682Abv";

        // SQL query to select all data from table
        String sql = "SELECT * FROM categories";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
