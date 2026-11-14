package semana5pokemon.Vista;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import modelo.Catalogo;
import modelo.Entrenador;
import modelo.Equipo;
import semana5pokemon.Control.ResultadoBatalla;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public static final String PANTALLA_INICIO = "INICIO";
    public static final String PANTALLA_EQUIPO = "EQUIPO";
    public static final String PANTALLA_BATALLA = "BATALLA";
    public static final String PANTALLA_RESULTADO = "RESULTADO";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    private final Catalogo catalogo = new Catalogo();
    private Entrenador entrenador;

    private final PanelInicio panelInicio;
    private final PanelEquipo panelEquipo;
    private final PanelBatalla panelBatalla;
    private final PanelResultado panelResultado;

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
        add(contenedor);

        configurarVentana();
        mostrar(PANTALLA_INICIO);
    }

    private void configurarVentana() {
        setMinimumSize(new Dimension(900, 650));
        setSize(950, 700);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });
    }

  
    public void irAEquipo(Entrenador entrenador) {
        this.entrenador = entrenador;
        panelEquipo.iniciar(entrenador);
        mostrar(PANTALLA_EQUIPO);
    }

  
    public void irABatalla() {
        Equipo equipoJugador = entrenador.getEquipo().copiar();  
        Equipo equipoRival = Equipo.generarRival(catalogo, equipoJugador.getCantidad());
        panelBatalla.iniciar(entrenador, equipoJugador, equipoRival);
        mostrar(PANTALLA_BATALLA);
    }

  
    public void irAResultado(ResultadoBatalla resultado) {
        panelResultado.mostrar(resultado);
        mostrar(PANTALLA_RESULTADO);
    }

    
    public void nuevaPartida() {
        panelEquipo.iniciar(entrenador);
        mostrar(PANTALLA_EQUIPO);
    }

    public void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres salir del juego?", "Salir",
                JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void mostrar(String pantalla) {
        cardLayout.show(contenedor, pantalla);
    }
}