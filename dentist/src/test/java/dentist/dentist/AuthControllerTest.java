package dentist.dentist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPatientRegistration_Success() throws Exception {
        mockMvc.perform(post("/api/auth/register/patient")
                .param("verificationCode", "1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"test_user\", \"password\": \"123456\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test_user"))
                .andExpect(jsonPath("$.role").value("PATIENT"));
    }

    @Test
    public void testLogin_InvalidPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"test_user\", \"password\": \"wrong\", \"role\": \"PATIENT\" }"))
                .andExpect(status().isUnauthorized());
    }
}