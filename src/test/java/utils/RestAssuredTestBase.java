package utils;

import dev.matheuslf.desafio.inscritos.dto.request.LoginRequest;
import dev.matheuslf.desafio.inscritos.dto.request.RegisterUserRequest;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class RestAssuredTestBase {

    @LocalServerPort
    private int port;

    @Autowired protected ProjectRepository projectRepository;
    @Autowired protected TaskRepository taskRepository;
    @Autowired protected UserRepository userRepository;
    @Autowired protected PasswordEncoder passwordEncoder;

    protected String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        token = gerarTokenValido();
    }

    private String gerarTokenValido() {
        var email = "rest@teste.com";
        var senha = "123456";

        var register = new RegisterUserRequest("Usuário Rest", email, senha);
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(register)
                .when()
                .post("/auth/register");

        var login = new LoginRequest(email, senha);
        Response response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(login)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(201)
                .extract()
                .response();

        return response.jsonPath().getString("token");
    }
}