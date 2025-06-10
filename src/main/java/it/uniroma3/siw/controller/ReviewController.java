package it.uniroma3.siw.controller;


import static it.uniroma3.siw.model.Review.DIR_PAGES_REVIEW_ADMIN;
import static it.uniroma3.siw.model.Review.DIR_PAGES_REVIEW_OCCASIONAL;
import static it.uniroma3.siw.model.Review.DIR_PAGES_REVIEW_GENERIC;

import static it.uniroma3.siw.model.Movie.DIR_PAGES_MOVIE_OCCASIONAL;
import static it.uniroma3.siw.model.Movie.DIR_PAGES_MOVIE_ADMIN;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import it.uniroma3.siw.utility.FileStore;

import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;


@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MovieService movieService;
	
	@Autowired 
	private ReviewValidator reviewValidator;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@GetMapping("/reviews/{id}")
	public String getReviews(@PathVariable("id") Long idMovie, Model model) {
		//List<Review> reviews = this.reviewService.findAll();
		//model.addAttribute("movie", this.movieService.findById(idMovie));
		model.addAttribute("reviews", this.movieService.getReviewsOfMovie(idMovie));
		//model.addAttribute("reviews",reviews);
		return DIR_PAGES_REVIEW_OCCASIONAL +"foundReviews.html";
 	}
	@GetMapping("/reviews")
	public String getAllReviews(Model model) {
		List<Review> reviews = this.reviewService.findAll();
		model.addAttribute("reviews",reviews);
		return DIR_PAGES_REVIEW_OCCASIONAL +"reviews.html";
	}
	
	@GetMapping("/generic/aggiungiReview/{idMovie}")
	public String addGetReview(Model model, @PathVariable("idMovie") Long idMovie) {
		model.addAttribute("review", new Review());
		model.addAttribute("idMovie", idMovie);
		return DIR_PAGES_REVIEW_GENERIC + "formNewReview";
	}
	
	@PostMapping("/generic/aggiungiReview/{idMovie}")
	public String addPostReview(@Valid @ModelAttribute("review") Review review, 
			                    BindingResult bindingResult, 
			                    @PathVariable("idMovie") Long idMovie, 
			                    Model model) {
	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	review.setUser(credentials.getUser());
	Movie movie = this.movieService.findById(idMovie);
	review.setMovie(movie);
	this.reviewValidator.validate(review, bindingResult);
	if(!bindingResult.hasErrors()) {
		this.movieService.addReview(idMovie, review);
		return "redirect:/movie/" + idMovie;
	}
	model.addAttribute("idMovie",idMovie);
	return DIR_PAGES_REVIEW_GENERIC + "formNewReview";
	}
	
	// -- CANCELLAZIONE -- //
		@GetMapping("/admin/review/delete/{idMovie}/{idReview}")
		public String deleteReview(@PathVariable("idMovie") Long idMovie,  
								   @PathVariable("idReview") Long idReview,  
								   Model model) {
			Review review = this.reviewService.findById(idReview);
			Movie movie = this.movieService.findById(idMovie);
			
			
			movie.getReviews().remove(review);
			this.reviewService.delete(review);
			this.movieService.save(movie);
			return "redirect:/" + "admin/formUpdateMovie/"+idMovie;
		}
	
	
	

}
