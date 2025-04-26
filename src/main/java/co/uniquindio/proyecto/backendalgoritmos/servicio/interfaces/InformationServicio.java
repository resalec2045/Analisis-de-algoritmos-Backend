package co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces;

import java.util.List;
import java.util.Map;

public interface InformationServicio {

    List<Object> getInformation() throws Exception;

    List<Object> getInformationAbstract() throws Exception;

    Map<String, Object> preprocesamientoTexto() throws Exception;

}
