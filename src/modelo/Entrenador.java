
package modelo;

import Excepciones.NombreInvalidoException;
import java.io.Serializable;

public class Entrenador implements Serializable {

    public static final int MIN_CARACTERES = 3;
    public static final int MAX_CARACTERES = 10;

    private String nombre;
    private Equipo equipo;

    public Entrenador(String nombre) throws NombreInvalidoException {
        this.nombre = validarNombre(nombre);
        this.equipo = new Equipo();
    }

   // nombre
    public static String validarNombre(String nombre) throws NombreInvalidoException {
        if (nombre == null) {
            throw new NombreInvalidoException("Escribe el nombre del entrenador.");
        }
        String limpio = nombre.trim();  

        if (limpio.isEmpty()) {
            throw new NombreInvalidoException("El nombre no puede estar vacío ni tener solo espacios.");
        }
        if (limpio.length() < MIN_CARACTERES || limpio.length() > MAX_CARACTERES) {
            throw new NombreInvalidoException("El nombre debe tener entre " + MIN_CARACTERES
                    + " y " + MAX_CARACTERES + " caracteres.");
        }
        for (int i = 0; i < limpio.length(); i++) {
            char c = limpio.charAt(i);
            if (Character.isLetterOrDigit(c) || c == ' ') {
                continue;                
            }
            throw new NombreInvalidoException("El nombre solo puede tener letras, números y espacios");
        }
        return limpio;
    }

    public String getNombre() {
        return nombre;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}