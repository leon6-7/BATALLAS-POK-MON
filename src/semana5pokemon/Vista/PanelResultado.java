package semana5pokemon.Vista;

import semana5pokemon.Control.ResultadoBatalla;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;

public class PanelResultado extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JLabel titulo = new JLabel("RESULTADO", SwingConstants.CENTER);
    private final JTextArea resumen = new JTextArea();
    private final JButton btnNueva = new JButton("Nueva partida");
    private final JButton btnSalir = new JButton("Salir");
    private final VentanaPrincipal ventana;

    public PanelResultado(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(35, 55, 35, 55));
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 30f));
        add(titulo, BorderLayout.NORTH);
        resumen.setEditable(false);
        resumen.setLineWrap(true);
        resumen.setWrapStyleWord(true);
        resumen.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        resumen.setMargin(new java.awt.Insets(18, 18, 18, 18));
        add(new JScrollPane(resumen), BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        botones.add(btnNueva);
        botones.add(btnSalir);
        add(botones, BorderLayout.SOUTH);
        btnNueva.addActionListener(e -> ventana.nuevaPartida());
        btnSalir.addActionListener(e -> ventana.salir());
    }

    public void mostrar(ResultadoBatalla resultado) {
        titulo.setText(resultado.getEstadoFinal().getTexto().toUpperCase());
        titulo.setForeground(resultado.esVictoria() ? new Color(30, 130, 50)
                : resultado.getEstadoFinal().name().equals("EMPATE") ? new Color(180, 120, 20) : new Color(180, 30, 30));
        StringBuilder texto = new StringBuilder();
        texto.append(resultado.getMensajeFinal()).append("\n\n");
        texto.append("Entrenador: ").append(resultado.getNombreEntrenador()).append("\n");
        texto.append("Rival: ").append(resultado.getNombreRival()).append("\n");
        texto.append("Rondas jugadas: ").append(resultado.getRondas()).append("\n\n");
        texto.append("POKÉMON DEBILITADOS\n");
        texto.append(resultado.getNombreEntrenador()).append(": ").append(resultado.getDebilitadosJugador()).append("\n");
        texto.append(resultado.getNombreRival()).append(": ").append(resultado.getDebilitadosRival()).append("\n\n");
        texto.append("POKÉMON EN PIE\n");
        texto.append(resultado.getNombreEntrenador()).append(":\n");
        if (resultado.getEnPieJugador().isEmpty()) texto.append("  Ninguno\n");
        else for (String pokemon : resultado.getEnPieJugador()) texto.append("  - ").append(pokemon).append("\n");
        texto.append("\n").append(resultado.getNombreRival()).append(":\n");
        if (resultado.getEnPieRival().isEmpty()) texto.append("  Ninguno\n");
        else for (String pokemon : resultado.getEnPieRival()) texto.append("  - ").append(pokemon).append("\n");
        resumen.setText(texto.toString());
        resumen.setCaretPosition(0);
    }
}
