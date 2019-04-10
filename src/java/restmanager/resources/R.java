/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restmanager.resources;

import com.restmanager.Negocio;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class R {

    public static boolean TABLETS_EN_COCINA = true;

    public static final EntityManagerFactory e = Persistence.createEntityManagerFactory("Restaurant_Manager_Web_ServicePU");
    public static final EntityManager em1 = e.createEntityManager();

    public static final String SEPARADOR = "_";
    
    public static boolean AUTO_UPDATE_INSUMO_PRICE = true;

    public static String REST_NAME = em1.find(Negocio.class, 1).getNombre();

    public static String logFilePath = "logs/";

    public static String COIN_SUFFIX = " "+ em1.find(Negocio.class, 1).getMonedaPrincipal();

    public static final Date TODAYS_DATE = new Date();

    public static final float PERCENTAGE = 10;

    public static final int COINCHANGE = 25;

    public static String MAIN_COIN = em1.find(Negocio.class, 1).getMonedaPrincipal();

    public static final String NO_MESA = "M-0";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd'/'MM'/'yy");

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(" hh ':' mm ' ' a ");

    public static final SimpleDateFormat DATE_FORMAT_FOR_LOGS = new SimpleDateFormat("yyyy'_'MM'_'dd");

    public static DecimalFormat formatoMoneda = new DecimalFormat("0.00");

    public static String RELEASE_VERSION = "Version 2.5.2";

    public static int BUILD_VERSION = 3;

    public static enum UM {
        U("U"),
        Gr("Gr"),
        Kg("Kg"),
        Lbs("Lbs"),
        Lts("Lts");

        private final String valor;

        private UM(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        @Override
        public String toString() {
            return valor;
        }

    }

    public enum NivelAcceso {
        DEPENDIENTE(0),
        CAJERO(1),
        ALMACENERO(2),
        ECONOMICO(3),
        ADMINISTRADOR(4),
        DESARROLLADOR(5);

        private final int nivel;

        private NivelAcceso(int nivel) {
            this.nivel = nivel;
        }

        public int getNivel() {
            return nivel;
        }
    }

}
