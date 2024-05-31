
import java.io.Serializable;

/**
 * Movie.java
 *
 * @author Moegamamd Nur Johnston 09/05/2023
 */
public class Movie implements Serializable {

    private String movieTitle;
    private String movieDirector;
    private String movieGenre;
//Movie
//- movieTitle: String
//- movieDirector: String
//- movieGenre: String
//+ getters
//+ setters
//+ toString: String

    public Movie(String movieTitle, String movieDirector, String movieGenre) {
        this.movieTitle = movieTitle;
        this.movieDirector = movieDirector;
        this.movieGenre = movieGenre;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    @Override
    public String toString() {
        return "Movies: \n" + movieTitle + "  " + movieDirector + "  " + movieGenre;
    }

}
