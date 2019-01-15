/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.clientesgarestws.test;

import com.example.clientesgarestws.domain.Persona;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author usuario
 */
public class TestPersonaServiceRS {
    
    private static final String URL_BASE = "http://localhost:8080/sga-web/webservice";
    private static Client cliente;
    private static WebTarget webTarget;
    private static Persona persona;
    private static List<Persona> personas;
    private static Invocation.Builder invocationBuilder;
    private static Response response;
    
    public static void main(String[] args) {
        cliente = ClientBuilder.newClient();
        
        // leer una persona (modo GET)
        webTarget = cliente.target(URL_BASE).path("/personas");
        // proporcionamos un idPersona válido
        persona = webTarget.path("/3").request(MediaType.APPLICATION_XML).get(Persona.class);
        System.out.println("Persona recuperada: " + persona);
        
        // Leer todas las personas (método get con readEntity de tipo List<>)
        personas = webTarget.request(MediaType.APPLICATION_XML).get(Response.class).readEntity(new GenericType<List<Persona>>(){});
        System.out.println("\nPersonas Recuperadas:");
        imprimirPersonas(personas);
        
        // Agregar una persona método POST
        Persona nuevaPersona = new Persona();
        nuevaPersona.setIdPersona(4);
        nuevaPersona.setNombre("Carlos");
        nuevaPersona.setApePaterno("Miranda");
        nuevaPersona.setApeMaterno("Ramirez");
        nuevaPersona.setEmail("cmiranda@mail.com");
        nuevaPersona.setTelefono("09345566789");
        invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
        response = invocationBuilder.post(Entity.entity(nuevaPersona, MediaType.APPLICATION_XML));
        System.out.println("");
        System.out.println(response.getStatus());
        // Recuperamos a la persona recién agregada para después modificarla y después eliminarla.
        Persona personaRecuperada = response.readEntity(Persona.class);
        System.out.println("Persona agregada: " + personaRecuperada);
        
        // Modificar una persona metodo PUT
        // persona recuperada anteriormente
        Persona personaModificar = personaRecuperada;
        personaModificar.setApeMaterno("CambioApeMaterno");
        String pathId = "/" + personaModificar.getIdPersona();
        invocationBuilder = webTarget.path(pathId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.put(Entity.entity(personaModificar, MediaType.APPLICATION_XML));
        
        System.out.println("");
        System.out.println(response.getStatus());
        System.out.println("Persona Modificada: " + response.readEntity(Persona.class));
        
        // Eliminar una persona
        // persona recuperada anteriormente
        Persona personaEliminar = personaRecuperada;
        String pathEliminarId = "/" + personaEliminar.getIdPersona();
        invocationBuilder = webTarget.path(pathEliminarId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.delete();
        
        System.out.println("");
        System.out.println(response.getStatus());
        System.out.println("Persona Eliminada");
    }

    private static void imprimirPersonas(List<Persona> personas) {
        for (Persona persona : personas) {
            System.out.println("Persona: " + persona);
        }
    }
}
