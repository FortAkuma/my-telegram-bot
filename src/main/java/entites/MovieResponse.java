package entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {
    public int page;
    public int total_pages;
    public List<Movie> results;
}
