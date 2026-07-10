package SystemITR.JosueGuinea1A.Departamentos.Controller;

import SystemITR.JosueGuinea1A.Departamentos.DTO.DepartamentosDTO;
import SystemITR.JosueGuinea1A.Departamentos.Service.DepartamentosService;
import SystemITR.JosueGuinea1A.Response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/departamento")
public class DepartamentosController {

    @Autowired
    private DepartamentosService service;
    public DepartamentosController(DepartamentosService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartamentosDTO>> nuevoDepartamento(@Valid @RequestBody DepartamentosDTO json){
        try{
            //Creamos un objeto DTO porque el service.insertarDatos retornará un objeto de tipo DepartamentosDTO
            DepartamentosDTO objDTO = service.insertarDatos(json);
            if (objDTO == null){
                log.info("Intento de insercion fallido: " + objDTO);
                ApiResponse respuesta = new ApiResponse(false,"No se pudo completar el proceso de inserción", json);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
            log.info("Nuevo datos de departamento ingresados: " + objDTO);
            ApiResponse respuesta = new ApiResponse(true, "Dato ingresado exitosamente", objDTO);
            return ResponseEntity.ok(respuesta);
        }catch (Exception e){
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuesta = new ApiResponse<>(false, "Error critico"+ e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartamentosDTO>>> obtenerDepartamentos(){
        try{
            List<DepartamentosDTO> listaDTO = service.listarTodos();
            if (listaDTO != null){
                ApiResponse<List<DepartamentosDTO>> respuestaExitosa = new ApiResponse<>(true,"Proceso completado", listaDTO);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<List<DepartamentosDTO>> respuestaNoData = new ApiResponse<>(true,"No hey datos por mostrar",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoData);
        }catch (Exception e){
            log.info("No hay departamentos registrados");
            e.printStackTrace();
            ApiResponse<List<DepartamentosDTO>> respuestaError = new ApiResponse<>(false,"No se pudo completar la busqueda del ID: ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentosDTO>> obtenerDepartamentoPorId(@PathVariable long id){
        try{
            DepartamentosDTO dto = service.buscarDepartamento(id);
            if (dto != null){
                //armar la respuesta
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(true,"Dato encontrado", dto);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<DepartamentosDTO> noEncontrado = new ApiResponse<>(false,"Datos no encontrados",null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        }catch (Exception e){
            log.info("No hay departamentos registrados");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false,"No se pudo completar la busqueda del ID: " + id, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentosDTO>> eliminarDepartamento(@PathVariable Long id){
        try {
            boolean resouesta = service.eliminarInfo(id);
            if (resouesta) {
                ApiResponse<DepartamentosDTO> respuestaExitosamento = new ApiResponse<>(true, "dato con ID: " + id + " eliminado", null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaExitosamento);
            }
            ApiResponse<DepartamentosDTO> respuestaNoRealizado = new ApiResponse<>(
                    false,
                    "El proceso de eliminacion no se pudo completar",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoRealizado);
        } catch (Exception e) {
            log.info("Error critico, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false, "Error Critico, Consulte con el administrador para solucionar el problema", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentosDTO>> actualizarDepartamentto(
      @PathVariable Long id,
      @Valid @RequestBody DepartamentosDTO dto
    ){
        try{
            DepartamentosDTO objdto = service.actualizarinfo(id, dto);
            if (objdto == null){
                ApiResponse<DepartamentosDTO> respuestaNoRealizado = new ApiResponse<>(
                        false,
                        "No se pudo completar el proceso de actualizar",
                        null
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaNoRealizado);
            }
            ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(
                    true,
                    "Proceso completado",
                    objdto
            );
            return ResponseEntity.ok(respuestaExitosa);
        }catch (Exception e){
            log.info("Error critico, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false, "Error Critico, Consulte con el administrador para solucionar el problema", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
}
