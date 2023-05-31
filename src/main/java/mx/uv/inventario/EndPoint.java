package mx.uv.inventario;

//import java.io.Console;
//import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
//import com.fasterxml.jackson.core.sym.Name;

public class EndPoint {

    @Autowired
    private IUsuarios iUsuarios;

    @PayloadRoot(localPart = "UsuarioRequest", namespace = "https://t4is.uv.mx/usuarios")
    @ResponsePayload
    public UsuarioResponse Usuario(@RequestPayload UsuarioResponse peticion) {
        UsuarioResponse response = new UsuarioResponse();
        response.setRespuesta("created");

        Usuarios usuarios = new Usuarios();
        usuarios.setNombre(peticion.getNombre());
        iUsuarios.save(usuarios);

        return response;
    }

    @PayloadRoot(localPart = "ModificarRequest", namespace="https://t4is.uv.mx/usuarios")
    @ResponsePayload
    public String Modificar( @RequestPayload  ModificarRequest peticion) {
        //ModificarResponse response= new ModificarResponse();

        Optional<Usuarios> users = iUsuarios.findById(peticion.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        Usuarios userResponse = users.get();
        String jsonResponse;
            try {
                jsonResponse = objectMapper.writeValueAsString(userResponse);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                jsonResponse = "{\"error\": \"Error al convertir a JSON\"}";
            }

        return jsonResponse;
        
    }

    @PayloadRoot(localPart = "EliminarRequest", namespace="https://t4is.uv.mx/usuarios")
    @ResponsePayload
    public EliminarResponse Eliminar( @RequestPayload  EliminarRequest peticion) {
        EliminarResponse response= new EliminarResponse();
       Optional<Usuarios> users = iUsuarios.findById(peticion.getId());
    Usuarios usersResponse = users.get();
        iUsuarios.deleteById(peticion.getId());
        response.setNombre("delete");
        return response;
    }
    
    @PayloadRoot(localPart = "UsuariosRequest", namespace = "https://t4is.uv.mx/usuarios")
    @ResponsePayload
    public UsuariosResponse Usuarios(@RequestPayload UsuariosRequest peticion) {
        UsuariosResponse response = new UsuariosResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        List<String> listaUsuarios = new ArrayList<>();
        Iterable<Usuarios> usuarios = iUsuarios.findAll();
        for (Usuarios usuario : usuarios) {
            String usuarioString = usuario.getId() + ": " + usuario.getNombre();
            listaUsuarios.add(usuarioString);
        }       


        String jsonLista;
        try {
            jsonLista = objectMapper.writeValueAsString(listaUsuarios);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonLista = "{\"error\": \"Error al convertir a JSON\"}";
        }

        response.setNombre(jsonLista);
        return response;

    }

}