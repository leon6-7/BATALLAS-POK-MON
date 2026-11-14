package semana5pokemon.Control;

public enum EstadoBatalla {
    PREPARACION,
    ESPERANDO_ACCION,
    RESOLVIENDO_RONDA,
    ELEGIR_REEMPLAZO,
    VICTORIA,
    DERROTA,
    EMPATE;

   
    public boolean esFinal() {
        return this == VICTORIA || this == DERROTA || this == EMPATE;
    }
}