import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
//import java.util.List;
import java.util.ArrayList;


/**
 * Movie.java
 *
 * @author Moegamamd Nur Johnston 09/05/2023
 */
public class MovieClient implements ActionListener {

    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static Socket server;
    private static JFrame frame = new JFrame();
    private static JPanel northPanel = new JPanel();
    private static JPanel southPanel = new JPanel();
    private static JPanel genrePanel = new JPanel();
    private static JPanel inputPanel = new JPanel();
    private static JPanel buttonPanel = new JPanel();
    private static JLabel genreLabel = new JLabel("Genre: ");
    private static JLabel titleLabel = new JLabel("Title: ");
    private static JLabel directorLabel = new JLabel("Director: ");
    private static JComboBox<String> genreComboBox = new JComboBox<>(new String[]{"Select", "Comedy", "Action", "Horror", "Romance", "Sci-Fi", "Thriller", "Drama"});
    private static JTextField titleField = new JTextField(20), directorField = new JTextField(20);
    private static JTextArea recordTextArea = new JTextArea(10, 50);
    private static JButton addButton = new JButton("Add Movie");
    private static JButton retrieveButton = new JButton("Retrieve Movies");
    private static JButton exitButton = new JButton("Exit");
    private static JButton findButton = new JButton("Find");
    private static ArrayList<Movie> movieList = new ArrayList<>();
    private static Object object;

    public MovieClient() {
        try {
            server = new Socket("localhost", 12345);
            output = new ObjectOutputStream(server.getOutputStream());
            output.flush();
            input = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame.setTitle("Movie Inventory Management");
        frame.setSize(600, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(southPanel, BorderLayout.SOUTH);

        northPanel.setLayout(new BorderLayout());
        northPanel.add(genrePanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        southPanel.add(recordTextArea);

        genrePanel.add(genreLabel);
        genrePanel.add(genreComboBox);

        inputPanel.add(titleLabel);
        inputPanel.add(titleField);
        inputPanel.add(directorLabel);
        inputPanel.add(directorField);

        buttonPanel.add(addButton);
        buttonPanel.add(retrieveButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(findButton);

        addButton.addActionListener(this);
        retrieveButton.addActionListener(this);
        exitButton.addActionListener(this);
        findButton.addActionListener(this);

        recordTextArea.append("movie Inventory: " + "\n");
        recordTextArea.append("\n");
    }

    private static void resetForm() {
        genreComboBox.setSelectedItem("Select");
        titleField.setText("");
        directorField.setText("");
        titleField.requestFocus();
    }

    private static void addMovie() {
        try {
            String genre = (String) genreComboBox.getSelectedItem();
            if (!genre.equals("Select")) {
                Movie movie = new Movie(titleField.getText(), directorField.getText(), genre);
                object = movie;
                output.writeObject(object);
                output.flush();
            } else {
                JOptionPane.showMessageDialog(null, "select a genre.");
            }
        } catch (IOException e) {
            System.out.println("exception error in addMovie() method: " + e.getMessage());
        }
        resetForm();
    }

    private static void retrieveMovies() {
        try {
            String retrieve = "retrieve";
            output.writeObject(retrieve);
            output.flush();
            movieList = (ArrayList<Movie>) input.readObject();
            displayMovies(movieList);
        } catch (IOException | ClassNotFoundException ioe) {
            System.out.println("exception error in retrieveMovies() method: " + ioe.getMessage());
        }
    }

    private static void displayMovies(ArrayList<Movie> movies) {
        recordTextArea.setText(null);
        for (Movie movie : movies) {
            String formattedMovieInfo = String.format(
                "Movie \n Title: %s. Director: %s. Genre: %s. ",
                movie.getMovieTitle(),
                movie.getMovieDirector(),
                movie.getMovieGenre()
            );
            recordTextArea.append(formattedMovieInfo + " .\n");
        }
        recordTextArea.append("\n");
    }

    private static void searchByGenre() {
        try {
            String selectedGenre = (String) genreComboBox.getSelectedItem();
            if (!selectedGenre.equals("Select")) {
                output.writeObject(selectedGenre);
                output.flush();
                movieList = (ArrayList<Movie>) input.readObject();
                displayMovies(movieList);
            } else {
                JOptionPane.showMessageDialog(null, "select a genre.");
            }
        } catch (IOException | ClassNotFoundException ioe) {
            System.out.println("exception error in searchByGenre() method: " + ioe.getMessage());
        }
    }

    private static void closeConnection() {
        try {
            String exit = "exit";
            output.writeObject(exit);
            output.flush();
            output.close();
            input.close();
            server.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addMovie();
        } else if (e.getSource() == retrieveButton) {
            retrieveMovies();
        } else if (e.getSource() == exitButton) {
            closeConnection();
        } else if (e.getSource() == findButton) {
            searchByGenre();
        }
    }

    public static void main(String[] args) {
        MovieClient client = new MovieClient();
    }
}

