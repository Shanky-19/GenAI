package com.genai.vector_embeddings.MovieRecommendation;


import com.genai.vector_embeddings.MovieRecommendation.model.Movie;
import com.genai.vector_embeddings.MovieRecommendation.model.MovieData;
import com.genai.vector_embeddings.MovieRecommendation.model.MovieMatch;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MovieService {

    private final EmbeddingModel embeddingModel;
    private final JsonMapper jsonMapper;

    private final List<Movie> moviesEmbeddings = new ArrayList<>();


    public MovieService(EmbeddingModel embeddingModel, JsonMapper jsonMapper) {
        this.embeddingModel = embeddingModel;
        this.jsonMapper = jsonMapper;
    }

    @PostConstruct
    public void initializeMovies() throws IOException{
        ClassPathResource resource = new ClassPathResource("movies.json");

        InputStream inputStream = resource.getInputStream();
        List<MovieData> movieDataList = jsonMapper.readValue(inputStream, new TypeReference<>() {});

        for(MovieData movieData: movieDataList) {
            float[] embedding = embeddingModel.embed(movieData.getDescription());

            Movie movie = new Movie(movieData.getTitle(), movieData.getDescription(), embedding);

            moviesEmbeddings.add(movie);
        }
        inputStream.close();

        System.out.println(moviesEmbeddings.size() + " movies loaded with embeddings");
    }

    private Movie findMovie(String title) {
        for (Movie movie : moviesEmbeddings) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }

        throw new IllegalArgumentException(
                "Movie not found: " + title
        );
    }

    public List<MovieMatch> similarMovies(String title) {
        Movie selectedMovie = findMovie(title);
        List<MovieMatch> matches = new ArrayList<>();

        for (Movie movie : moviesEmbeddings) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                continue;
            }

            double similarity = cosineSimilarity(
                    selectedMovie.getEmbeddings(),
                    movie.getEmbeddings());

            MovieMatch match = new MovieMatch(
                    movie.getTitle(),
                    movie.getDescription(),
                    similarity);

            matches.add(match);
        }
        sortBySimilarity(matches);
        return topKMatches(matches, 3);
    }

    private double cosineSimilarity(float[] a, float[] b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dotProduct += (a[i] * b[i]);
            normA += (a[i] * a[i]);
            normB += (b[i] * b[i]);
        }

        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private void sortBySimilarity(List<MovieMatch> matches) {
        Collections.sort(matches,
                (first, second) -> Double.compare(
                        second.getMatch(),
                        first.getMatch())
        );
    }

    private List<MovieMatch> topKMatches(
            List<MovieMatch> matches, int limit) {

        List<MovieMatch> topMatches = new ArrayList<>();

        int numberOfMatches = Math.min(limit, matches.size());

        for (int i = 0; i < numberOfMatches; i++) {
            topMatches.add(matches.get(i));
        }

        return topMatches;
    }


    public List<MovieMatch> search(String query) {
        float[] userQueryEmbedding = embeddingModel.embed(query);

        List<MovieMatch> matches = new ArrayList<>();

        for(Movie movie : moviesEmbeddings) {
            double cosineSimilarity = cosineSimilarity(userQueryEmbedding, movie.getEmbeddings());

            MovieMatch match = new MovieMatch(movie.getTitle(), movie.getDescription(), cosineSimilarity);
            matches.add(match);
        }

        sortBySimilarity(matches);

        return topKMatches(matches, 3);
    }
}
