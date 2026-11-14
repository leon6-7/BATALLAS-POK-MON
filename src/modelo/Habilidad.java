package modelo;
import Excepciones.AccionInvalidaException ;

import java.io.Serializable;

public interface Habilidad extends Serializable {
    String ejecutar(Pokemon usuario, Pokemon objetivo) throws AccionInvalidaException;
    String obtenerNombre();
}
