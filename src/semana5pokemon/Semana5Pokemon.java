/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package semana5pokemon;
   import semana5pokemon.Vista.VentanaPrincipal;
    import javax.swing.JFrame;
    import javax.swing.SwingUtilities;
 /**
 *
 * @author Abril
 */
public class Semana5Pokemon {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         // Always launch Swing GUIs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLocationRelativeTo(null); // Centers the window on screen
            ventana.setVisible(true);            // Makes the window appear
     });
    
}}
