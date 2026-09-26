public final class Participante {
    private final String nombre;
    private final int edad;

    public Participante(String nombre, int edad) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre obligatorio");
        }

        if (edad <= 0) {
            throw new IllegalArgumentException("Edad debe ser positiva");
        }

        this.nombre = nombre.trim();
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    @Override
    public String toString() {
        return nombre + " - " + edad + " años";
    }
}