# Sistema Informativo Film - Progetto Spring Boot

## Descrizione

Questo progetto consiste nella progettazione e implementazione di un **sistema informativo** per la gestione di **film, attori, registi e recensioni**.  
Il sistema è sviluppato con **Spring Boot** e prevede l'accesso tramite tre tipologie di utenti: utenti occasionali, utenti registrati e amministratori.

## Funzionalità principali

### ✅ Utente generico (non autenticato)
- Consultazione di film, attori, registi e recensioni

### ✅ Utente registrato
- Consultazione dei dati
- Inserimento di **una recensione** per ciascun film

### ✅ Amministratore
- Inserimento, modifica e cancellazione di:
  - Film
  - Attori
  - Registi
- Cancellazione delle recensioni

## Specifiche tecniche

- Ogni **film** include:
  - Titolo
  - Anno di uscita
  - Una o più immagini (caricate localmente)
  - Attori (zero o più)
  - Un regista (uno solo)

- Ogni **attore/regista** include:
  - Nome e cognome
  - Data di nascita e (eventuale) data di morte
  - Fotografia

- Ogni **recensione** include:
  - Titolo
  - Testo
  - Valutazione (1-5)
  - Utente autore (registrato)

- **Immagini e fotografie** vengono caricate e salvate sul server, **non tramite link esterni**.

## Casi d'uso implementati

### 👤 Amministratore
1. Inserimento di un nuovo film
2. Modifica di un attore esistente

### 👥 Utente registrato
1. Inserimento di una recensione
2. Visualizzazione dei dettagli di un film

### 🌐 Utente generico
1. Consultazione dell'elenco dei film
2. Visualizzazione delle recensioni di un film

## Tecnologie utilizzate

- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- Bootstrap (per interfaccia responsive)
- Oauth2 (opzionale, se implementato)

## Requisiti

- Java 17+
- Maven
- Database relazionale (es. H2, PostgreSQL o MySQL)

## Avvio del progetto

1. Clona la repository:
   ```bash
   git clone https://github.com/tuo-utente/Siw-Movies.git
   cd sistema-film
