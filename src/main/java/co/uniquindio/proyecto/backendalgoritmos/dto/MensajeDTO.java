package co.uniquindio.proyecto.backendalgoritmos.dto;

public record MensajeDTO<T>(
        boolean error,
        T respuesta
) {
}
