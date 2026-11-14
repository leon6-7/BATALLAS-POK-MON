package batallas;

import semana5pokemon.Vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}
