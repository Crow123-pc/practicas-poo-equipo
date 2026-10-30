package bienestar;

final class Validador {
    private Validador() { }
    static String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException(campo + " obligatorio");
        String limpio = valor.strip();
        if (limpio.length() > maximo || limpio.chars().anyMatch(Character::isISOControl))
            throw new IllegalArgumentException(campo + " inválido o demasiado largo");
        return limpio;
    }
    static String codigo(String valor) {
        String codigo = texto(valor, "Código", 20).toUpperCase(java.util.Locale.ROOT);
        if (!codigo.matches("[A-Z0-9][A-Z0-9_-]{0,19}"))
            throw new IllegalArgumentException("Código: use letras, números, guion o guion bajo");
        return codigo;
    }
}
