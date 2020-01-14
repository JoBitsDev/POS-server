/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restmanager.resources;

import com.restmanager.Configuracion;
import com.restmanager.Negocio;
import com.restmanager.printservice.Impresion;
import com.restmanager.printservice.Ticket;
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

    public static final EntityManagerFactory e = Persistence.createEntityManagerFactory("Restaurant_Manager_Web_ServicePU");
    public static final EntityManager em1 = e.createEntityManager();

    public static boolean TABLETS_EN_COCINA = R.em1.find(Configuracion.class, R.SettingID.GENERAL_TABLET_COCINA.getValue()).getValor() == 1;

    public static final String SEPARADOR = "_";

    public static boolean AUTO_UPDATE_INSUMO_PRICE = true;

    public static String REST_NAME = em1.find(Negocio.class, 1).getNombre();

    public static String logFilePath = "logs/";

    public static String COIN_SUFFIX = " " + em1.find(Negocio.class, 1).getMonedaPrincipal();

    public static final Date TODAYS_DATE = new Date();

    public static int COINCHANGE = R.em1.find(Configuracion.class, R.SettingID.GENERAL_CAMBIO_MONEDA.getValue()).getValor();

    public static String MAIN_COIN = em1.find(Negocio.class, 1).getMonedaPrincipal();

    public static final String NO_MESA = "M-0";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd'/'MM'/'yy");

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(" hh ':' mm ' ' a ");

    public static final SimpleDateFormat DATE_FORMAT_FOR_LOGS = new SimpleDateFormat("yyyy'_'MM'_'dd");

    public static DecimalFormat formatoMoneda = new DecimalFormat("0.00");

    public static String RELEASE_VERSION = "Version 2.6.3";

    public static int BUILD_VERSION = 11;

    public static enum SettingID {

        //
        //GENERALES
        //
        GENERAL_CAMBIO_MONEDA("GENERAL_CAMBIO_MONEDA"),
        GENERAL_TURNOS_VARIOS("GENERAL_MULTIPLES_TURNOS"),
        GENERAL_CAJERO_PERMISOS_ESP("GENERAL_CAJERO_PERMISOS_ESP"),
        GENERAL_CONSUMO_CASA_ESTADISTICAL("GENERAL_CONSUMO_CASA_ESTADISTICAS"),
        GENERAL_SERVER_IP("GENERAL_SERVIDOR_IP"),
        GENERAL_TABLET_COCINA("GENERAL_TABLET_COCINA"),
        //
        //IMPRESION
        //

        IMPRESION_TICKET_TAMANO_PAPEL("PRINTING_TICKET_PAPER_SIZE"),
        IMPRESION_TICKET_CARACTER_SEPARADOR("PRINTING_TICKET_SEPARATOR_CHAR"),
        IMPRESION_TICKET_ENCABEZADO_RESTAURANTE("PRINTING_TICKET_HEADER"),
        IMPRESION_TICKET_SUBTOTAL("PRINTING_TICKET_SUBTOTAL"),
        IMPRESION_IMPRIMIR_COCINA_CENTRAL("PRINTING_CENTRAL_KITCHEN"),
        IMPRESION_IMPRIMIR_GASTOS_AUTORIZOS("PRINTING_EXPENSES_IN_HAUSE_TICKETS"),
        IMPRESION_IMPRIMIR_TICKET_EN_COCINA("PRINTING_PRINT_KITCHEN_TICKET"),
        IMPRESION_CANTIDAD_COPIAS("PRINTING_COPIES"),
        IMPRESION_REDONDEO_EXCESO("PRINTING_ROUNDING");

        private final String value;

        private SettingID(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

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
