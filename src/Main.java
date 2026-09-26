public final class Main {
    public static void main(String[] args) {
        RegistroTaller registro = new RegistroTaller();

        registro.registrar("Ana", 15);
        registro.registrar(new Participante("Luis", 16));

        System.out.println(
            "Registrados inicialmente: " + registro.total()
        );

        intentar(() -> registro.registrar("", 15));
        intentar(() -> registro.registrar("Eva", 0));
        intentar(() -> registro.registrar((Participante) null));

        for (Participante participante : registro.listar()) {
            System.out.println(participante);
        }

        System.out.println("Total final: " + registro.total());
    }

    private static void intentar(Runnable operacion) {
        try {
            operacion.run();
        } catch (IllegalArgumentException error) {
            System.out.println(
                "Error controlado: " + error.getMessage()
            );
        }
    }
}