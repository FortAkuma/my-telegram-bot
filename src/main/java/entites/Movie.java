package entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    public String title;
    public String overview;
    @JsonProperty("poster_path")
    public String posterPath;
    @JsonProperty("vote_average")
    public double votes;
    @JsonProperty("release_date")
    public String releaseDate;
    public int[] genre_ids;

    public String getFullPosterPath() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public String getReleaseYear() {
        if (releaseDate != null && releaseDate.length() >= 4) {
            return releaseDate.substring(0,4);
        }
        return "Not found";
    }
}
