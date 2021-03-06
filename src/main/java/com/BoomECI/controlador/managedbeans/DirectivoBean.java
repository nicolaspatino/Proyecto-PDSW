/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BoomECI.controlador.managedbeans;

import com.BoomECI.entidades.Consejero;
import com.BoomECI.entidades.Estudiante;
import com.BoomECI.entidades.Grafo;
import com.BoomECI.entidades.Materia;
import com.BoomECI.entidades.PlanDeEstudios;
import com.BoomECI.entidades.SolicitudCancelacion;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.BoomECI.logica.services.ExcepcionServiciosCancelaciones;
import com.BoomECI.logica.services.ParserGrafo;
import com.BoomECI.logica.services.ServiciosCancelaciones;
import com.BoomECI.logica.services.ServiciosCancelacionesFactory;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;

/**
 *
 * @author BoomEci
 */

@ManagedBean(name="beanSolicitudDirectivo")
@SessionScoped
public class DirectivoBean implements Serializable{
    
    
    
    private final ServiciosCancelaciones servCanc = ServiciosCancelacionesFactory.getInstance().getServiciosCancelaciones();
    private int creditosActuales;
    private Grafo grafoPlanDeEstudios;
    private DefaultDiagramModel model;
    private List<List<String>> configuracion;
    private Consejero directivoActual;
    private int carrera;
    private List<SolicitudCancelacion> solicitudesNoFinalizadas;
    private List<SolicitudCancelacion> solicitudesFinalizadas;
    private Estudiante estudianteAconsejado;
    private int carneDirectivo;
    private boolean sistemas = true;
    
    public DirectivoBean() throws ExcepcionServiciosCancelaciones{
        setProperties();
    }
    
    
    
    public int getCreditosActuales() {
        return creditosActuales;
    }

    public void setCreditosActuales(int creditosActuales) {
        this.creditosActuales = creditosActuales;
    }

    public List<SolicitudCancelacion> getSolicitudesNoFinalizadas() {
        return solicitudesNoFinalizadas;
    }

    public void setSolicitudesNoFinalizadas(List<SolicitudCancelacion> solicitudesNoFinalizadas) {
        this.solicitudesNoFinalizadas = solicitudesNoFinalizadas;
    }

    public List<SolicitudCancelacion> getSolicitudesFinalizadas() {
        return solicitudesFinalizadas;
    }

    public void setSolicitudesFinalizadas(List<SolicitudCancelacion> solicitudesFinalizadas) {
        this.solicitudesFinalizadas = solicitudesFinalizadas;
    }

    public Estudiante getEstudianteAconsejado() {
        return estudianteAconsejado;
    }

    public void setEstudianteAconsejado(Estudiante estudianteAconsejado) {
        this.estudianteAconsejado = estudianteAconsejado;
    }
    
    

    public Grafo getGrafoPlanDeEstudios() {
        return grafoPlanDeEstudios;
    }

    public void setGrafoPlanDeEstudios(Grafo grafoPlanDeEstudios) {
        this.grafoPlanDeEstudios = grafoPlanDeEstudios;
    }
    
    public Consejero getDirectivoActual() throws ExcepcionServiciosCancelaciones{
        return directivoActual;
    }
    
    public void setDirectivoActual(Consejero directivoActual){
        this.directivoActual = directivoActual;
    }

    public int getCarneDirectivo() {
        return carneDirectivo;
    }

    public void setCarneDirectivo(int carneDirectivo) {
        this.carneDirectivo = carneDirectivo;
    }
    
    

    public int getCarrera() {
        return carrera;
    }

    public void setCarrera(int carrera) {
        this.carrera = carrera;
    }
    

    public List<List<String>> getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(List<List<String>> configuracion) {
        this.configuracion = configuracion;
    }

    public boolean isSistemas() {
        return sistemas;
    }

    public void setSistemas(boolean sistemas) {
        this.sistemas = sistemas;
    }
    
    public void cambiar() throws ExcepcionServiciosCancelaciones{
        setProperties();
    }
    
    

    private EndPoint createEndPoint(EndPointAnchor anchor,String id) {
        DotEndPoint endPoint = new DotEndPoint(anchor);
        endPoint.setStyle("{fillStyle:'#404a4e'}");
        endPoint.setHoverStyle("{fillStyle:'#20282b'}");
        endPoint.setId(id);
         
        return endPoint;
    }

    public DiagramModel getModel() {
        return model;
    }

    private void setProperties() throws ExcepcionServiciosCancelaciones {
        carneDirectivo = sistemas?2001212:2345681;
        directivoActual= servCanc.consultarConsejero(carneDirectivo);           
        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);
        ParserGrafo p = ServiciosCancelacionesFactory.getInstance().getParserGrafo();
        long aconsejado = directivoActual.getEstudiantesAconsejados().get(0).getCodigo();
        estudianteAconsejado = servCanc.consultarEstudiante(aconsejado);
        List<Materia> act = new ArrayList();
        act.add(new Materia("CALD"));
        List<Materia> vist = new ArrayList();
        vist.add(new Materia("DEPD"));
        estudianteAconsejado.setMateriasActuales(act);
        estudianteAconsejado.setMateriasCursadas(vist);
        carrera = estudianteAconsejado.getPlanDeEstudios().getNumeroPlanDeEstudio();
        solicitudesNoFinalizadas = servCanc.consultarCancelacionesNoFinalizadas(carrera);
        solicitudesFinalizadas = servCanc.consultarCancelacionesFinalizadas(carrera);
        grafoPlanDeEstudios = p.convertStringToGrafo(estudianteAconsejado.getPlanDeEstudios().getGrafo());
        List<String> lista = new ArrayList();
        List<Materia> materias = grafoPlanDeEstudios.getLista();
        lista.add("CALD");
        configuracion = servCanc.calcularProyeccion(estudianteAconsejado, lista, grafoPlanDeEstudios);
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{strokeStyle:'#404a4e', lineWidth:3}");
        connector.setHoverPaintStyle("{strokeStyle:'#20282b'}");
        model.setDefaultConnector(connector);
        for(int i=0; i<configuracion.size(); i++){
            List<String> contenedor = configuracion.get(i);
            for(int j=0; j<contenedor.size(); j++){
                int x =(j*15)+5;
                int y = (i*15)+5;
                String contenedorActual = contenedor.get(j);
                Element a= new Element(contenedorActual, x+"em", y+"em");
                a.setId(contenedorActual);
                Materia materiaActual = grafoPlanDeEstudios.getMateria(contenedorActual);
                List<String> pre = materiaActual.getPrerequisitos();
                String idElementoActual = a.getId();
                int k= 0;
                for(String l: pre){
                    Element prerrequisito = model.findElement(l);
                    k++;
                    String idPre = prerrequisito.getId();
                    a.addEndPoint(createEndPoint(EndPointAnchor.TOP, idElementoActual+"-"+idPre));
                    
                    model.findElement(l).addEndPoint(createEndPoint(EndPointAnchor.BOTTOM,idPre+"-"+idElementoActual));
                    model.connect(new Connection(model.findEndPoint(model.findElement(l), idPre+"-"+idElementoActual), model.findEndPoint(a, idElementoActual+"-"+idPre)));                      
                }
                model.addElement(a);
            }
        }
        creditosActuales = PlanDeEstudios.creditosPorSemestre;
    }
    
    
    
    public String booleanToString(Boolean bool){
        return bool==null? "No aplica": (bool? "Aceptada": "Rechazada");
    }
    
    
    public String consultarEstudianteNombre(long id) throws ExcepcionServiciosCancelaciones{
        return servCanc.consultarEstudiante(id).getNombre();
    }
    
    public void finalizarSolicitud(int idSolicitud){
        servCanc.cambiarSolicitudAFinalizada(idSolicitud);
    }
    
}
