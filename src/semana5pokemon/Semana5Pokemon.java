package semana5pokemon;
   import semana5pokemon.Vista.VentanaPrincipal;
    import javax.swing.JFrame;
    import javax.swing.SwingUtilities;

public class Semana5Pokemon {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
     });

}}
