
package modelo;

import Excepciones.BatallaException;
import Excepciones.EquipoNoGuardadoException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GestorEquipo {

    private byte[] copiaGuardada;   

   // boton guardar
    public void guardar(Entrenador entrenador) throws BatallaException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream salida = new ObjectOutputStream(bytes)) {
            salida.writeObject(entrenador);
            salida.flush();
            copiaGuardada = bytes.toByteArray();
        } catch (IOException e) {
            throw new BatallaException("No se pudo guardar el equipo: " + e.getMessage());
        }
    }

    // boton Restaurar
    public Entrenador restaurar() throws BatallaException {
        if (copiaGuardada == null) {
            throw new EquipoNoGuardadoException("Primero debes guardar un equipo");
        }
        try (ByteArrayInputStream bytes = new ByteArrayInputStream(copiaGuardada);
             ObjectInputStream entrada = new ObjectInputStream(bytes)) {
            return (Entrenador) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new BatallaException("No se pudo restaurar el equipo: " + e.getMessage());
        }
    }

    public boolean hayEquipoGuardado() {
        if (copiaGuardada == null) {
            return false;
        }
        return true;
    }
}