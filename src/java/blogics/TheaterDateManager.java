package blogics;

import exceptions.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import services.database.*;

/**
 *
 * @author Guido Pio
 */
public class TheaterDateManager {

    public static void add(FilmModel film, DateTimeModel show, TheaterModel theater)
            throws Exception {

        // controllo su validità data e ora inizio e fine
        LocalTime inizio = show.getOra_inizio();
        LocalTime fine = show.getOra_fine();

        if (fine.isBefore(inizio)) { // controlla già che anche la durata sia maggiore di 0
            throw new Exception();
        }

        int ora_inizio = inizio.getHour();
        int min_inizio = inizio.getMinute();
        LocalTime durata = fine.minusHours(ora_inizio).minusMinutes(min_inizio);
        if (durata.compareTo(film.getDurata()) < 0) { // < 0 se inferiore a quella del film
            throw new Exception();
        }

        DataBase database = DBService.getDataBase();
        try {
            // gestione inizio e fine rispetto agli altri orari
            String sql = "SELECT P.ora_inizio, P.ora_fine " +
                        "FROM `film_sala_programmazione` AS FSP " +
                        "JOIN `sale` AS S ON FSP.id_sala=S.id_sala " +
                        "JOIN `programmazione` AS P ON FSP.id_data=P.id_data " +
                        "WHERE `numero_sala`='" + theater.getNumero_sala() + "' AND " +
                        "P.data='" + show.getData() + "'" +
                        "ORDER BY P.ora_inizio;";
            ResultSet resultSet = database.select(sql);
            while(resultSet.next()) {
                LocalTime _inizio = resultSet.getTime("ora_inizio").toLocalTime();
                LocalTime _fine = resultSet.getTime("ora_fine").toLocalTime();
                
                // errore se un film inizia dopo un altro film ma prima della fine di quest'ultimo
                if (inizio.isAfter(_inizio) && inizio.isBefore(_fine)) {
                    throw new Exception();
                }
                // errore se il film è compreso nell'inizio di un altro film
                if (inizio.isBefore(_inizio) && fine.isAfter(_inizio)) {
                    throw new Exception();
                }
            }
            resultSet.close();

            // inserimento data con recupero id_data
            sql = "INSERT INTO `programmazione`"
                    + "(`data`,`ora_inizio`,`ora_fine`) VALUES "
                    + "('" + show.getData() + "',"
                    + "'" + show.getOra_inizio() + "',"
                    + "'" + show.getOra_fine() + "');";
            resultSet = database.modifyPK(sql);
            if (resultSet.next()) {
                show.setId_data(resultSet.getInt(1));
            }
            resultSet.close();

            // inserimento sala con recupero id_sala
            sql = "INSERT INTO `sale`"
                    + "(`numero_sala`) VALUES "
                    + "('" + theater.getNumero_sala() + "');";
            resultSet = database.modifyPK(sql);
            if (resultSet.next()) {
                theater.setId_sala(resultSet.getInt(1));
            }
            resultSet.close();

            // inserimento id_film - id_data - id_sala nella relativa tabella
            sql = "INSERT INTO `film_sala_programmazione`"
                    + "(`id_film`, `id_sala`, `id_data`) VALUES "
                    + "('" + film.getId_film() + "',"
                    + "'" + theater.getId_sala() + "',"
                    + "'" + show.getId_data() + "');";
            int count = database.modify(sql);
            if (count == 0) {
                throw new Exception();
            }

            database.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

    // Recupero un array con le date associate al numero della sala
    public static DateTimeModel[] getDate(int num_sala, LocalDate from, LocalDate to)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        DateTimeModel[] model = null;
        try {
            /**
             * Recupero tutte le date e le sale assegnate ad un dato film a
             * partire da una from fino a to, ordinate per data e ora_inizio.
             */
            /*
             SELECT * 
             FROM `film_sala_programmazione` AS FSP 
             JOIN `sale` AS S ON FSP.id_sala=S.id_sala 
             JOIN `programmazione` AS P ON FSP.id_data=P.id_data 
             WHERE `numero_sala`='1' AND P.date BETWEEN from AND to
             ORDER BY P.data, P.ora_inizio
             */
            String sql = "SELECT * "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `sale` AS S ON FSP.id_sala=S.id_sala "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "WHERE `numero_sala`='" + num_sala + "' AND "
                    + "P.data BETWEEN '" + from.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' AND "
                    + "'" + to.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' "
                    + "ORDER BY P.data, P.ora_inizio";
            ResultSet resultSet = database.select(sql);
            List<DateTimeModel> list;
            list = new ArrayList<>();
            while (resultSet.next()) {
                DateTimeModel tmp = new DateTimeModel(resultSet);
                list.add(tmp);
            }
            resultSet.close();
            database.commit();

            // preparo l'array per il modello inserito come parametro
            model = new DateTimeModel[list.size()];
            for (int i = 0; i < list.size(); i++) {
                model[i] = list.get(i);
            }
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }
}
