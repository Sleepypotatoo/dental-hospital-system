package dentist.dentist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

//用户实体类，映射数据库表 user，存储用户信息。
@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // 主键，自动增长
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 用户名，唯一，不允许为空
    @Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "用户名不能为空")
    private String username;

    // 密码，不允许为空
    @Column(name = "password", nullable = false)
    private String password;

    // 姓名
    @Column(name = "name")
    private String name;

    // 医生执业证书
    @Column(name = "license")
    private String license;

    // 邮箱
    @Email(message = "邮箱格式不正确")
    @Column(name = "email")
    private String email;

    // 手机号
    @Column(name = "phone")
    private String phone;

    // 角色，不允许为空
    @Column(name = "role", nullable = false)
    private String role;

     public String getRole() {
        return role;
    }
    public void setLicense(String license2) {
        this.license = license2;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getLicense() {
        return license;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    // Getters and setters
}
