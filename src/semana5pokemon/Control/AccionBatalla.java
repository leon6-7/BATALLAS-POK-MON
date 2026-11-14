/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semana5pokemon.Control;

/**
 *
 * @author Abril
 */
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