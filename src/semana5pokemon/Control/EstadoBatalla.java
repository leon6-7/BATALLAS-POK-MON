package semana5pokemon.Control;

public enum EstadoBatalla {
    PREPARACION("Preparación", false),
    ESPERANDO_ACCION("Esperando acción", false),
    RESOLVIENDO_RONDA("Resolviendo ronda", false),
    ELEGIR_REEMPLAZO("Elegir reemplazo", false),
    VICTORIA("Victoria", true),
    DERROTA("Derrota", true),
    EMPATE("Empate", true);

    private final String texto;
    private final boolean terminado;

    EstadoBatalla(String texto, boolean terminado) {
        this.texto = texto;
        this.terminado = terminado;
    }

    public String getTexto() {
        return texto;
    }

    public boolean estaTerminado() {
        return terminado;
    }
}
