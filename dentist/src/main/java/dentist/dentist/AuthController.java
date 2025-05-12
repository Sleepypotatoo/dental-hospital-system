package dentist.dentist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Spring Boot 3.x 使用 jakarta 包

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 患者注册接口
    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody User user, @RequestParam String verificationCode) {
        // 调用服务层的患者注册方法
        User savedUser = userService.registerPatient(user, verificationCode);
        if (savedUser != null) {
            // 注册成功，返回用户信息
            return ResponseEntity.ok(savedUser);
        } else {
            // 注册失败，返回错误信息
            return ResponseEntity.badRequest().body("Registration failed");
        }
    }

    // 医生注册接口
    @PostMapping("/register/doctor")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody User user, @RequestParam String license) {
        // 调用服务层的医生注册方法
        User savedUser = userService.registerDoctor(user, license);
        if (savedUser != null) {
            // 注册成功，返回用户信息
            return ResponseEntity.ok(savedUser);
        } else {
            // 注册失败，返回错误信息
            return ResponseEntity.badRequest().body("Registration failed");
        }
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        // 调用服务层的登录方法
        User user = userService.login(username, password, role);
        if (user != null) {
            // 登录成功，生成并返回JWT Token
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(token);
        } else {
            // 登录失败，返回错误信息
            return ResponseEntity.status(401).body("Invalid credentials or role");
        }
    }

    // 获取用户信息接口
    @GetMapping("/user/info")
    public ResponseEntity<?> getUserInfo(@RequestParam Long id) {
        // 调用服务层的获取用户信息方法
        User user = userService.getUserInfo(id);
        if (user != null) {
            // 返回用户信息
            return ResponseEntity.ok(user);
        } else {
            // 用户不存在，返回404
            return ResponseEntity.notFound().build();
        }
    }

    // 更新用户信息接口
    @PutMapping("/user/update")
    public ResponseEntity<?> updateUserInfo(@RequestParam Long id, @Valid @RequestBody User user) {
        // 调用服务层的更新用户信息方法
        User updatedUser = userService.updateUserInfo(id, user);
        if (updatedUser != null) {
            // 更新成功，返回更新后的用户信息
            return ResponseEntity.ok(updatedUser);
        } else {
            // 更新失败，返回404
            return ResponseEntity.notFound().build();
        }
    }
}