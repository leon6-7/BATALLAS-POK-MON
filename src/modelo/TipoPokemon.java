
package modelo;


public enum TipoPokemon {
    FUEGO("Fuego"),
    AGUA("Agua"),
    PLANTA("Planta"),
    ELECTRICO("Eléctrico");

    private static final double[][] TABLA = {
        //  Fuego  Agua  Planta  Eléctrico
        {   0.5,   0.5,  2.0,    1.0 },  
        {   2.0,   0.5,  0.5,    1.0 },  
        {   0.5,   2.0,  0.5,    1.0 },  
        {   1.0,   2.0,  0.5,    0.5 }   
    };

    private final String nombre;

    TipoPokemon(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public double getMultiplicador(TipoPokemon defensor) {
        return TABLA[this.ordinal()][defensor.ordinal()];
    }
        
    public static String mensajeEfectividad(double multiplicador) {
    if (multiplicador == 2.0) {
        return "¡Es muy eficaz!";
    } else if (multiplicador == 0.5) {
        return "No es muy eficaz…";
    }
    return "";
}

    @Override
    public String toString() {
        return nombre;
    }
}
