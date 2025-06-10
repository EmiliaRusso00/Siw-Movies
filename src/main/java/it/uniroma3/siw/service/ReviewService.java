package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	
	public static final String DIR_PAGES_REVIEW_OCCASIONAL = "occasional/review/";
	public static final String DIR_PAGES_REVIEW_GENERIC = "generic/review/";
	public static final String DIR_PAGES_REVIEW_ADMIN = "admin/review/";
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Transactional 
	public Review save(Review review) {
		return reviewRepository.save(review);
	}
	
	@Transactional
	public void delete(Review review) {
		reviewRepository.delete(review);
	}
	
	
	public Review findById(Long id) {
		return reviewRepository.findById(id).get();
		
	}
	
	public List<Review> findAll(){
		List<Review> reviews = new ArrayList<Review>();
		for (Review r: reviewRepository.findAll()) {
			reviews.add(r);
		}
		return reviews;
	}
	
	public boolean existsByUserAndMovie (User user, Movie movie) {
		return this.reviewRepository.existsByUserAndMovie(user, movie);
	}

	

}
