package semana5pokemon.Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import modelo.Pokemon;


public class PanelPokemonActivo extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color VERDE = new Color(40, 160, 60);
    private static final Color AMARILLO = new Color(230, 180, 20);
    private static final Color ROJO = new Color(200, 40, 40);

    private final JLabel lblNombre = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblNivel = new JLabel(" ", SwingConstants.CENTER);
    private final JProgressBar barraPs = new JProgressBar();

    public PanelPokemonActivo() {
        setLayout(new GridLayout(3, 1, 0, 6));

        lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD, 20f));
        lblNivel.setFont(lblNivel.getFont().deriveFont(14f));
        barraPs.setStringPainted(true);
        barraPs.setFont(barraPs.getFont().deriveFont(Font.BOLD, 13f));

        add(lblNombre);
        add(lblNivel);
        add(barraPs);
    }

  
    public void setDueno(String dueno) {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(dueno),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
    }

    public void actualizar(Pokemon p) {
        lblNombre.setText(p.getNombre() + " (" + p.getTipo() + ")");
        lblNivel.setText("Nivel " + p.getNivel());

        barraPs.setMaximum(p.getPsMax());
        barraPs.setValue(p.getPsActual());
        barraPs.setString("PS " + p.getPsActual() + " / " + p.getPsMax());

        double porcentaje = (double) p.getPsActual() / p.getPsMax();
        if (porcentaje > 0.5) {
            barraPs.setForeground(VERDE);
        } else if (porcentaje > 0.2) {
            barraPs.setForeground(AMARILLO);
        } else {
            barraPs.setForeground(ROJO);
        }
    }
}