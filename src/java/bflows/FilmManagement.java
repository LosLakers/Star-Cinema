package bflows;

import blogics.*;
import exceptions.NotFoundDBException;
import java.beans.*;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.time.*;
import java.time.format.*;
import java.util.List;
import javax.mail.MessagingException;

/**
 *
 * @author Guido Pio
 */
public class FilmManagement extends BaseBean implements Serializable {

    // proprietà per la gestione del film
    private int id_film;
    private String titolo;
    private String descrizione;
    private String trailer;
    private String durata;
    private String locandina;

    // proprietà per la gestione del commento di un utente
    private int id_commento;
    private int voto;
    private String giudizio;
    private String user;

    // lista dei commenti per un film
    private CommentModel[] commenti;

    // stringa di ricerca film per titolo o data
    private String searchString;
    private FilmModel[] filmList;

    public FilmManagement() {
    }

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Inserisco il film nel database
     */
    public void addFilm() {
        try {
            FilmModel film = new FilmModel(0, this.getTitolo(), LocalTime.parse(this.getDurata()),
                    this.getDescrizione(), this.getTrailer(), this.getLocandina());
            FilmManager.add(film);
            this.setId_film(film.getId_film());
        } catch (NotFoundDBException | SQLException ex) {
            // gestione eccezione
        }
    }

    /**
     * Aggiorno le informazioni del film nel database
     */
    public void updateFilm() {
        try {
            FilmModel film = new FilmModel(this.getId_film(), this.getTitolo(), LocalTime.parse(this.getDurata()),
                    this.getDescrizione(), this.getTrailer(), this.getLocandina());
            FilmManager.update(film);
        } catch (NotFoundDBException ex) {
            // gestione eccezione
        }
    }

    /**
     * L'admin elimina un film dal database
     */
    public void deleteFilm() {
        try {
            FilmManager.delete(this.getId_film());
        } catch (NotFoundDBException ex) {
            // gestione eccezione
        }
    }

    /**
     * Recupero il film dal database tramite id_film
     */
    public void getFilm() {
        try {
            FilmModel film = FilmManager.get(this.getId_film());
            this.setTitolo(film.getTitolo());
            this.setDescrizione(film.getDescrizione());
            this.setTrailer(film.getTrailer());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            this.setDurata(film.getDurata().format(formatter));

            this.setLocandina(film.getLocandina());
        } catch (NotFoundDBException | SQLException ex) {
            // gestione eccezione
        }
    }
    // </editor-fold>

    /**
     * Recupero la lista di tutti i film nel database
     */
    public void index() {
        try {
            List<FilmModel> index = FilmManager.searchFilm("");
            this.setFilmList(index.toArray(new FilmModel[index.size()]));
        } catch (NotFoundDBException | SQLException ex) {
            // messaggio di errore
        }
    }

    /**
     * Ricerca film per titolo o data
     */
    public void search() {
        try {
            try {
                LocalDate search = LocalDate.parse(this.getSearchString(), DateTimeFormatter.ISO_LOCAL_DATE);
                FilmModel[] index = FilmManager.searchFilm(search);
                this.setFilmList(index);
            } catch (DateTimeParseException ex) {
                String search = this.getSearchString();
                List<FilmModel> index = FilmManager.searchFilm(search);
                this.setFilmList(index.toArray(new FilmModel[index.size()]));
            }
        } catch (NotFoundDBException | SQLException ex) {
            // messaggio di errore
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" Metodi Custom Film List ">
    public int filmList_length() {
        return this.filmList.length;
    }

    public int filmList_idfilm(int index) {
        return this.filmList[index].getId_film();
    }

    public String filmList_titolo(int index) {
        return this.filmList[index].getTitolo();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Comment-Management ">
    public void addComment() {
        try {
            CommentModel commento = new CommentModel(0, this.getVoto(), this.getGiudizio(),
                    this.getUser(), this.getId_film());
            CommentManager.add(commento);
            this.setId_commento(commento.getId_commento());
            this.setMessage("Inserimento avvenuto con successo");
            this.setMessagetype("green");
        } catch (Exception ex) {
            this.setMessage("Riprovare l'inserimento. Se il problema persiste contattare l'amministratore");
            this.setMessagetype("red");
        }
    }

    /**
     * Aggiorno un commento inserito da un utente
     */
    public void updateComment() {
        try {
            CommentModel commento = new CommentModel(this.getId_commento(), this.getVoto(), this.getGiudizio(),
                    this.getUser(), this.getId_film());
            CommentManager.update(commento);
            this.setMessage("Aggiornamento avvenuto con successo");
            this.setMessagetype("green");
        } catch (Exception ex) {
            this.setMessage("Riprovare l'aggiornamento. Se il problema persiste contattare l'amministratore");
            this.setMessagetype("red");
        }
    }

    public void deleteComment() {
        // TODO
    }

    /**
     * Un admin elimina dal sistema il commento con id inserito come parametro.
     *
     * @param id_commento Id del commento da eliminare
     */
    public void deleteComment(int id_commento) {
        try {
            CommentModel comment = CommentManager.get(id_commento);
            UserModel user = UserManager.get(comment.getUsername());
            FilmModel film = FilmManager.get(comment.getId_film());
            CommentManager.delete(comment, user.getEmail(), film.getTitolo());
        } catch (NotFoundDBException | SQLException | MessagingException | IOException ex) {
            // gestione errore
        }
    }

    public void getComment() {
        try {
            CommentModel commento = CommentManager.get(this.getUser(), this.getId_film());
            this.setId_commento(commento.getId_commento());
            if (this.getId_commento() != 0) {
                this.setVoto(commento.getVoto());
                this.setGiudizio(commento.getGiudizio());
            }

            // creo la lista dei commenti associati al film
            this.setCommenti(CommentManager.getCommenti(this.getId_film()));
        } catch (Exception ex) {
            this.setMessage("Errore nel recupero dei commenti. Se il problema persiste contattare l'amministratore");
            this.setMessagetype("red");
        }
    }

    public int getComment_IdCommento(CommentModel commento) {
        return commento.getId_commento();
    }

    public int getComment_Voto(CommentModel commento) {
        return commento.getVoto();
    }

    public String getComment_Giudizio(CommentModel commento) {
        return commento.getGiudizio();
    }

    public String getComment_User(CommentModel commento) {
        return commento.getUsername();
    }

    public int getComment_Length() {
        CommentModel[] commenti = this.getCommenti();
        if (commenti == null) {
            return 0;
        }
        return commenti.length;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_film
     *
     * @return the value of id_film
     */
    public int getId_film() {
        return id_film;
    }

    /**
     * Set the value of id_film
     *
     * @param id_film new value of id_film
     */
    public void setId_film(int id_film) {
        this.id_film = id_film;
    }

    /**
     * Get the value of titolo
     *
     * @return the value of titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Set the value of titolo
     *
     * @param titolo new value of titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Get the value of descrizione
     *
     * @return the value of descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Set the value of descrizione
     *
     * @param descrizione new value of descrizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Get the value of trailer
     *
     * @return the value of trailer
     */
    public String getTrailer() {
        return trailer;
    }

    /**
     * Set the value of trailer
     *
     * @param trailer new value of trailer
     */
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    /**
     * Get the value of durata
     *
     * @return the value of durata
     */
    public String getDurata() {
        return durata;
    }

    /**
     * Set the value of durata
     *
     * @param durata new value of durata
     */
    public void setDurata(String durata) {
        this.durata = durata;
    }

    /**
     * Get the value of locandina
     *
     * @return the value of locandina
     */
    public String getLocandina() {
        return locandina;
    }

    /**
     * Set the value of locandina
     *
     * @param locandina new value of locandina
     */
    public void setLocandina(String locandina) {
        this.locandina = locandina;
    }

    /**
     * Get the value of id_commento
     *
     * @return the value of id_commento
     */
    public int getId_commento() {
        return id_commento;
    }

    /**
     * Set the value of id_commento
     *
     * @param id_commento new value of id_commento
     */
    public void setId_commento(int id_commento) {
        this.id_commento = id_commento;
    }

    /**
     * Get the value of voto
     *
     * @return the value of voto
     */
    public int getVoto() {
        return voto;
    }

    /**
     * Set the value of voto
     *
     * @param voto new value of voto
     */
    public void setVoto(int voto) {
        this.voto = voto;
    }

    /**
     * Get the value of giudizio
     *
     * @return the value of giudizio
     */
    public String getGiudizio() {
        return giudizio;
    }

    /**
     * Set the value of giudizio
     *
     * @param giudizio new value of giudizio
     */
    public void setGiudizio(String giudizio) {
        this.giudizio = giudizio;
    }

    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the value of user
     *
     * @param user new value of user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get the value of commenti
     *
     * @return the value of commenti
     */
    public CommentModel[] getCommenti() {
        return commenti;
    }

    /**
     * Set the value of commenti
     *
     * @param commenti new value of commenti
     */
    public void setCommenti(CommentModel[] commenti) {
        this.commenti = commenti;
    }

    /**
     * Get the value of commenti at specified index
     *
     * @param index the index of commenti
     * @return the value of commenti at specified index
     */
    public CommentModel getCommenti(int index) {
        return this.commenti[index];
    }

    /**
     * Set the value of commenti at specified index.
     *
     * @param index the index of commenti
     * @param commenti new value of commenti at specified index
     */
    public void setCommenti(int index, CommentModel commenti) {
        this.commenti[index] = commenti;
    }

    /**
     * Get the value of searchString
     *
     * @return the value of searchString
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * Set the value of searchString
     *
     * @param searchString new value of searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * Get the value of filmList
     *
     * @return the value of filmList
     */
    public FilmModel[] getFilmList() {
        return filmList;
    }

    /**
     * Set the value of filmList
     *
     * @param filmList new value of filmList
     */
    public void setFilmList(FilmModel[] filmList) {
        this.filmList = filmList;
    }

    /**
     * Get the value of filmList at specified index
     *
     * @param index the index of filmList
     * @return the value of filmList at specified index
     */
    public FilmModel getFilmList(int index) {
        return this.filmList[index];
    }

    /**
     * Set the value of filmList at specified index.
     *
     * @param index the index of filmList
     * @param filmList new value of filmList at specified index
     */
    public void setFilmList(int index, FilmModel filmList) {
        this.filmList[index] = filmList;
    }
    // </editor-fold>

}
