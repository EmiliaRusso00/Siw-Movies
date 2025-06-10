package it.uniroma3.siw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class Review {
	
	public static final String DIR_PAGES_REVIEW_OCCASIONAL = "occasional/review/";
	public static final String DIR_PAGES_REVIEW_GENERIC = "generic/review/";
	public static final String DIR_PAGES_REVIEW_ADMIN = "admin/review/";
	

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	

	private String title;
	
	
	
	@Max(5)
	@Min(1)
	private int valutazione;
	
	@Size(min = 3, max = 200)
	private String text;
	
	@OneToOne
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@ManyToOne 
	private Movie movie;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getValutazione() {
		return valutazione;
	}

	public void setValutazione(int valutazione) {
		this.valutazione = valutazione;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	

	
	
	
	
	

}
