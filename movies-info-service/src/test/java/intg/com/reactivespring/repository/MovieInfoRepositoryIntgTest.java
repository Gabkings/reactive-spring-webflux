package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

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
    void findAll() {
        //given

        //when
        var moviesInfoFlux = movieInfoRepository.findAll().log();

        //then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        var movieInforFlux = movieInfoRepository.findById("abc").log();
        StepVerifier.create(movieInforFlux)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                }).verifyComplete();
    }

    @Test
    void setMovieInfoRepository(){

        var movieInfoFlux = new MovieInfo(null, "Batman Begins123",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        var movieinfos = movieInfoRepository.save(movieInfoFlux).log();

        StepVerifier.create(movieinfos)
                .assertNext(movieInfo -> {
                    assertNotNull(movieInfo.getMovieInfoId());
                    assertEquals("Batman Begins123", movieInfo.getName());
                }).verifyComplete();


    }

    @Test
    void updateMovieInfoRepository(){
        var movieInfoFlux = movieInfoRepository.findById("abc").log();
        var movieinfos = new MovieInfo( "abc","Dark Knight Rises3342",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        var movieinfos1 = movieInfoRepository.save(movieinfos).log();
        StepVerifier.create(movieinfos1).assertNext(movieInfo -> {
            assertNotNull(movieInfo.getMovieInfoId());
            assertEquals("Dark Knight Rises3342", movieInfo.getName());
        }).verifyComplete();
    }

    @Test
    void deleteMovieInfoRepository(){
        movieInfoRepository.deleteById("abc").block();
        var movieinfos = movieInfoRepository.findAll().log();

        StepVerifier.create(movieinfos)
                .expectNextCount(2).verifyComplete();
    }
}