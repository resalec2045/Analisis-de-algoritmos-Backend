package co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces;

import java.util.List;
import java.util.Map;

public interface InformationServicio {

    List<Object> getInformation() throws Exception;

    List<Object> getInformationAbstract() throws Exception;

    Map<String, Object> preprocesamientoTextoAgnes() throws Exception;

    Map<String, Object> preprocesamientoTextoDiana() throws Exception;

    String preprocesamientoDescriptionUtiliced() throws Exception;

    List<Object> requerimiento2() throws Exception;

}
