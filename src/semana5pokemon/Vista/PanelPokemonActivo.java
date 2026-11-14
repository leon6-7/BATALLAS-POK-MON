/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semana5pokemon.Vista;
import modelo.Pokemon;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/**
 *
 * @author Abril
 */

public class PanelPokemonActivo extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_BIEN = new Color(60, 160, 60);
    private static final Color COLOR_MEDIO = new Color(230, 160, 20);
    private static final Color COLOR_CRITICO = new Color(200, 50, 50);

    private final JLabel lblEntrenador = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblNombre = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblDetalle = new JLabel(" ", SwingConstants.CENTER);
    private final JProgressBar barraPs = new JProgressBar();
    private final JLabel lblEstado = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblRestantes = new JLabel(" ", SwingConstants.CENTER);

    public PanelPokemonActivo() {
        setLayout(new GridLayout(0, 1, 0, 6));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(20, 30, 100), 2),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        lblEntrenador.setFont(lblEntrenador.getFont().deriveFont(Font.BOLD, 14f));
        lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD, 24f));
        lblDetalle.setFont(lblDetalle.getFont().deriveFont(13f));
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.BOLD, 13f));
        lblEstado.setForeground(COLOR_CRITICO);
        lblRestantes.setFont(lblRestantes.getFont().deriveFont(12f));
        lblRestantes.setForeground(Color.DARK_GRAY);

        barraPs.setMinimum(0);
        barraPs.setStringPainted(true);
        barraPs.setPreferredSize(new Dimension(220, 24));

        add(lblEntrenador);
        add(lblNombre);
        add(lblDetalle);
        add(barraPs);
        add(lblEstado);
        add(lblRestantes);
    }

    public void setEntrenador(String texto) {
        lblEntrenador.setText(texto);
    }

    public void setRestantes(int enPie, int total) {
        lblRestantes.setText("Pokémon en pie: " + enPie + "/" + total);
    }

    public void mostrar(Pokemon pokemon) {
        int psActuales = pokemon.getPsActual();
        int psMaximos = pokemon.getPsMax();

        lblNombre.setText(pokemon.getNombre());
        lblDetalle.setText("Tipo: " + pokemon.getTipo().getNombre() + "   |   Nivel " + pokemon.getNivel());

        barraPs.setMaximum(psMaximos);
        barraPs.setValue(psActuales);
        barraPs.setString(psActuales + " / " + psMaximos + " PS");
        barraPs.setForeground(colorSegunSalud(psActuales, psMaximos));

        lblEstado.setText(pokemon.estaDebilitado() ? "¡DEBILITADO!" : " ");
    }

    private Color colorSegunSalud(int actuales, int maximos) {
        double proporcion = (maximos == 0) ? 0 : (double) actuales / maximos;
        if (proporcion > 0.5) {
            return COLOR_BIEN;
        }
        if (proporcion > 0.2) {
            return COLOR_MEDIO;
        }
        return COLOR_CRITICO;
    }
}