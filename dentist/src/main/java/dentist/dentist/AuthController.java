package dentist.dentist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证与用户管理", description = "用户注册、登录、信息查询与更新接口")
@SecurityScheme(
    name = "JWT",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "登录成功后返回的JWT令牌，需在请求头中添加: Authorization: Bearer {token}"
)
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public String home() {
        return "Welcome to Dental System!";
    }
    // ========================== 患者注册接口 ==========================
    @PostMapping("/register/patient")
    @Operation(
        summary = "患者注册",
        description = "通过短信验证码注册患者账户，密码自动加密存储",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "注册成功",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class),
                    examples = @ExampleObject(value = """
                        {
                          "id": 1,
                          "username": "patient_zhang",
                          "role": "PATIENT",
                          "email": "zhang@example.com"
                        }
                        """)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "验证码无效/用户名已存在/参数格式错误",
                content = @Content(examples = @ExampleObject("注册失败"))
            )
        }
    )
    public ResponseEntity<?> registerPatient(
        @Valid @RequestBody User user,
        @Parameter(
            name = "verificationCode",
            description = "短信验证码",
            required = true,
            example = "123456"
        )
        @RequestParam String verificationCode
    ) {
        User savedUser = userService.registerPatient(user, verificationCode);
        return savedUser != null ?
            ResponseEntity.ok(savedUser) :
            ResponseEntity.badRequest().body("Registration failed");
    }

    // ========================== 医生注册接口 ==========================
    @PostMapping("/register/doctor")
    @Operation(
        summary = "医生注册",
        description = "需提供有效的执业证书编号，密码自动加密存储",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "注册成功",
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "400", description = "证书无效/参数错误")
        }
    )
    public ResponseEntity<?> registerDoctor(
        @Valid @RequestBody User user,
        @Parameter(
            name = "license",
            description = "医生执业证书编号",
            required = true,
            example = "LIC-2023-001"
        )
        @RequestParam String license
    ) {
        User savedUser = userService.registerDoctor(user, license);
        return savedUser != null ?
            ResponseEntity.ok(savedUser) :
            ResponseEntity.badRequest().body("Registration failed");
    }

    // ========================== 用户登录接口 ==========================
    @PostMapping("/login")
    @Operation(
        summary = "用户登录",
        description = "验证用户名、密码和角色，返回JWT访问令牌",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "登录成功",
                content = @Content(
                    examples = @ExampleObject(value = "eyJhbGciOiJIzI1NiIsInR5cCI6IkpXVCJ9...")
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "用户名/密码错误 或 角色不匹配",
                content = @Content(examples = @ExampleObject("Invalid credentials"))
            )
        }
    )
    public ResponseEntity<?> login(
        @Parameter(
            name = "username",
            description = "用户名",
            required = true,
            example = "doctor_li"
        )
        @RequestParam String username,
        @Parameter(
            name = "password",
            description = "密码（明文，至少8位含字母和数字）",
            required = true,
            example = "Passw0rd!"
        )
        @RequestParam String password,
        @Parameter(
            name = "role",
            description = "用户角色",
            required = true,
            example = "DOCTOR"
        )
        @RequestParam String role
    ) {
        User user = userService.login(username, password, role);
        if (user != null) {
            return ResponseEntity.ok(jwtUtil.generateToken(user));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials or role");
        }
    }

    // ========================== 获取用户信息接口 ==========================
    @GetMapping("/user/info")
    @Operation(
        summary = "获取用户详细信息",
        description = "根据用户ID查询完整信息（需携带JWT令牌）",
        security = @SecurityRequirement(name = "JWT"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "查询成功",
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "404", description = "用户不存在")
        }
    )
    public ResponseEntity<?> getUserInfo(
        @Parameter(
            name = "id",
            description = "用户唯一ID",
            required = true,
            example = "1"
        )
        @RequestParam Long id
    ) {
        User user = userService.getUserInfo(id);
        return user != null ?
            ResponseEntity.ok(user) :
            ResponseEntity.notFound().build();
    }

    // ========================== 更新用户信息接口 ==========================
    @PutMapping("/user/update")
    @Operation(
        summary = "更新用户信息",
        description = "修改用户基本信息（邮箱、电话等，需携带JWT令牌）",
        security = @SecurityRequirement(name = "JWT"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "更新成功",
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(responseCode = "404", description = "用户不存在")
        }
    )
    public ResponseEntity<?> updateUserInfo(
        @Parameter(
            name = "id",
            description = "待更新用户的ID",
            required = true,
            example = "1"
        )
        @RequestParam Long id,
        @Valid @RequestBody User user
    ) {
        User updatedUser = userService.updateUserInfo(id, user);
        return updatedUser != null ?
            ResponseEntity.ok(updatedUser) :
            ResponseEntity.notFound().build();
    }
}