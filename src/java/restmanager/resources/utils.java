package restmanager.resources;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import restmanager.resources.utils;

/**
 *
 * @author Jorge
 */
public class utils {

    public static void limpiarTabla(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        while (table.getRowCount() != 0) {
            model.removeRow(0);
        }
    }

    /**
     * Busca en una tabla y borra los valores que no contienen la secuencia
     * pasada por parametro
     *
     * @param tabla la tabla en la que se va a buscar
     * @param SearchSubSecuence la secuencia de texto que se quiere buscar
     */
    public static void searchInTable(JTable tabla, String SearchSubSecuence) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean found = false;
            for (int j = 0; j < model.getColumnCount() && !found; j++) {
                if (tabla.getValueAt(i, j).toString().contains(SearchSubSecuence)) {
                    found = true;
                }
            }
            if (!found) {
                model.removeRow(i);
            }
        }

    }

    /**
     * Este metodo le pone una imagen de fondo a un frame
     *
     * @param frame el frame que le vas a poner el fondo
     * @param path el path convertido a trsing de donde se encuentra la foto
     */
    public static void setFondo(JFrame frame, String path) {

        ((JPanel) frame.getContentPane()).setOpaque(false);

        //Carga la imagen del path
        ImageIcon fondo = new ImageIcon(frame.getClass().getResource(path));

        //Crea un label con la imagen de fondo
        JLabel aux = new JLabel(fondo);

        //Pone el label en el panel de fondo del frame
        frame.getLayeredPane().add(aux, JLayeredPane.FRAME_CONTENT_LAYER);

        //Agranda la imagen al tamaño del frame
        aux.setBounds(0, 0, frame.getWidth(), frame.getHeight());
    }

    public static void setFondo(JDialog frame, String path) {
        ((JPanel) frame.getContentPane()).setOpaque(false);
        ImageIcon fondo = new ImageIcon(frame.getClass().getResource(path));
        JLabel aux = new JLabel(fondo);
        frame.getLayeredPane().add(aux, JLayeredPane.FRAME_CONTENT_LAYER);
        aux.setBounds(0, 0, frame.getWidth(), frame.getHeight());
    }

    /**
     * vacia una tabla y luego la llena con la informacion pasada por parametro
     *
     * @param rowData la informacion de cada celda en un array de arrays
     * @param table la tabla donde se va a mostrar la informacion
     * @throws java.lang.Exception
     */
    public static void UpdateTable(ArrayList[] rowData, JTable table) throws Exception {

        limpiarTabla(table);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (table.getColumnCount() != rowData.length) {
            throw new Exception("Los datos tienen mas columnas que la tabla");
        }
        Object[] row = new Object[rowData.length];
        for (int i = 0; i < rowData[0].size(); i++) {
            for (int j = 0; j < rowData.length; j++) {
                row[j] = rowData[j].get(i);
            }
            model.addRow(row);
        }

    }

    /**
     * añade a la tabla seleccionada la informacion pasada por parametro sin
     * borrar la informacion que esta ya contiene
     *
     * @param rowData los datos pasados en un array de arrays
     * @param table la tabla que se va a llenar con la info
     * @throws java.lang.Exception si los datos contienen mas columnas que la
     * tabla
     */
    public static void AddToTable(ArrayList[] rowData, JTable table) throws Exception {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (table.getColumnCount() != rowData.length) {
            throw new Exception("Los datos tienen mas columnas que la tabla");
        }
        Object[] row = new Object[rowData.length];
        for (int i = 0; i < rowData[0].size(); i++) {
            for (int j = 0; j < rowData.length; j++) {
                row[j] = rowData[j].get(i);

            }
            model.addRow(row);
        }

    }

    public static float calcularSumaTabla(JTable table, int column) {
        float total = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            total += (float) table.getValueAt(i, column);
        }
        return total;

    }

    /**
     * inicializa un arrays de arrays
     *
     * @param array el array a inicializar
     * @return el array inicializado
     *
     */
    public static ArrayList[] initArray(ArrayList[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = new ArrayList();
        }
        return array;
    }

    public static void addToTable(JTable table, Object[] row) throws Exception {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (table.getColumnCount() != row.length) {
            throw new Exception("Los datos tienen mas columnas que la tabla");
        }
        model.addRow(row);

    }

    public static void removeFromTable(JTable table, int row) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(row);
    }

    /**
     * borra de un string seleccionado la secuencia de caracteres y devuelve l
     * string
     *
     * @param charsecuence
     * @return
     */
    public static String removeFromString(String charsecuence) {
        return null;
    }

    /**
     * redondea al centavo mas arriba las cuenta. si la cuentaes 2.71 redondea a
     * 2.75
     *
     * @param valorARedondear el valor a redondear en entero (multiplicando el
     * float por 100)
     * @return un string con el valor a imprimir o usarel valor tiene incluida
     * la moneda separada con un espacio
     */
    public static String redondeoPorExceso(float valorARedondear) {
        int valorConvertidoEntero = (int) Math.ceil(valorARedondear * 100);
        int ref = valorConvertidoEntero % 5;
        if (ref != 0) {
            valorConvertidoEntero += 5 - ref;
        }
        return R.formatoMoneda.format((float) valorConvertidoEntero / 100) + R.COIN_SUFFIX;
    }

    /**
     * redondea al centavo mas bajo las cuentas. si la cuenta es 2.79 redondea a
     * 2.75
     *
     * @param valorARedondear el valor a redondear en entero (multiplicando el
     * float por 100)
     * @return un string con el valor a imprimir o usar
     */
    public static String redondeoPorDefecto(float valorARedondear) {
        int valorConvertidoEntero = (int) Math.floor(valorARedondear * 100);
        int ref = valorConvertidoEntero % 5;
        if (ref != 0) {
            valorConvertidoEntero += 5 - ref;
        }
        return R.formatoMoneda.format((float) valorConvertidoEntero / 100) + R.COIN_SUFFIX;
    }

    /**
     *
     * reduce los decimales de un float a 2 y ademas agrega la moneda d sufijo
     * @param valorARedondear
     * @return
     */
    public static String setDosLugaresDecimales(float valorARedondear) {
        return Math.round(valorARedondear * Math.pow(10, 2)) / Math.pow(10, 2) + R.COIN_SUFFIX;
    }

    /**
     *
     * @param valorARedondear
     * @return
     */
    public static float setDosLugaresDecimalesFloat(float valorARedondear) {
        return (float) (Math.round(valorARedondear * Math.pow(10, 2)) / Math.pow(10, 2));
    }

    public static int cantidadARedondearPorExceso(int valorARedondear) {
        return valorARedondear % 5 != 0 ? 5 - (valorARedondear % 5) : 0;
    }

    public static float redondeoPorExcesoFloat(Float valorARedondear) {
        int valorConvertidoEntero = (int) Math.ceil((int)(valorARedondear * 100));
        int ref = valorConvertidoEntero % 5;
        if (ref != 0) {
            valorConvertidoEntero += 5 - ref;
        }
        return ((float) valorConvertidoEntero / 100) ;
    }
}
