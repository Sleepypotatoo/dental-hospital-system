package dentist.dentist;

import lombok.extern.slf4j.Slf4j;
import java.util.Optional; // 必须导入 java.util.Optional
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//用户服务类，处理注册、登录、信息更新等业务逻辑
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 患者注册
    public User registerPatient(User user, String verificationCode) {
         log.info("开始患者注册流程 - 用户名: {}", user.getUsername());
        
        if (!isValidVerificationCode(verificationCode)) {
            log.warn("验证码无效 - 用户名: {}", user.getUsername());
            return null;
        }

        try {
            user.setRole(RoleEnum.PATIENT.name());
            log.debug("设置用户角色为 PATIENT - 用户名: {}", user.getUsername());
            
            String rawPassword = user.getPassword();
            user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
            log.debug("密码加密完成 - 用户名: {}", user.getUsername());
            
            User savedUser = userRepository.save(user);
            log.info("患者注册成功 - 用户ID: {}, 用户名: {}", savedUser.getId(), savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            log.error("患者注册失败 - 用户名: {}, 错误原因: {}", user.getUsername(), e.getMessage(), e);
            return null;
        }
    }

    // 验证验证码是否有效（这里只是一个简单的示例）
    private boolean isValidVerificationCode(String verificationCode) {
        log.debug("验证验证码: {}", verificationCode);// 实际项目中，这里可以加入与发送的验证码进行比对的逻辑
        return true; // 假设验证码有效
    }

    // 医生注册
    public User registerDoctor(User user, String license) {
        log.info("开始医生注册流程 - 用户名: {}", user.getUsername());
        
        try {
            user.setRole(RoleEnum.DOCTOR.name());
            log.debug("设置用户角色为 DOCTOR - 用户名: {}", user.getUsername());
            
            user.setLicense(license);
            log.debug("设置医生执业证书 - 证书号: {}", license);
            
            String rawPassword = user.getPassword();
            user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
            log.debug("密码加密完成 - 用户名: {}", user.getUsername());
            
            User savedUser = userRepository.save(user);
            log.info("医生注册成功 - 用户ID: {}, 用户名: {}", savedUser.getId(), savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            log.error("医生注册失败 - 用户名: {}, 错误原因: {}", user.getUsername(), e.getMessage(), e);
            return null;
        }
    }

    // 登录功能
    public User login(String username, String password, String role) {
        // 根据用户名查找用户
        log.info("用户尝试登录 - 用户名: {}, 角色: {}", username, role);
        
        User user = userRepository.findByUsername(username).orElse(null);;
        if (user == null) {
            log.warn("登录失败 - 用户名不存在: {}", username);
            return null;
        }

        if (!user.getRole().equals(role)) {
            log.warn("角色不匹配 - 用户名: {}, 预期角色: {}, 实际角色: {}", username, role, user.getRole());
            return null;
        }

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            log.info("登录成功 - 用户ID: {}, 用户名: {}", user.getId(), username);
            return user;
        } else {
            log.warn("密码验证失败 - 用户名: {}", username);
            return null;
        }
    }

    // 获取用户信息
    public User getUserInfo(Long id) {
        // 根据ID查找用户
        log.debug("查询用户信息 - 用户ID: {}", id);
        return userRepository.findById(id).orElse(null);
    }

    // 更新用户信息
    public User updateUserInfo(Long id, User user) {
        log.info("开始更新用户信息 - 用户ID: {}", id);
        
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (!existingUserOptional.isPresent()) {
            log.warn("更新失败 - 用户不存在: 用户ID: {}", id);
            return null;
        }

        try {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setLicense(user.getLicense());
            
            User updatedUser = userRepository.save(existingUser);
            log.info("用户信息更新成功 - 用户ID: {}", id);
            return updatedUser;
        } catch (Exception e) {
            log.error("用户信息更新失败 - 用户ID: {}, 错误原因: {}", id, e.getMessage(), e);
            return null;
        }
    }

    
}