package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;



import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import jakarta.transaction.Transactional;

@Service
public class ArtistService {

	@Autowired
	private ArtistRepository artistRepository;
	
	@Transactional
	public Artist save(Artist a) {
		return this.artistRepository.save(a);
	}
	
	@Transactional
	public void delete(Artist a) {
		this.artistRepository.delete(a);
	}
	
	@Transactional
	public void update(Artist artist, Long id) {
		Artist a = this.artistRepository.findById(id).get();
		a.setName(artist.getName());
		a.setSurname(artist.getSurname());
		a.setDateOfBirth(artist.getDateOfBirth());
		this.artistRepository.save(a);
	}
	
	
	public Artist findById(Long id) {
		return this.artistRepository.findById(id).get();
	}
	
	public List<Artist> findAll(){
		
		List<Artist> artists = new ArrayList<Artist>();
		for (Artist a : this.artistRepository.findAll()) {
			artists.add(a);
		}
		return artists;
	}
	
	public boolean existsByNameAndSurnameAndDateOfBirth(String name, String surname, LocalDate dateOfBirth) {
		return this.artistRepository.existsByNameAndSurnameAndDateOfBirth(name, surname,dateOfBirth);
	}
	
	public List<Artist> findActorsNotInMovie(Long id){
		List<Artist> artists = new ArrayList<Artist>();
		for (Artist a : this.artistRepository.findActorsNotInMovie(id)) {
			artists.add(a);
		}
		return artists;
	}
}
