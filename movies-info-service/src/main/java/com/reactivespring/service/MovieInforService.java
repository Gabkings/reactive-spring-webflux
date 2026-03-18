package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInforService {

    private final MovieInfoRepository movieInfoRepository;

    public MovieInforService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> addMovie(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMovies() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieById(String id) {
        return movieInfoRepository.findById(id);
    }


    public Mono<MovieInfo> updateMovie(String id, MovieInfo movieInfo) {
        movieInfo.setMovieInfoId(id);
        return movieInfoRepository.save(movieInfo);
    }


    public Mono<Void> deleteMovie(String id) {
        return movieInfoRepository.deleteById(id);
    }
}
