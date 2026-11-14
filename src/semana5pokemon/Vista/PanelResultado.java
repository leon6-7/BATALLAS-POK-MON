package semana5pokemon.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import semana5pokemon.Control.EstadoBatalla;
import semana5pokemon.Control.ResultadoBatalla;

public class PanelResultado extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color VERDE = new Color(30, 130, 50);
    private static final Color ROJO = new Color(180, 30, 30);
    private static final Color GRIS = new Color(90, 90, 90);

    private final VentanaPrincipal ventana;

    private final JLabel lblTitulo = new JLabel(" ", SwingConstants.CENTER);
    private final JTextArea txtResumen = new JTextArea();
    private final JButton btnNuevaPartida = new JButton("Nueva partida");
    private final JButton btnSalir = new JButton("Salir");

    public PanelResultado(VentanaPrincipal ventana) {
        this.ventana = ventana;
        construirInterfaz();
        btnNuevaPartida.addActionListener(e -> ventana.nuevaPartida());
        btnSalir.addActionListener(e -> ventana.confirmarSalida());
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 60, 25, 60));

        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 34f));
        add(lblTitulo, BorderLayout.NORTH);

        txtResumen.setEditable(false);
        txtResumen.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        txtResumen.setMargin(new Insets(12, 16, 12, 16));
        JScrollPane scroll = new JScrollPane(txtResumen);
        scroll.setBorder(BorderFactory.createTitledBorder("Resumen de la partida"));
        add(scroll, BorderLayout.CENTER);

        btnNuevaPartida.setFont(btnNuevaPartida.getFont().deriveFont(Font.BOLD, 15f));
        btnSalir.setFont(btnSalir.getFont().deriveFont(15f));
        JPanel botones = new JPanel();
        botones.add(btnNuevaPartida);
        botones.add(btnSalir);
        add(botones, BorderLayout.SOUTH);
    }

    public void mostrar(ResultadoBatalla resultado) {
        lblTitulo.setText(resultado.getTitulo());
        if (resultado.getEstadoFinal() == EstadoBatalla.VICTORIA) {
            lblTitulo.setForeground(VERDE);
        } else if (resultado.getEstadoFinal() == EstadoBatalla.DERROTA) {
            lblTitulo.setForeground(ROJO);
        } else {
            lblTitulo.setForeground(GRIS);
        }
        txtResumen.setText(resultado.getResumen());
        txtResumen.setCaretPosition(0);
    }
}