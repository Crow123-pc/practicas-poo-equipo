package bienestar;

/** Datos mínimos del participante. No requiere DNI, teléfono ni correo. */
public record Participante(String codigo, String nombre, int edad, String grado) {
    public Participante {
        codigo = Validador.codigo(codigo);
        nombre = Validador.texto(nombre, "Nombre", 100);
        grado = Validador.texto(grado, "Grado", 60);
        if (edad < 1 || edad > 120) throw new IllegalArgumentException("Edad fuera de rango (1 a 120)");
    }
    @Override public String toString() {
        return codigo + " | " + nombre + " | " + edad + " años | " + grado;
    }
}
