package it.uniroma3.siw.controller;

import static it.uniroma3.siw.model.Artist.DIR_PAGES_ARTIST_OCCASIONAL;

import jakarta.validation.Valid;


import static it.uniroma3.siw.model.Artist.DIR_PAGES_ARTIST_GENERIC;

import static it.uniroma3.siw.model.Artist.DIR_FOLDER_IMG;
import static it.uniroma3.siw.model.Artist.DIR_PAGES_ARTIST_ADMIN;


import org.springframework.beans.factory.annotation.Autowired;
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

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;


@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistService artistService;
	
	@Autowired 
	private MovieService movieService;
	
	@Autowired
	private ArtistValidator artistValidator;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return  DIR_PAGES_ARTIST_ADMIN +"formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return DIR_PAGES_ARTIST_ADMIN+"indexArtist.html";
	}
	
	@PostMapping("/admin/artist")
	public String newArtist(@Valid @ModelAttribute("artist")Artist artist, BindingResult bindingResult, 
			                @RequestParam("file") MultipartFile file, Model model) {
		
		this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			artist.setImg(FileStore.store(file, DIR_FOLDER_IMG));
			this.artistService.save(artist);
			model.addAttribute("artist", artist);
			return DIR_PAGES_ARTIST_OCCASIONAL + "artist.html";
		} else {
			return DIR_PAGES_ARTIST_ADMIN+"formNewArtist.html"; 
		}
	}
	

	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.findById(id));
		return DIR_PAGES_ARTIST_OCCASIONAL +"artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return DIR_PAGES_ARTIST_OCCASIONAL +"artists.html";
	}
	
	@GetMapping("/admin/artist")
	public String getArtistsAdmin(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return DIR_PAGES_ARTIST_ADMIN+"artists.html";
	}
	
	// -- CANCELLAZIONE -- //
		@GetMapping("/admin/artist/delete/{id}")
		public String deleteArtist(@PathVariable("id") Long id,  Model model) {
			Artist artist = this.artistService.findById(id);
			FileStore.removeImg(DIR_FOLDER_IMG, artist.getImg());
			
			this.artistService.delete(artist);		
			return  "admin/artist";
		}
		
		// -- MODIFICA -- //
		@GetMapping("/admin/artist/modificaArtist/{id}")
		public String selezionaArtist(@PathVariable("id") Long id, Model model) {
			model.addAttribute("artist", this.artistService.findById(id));
			return DIR_PAGES_ARTIST_ADMIN + "modificaArtist";
		}
		
		@PostMapping("/admin/artist/modificaArtist/{idArtist}")
		public String modificaArtist(@Valid @ModelAttribute("artist") Artist artist,
								   BindingResult bindingResult,
								   @PathVariable("idArtist") Long idArtist,
								   Model model) {
			this.artistValidator.validate(artist, bindingResult);
			artist.setId(idArtist);
			if(!bindingResult.hasErrors()) {
				this.artistService.update(artist, artist.getId());
				return this.getArtistsAdmin(model);
			}
			Artist a = this.artistService.findById(idArtist);
			artist.setImg(a.getImg());
			
			return  DIR_PAGES_ARTIST_ADMIN + "modificaArtist";
		}
		
		@PostMapping("/admin/artist/changeImg/{idArtist}")
		public String changeImgArtist(@PathVariable("idArtist") Long idArtist,
				   				@RequestParam("file") MultipartFile file, Model model) {
			
			Artist a = this.artistService.findById(idArtist);
			if(!a.getImg().equals("profili")) {
				FileStore.removeImg(DIR_FOLDER_IMG, a.getImg());
			}

			a.setImg(FileStore.store(file, DIR_FOLDER_IMG));
			this.artistService.save(a);
			return this.selezionaArtist(idArtist, model);
		}
		
		
}