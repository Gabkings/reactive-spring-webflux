package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import com.reactivespring.service.MovieInforService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/")
public class MovieInforController {

    private final MovieInforService movieInforService;
    public MovieInforController(MovieInforService movieInforService) {
        this.movieInforService = movieInforService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovie( @Valid MovieInfo movieInfo) {
       return movieInforService.addMovie(movieInfo);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieInfo> getAllMovies() {
        return movieInforService.getAllMovies();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> getMovieById(@PathVariable String id) {
        return movieInforService.getMovieById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> updateMovie(@PathVariable String id, @Valid MovieInfo movieInfo) {
        return movieInforService.updateMovie(id, movieInfo);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieById(@PathVariable String id) {
        return movieInforService.deleteMovie(id);
    }




}
