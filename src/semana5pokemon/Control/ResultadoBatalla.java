/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semana5pokemon.Control;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Abril
 */

public class ResultadoBatalla {

    private final String nombreEntrenador;
    private final String nombreRival;
    private final EstadoBatalla estadoFinal;
    private final int rondas;
    private final int debilitadosJugador;
    private final int debilitadosRival;
    private final List<String> enPieJugador;
    private final List<String> enPieRival;

    ResultadoBatalla(String nombreEntrenador, String nombreRival, EstadoBatalla estadoFinal,
                     int rondas, int debilitadosJugador, int debilitadosRival,
                     List<String> enPieJugador, List<String> enPieRival) {
        this.nombreEntrenador = nombreEntrenador;
        this.nombreRival = nombreRival;
        this.estadoFinal = estadoFinal;
        this.rondas = rondas;
        this.debilitadosJugador = debilitadosJugador;
        this.debilitadosRival = debilitadosRival;
        this.enPieJugador = Collections.unmodifiableList(new ArrayList<>(enPieJugador));
        this.enPieRival = Collections.unmodifiableList(new ArrayList<>(enPieRival));
    }

    public String getNombreEntrenador() {
        return nombreEntrenador;
    }

    public String getNombreRival() {
        return nombreRival;
    }

    public EstadoBatalla getEstadoFinal() {
        return estadoFinal;
    }

    public boolean esVictoria() {
        return estadoFinal == EstadoBatalla.VICTORIA;
    }

    public int getRondas() {
        return rondas;
    }

    public int getDebilitadosJugador() {
        return debilitadosJugador;
    }

    public int getDebilitadosRival() {
        return debilitadosRival;
    }

    public List<String> getEnPieJugador() {
        return enPieJugador;
    }

    public List<String> getEnPieRival() {
        return enPieRival;
    }

    public String getMensajeFinal() {
        return switch (estadoFinal) {
            case VICTORIA -> "¡Victoria! " + nombreEntrenador + " derrotó a " + nombreRival + ".";
            case DERROTA -> "Derrota. " + nombreEntrenador + " fue vencido por " + nombreRival + ".";
            case EMPATE -> "Empate entre " + nombreEntrenador + " y " + nombreRival + ".";
            default -> "La batalla aún no ha terminado.";
        };
    }
}
