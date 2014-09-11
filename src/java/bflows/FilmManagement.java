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
 * JavaBean per la gestione di un Film e dei suoi Commenti
 */
public class FilmManagement extends BaseBean implements Serializable {

    // proprietà per la gestione del film
    private int id_film;
    private String titolo;
    private String descrizione;
    private String trailer;
    private String durata;
    private String locandina;

    /* 
     Utilizzato per eliminazione multipla, corrisponde ai vari id_film ma si è preferito
     usare un altra proprietà per evitare usi strani di id_film
     */
    private int[] deleteFilm;

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
     *
     * @throws exceptions.NotFoundDBException Eccezione
     * @throws java.sql.SQLException Eccezione
     */
    public void addFilm() throws NotFoundDBException, SQLException {
        try {
            FilmModel film = new FilmModel(0, this.getTitolo(), LocalTime.parse(this.getDurata()),
                    this.getDescrizione(), this.getTrailer(), this.getLocandina());
            FilmManager.add(film);
            this.setId_film(film.getId_film());
        } catch (NotFoundDBException | SQLException ex) {
            this.setAlert(Message.INSERTERROR);
            throw ex;
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
            this.setAlert(Message.UPDATESUCCESS);
        } catch (NotFoundDBException ex) {
            this.setAlert(Message.UPDATEERROR);
        }
    }

    /**
     * L'admin elimina un film dal database
     *
     * @throws exceptions.NotFoundDBException Eccezione
     */
    public void deleteFilm() throws NotFoundDBException {
        try {
            FilmManager.delete(this.getId_film());
        } catch (NotFoundDBException ex) {
            this.setAlert(Message.DELETEERROR);
            throw ex;
        }
    }

    /**
     * L'admin elimina uno o più film dal database
     *
     * @throws NotFoundDBException Eccezione
     */
    public void deleteMultiFilm() throws NotFoundDBException {
        try {
            FilmManager.delete(this.getDeleteFilm());
        } catch (NotFoundDBException ex) {
            this.setAlert(Message.DELETEERROR);
            throw ex;
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
            // reindirizzamento a una pagina di errore nella jsp
        }
    }

    /**
     * Ricerca film per titolo o data
     */
    public void search() {
        try {
            try {
                LocalDate search = LocalDate.parse(this.getSearchString(), DateTimeFormatter.ISO_LOCAL_DATE);
                List<FilmModel> index = FilmManager.searchFilm(search);
                this.setFilmList(index.toArray(new FilmModel[index.size()]));
            } catch (DateTimeParseException ex) {
                String search = this.getSearchString();
                List<FilmModel> index = FilmManager.searchFilm(search);
                this.setFilmList(index.toArray(new FilmModel[index.size()]));
            }
        } catch (NotFoundDBException | SQLException ex) {
            this.setAlert(Message.SEARCHERROR);
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" Metodi Custom Film List ">
    /**
     * Recupero la lunghezza dell'array FilmList
     *
     * @return La lunghezza dell'array
     */
    public int filmList_length() {
        return this.filmList.length;
    }

    /**
     * Recupero l'id di un elemento dell'array FilmList
     *
     * @param index Indice dell'elemento nell'array
     * @return Id del film
     */
    public int filmList_idfilm(int index) {
        return this.filmList[index].getId_film();
    }

    /**
     * Recupero il titolo di un elemento dell'array FilmList
     *
     * @param index Indice dell'elemento nell'array
     * @return Il titolo del film
     */
    public String filmList_titolo(int index) {
        return this.filmList[index].getTitolo();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Comment-Management ">
    /**
     * Inserisco un commento
     *
     * @throws exceptions.NotFoundDBException Eccezione
     * @throws java.sql.SQLException Eccezione
     */
    public void addComment() throws NotFoundDBException, SQLException {
        try {
            CommentModel commento = new CommentModel(0, this.getVoto(), this.getGiudizio(),
                    this.getUser(), this.getId_film());
            CommentManager.add(commento);
            this.setId_commento(commento.getId_commento());
        } catch (NotFoundDBException | SQLException ex) {
            this.setAlert(Message.COMMENTERROR);
            throw ex;
        }
    }

    /**
     * Aggiorno un commento inserito da un utente
     *
     * @throws exceptions.NotFoundDBException Eccezione
     */
    public void updateComment() throws NotFoundDBException {
        try {
            CommentModel commento = new CommentModel(this.getId_commento(), this.getVoto(), this.getGiudizio(),
                    this.getUser(), this.getId_film());
            CommentManager.update(commento);
        } catch (NotFoundDBException ex) {
            this.setAlert(Message.COMMENTERROR);
            throw ex;
        }
    }

    public void deleteComment() {
        // TODO
    }

    /**
     * Un admin elimina dal sistema il commento con id inserito come parametro.
     *
     * @param id_commento Id del commento da eliminare
     * @throws exceptions.NotFoundDBException Eccezione
     * @throws java.sql.SQLException Eccezione
     * @throws javax.mail.MessagingException Eccezione
     * @throws java.io.IOException Eccezione
     */
    public void deleteComment(int id_commento)
            throws NotFoundDBException, SQLException, MessagingException, IOException {
        try {
            CommentModel comment = CommentManager.get(id_commento);
            UserModel user = UserManager.get(comment.getUsername());
            FilmModel film = FilmManager.get(comment.getId_film());
            CommentManager.delete(comment, user.getEmail(), film.getTitolo());
        } catch (NotFoundDBException | SQLException | MessagingException | IOException ex) {
            this.setAlert(Message.DELETEERROR);
            throw ex;
        }
    }

    /**
     * Recupero il commento dell'utente e la lista dei commenti associati al
     * film
     */
    public void getComment() {
        try {
            CommentModel commento = CommentManager.get(this.getUser(), this.getId_film());
            this.setId_commento(commento != null ? commento.getId_commento() : 0);
            if (this.getId_commento() != 0) {
                this.setVoto(commento.getVoto());
                this.setGiudizio(commento.getGiudizio());
            }

            // creo la lista dei commenti associati al film
            List<CommentModel> commenti = CommentManager.getCommenti(this.getId_film());
            this.setCommenti(commenti.toArray(new CommentModel[commenti.size()]));
        } catch (Exception ex) {
            this.setAlert(Message.COMMENTGETERROR);
        }
    }

    /**
     * Recupero id di un commento
     *
     * @param commento Il commento di interesse
     * @return L'id del commento
     */
    public int getComment_IdCommento(CommentModel commento) {
        return commento.getId_commento();
    }

    /**
     * Recupero il voto di un commento
     *
     * @param commento Il commento di interesse
     * @return Il voto del commento
     */
    public int getComment_Voto(CommentModel commento) {
        return commento.getVoto();
    }

    /**
     * Recupero il giudizio di un commento
     *
     * @param commento Il commento di interesse
     * @return Il giudizio del commento
     */
    public String getComment_Giudizio(CommentModel commento) {
        return commento.getGiudizio();
    }

    /**
     * Recupero l'utente che ha postato il commento
     *
     * @param commento Il commento di interesse
     * @return L'username dell'utente del commento
     */
    public String getComment_User(CommentModel commento) {
        return commento.getUsername();
    }

    /**
     * Recupero la lunghezza dell'array Commenti
     *
     * @return La lunghezza dell'array
     */
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
     * Get the value of deleteFilm
     *
     * @return the value of deleteFilm
     */
    public int[] getDeleteFilm() {
        return deleteFilm;
    }

    /**
     * Set the value of deleteFilm
     *
     * @param deleteFilm new value of deleteFilm
     */
    public void setDeleteFilm(int[] deleteFilm) {
        this.deleteFilm = deleteFilm;
    }

    /**
     * Get the value of deleteFilm at specified index
     *
     * @param index the index of deleteFilm
     * @return the value of deleteFilm at specified index
     */
    public int getDeleteFilm(int index) {
        return this.deleteFilm[index];
    }

    /**
     * Set the value of deleteFilm at specified index.
     *
     * @param index the index of deleteFilm
     * @param deleteFilm new value of deleteFilm at specified index
     */
    public void setDeleteFilm(int index, int deleteFilm) {
        this.deleteFilm[index] = deleteFilm;
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
