package mx.uv.inventario;

import org.springframework.data.repository.CrudRepository;

public interface IUsuarios extends CrudRepository<Usuarios, Integer>{

    static String getNombre() {
        return null;
    }

}
