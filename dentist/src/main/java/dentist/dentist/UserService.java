package dentist.dentist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//用户服务类，处理注册、登录、信息更新等业务逻辑
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 患者注册
    public User registerPatient(User user, String verificationCode) {
        // 验证患者注册的验证码
        if (isValidVerificationCode(verificationCode)) {
            // 设置患者角色
            user.setRole(RoleEnum.PATIENT.name());
            // 对密码进行加密
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            // 保存用户到数据库
            return userRepository.save(user);
        } else {
            // 验证码无效，返回null
            return null;
        }
    }

    // 验证验证码是否有效（这里只是一个简单的示例）
    private boolean isValidVerificationCode(String verificationCode) {
        // 实际项目中，这里可以加入与发送的验证码进行比对的逻辑
        return true; // 假设验证码有效
    }

    // 医生注册
    public User registerDoctor(User user, String license) {
        // 设置医生角色
        user.setRole(RoleEnum.DOCTOR.name());
        // 设置医生执业证书
        user.setLicense(license);
        // 对密码进行加密
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // 保存用户到数据库
        return userRepository.save(user);
    }

    // 登录功能
    public User login(String username, String password, String role) {
        // 根据用户名查找用户
        User user = userRepository.findByUsername(username);
        if (user != null && user.getRole() != null && user.getRole().equals(role)) {
            // 验证密码是否正确
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                // 返回用户信息
                return user;
            }
        }
        // 用户名或密码错误，或角色不匹配，返回null
        return null;
    }

    // 获取用户信息
    public User getUserInfo(Long id) {
        // 根据ID查找用户
        return userRepository.findById(id).orElse(null);
    }

    // 更新用户信息
    public User updateUserInfo(Long id, User user) {
        // 根据ID查找现有用户
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            // 更新用户信息
            existingUser.setUsername(user.getUsername());
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setLicense(user.getLicense());
            // 保存更新后的用户
            return userRepository.save(existingUser);
        }
        // 用户不存在，返回null
        return null;
    }

    
}