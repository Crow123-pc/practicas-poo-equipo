package bienestar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** CSV de una línea por registro, con punto y coma, comillas y UTF-8. */
public final class ArchivoCsv {
    private ArchivoCsv() { }
    public static String linea(String... campos) {
        return Arrays.stream(campos).map(v -> "\"" + v.replace("\"", "\"\"") + "\"")
                .collect(java.util.stream.Collectors.joining(";"));
    }
    public static List<String> separar(String linea) throws IOException {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean comillas = false, cerrada = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (comillas) {
                if (c == '"') {
                    if (i + 1 < linea.length() && linea.charAt(i + 1) == '"') { actual.append('"'); i++; }
                    else { comillas = false; cerrada = true; }
                } else actual.append(c);
            } else if (c == ';') {
                campos.add(actual.toString()); actual.setLength(0); cerrada = false;
            } else if (c == '"' && actual.length() == 0 && !cerrada) {
                comillas = true;
            } else if (cerrada || c == '"') {
                throw new IOException("CSV inválido: comillas o separador incorrectos");
            } else actual.append(c);
        }
        if (comillas) throw new IOException("CSV inválido: comillas sin cerrar");
        campos.add(actual.toString());
        return campos;
    }
    public static List<String> leer(Path ruta) throws IOException {
        return Files.readAllLines(ruta, StandardCharsets.UTF_8);
    }
    public static void guardar(Path ruta, List<String> lineas) throws IOException {
        Path destino = ruta.toAbsolutePath();
        Files.createDirectories(destino.getParent());
        Path temporal = Files.createTempFile(destino.getParent(), "bienestar-", ".tmp");
        try {
            Files.write(temporal, lineas, StandardCharsets.UTF_8);
            try { Files.move(temporal, destino, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING); }
            catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally { Files.deleteIfExists(temporal); }
    }
}
