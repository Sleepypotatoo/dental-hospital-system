package dentist.dentist;

import org.junit.jupiter.api.Test; // 注意是 JUnit 5 的注解
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest { // 类必须为 public
    @Autowired
    private MockMvc mockMvc;

    @Test // 使用 JUnit 5 注解
    public void testPatientRegistration() throws Exception {
        mockMvc.perform(post("/api/auth/register/patient")
                .param("verificationCode", "1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"test_user\", \"password\": \"123456\" }"))
                .andExpect(status().isOk());
    }
}