package bussines_manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static GestorLocales gestorLocales = new GestorLocales();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gestión de Locales");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        frame.add(panel, BorderLayout.SOUTH);

        JTextField nombreField = new JTextField(15); // Campo para el nombre del local
        JTextField direccionField = new JTextField(15); // Campo para la dirección del local
        JTextField latField = new JTextField(15); // Campo para la latitud del local
        JTextField lonField = new JTextField(15); // Campo para la longitud del local
        JButton findButton = new JButton("Encontrar Local Más Cercano");
        JButton addButton = new JButton("Agregar Local");
        JButton infoButton = new JButton("Busca Información de Locales");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5); // Espacios entre componentes

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Nombre:"), c);

        c.gridx = 1;
        panel.add(nombreField, c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Dirección:"), c);

        c.gridx = 1;
        panel.add(direccionField, c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Latitud:"), c);

        c.gridx = 1;
        panel.add(latField, c);

        c.gridx = 2;
        panel.add(new JLabel("Longitud:"), c);

        c.gridx = 3;
        panel.add(lonField, c);

        c.gridx = 0;
        c.gridy = 3;
        panel.add(addButton, c);

        c.gridx = 1;
        panel.add(findButton, c);

        c.gridx = 2;
        panel.add(infoButton, c);

        // Acción para el botón "Agregar Local"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String direccion = direccionField.getText();
                double latitud = Double.parseDouble(latField.getText());
                double longitud = Double.parseDouble(lonField.getText());
                Local local = new Local(nombre, direccion, latitud, longitud);
                try {
                    gestorLocales.agregarLocal(local);
                    textArea.setText("Local agregado: \n" +
                            "Nombre: " + local.getNombre() + "\n" +
                            "Dirección: " + local.getDireccion() + "\n" +
                            "Latitud: " + local.getLatitud() + "\n" +
                            "Longitud: " + local.getLongitud());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    textArea.setText("El local NO pudo ser agregado, revisa el log");
                }
            }
        });

        // Acción para el botón "Busca Información de Locales"
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(""); // Limpiar el área de texto
                for (Local local : gestorLocales.obtenerLocales()) {
                    textArea.append("Local: \n" +
                            "Nombre: " + local.getNombre() + "\n" +
                            "Dirección: " + local.getDireccion() + "\n" +
                            "Latitud: " + local.getLatitud() + "\n" +
                            "Longitud: " + local.getLongitud() + "\n\n");
                }
            }
        });

        // Acción para el botón "Encontrar Local Más Cercano"
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double latitud = Double.parseDouble(latField.getText());
                double longitud = Double.parseDouble(lonField.getText());
                Local localMasCercano = gestorLocales.obtenerLocalMasCercano(latitud, longitud);
                if (localMasCercano != null) {
                    textArea.setText("Local más cercano: \n" +
                            "Nombre: " + localMasCercano.getNombre() + "\n" +
                            "Dirección: " + localMasCercano.getDireccion() + "\n" +
                            "Latitud: " + localMasCercano.getLatitud() + "\n" +
                            "Longitud: " + localMasCercano.getLongitud());
                } else {
                    textArea.setText("No se encontró ningún local cercano.");
                }
            }
        });

        frame.setVisible(true);
    }
}
