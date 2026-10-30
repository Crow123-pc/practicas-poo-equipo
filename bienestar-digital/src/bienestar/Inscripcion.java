package bienestar;

public record Inscripcion(String taller, String participante, Asistencia asistencia, Integer nota) {
    public enum Asistencia { PENDIENTE, PRESENTE, AUSENTE }
    public Inscripcion {
        taller = Validador.codigo(taller);
        participante = Validador.codigo(participante);
        if (asistencia == null) throw new IllegalArgumentException("Asistencia obligatoria");
        if (nota != null && (nota < 0 || nota > 20)) throw new IllegalArgumentException("Nota: 0 a 20");
    }
}
