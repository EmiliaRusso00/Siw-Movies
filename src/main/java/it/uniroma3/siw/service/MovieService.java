package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
	

	@Transactional 
	public Movie save(Movie movie) {
		return movieRepository.save(movie);	
	}

	@Transactional
	public void delete(Movie movie) {
		this.movieRepository.delete(movie);
	}

	public Movie findById(Long id) {
		return this.movieRepository.findById(id).get();
	}

	public List<Movie> findAll(){
		List<Movie> movies = new ArrayList<Movie>();
		for (Movie m: movieRepository.findAll()) {
			movies.add(m);
		}
		return movies;
	}
	
	public List<Movie> findByYear(Integer year) {
		List<Movie> movies = new ArrayList<Movie>();
		for (Movie m : movieRepository.findByYear(year.intValue())) {
			movies.add(m);
		}
		return movies;	
	}
	
	@Transactional
	public void addReview(Long idMovie, Review review) {
		Movie movie = this.movieRepository.findById(idMovie).get();
		review.setMovie(movie);
		movie.getReviews().add(review);
		this.movieRepository.save(movie);
		
	}
	
	@Transactional
	public void update(Movie movie, Long id) {
		Movie m = this.movieRepository.findById(id).get();
		m.setTitle(movie.getTitle());
		m.setYear(movie.getYear());
		this.movieRepository.save(m);
	}
	
	public boolean existsByTitleAndYear(String title, int year){
		return this.movieRepository.existsByTitleAndYear(title, year);
		
	}

	public List<Review> getReviewsOfMovie(Long idMovie) {
		Movie movie = this.movieRepository.findById(idMovie).get();
		return movie.getReviews();
	}

	
}
