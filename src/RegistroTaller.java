import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RegistroTaller {
    private final List<Participante> participantes = new ArrayList<>();

    // Sobrecarga: registra usando nombre y edad.
    public void registrar(String nombre, int edad) {
        registrar(new Participante(nombre, edad));
    }

    // Sobrecarga: registra usando un objeto Participante.
    public void registrar(Participante participante) {
        if (participante == null) {
            throw new IllegalArgumentException("Participante nulo");
        }

        participantes.add(participante);
    }

    public List<Participante> listar() {
        return Collections.unmodifiableList(
            new ArrayList<>(participantes)
        );
    }

    public int total() {
        return participantes.size();
    }
}