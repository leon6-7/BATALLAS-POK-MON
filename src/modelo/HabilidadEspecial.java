
package modelo;

import Excepciones.AccionInvalidaException;

public class HabilidadEspecial implements Habilidad {
    public static final int USOS_MAXIMOS = 3;
  // condicionales para los pokemons
    @Override
    public String ejecutar(Pokemon usuario, Pokemon objetivo) throws AccionInvalidaException {
        if (usuario.estaDebilitado()) {
            throw new AccionInvalidaException(usuario.getNombre() + " está debilitado y no puede actuar.");
        }
        if (usuario.getUsosEspecial() <= 0) {
            throw new AccionInvalidaException(usuario.getNombre() + " ya no tiene usos de "
                    + usuario.getNombreEspecial() + ".");
        }
        usuario.consumirUsoEspecial();
        
        return usuario.getNombre() + " usó " + usuario.getNombreEspecial() + ". "
                + usuario.efectoEspecial(objetivo);
    }

    @Override
    public String obtenerNombre() {
        return "Habilidad especial";
    }
}