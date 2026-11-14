package semana5pokemon.Vista;

import modelo.Pokemon;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PanelPokemonActivo extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_BIEN = new Color(60, 160, 60);
    private static final Color COLOR_MEDIO = new Color(230, 160, 20);
    private static final Color COLOR_CRITICO = new Color(200, 50, 50);
    private static final int ANCHO_IMAGEN = 190;
    private static final int ALTO_IMAGEN = 185;
    private static final Map<String, ImageIcon> IMAGENES = new HashMap<>();

    private final JLabel lblEntrenador = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblNombre = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblDetalle = new JLabel(" ", SwingConstants.CENTER);
    private final JProgressBar barraPs = new JProgressBar();
    private final JLabel lblEstado = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblRestantes = new JLabel(" ", SwingConstants.CENTER);

    public PanelPokemonActivo() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(20, 30, 100), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        lblEntrenador.setFont(lblEntrenador.getFont().deriveFont(Font.BOLD, 14f));
        lblImagen.setPreferredSize(new Dimension(ANCHO_IMAGEN, ALTO_IMAGEN));
        lblNombre.setFont(lblNombre.getFont().deriveFont(Font.BOLD, 24f));
        lblDetalle.setFont(lblDetalle.getFont().deriveFont(13f));
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.BOLD, 13f));
        lblEstado.setForeground(COLOR_CRITICO);
        lblRestantes.setFont(lblRestantes.getFont().deriveFont(12f));
        lblRestantes.setForeground(Color.DARK_GRAY);

        barraPs.setMinimum(0);
        barraPs.setStringPainted(true);
        barraPs.setPreferredSize(new Dimension(220, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(3, 2, 3, 2);

        gbc.gridy = 0;
        add(lblEntrenador, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        add(lblImagen, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;

        gbc.gridy = 2;
        add(lblNombre, gbc);

        gbc.gridy = 3;
        add(lblDetalle, gbc);

        gbc.gridy = 4;
        add(barraPs, gbc);

        gbc.gridy = 5;
        add(lblEstado, gbc);

        gbc.gridy = 6;
        add(lblRestantes, gbc);
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

        lblImagen.setIcon(obtenerImagen(pokemon.getNombre()));
        lblImagen.setText(lblImagen.getIcon() == null ? "Imagen no disponible" : "");
        lblNombre.setText(pokemon.getNombre());
        lblDetalle.setText("Tipo: " + pokemon.getTipo().getNombre() + "   |   Nivel " + pokemon.getNivel());

        barraPs.setMaximum(psMaximos);
        barraPs.setValue(psActuales);
        barraPs.setString(psActuales + " / " + psMaximos + " PS");
        barraPs.setForeground(colorSegunSalud(psActuales, psMaximos));

        lblEstado.setText(pokemon.estaDebilitado() ? "¡DEBILITADO!" : " ");
    }

    private ImageIcon obtenerImagen(String nombrePokemon) {
        String clave = nombrePokemon.toLowerCase(Locale.ROOT);
        if (IMAGENES.containsKey(clave)) {
            return IMAGENES.get(clave);
        }

        String archivo = archivoImagen(clave);
        if (archivo == null) {
            return null;
        }

        URL recurso = getClass().getResource("/Imagenes/" + archivo);
        if (recurso == null) {
            return null;
        }

        try {
            BufferedImage original = ImageIO.read(recurso);
            if (original == null) {
                return null;
            }

            Image escalada = original.getScaledInstance(
                    ANCHO_IMAGEN, ALTO_IMAGEN, Image.SCALE_SMOOTH);
            ImageIcon icono = new ImageIcon(escalada);
            IMAGENES.put(clave, icono);
            return icono;
        } catch (IOException ex) {
            return null;
        }
    }

    private String archivoImagen(String nombre) {
        switch (nombre) {
            case "bellsprout": return "bellsprout.jpg";
            case "bulbasaur": return "bulbasaur.jpg";
            case "charmander": return "charmander.png";
            case "chikorita": return "chikorita.png";
            case "growlithe": return "growlithe.png";
            case "mareep": return "mareep.jpg";
            case "oddish": return "oddish.png";
            case "pikachu": return "pikachu.jpg";
            case "poliwag": return "poliwag.png";
            case "ponyta": return "ponyta.jpg";
            case "psyduck": return "psyduck.png";
            case "squirtle": return "squirtle.png";
            case "staryu": return "staryu.png";
            case "voltorb": return "volotorb.png";
            case "vulpix": return "vulpix.png";
            default: return null;
        }
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
