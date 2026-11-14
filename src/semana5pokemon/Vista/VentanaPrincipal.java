package semana5pokemon.Vista;

import Excepciones.AccionInvalidaException;
import modelo.Catalogo;
import modelo.Entrenador;
import semana5pokemon.Control.Batalla;
import semana5pokemon.Control.ResultadoBatalla;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    public static final String PANTALLA_INICIO = "INICIO";
    public static final String PANTALLA_EQUIPO = "EQUIPO";
    public static final String PANTALLA_BATALLA = "BATALLA";
    public static final String PANTALLA_RESULTADO = "RESULTADO";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);
    private final Catalogo catalogo = new Catalogo();
    private final PanelInicio panelInicio;
    private final PanelEquipo panelEquipo;
    private final PanelBatalla panelBatalla;
    private final PanelResultado panelResultado;
    private Entrenador entrenador;
    private Batalla batalla;

    public VentanaPrincipal() {
        super("Batallas Pokémon");
        panelInicio = new PanelInicio(this);
        panelEquipo = new PanelEquipo(this, catalogo);
        panelBatalla = new PanelBatalla(this);
        panelResultado = new PanelResultado(this);
        contenedor.add(panelInicio, PANTALLA_INICIO);
        contenedor.add(panelEquipo, PANTALLA_EQUIPO);
        contenedor.add(panelBatalla, PANTALLA_BATALLA);
        contenedor.add(panelResultado, PANTALLA_RESULTADO);
        setContentPane(contenedor);
        setMinimumSize(new Dimension(950, 680));
        setSize(1100, 760);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { salir(); }
        });
        setLocationRelativeTo(null);
        mostrar(PANTALLA_INICIO);
    }

    private void mostrar(String pantalla) {
        cardLayout.show(contenedor, pantalla);
    }

    public void irAEquipo(Entrenador nuevoEntrenador) {
        entrenador = nuevoEntrenador;
        panelEquipo.iniciar(entrenador);
        mostrar(PANTALLA_EQUIPO);
    }

    public void irABatalla() {
        try {
            batalla = new Batalla(entrenador, catalogo);
            batalla.comenzar();
            panelBatalla.iniciar(batalla);
            mostrar(PANTALLA_BATALLA);
        } catch (AccionInvalidaException | RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se puede iniciar", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void mostrarResultado(ResultadoBatalla resultado) {
        panelResultado.mostrar(resultado);
        mostrar(PANTALLA_RESULTADO);
    }

    public void nuevaPartida() {
        panelEquipo.iniciar(entrenador);
        mostrar(PANTALLA_EQUIPO);
    }

    public void salir() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas salir del juego?", "Salir",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
