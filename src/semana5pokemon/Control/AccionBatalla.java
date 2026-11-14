package semana5pokemon.Control;

public enum AccionBatalla {
    ATACAR("Atacar", false),
    HABILIDAD_ESPECIAL("Habilidad especial", false),
    RECUPERAR("Recuperar", false),
    CAMBIAR("Cambiar de Pokémon", true);

    private final String texto;
    private final boolean prioritaria;

    AccionBatalla(String texto, boolean prioritaria) {
        this.texto = texto;
        this.prioritaria = prioritaria;
    }

    public String getTexto() {
        return texto;
    }

    public boolean esPrioritaria() {
        return prioritaria;
    }
}
