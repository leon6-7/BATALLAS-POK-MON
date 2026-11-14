package semana5pokemon.Vista;

import Excepciones.AccionInvalidaException;
import modelo.Pokemon;
import semana5pokemon.Control.AccionBatalla;
import semana5pokemon.Control.Batalla;
import semana5pokemon.Control.EstadoBatalla;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class PanelBatalla extends JPanel {
    private static final long serialVersionUID = 1L;
    private final VentanaPrincipal ventana;
    private Batalla batalla;
    private final JLabel lblRonda = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblEstado = new JLabel("", SwingConstants.CENTER);
    private final PanelPokemonActivo panelJugador = new PanelPokemonActivo();
    private final PanelPokemonActivo panelRival = new PanelPokemonActivo();
    private final JTextArea mensajes = new JTextArea();
    private final JComboBox<OpcionPokemon> comboCambio = new JComboBox<>();
    private final JButton btnAtacar = new JButton("Atacar");
    private final JButton btnEspecial = new JButton("Habilidad especial");
    private final JButton btnRecuperar = new JButton("Recuperar");
    private final JButton btnCambiar = new JButton("Cambiar Pokémon");
    private final JButton btnReemplazar = new JButton("Confirmar reemplazo");

    public PanelBatalla(VentanaPrincipal ventana) {
        this.ventana = ventana;
        construir();
        registrarEventos();
    }

    private void construir() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 4));
        JLabel titulo = new JLabel("BATALLA POKÉMON", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 25f));
        lblRonda.setFont(lblRonda.getFont().deriveFont(Font.BOLD, 16f));
        encabezado.add(titulo);
        encabezado.add(lblRonda);
        add(encabezado, BorderLayout.NORTH);

        JPanel bandos = new JPanel(new GridLayout(1, 2, 14, 0));
        bandos.add(panelJugador);
        bandos.add(panelRival);
        add(bandos, BorderLayout.CENTER);

        mensajes.setEditable(false);
        mensajes.setLineWrap(true);
        mensajes.setWrapStyleWord(true);
        mensajes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(mensajes);
        scroll.setBorder(BorderFactory.createTitledBorder("Registro de la batalla"));
        scroll.setPreferredSize(new java.awt.Dimension(100, 190));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        acciones.add(btnAtacar);
        acciones.add(btnEspecial);
        acciones.add(btnRecuperar);
        acciones.add(comboCambio);
        acciones.add(btnCambiar);
        acciones.add(btnReemplazar);
        JPanel sur = new JPanel(new BorderLayout(5, 5));
        sur.add(lblEstado, BorderLayout.NORTH);
        sur.add(scroll, BorderLayout.CENTER);
        sur.add(acciones, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);
    }

    private void registrarEventos() {
        btnAtacar.addActionListener(e -> ejecutar(AccionBatalla.ATACAR));
        btnEspecial.addActionListener(e -> ejecutar(AccionBatalla.HABILIDAD_ESPECIAL));
        btnRecuperar.addActionListener(e -> ejecutar(AccionBatalla.RECUPERAR));
        btnCambiar.addActionListener(e -> ejecutar(AccionBatalla.CAMBIAR));
        btnReemplazar.addActionListener(e -> confirmarReemplazo());
    }

    public void iniciar(Batalla batalla) {
        this.batalla = batalla;
        mensajes.setText("");
        actualizar();
    }

    private void ejecutar(AccionBatalla accion) {
        if (batalla == null) return;
        int indice = accion == AccionBatalla.CAMBIAR ? indiceSeleccionado() : -1;
        try {
            batalla.ejecutarRonda(accion, indice);
            actualizar();
        } catch (AccionInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acción inválida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int indiceSeleccionado() {
        OpcionPokemon seleccionado = (OpcionPokemon) comboCambio.getSelectedItem();
        return seleccionado == null ? -1 : seleccionado.indice;
    }

    private void confirmarReemplazo() {
        int indice = indiceSeleccionado();
        try {
            batalla.elegirReemplazo(indice);
            actualizar();
        } catch (AccionInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Reemplazo inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizar() {
        if (batalla == null) return;
        lblRonda.setText("Ronda " + batalla.getRonda() + " / " + Batalla.LIMITE_RONDAS);
        lblEstado.setText(batalla.getDescripcionTurno());
        panelJugador.setEntrenador(batalla.getNombreEntrenador());
        panelRival.setEntrenador(batalla.getNombreRival());
        panelJugador.mostrar(batalla.getPokemonJugador());
        panelRival.mostrar(batalla.getPokemonRival());
        panelJugador.setRestantes(contarEnPie(batalla.getEquipoJugador()), batalla.getEquipoJugador().size());
        panelRival.setRestantes(contarEnPie(batalla.getEquipoRival()), batalla.getEquipoRival().size());
        for (String mensaje : batalla.consumirMensajesNuevos()) mensajes.append(mensaje + "\n");
        mensajes.setCaretPosition(mensajes.getDocument().getLength());

        List<AccionBatalla> validas = batalla.getAccionesValidas();
        btnAtacar.setEnabled(validas.contains(AccionBatalla.ATACAR));
        btnEspecial.setEnabled(validas.contains(AccionBatalla.HABILIDAD_ESPECIAL));
        btnRecuperar.setEnabled(validas.contains(AccionBatalla.RECUPERAR));
        btnCambiar.setEnabled(validas.contains(AccionBatalla.CAMBIAR));
        boolean reemplazo = batalla.getEstado() == EstadoBatalla.ELEGIR_REEMPLAZO;
        cargarCambios(reemplazo);
        btnReemplazar.setVisible(reemplazo);
        btnCambiar.setVisible(!reemplazo);
        comboCambio.setEnabled(reemplazo || validas.contains(AccionBatalla.CAMBIAR));
        if (batalla.haTerminado()) {
            btnAtacar.setEnabled(false);
            btnEspecial.setEnabled(false);
            btnRecuperar.setEnabled(false);
            btnCambiar.setEnabled(false);
            btnReemplazar.setEnabled(false);
            ventana.mostrarResultado(batalla.getResultado());
        }
    }

    private void cargarCambios(boolean reemplazo) {
        DefaultComboBoxModel<OpcionPokemon> modelo = new DefaultComboBoxModel<>();
        List<Integer> indices = batalla.getIndicesCambioDisponibles();
        for (Integer indice : indices) {
            Pokemon pokemon = batalla.getEquipoJugador().get(indice);
            modelo.addElement(new OpcionPokemon(indice, pokemon));
        }
        comboCambio.setModel(modelo);
        if (reemplazo) {
            comboCambio.setToolTipText("Selecciona el Pokémon que entrará en combate");
        }
    }

    private int contarEnPie(List<Pokemon> equipo) {
        int total = 0;
        for (Pokemon pokemon : equipo) if (!pokemon.estaDebilitado()) total++;
        return total;
    }

    private static class OpcionPokemon {
        private final int indice;
        private final String texto;
        OpcionPokemon(int indice, Pokemon pokemon) {
            this.indice = indice;
            this.texto = pokemon.getNombre() + " - " + pokemon.getPsActual() + "/" + pokemon.getPsMax() + " PS";
        }
        @Override public String toString() { return texto; }
    }
}
