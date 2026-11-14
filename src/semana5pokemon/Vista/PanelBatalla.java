package semana5pokemon.Vista;

import Excepciones.AccionInvalidaException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import modelo.Entrenador;
import modelo.Equipo;
import modelo.Pokemon;
import semana5pokemon.Control.AccionBatalla;
import semana5pokemon.Control.Batalla;
import semana5pokemon.Control.EstadoBatalla;

public class PanelBatalla extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color AZUL_OSCURO = new Color(20, 30, 100);

    private final VentanaPrincipal ventana;
    private Batalla batalla;

    private final JLabel lblRonda = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblEstado = new JLabel(" ", SwingConstants.CENTER);
    private final PanelPokemonActivo panelJugador = new PanelPokemonActivo();
    private final PanelPokemonActivo panelRival = new PanelPokemonActivo();
    private final JTextArea txtMensajes = new JTextArea();

    private final JButton btnAtacar = new JButton("Atacar");
    private final JButton btnEspecial = new JButton("Habilidad especial");
    private final JButton btnRecuperar = new JButton("Recuperar");
    private final JButton btnCambiar = new JButton("Cambiar Pokémon");
    private final JButton btnResultado = new JButton("Ver resultado");

    public PanelBatalla(VentanaPrincipal ventana) {
        this.ventana = ventana;
        construirInterfaz();
        registrarEventos();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        lblRonda.setFont(lblRonda.getFont().deriveFont(Font.BOLD, 24f));
        lblRonda.setForeground(AZUL_OSCURO);
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.BOLD, 14f));
        JPanel norte = new JPanel(new GridLayout(2, 1));
        norte.add(lblRonda);
        norte.add(lblEstado);
        add(norte, BorderLayout.NORTH);

        JPanel combatientes = new JPanel(new GridLayout(1, 2, 20, 0));
        combatientes.add(panelJugador);
        combatientes.add(panelRival);

        txtMensajes.setEditable(false);
        txtMensajes.setLineWrap(true);
        txtMensajes.setWrapStyleWord(true);
        txtMensajes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        txtMensajes.setMargin(new java.awt.Insets(6, 8, 6, 8));
        JScrollPane scroll = new JScrollPane(txtMensajes);
        scroll.setBorder(BorderFactory.createTitledBorder("Mensajes de la batalla"));

        JPanel centro = new JPanel(new BorderLayout(0, 10));
        centro.add(combatientes, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new GridLayout(1, 4, 10, 0));
        acciones.add(btnAtacar);
        acciones.add(btnEspecial);
        acciones.add(btnRecuperar);
        acciones.add(btnCambiar);

        btnResultado.setFont(btnResultado.getFont().deriveFont(Font.BOLD, 14f));
        JPanel pieResultado = new JPanel();
        pieResultado.add(btnResultado);

        JPanel sur = new JPanel(new BorderLayout(0, 8));
        sur.add(acciones, BorderLayout.CENTER);
        sur.add(pieResultado, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);
    }

    private void registrarEventos() {
        btnAtacar.addActionListener(e -> jugar(AccionBatalla.ATACAR, -1));
        btnEspecial.addActionListener(e -> jugar(AccionBatalla.ESPECIAL, -1));
        btnRecuperar.addActionListener(e -> jugar(AccionBatalla.RECUPERAR, -1));
        btnCambiar.addActionListener(e -> cambiarOReemplazar());
        btnResultado.addActionListener(e -> ventana.irAResultado(batalla.getResultado()));
    }

    public void iniciar(Entrenador entrenador, Equipo equipoJugador, Equipo equipoRival) {
        batalla = new Batalla(entrenador, equipoJugador, equipoRival);
        panelJugador.setDueno(entrenador.getNombre());
        panelRival.setDueno(Batalla.NOMBRE_RIVAL);
        txtMensajes.setText("");
        agregarMensaje(batalla.iniciar());
        actualizar();
    }

    private void jugar(AccionBatalla accion, int indiceCambio) {
        bloquearAcciones();                    
        try {
            agregarMensaje(batalla.jugarRonda(accion, indiceCambio));
        } catch (AccionInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acción no válida",
                    JOptionPane.WARNING_MESSAGE);
        }
        actualizar();
        if (batalla.getEstado() == EstadoBatalla.ELEGIR_REEMPLAZO) {
            pedirReemplazo();
        }
    }

    private void cambiarOReemplazar() {
        if (batalla.getEstado() == EstadoBatalla.ELEGIR_REEMPLAZO) {
            pedirReemplazo();
            return;
        }
        int indice = elegirPokemon("¿A qué Pokémon quieres cambiar?", "Cambiar Pokémon");
        if (indice >= 0) {
            jugar(AccionBatalla.CAMBIAR, indice);
        }
    }

    private void pedirReemplazo() {
        int indice = elegirPokemon(batalla.getPokemonJugador().getNombre()
                + " fue debilitado. ¿Quién entra ahora?", "Elegir reemplazo");
        if (indice < 0) {
            return;                            
        }
        try {
            agregarMensaje(batalla.elegirReemplazo(indice));
        } catch (AccionInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acción no válida",
                    JOptionPane.WARNING_MESSAGE);
        }
        actualizar();
    }

   
    private int elegirPokemon(String mensaje, String titulo) {
        ArrayList<Integer> posiciones = batalla.getOpcionesCambio();
        if (posiciones.isEmpty()) {
            return -1;
        }
        String[] opciones = new String[posiciones.size()];
        for (int i = 0; i < posiciones.size(); i++) {
            Pokemon p = batalla.getEquipoJugador().get(posiciones.get(i));
            opciones[i] = p.getNombre() + " (" + p.getTipo() + ")  PS " + p.getPsActual() + "/" + p.getPsMax();
        }
        Object eleccion = JOptionPane.showInputDialog(this, mensaje, titulo,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(eleccion)) {
                return posiciones.get(i);
            }
        }
        return -1;
    }

    private void actualizar() {
        Pokemon jugador = batalla.getPokemonJugador();
        panelJugador.actualizar(jugador);
        panelRival.actualizar(batalla.getPokemonRival());

        lblRonda.setText("Ronda " + batalla.getRonda());
        btnEspecial.setText(jugador.getNombreEspecial() + " (" + jugador.getUsosEspecial() + ")");
        btnRecuperar.setText("Recuperar (" + jugador.getUsosRecuperacion() + ")");
        btnCambiar.setText("Cambiar Pokémon");

        EstadoBatalla estado = batalla.getEstado();
        bloquearAcciones();
        btnResultado.setVisible(false);

        if (estado == EstadoBatalla.ESPERANDO_ACCION) {
            lblEstado.setText("Turno de " + batalla.getNombreEntrenador() + ": elige una acción");
            btnAtacar.setEnabled(batalla.esAccionValida(AccionBatalla.ATACAR));
            btnEspecial.setEnabled(batalla.esAccionValida(AccionBatalla.ESPECIAL));
            btnRecuperar.setEnabled(batalla.esAccionValida(AccionBatalla.RECUPERAR));
            btnCambiar.setEnabled(batalla.esAccionValida(AccionBatalla.CAMBIAR));
        } else if (estado == EstadoBatalla.ELEGIR_REEMPLAZO) {
            lblEstado.setText(batalla.getNombreEntrenador() + ": elige un reemplazo");
            btnCambiar.setText("Elegir reemplazo");
            btnCambiar.setEnabled(true);
        } else if (batalla.estaTerminada()) {
            lblEstado.setText("¡La batalla terminó!");
            btnResultado.setVisible(true);
        }
    }

    private void bloquearAcciones() {
        btnAtacar.setEnabled(false);
        btnEspecial.setEnabled(false);
        btnRecuperar.setEnabled(false);
        btnCambiar.setEnabled(false);
    }

    private void agregarMensaje(String texto) {
        txtMensajes.append(texto);
        txtMensajes.setCaretPosition(txtMensajes.getDocument().getLength());
    }
}