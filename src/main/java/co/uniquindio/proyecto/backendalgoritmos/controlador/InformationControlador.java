package co.uniquindio.proyecto.backendalgoritmos.controlador;

import co.uniquindio.proyecto.backendalgoritmos.dto.MensajeDTO;
import co.uniquindio.proyecto.backendalgoritmos.models.WordCloudItem;
import co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces.InformationServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/getInformationAbstract")
    public ResponseEntity<MensajeDTO<List<Object>>> getInformationAbstract() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.getInformationAbstract()));
    }

    @GetMapping("/preprocesamientoTextoAgnes")
    public ResponseEntity<MensajeDTO<Map<String, Object>>> preprocesamientoTextoAgnes() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.preprocesamientoTextoAgnes()));
    }

    @GetMapping("/preprocesamientoTextoDiana")
    public ResponseEntity<MensajeDTO<Map<String, Object>>> preprocesamientoTextoDiana() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.preprocesamientoTextoDiana()));
    }

    @GetMapping("/preprocesamientoDescriptionUtiliced")
    public ResponseEntity<MensajeDTO<String>> preprocesamientoDescriptionUtiliced() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.preprocesamientoDescriptionUtiliced()));
    }

//    ! Requerimiento 2

    @GetMapping("/requerimiento2")
    public ResponseEntity<MensajeDTO<List<Object>>> requerimiento2() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.requerimiento2()));
    }

//    ! Requerimiento 3

    @GetMapping("/requerimiento3_1")
    public ResponseEntity<MensajeDTO<Map<String, Object>>> requerimiento3_1() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.requerimiento3_1()));
    }

    @GetMapping("/requerimiento3")
    public ResponseEntity<MensajeDTO<List<WordCloudItem>>> requerimiento3() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.requerimiento3()));
    }

    @GetMapping("/requerimiento3PorCategoria")
    public ResponseEntity<MensajeDTO<Map<String, List<WordCloudItem>>>> requerimiento3PorCategoria() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.requerimiento3PorCategoria()));
    }

    @GetMapping("/requerimiento3_2")
    public ResponseEntity<MensajeDTO<Map<String, Object>>> requerimiento3_2() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.requerimiento3_2()));
    }

//    ! Requerimiento 5

    @GetMapping("/requerimiento5")
    public ResponseEntity<MensajeDTO<Map<String, Object>>> requerimiento5() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, informationServicio.requerimiento5()));
    }

}

