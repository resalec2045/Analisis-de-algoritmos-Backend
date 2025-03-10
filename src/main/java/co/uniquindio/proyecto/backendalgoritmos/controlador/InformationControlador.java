package co.uniquindio.proyecto.backendalgoritmos.controlador;

import co.uniquindio.proyecto.backendalgoritmos.dto.MensajeDTO;
import co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces.InformationServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InformationControlador {

    private final InformationServicio informationServicio;

    @GetMapping("/getInformation")
    public ResponseEntity<MensajeDTO<String>> getInformation() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.getInformation()));
    }
}
