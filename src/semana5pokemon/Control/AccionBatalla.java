package semana5pokemon.Control;

public enum AccionBatalla {
    ATACAR("Atacar"),
    ESPECIAL("Habilidad especial"),
    RECUPERAR("Recuperar"),
    CAMBIAR("Cambiar de Pokémon");

    private final String texto;

    AccionBatalla(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    
    public boolean tienePrioridad() {
        return this == CAMBIAR;
    }

    @Override
    public String toString() {
        return texto;
    }
}
