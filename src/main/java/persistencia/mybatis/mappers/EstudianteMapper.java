/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia.mybatis.mappers;

import entidades.Estudiante;
import entidades.SolicitudCancelacion;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author BOOMECI
 */
public interface EstudianteMapper {    
    
    public Estudiante loadEstudianteById(@Param("ide")int id);
    
    public List<Estudiante> consultarEstudiantes();
    
    public void insertarSolicitud(@Param("idEst") int id, @Param("sol") SolicitudCancelacion sol);
    
    //public void insertarEstudiante(@Param("est")Estudiante e);
    
    //public void actualizarEstudiante(@Param("uest") Estudiante e );
}
