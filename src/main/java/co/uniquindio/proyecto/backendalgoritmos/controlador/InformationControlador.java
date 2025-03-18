package co.uniquindio.proyecto.backendalgoritmos.controlador;

import co.uniquindio.proyecto.backendalgoritmos.dto.MensajeDTO;
import co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces.InformationServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InformationControlador {

    private final InformationServicio informationServicio;

    @GetMapping("/getInformation")
    public ResponseEntity<MensajeDTO<List<Object>>> getInformation() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.getInformation()));
    }
}
