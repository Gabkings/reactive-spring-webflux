package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInforControllerTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    public static String MOVIE_INFO_URL = "/api/";

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }


    @Test
    void addMovie() {
        var movieInfor = new MovieInfo(null, "Batman Begins12342",
                2005, List.of("Christian Bale3e4", "Michael Cane33"), LocalDate.parse("2005-06-15"));

        webTestClient.post()
                .uri(MOVIE_INFO_URL)
                .bodyValue(movieInfor)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .returnResult();
    }

    @Test
    void getAllMovies() {

        webTestClient.get()
                .uri(MOVIE_INFO_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(3)
                .returnResult();

    }

    @Test
    void getMovieById() {

        webTestClient.get().uri(MOVIE_INFO_URL+"{id}", "abc")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfo.class)
                .returnResult();

    }

    @Test
    void updateMovie() {

        var movieInfor = movieInfoRepository.findById("abc");

        var movieInfo = new MovieInfo(null, "Batman 23435",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        webTestClient.put()
                .uri(MOVIE_INFO_URL+"{id}", "abc")
                .bodyValue(movieInfo)
                .exchange()
                .expectBody(MovieInfo.class)
                .returnResult();

    }

    @Test
    void deleteMovieById() {
        webTestClient.delete()
                .uri(MOVIE_INFO_URL+"{id}", "abc")
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody()
                .returnResult();
    }
}