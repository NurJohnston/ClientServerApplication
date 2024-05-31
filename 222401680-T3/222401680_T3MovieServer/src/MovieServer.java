
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Movie.java
 *
 * @author Moegamamd Nur Johnston 09/05/2023
 */
public class MovieServer {

    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static Object receivedObject;
    private static ArrayList<Movie> movieList = new ArrayList<>();

    public MovieServer() {
        int port = 12345;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            establishStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void establishStreams() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processClient() {
        String searchString;
        try {
            while (true) {
                receivedObject = input.readObject();
                if (receivedObject instanceof Movie) {
                    Movie newMovie = (Movie) receivedObject;
                    movieList.add(newMovie);
                    System.out.println("added new Movie to the record: " + newMovie);
                } else if (receivedObject instanceof String && ((String) receivedObject).equals("retrieve")) {
                    output.writeObject(movieList);
                    output.flush();
                    System.out.println("arrayList sent to client.");
                } else if (receivedObject instanceof String && ((String) receivedObject).equals("exit")) {
                    closeConnection();
                } else if (receivedObject instanceof String) {
                    searchString = (String) receivedObject;
                    List<Movie> matchingMovies = searchMoviesByGenre(searchString);
                    output.writeObject(matchingMovies);
                    output.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Movie> searchMoviesByGenre(String genre) {
        List<Movie> matchingMovies = new ArrayList<>();
        for (Movie movie : movieList) {
            if (genre.equalsIgnoreCase(movie.getMovieGenre())) {
                matchingMovies.add(movie);
            }
        }
        return matchingMovies;
    }

    private static void closeConnection() {
        try {
            output.writeObject("Exit");
            output.flush();
            output.close();
            input.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("the server connection has been closed.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        MovieServer server = new MovieServer();
        server.processClient();
    }
}
