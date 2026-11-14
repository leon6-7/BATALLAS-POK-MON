package semana5pokemon.Control;

import java.util.ArrayList;
import modelo.Equipo;
import modelo.Pokemon;

public class ResultadoBatalla {

    private final EstadoBatalla estadoFinal;
    private final String nombreEntrenador;
    private final int rondas;
    private final int debilitadosJugador;
    private final int debilitadosRival;
    private final ArrayList<String> enPieJugador;
    private final ArrayList<String> enPieRival;
    private final int psRestantesJugador;
    private final int psRestantesRival;

    public ResultadoBatalla(EstadoBatalla estadoFinal, String nombreEntrenador, int rondas,
                            Equipo jugador, Equipo rival) {
        this.estadoFinal = estadoFinal;
        this.nombreEntrenador = nombreEntrenador;
        this.rondas = rondas;
        this.debilitadosJugador = jugador.contarDebilitados();
        this.debilitadosRival = rival.contarDebilitados();
        this.enPieJugador = nombresEnPie(jugador);
        this.enPieRival = nombresEnPie(rival);
        this.psRestantesJugador = jugador.sumarPsRestantes();
        this.psRestantesRival = rival.sumarPsRestantes();
    }

    private ArrayList<String> nombresEnPie(Equipo equipo) {
        ArrayList<String> nombres = new ArrayList<>();
        for (Pokemon p : equipo.getIntegrantes()) {
            if (p.estaDebilitado()) {
                continue;
            }
            nombres.add(p.getNombre() + " (" + p.getPsActual() + "/" + p.getPsMax() + " PS)");
        }
        return nombres;
    }

    /** Título grande de la pantalla de resultado, con el nombre del entrenador. */
    public String getTitulo() {
        if (estadoFinal == EstadoBatalla.VICTORIA) {
            return "¡Victoria, " + nombreEntrenador + "!";
        }
        if (estadoFinal == EstadoBatalla.DERROTA) {
            return "Derrota, " + nombreEntrenador + "...";
        }
        return "¡Empate, " + nombreEntrenador + "!";
    }

    /** Resumen completo para el JTextArea de PanelResultado. */
    public String getResumen() {
        String texto = "Rondas jugadas: " + rondas + "\n\n";
        texto += "Pokémon debilitados\n";
        texto += "  " + nombreEntrenador + ": " + debilitadosJugador + "\n";
        texto += "  " + Batalla.NOMBRE_RIVAL + ": " + debilitadosRival + "\n\n";
        texto += "Quedaron en pie (" + nombreEntrenador + ")\n" + listar(enPieJugador) + "\n";
        texto += "Quedaron en pie (" + Batalla.NOMBRE_RIVAL + ")\n" + listar(enPieRival);
        if (rondas >= Batalla.LIMITE_RONDAS) {
            texto += "\nSe alcanzó el límite de " + Batalla.LIMITE_RONDAS + " rondas.\n";
            texto += "PS restantes: " + nombreEntrenador + " " + psRestantesJugador
                    + "  -  " + Batalla.NOMBRE_RIVAL + " " + psRestantesRival + "\n";
        }
        return texto;
    }

    private String listar(ArrayList<String> nombres) {
        if (nombres.isEmpty()) {
            return "  (ninguno)\n";
        }
        String texto = "";
        for (String n : nombres) {
            texto += "  - " + n + "\n";
        }
        return texto;
    }

    public EstadoBatalla getEstadoFinal() { return estadoFinal; }
    public String getNombreEntrenador() { return nombreEntrenador; }
    public int getRondas() { return rondas; }
    public int getDebilitadosJugador() { return debilitadosJugador; }
    public int getDebilitadosRival() { return debilitadosRival; }
    public ArrayList<String> getEnPieJugador() { return new ArrayList<>(enPieJugador); }
    public ArrayList<String> getEnPieRival() { return new ArrayList<>(enPieRival); }
    public int getPsRestantesJugador() { return psRestantesJugador; }
    public int getPsRestantesRival() { return psRestantesRival; }
}