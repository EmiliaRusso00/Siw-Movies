package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static it.uniroma3.siw.model.Artist.DIR_PAGES_ARTIST_ADMIN;
import static it.uniroma3.siw.model.Movie.DIR_PAGES_MOVIE_ADMIN;
import static it.uniroma3.siw.model.Movie.DIR_PAGES_MOVIE_OCCASIONAL;
import static it.uniroma3.siw.model.Movie.DIR_PAGES_MOVIE_GENERIC;
import static it.uniroma3.siw.model.Movie.DIR_FOLDER_IMG;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import it.uniroma3.siw.utility.FileStore;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;

@Controller
public class MovieController {
	@Autowired 
	private MovieService movieService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired 
	private ArtistRepository artistRepository;
	
	@Autowired 
	private ArtistService artistService;

	@Autowired 
	private MovieValidator movieValidator;

	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return DIR_PAGES_MOVIE_ADMIN + "formNewMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		model.addAttribute("reviews", this.movieService.getReviewsOfMovie(id));
		return DIR_PAGES_MOVIE_ADMIN + "formUpdateMovie.html";
	}

	@GetMapping(value="/admin/indexMovie")
	public String indexMovie(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return DIR_PAGES_MOVIE_ADMIN + "indexMovie.html";
	}
	
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return DIR_PAGES_MOVIE_ADMIN + "manageMovies.html";
	}
	
	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		
		Artist director = this.artistRepository.findById(directorId).get();
		Movie movie = this.movieService.findById(movieId);
		movie.setDirector(director);
		this.movieService.save(movie);
		
		model.addAttribute("movie", movie);
		return DIR_PAGES_MOVIE_ADMIN + "formUpdateMovie.html";
	}
	
	
	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistRepository.findAll());
		model.addAttribute("movie", this.movieService.findById(id));
		return DIR_PAGES_ARTIST_ADMIN + "director/directorsToAdd.html";
	}

	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, 
			               @RequestParam("file") MultipartFile file,Model model) {
		
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			movie.setImg(FileStore.store(file, DIR_FOLDER_IMG));
			this.movieService.save(movie); 
			model.addAttribute("movie", movie);
			return DIR_PAGES_MOVIE_OCCASIONAL + "movie.html";
		} else {
			return DIR_PAGES_MOVIE_ADMIN + "formNewMovie.html"; 
		}
	}

	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	model.addAttribute("movie", this.movieService.findById(id));
    	if (credentials.getRole().equals(Credentials.GENERIC_ROLE)) {
    		return DIR_PAGES_MOVIE_GENERIC + "movie.html";
    	}
		return DIR_PAGES_MOVIE_OCCASIONAL + "movie.html";
	}
	
	@GetMapping("/admin/movie/{id}")
	public String getAdminMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return DIR_PAGES_MOVIE_ADMIN + "movie.html";
	}


	
	@GetMapping("/movie")
	public String getMovies(Model model) {		
		model.addAttribute("movies", this.movieService.findAll());
		return DIR_PAGES_MOVIE_OCCASIONAL + "movies.html";
	}
	
	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return DIR_PAGES_MOVIE_OCCASIONAL + "formSearchMovies.html";
	}
	
	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies", this.movieService.findByYear(year));
		return DIR_PAGES_MOVIE_OCCASIONAL + "foundMovies.html";
	}

	/*@PostMapping("/searchMovies")
	public String searchMovies(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult,Model model) {
		
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			model.addAttribute("movies", this.movieService.findByYear(movie.getYear()));
			return DIR_PAGES_MOVIE_OCCASIONAL + "foundMovies.html";
		}
		else {
		return  DIR_PAGES_MOVIE_OCCASIONAL + "formSearchMovies.html";
	    }
	}
	*/
	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Artist> actorsToAdd = this.artistService.findActorsNotInMovie(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		model.addAttribute("movie", this.movieService.findById(id));

		return DIR_PAGES_ARTIST_ADMIN + "actor/actorsToAdd.html";
	}

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.findById(movieId);
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		this.movieService.save(movie);
		
		List<Artist> actorsToAdd = this.artistService.findActorsNotInMovie(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return DIR_PAGES_ARTIST_ADMIN + "actor/actorsToAdd.html";
	}
	
	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.findById(movieId);
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		this.movieService.save(movie);

		List<Artist> actorsToAdd = this.artistService.findActorsNotInMovie(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return DIR_PAGES_ARTIST_ADMIN + "actor/actorsToAdd.html";
	}
	
	// -- CANCELLAZIONE -- //
	@GetMapping("/admin/movie/delete/{idMovie}")
	public String deleteBuffet( 
							   @PathVariable("idMovie") Long idMovie,  
							   Model model) {
	    Movie movie = this.movieService.findById(idMovie);
		
		FileStore.removeImg(DIR_FOLDER_IMG, movie.getImg());
		
		this.movieService.delete(movie);
	
		return "redirect:/" + "admin/manageMovies";
	}
	@GetMapping("/admin/movie")
	public String getAdminMovies(Model model) {		
		model.addAttribute("movies", this.movieService.findAll());
		return DIR_PAGES_MOVIE_ADMIN+ "movies.html";
	}

	

	// -- MODIFICA -- //
		
	
	// -- MODIFICA -- //
			@GetMapping("/admin/movie/modificaMovie/{id}")
			public String selezionaMovie(@PathVariable("id") Long id, Model model) {
				model.addAttribute("movie", this.movieService.findById(id));
				return DIR_PAGES_MOVIE_ADMIN + "modificaMovie";
			}
			
			@PostMapping("/admin/movie/modificaMovie/{idMovie}")
			public String modificaMovie(@Valid @ModelAttribute("movie") Movie movie,
									   BindingResult bindingResult,
									   @PathVariable("idMovie") Long idMovie,
									   Model model) {
				this.movieValidator.validate(movie, bindingResult);
				movie.setId(idMovie);
				if(!bindingResult.hasErrors()) {
					this.movieService.update(movie, movie.getId());
					return this.manageMovies(model);
				}
				Movie m = this.movieService.findById(idMovie);
				movie.setImg(m.getImg());
				
				return  DIR_PAGES_MOVIE_ADMIN + "modificaMovie";
			}
			
		
		@PostMapping("/admin/movie/changeImg/{idMovie}")
		public String changeImgMovie(@PathVariable("idMovie") Long idMovie,
				   					  @RequestParam("file") MultipartFile file, Model model) {
			
			Movie m = this.movieService.findById(idMovie);
			if(!m.getImg().equals("profili")) {
				FileStore.removeImg(DIR_FOLDER_IMG, m.getImg());
			}

			m.setImg(FileStore.store(file, DIR_FOLDER_IMG));
			this.movieService.save(m);
			return this.selezionaMovie(idMovie, model);
		}
}
