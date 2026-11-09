package bienestar;

import java.time.LocalDate;

public record Taller(String codigo, String titulo, LocalDate fecha, int minutos, int cupo, String expositor) {
    public Taller(String codigo, String titulo, LocalDate fecha, int minutos, int cupo) {
        this(codigo, titulo, fecha, minutos, cupo, "");
    }
    public Taller {
        codigo = Validador.codigo(codigo);
        titulo = Validador.texto(titulo, "Título", 150);
        if (fecha == null) throw new IllegalArgumentException("Fecha obligatoria");
        if (minutos < 1 || minutos > 1440) throw new IllegalArgumentException("Duración: 1 a 1440 minutos");
        if (cupo < 1 || cupo > 10000) throw new IllegalArgumentException("Cupo: 1 a 10000 participantes");
        expositor = expositor == null || expositor.isBlank() ? "" : Validador.texto(expositor, "Expositor", 100);
    }
    @Override public String toString() {
        return codigo + " | " + titulo + " | " + fecha + " | " + minutos + " min | cupo " + cupo
                + (expositor.isEmpty() ? "" : " | expositor: " + expositor);
    }
}
