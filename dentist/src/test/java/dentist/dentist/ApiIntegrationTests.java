package dentist.dentist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private String patientToken;
    private String doctorToken;

    // 初始化测试数据
    @BeforeEach
    void setup() throws Exception {
        // 注册测试患者
        mockMvc.perform(post("/api/auth/register/patient")
                .param("verificationCode", "1234")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"test_patient\", \"password\": \"Passw0rd!\" }"));

        // 注册测试医生
        mockMvc.perform(post("/api/auth/register/doctor")
                .param("license", "LIC-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"test_doctor\", \"password\": \"Passw0rd!\" }"));

        // 获取患者 Token
        patientToken = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"test_patient\", \"password\": \"Passw0rd!\", \"role\": \"PATIENT\" }"))
                .andReturn().getResponse().getContentAsString();

        // 获取医生 Token
        doctorToken = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"username\": \"test_doctor\", \"password\": \"Passw0rd!\", \"role\": \"DOCTOR\" }"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test //患者注册接口测试
    void testPatientRegistration_Success() throws Exception {
    mockMvc.perform(post("/api/auth/register/patient")
            .param("verificationCode", "1234")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"username\": \"new_patient\", \"password\": \"Passw0rd!\" }"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("new_patient"))
            .andExpect(jsonPath("$.role").value("PATIENT"));
}

    @Test
    void testPatientRegistration_DuplicateUsername() throws Exception {
    mockMvc.perform(post("/api/auth/register/patient")
            .param("verificationCode", "1234")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"username\": \"test_patient\", \"password\": \"Passw0rd!\" }"))
            .andExpect(status().isBadRequest());
}

    @Test
    void testPatientRegistration_InvalidVerificationCode() throws Exception {
    mockMvc.perform(post("/api/auth/register/patient")
            .param("verificationCode", "0000")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"username\": \"new_patient\", \"password\": \"Passw0rd!\" }"))
            .andExpect(status().isBadRequest());
        }

    @Test //医生注册接口测试
    void testDoctorRegistration_Success() throws Exception {
        mockMvc.perform(post("/api/auth/register/doctor")
            .param("license", "LIC-002")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"username\": \"new_doctor\", \"password\": \"Passw0rd!\" }"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("DOCTOR"));
}

    @Test
    void testDoctorRegistration_MissingLicense() throws Exception {
        mockMvc.perform(post("/api/auth/register/doctor")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"username\": \"new_doctor\", \"password\": \"Passw0rd!\" }"))
            .andExpect(status().isBadRequest());
}
    @Test
    void testLogin_Success() throws Exception {
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"username\": \"test_patient\", \"password\": \"Passw0rd!\", \"role\": \"PATIENT\" }"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty());
}

    @Test  //用户登录接口测试
    void testLogin_InvalidPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"username\": \"test_patient\", \"password\": \"wrong\", \"role\": \"PATIENT\" }"))
            .andExpect(status().isUnauthorized());
}

    @Test
    void testLogin_RoleMismatch() throws Exception {
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"username\": \"test_patient\", \"password\": \"Passw0rd!\", \"role\": \"DOCTOR\" }"))
            .andExpect(status().isForbidden());
}

    @Test //获取用户信息接口测试
    void testGetUserInfo_Success() throws Exception {
        mockMvc.perform(get("/api/auth/user/info")
            .param("id", "1")
            .header("Authorization", "Bearer " + patientToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("test_patient"));
}

    @Test
    void testGetUserInfo_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/auth/user/info")
            .param("id", "1"))
            .andExpect(status().isUnauthorized());
}

    @Test //更新用户信息接口测试
    void testUpdateUserInfo_Success() throws Exception {
        mockMvc.perform(put("/api/auth/user/update")
            .param("id", "1")
            .header("Authorization", "Bearer " + patientToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"email\": \"new_email@example.com\" }"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("new_email@example.com"));
}

    @Test
    void testUpdateUserInfo_Forbidden() throws Exception {
        mockMvc.perform(put("/api/auth/user/update")
            .param("id", "2")
            .header("Authorization", "Bearer " + patientToken) // 患者尝试更新医生信息
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"email\": \"new_email@example.com\" }"))
            .andExpect(status().isForbidden());
}


}