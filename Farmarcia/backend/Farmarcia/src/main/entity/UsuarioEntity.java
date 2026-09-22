@Entity
@Setter

public class UsuarioEntity {

    
    private Long id;

    private String name;

    private String email;

    private String password;

    private RolEntity rol;
}
