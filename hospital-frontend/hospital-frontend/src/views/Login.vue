<!--
 * 登录页

-->
<template>
    <div class="login-index" :style="backgroundDiv">
        <div class="login-window-index">
            <!--登录标题-->
            <i style=" top: 40px; font-size: 28px; left: 70px;position: absolute;"class="iconfont">
              <span style="color: #e75c09;" >登录医院管理系统</span>
            </i>

            <!--登录表单-->
            <el-form
                :model="loginForm"
                :rules="loginRules"
                ref="ruleForm"
                class="loginForm"
            >
                <!--用户名-->
                <el-form-item prop="id">
                    <el-input v-model="loginForm.id">
                        <i  slot="prefix"
                            class="iconfont icon-login-user"
                            style="font-size: 22px"
                        ></i>
                    </el-input>
                </el-form-item>

              <!--密码-->
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" show-password>
                        <i
                            slot="prefix"
                            class="iconfont icon-login-password"
                            style="font-size: 22px"
                        ></i>
                    </el-input>
                </el-form-item>

                <!-- 角色登录选择框 -->
                <el-form-item class="role_radio">
                    <el-radio-group v-model="role" style="margin-left: 1px" size="small">
                        <el-radio-button label="患者"></el-radio-button>
                        <el-radio-button label="医生"></el-radio-button>
                        <el-radio-button label="管理员"></el-radio-button>
                    </el-radio-group>
                </el-form-item>

                <el-form-item class="login-buttons">
                    <el-button
                        type="success"
                        style="font-size: 18px"
                        @click="submitLoginForm('ruleForm')"
                    >
                        <i class="iconfont icon-login-button"
                           style="font-size: 20px">
                        </i>
                        登录
                    </el-button>
                    <el-button
                        type="info"
                        style="font-size: 18px"
                        @click="registerFormVisible = true"
                    >
                        <i class="iconfont icon-login-register"
                           style="font-size: 20px">
                        </i>
                        注册
                    </el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- 注册对话框 -->
        <el-dialog title="用户注册" :visible.sync="registerFormVisible" >
            <el-form
                :model="registerForm"
                :rules="registerRules"
                ref="registerForm"
            >
                <el-form-item label="账号" label-width="80px" prop="pId">
                    <el-input v-model.number="registerForm.pId"></el-input>
                </el-form-item>

                <el-form-item label="密码" label-width="80px" prop="pPassword">
                  <el-input v-model="registerForm.pPassword"></el-input>
                </el-form-item>

                <el-form-item label="姓名" label-width="80px" prop="pName">
                    <el-input v-model="registerForm.pName"></el-input>
                </el-form-item>

                <el-form-item label="性别" label-width="80px">
                <el-radio v-model="registerForm.pGender" label="男">
                  男
                </el-radio>
                <el-radio v-model="registerForm.pGender" label="女">
                  女
                </el-radio>
              </el-form-item>

                <el-form-item
                    label="出生日期"
                    label-width="80px"
                    prop="pBirthday"
                >
                    <el-date-picker
                        v-model="registerForm.pBirthday"
                        type="date"
                        placeholder="选择日期"
                        value-format="yyyy-MM-dd"
                    >
                    </el-date-picker>
                </el-form-item>

                <el-form-item label="手机号" label-width="80px" prop="pPhone">
                    <el-input v-model="registerForm.pPhone"></el-input>
                </el-form-item>

                <el-form-item label="邮箱" label-width="80px" prop="pEmail">
                    <el-input v-model="registerForm.pEmail"></el-input>
                </el-form-item>

                <el-form-item label="身份证号" label-width="80px" prop="pCard">
                    <el-input v-model="registerForm.pCard"></el-input>
                </el-form-item>

            </el-form>

            <div slot="footer">
                <el-button
                    @click="registerFormVisible = false"
                    style="font-size: 18px"
                    >
                   <i class="iconfont icon-cancel-button"style="font-size: 20px"></i>
                    取 消
                </el-button>
                <el-button
                    type="primary"
                    @click="register('registerForm')"
                    style="font-size: 18px"
                  >
                  <i class="iconfont icon-sure-button" style="font-size: 20px"></i>
                    确 定
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";
export default {
    name: "Login",
    data() {
        var validatePhone = (rule, value, callback) => {
            if (value === undefined) {
                callback(new Error("请输入手机号"));
            } else {
                let reg =/^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                if (!reg.test(value)) {
                    callback(new Error("请输入合法的手机号"));
                }
                callback();
            }
        };
        var validateCard = (rule, value, callback) => {
            if (value === undefined) {
                callback(new Error("请输入身份证号"));
            } else {
                let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
                if (!reg.test(value)) {
                    callback(new Error("请输入合法的身份证号码"));
                }
                callback();
            }
        };
        let validatePass = (rule, value, callback) => {
            if (value === "") {
                callback(new Error("请输入密码"));
            } else {
                if (this.findForm.checkPassword !== "") {
                    this.$refs.findForm.validateField("checkPassword");
                }
                callback();
            }
        };
       let validatePassAgain = (rule, value, callback) => {
            if (value === "") {
                callback(new Error("请再次输入密码"));
            } else if (value !== this.findForm.newPassword) {
                callback(new Error("两次输入密码不一致!"));
            } else {
                callback();
            }
        };
        return {
            //背景图片
            backgroundDiv: {
                backgroundImage:
                    "url(" + require("../assets/login-back.jpeg") + ")",
                backgroundRepeat: "no-repeat",
                backgroundSize: "100% 100%",
            },
            loginForm: {
                id: "",
                password: "",
            },
            loginRules: {
                id: [
                    {
                        required: true,
                        message: "请输入账号编号",
                        trigger: "blur",
                    },
                    {
                        min: 3,
                        max: 50,
                        message: "长度在 3到 50 个字符",
                        trigger: "blur",
                    },
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: "blur" },
                ],
            },
            role: "患者",
            findRole: "患者",
            //找回密码
            findFormVisible: false,
            findForm: {
                code: "",
                newPassword: "",
                checkPassword: "",
                pEmail: "",
            },

            findRules: {
                pEmail: [
                    {
                        required: true,
                        message: "请输入邮箱地址",
                        trigger: "blur",
                    },
                    {
                        type: "email",
                        message: "请输入正确的邮箱地址",
                        trigger: ["blur", "change"],
                    },
                ],
                code: [
                    {
                        required: true,
                        message: "请输入验证码",
                        trigger: "blur",
                    },
                ],
                newPassword: [{ validator: validatePass, trigger: "blur" }],
                checkPassword: [{ validator: validatePassAgain, trigger: "blur" }],
            },
            totalTime: 60,
            content: "发送验证码",
            canClick: true,
            //注册
            registerFormVisible: false,
            registerForm: {
                pGender: "男",
            },
            registerRules: {
                pId: [
                    { required: true, message: "请输入账号", trigger: "blur" },
                    {
                        type: "number",
                        message: "账号必须数字类型",
                        trigger: "blur",
                    },
                ],
                pPassword: [
                    { required: true, message: "请输入密码", trigger: "blur" },
                    {
                        min: 4,
                        max: 50,
                        message: "长度在 4到 50 个字符",
                        trigger: "blur",
                    },
                ],
                pName: [
                    { required: true, message: "请输入姓名", trigger: "blur" },
                    {
                        min: 2,
                        max: 8,
                        message: "长度在 2到 8 个字符",
                        trigger: "blur",
                    },
                ],
                pEmail: [
                    { required: true, message: "请输入邮箱", trigger: "blur" },
                    {
                        type: "email",
                        message: "请输入正确的邮箱地址",
                        trigger: ["blur", "change"],
                    },
                ],
                pPhone: [{ validator: validatePhone }],
                pCard: [{ validator: validateCard }],
                pBirthday: [
                    {
                        required: true,
                        message: "选择出生日期",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        //点击注册确认按钮
        register(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("patient/addPatient", {
                            params: {
                                pId: this.registerForm.pId,
                                pName: this.registerForm.pName,
                                pPassword: this.registerForm.pPassword,
                                pGender: this.registerForm.pGender,
                                pEmail: this.registerForm.pEmail,
                                pPhone: this.registerForm.pPhone,
                                pCard: this.registerForm.pCard,
                                pBirthday: this.registerForm.pBirthday,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error(
                                    "账号或邮箱已被占用！"
                                );
                            this.registerFormVisible = false;
                            this.$message.success("注册成功！");
                        });
                } else {
                    return false;
                }
            });
        },
        //提交表单
        submitLoginForm(formName) {
            if (!/^\d+$/.test(this.loginForm.id)) {
                this.$message.error("用户名有误");
                return;
            }
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.role === "管理员") {
                        var params = new URLSearchParams();
                        params.append("aId", this.loginForm.id);
                        params.append("aPassword", this.loginForm.password);

                        request
                            .post("admin/login", params)
                            .then((res) => {
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "用户名或密码错误"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/adminLayout");
                            })
                            .catch((error) => {
                                this.$message.error(
                                    "服务器异常：" + error.response.data
                                );
                            });
                    }
                    if (this.role === "医生") {
                        var params1 = new URLSearchParams();
                        params1.append("dId", this.loginForm.id);
                        params1.append("dPassword", this.loginForm.password);

                        request
                            .post("doctor/login", params1)
                            .then((res) => {
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "用户名或密码错误"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/doctorLayout");
                            })
                            .catch((error) => {
                                this.$message.error(
                                    "服务器异常：" + error.response.data
                                );
                            });
                    }
                    if (this.role === "患者") {
                        var params2 = new URLSearchParams();
                        params2.append("pId", this.loginForm.id);
                        params2.append("pPassword", this.loginForm.password);

                        request
                            .post("patient/login", params2)
                            .then((res) => {
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "用户名或密码错误"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/patientLayout");
                            })
                            .catch((error) => {
                                this.$message.error(
                                    "服务器异常：" + error.response.data
                                );
                            });
                    }
                } else {
                    return false;
                }
            });
        },
    },
};
</script>

<style lang="scss">
.codeInput {
    width: 70%;
    margin-right: 10px;
}

.login-index {
    background: #ffffff;
    height: 100%;
    position: relative;
}
.login-window-index {
    opacity: 0.9;
    width: 450px;
    height: 390px;
    background: #ffffff;
    position: absolute;
    left: 80%;
    top: 50%;
    transform: translate(-50%, -50%);
}
.logo-index {
    background: white;
    height: 130px;
    width: 130px;
    border-radius: 50%;
    padding: 10px;
    //子绝父相,使一个div悬挂在另一个div上中间
    position: absolute;
    left: 50%;
    top: 0;
    transform: translate(-50%, -50%);

    border: 1px solid #eee;
    box-shadow: 0px 0px 10px #ddd;

    img {
        height: 100%;
        width: 100%;
        border-radius: 50%;
        background: #eeeeee;
    }
}

.loginForm {
    margin-top: 120px;
}
.el-form-item {
    margin-left: 20px;
    margin-right: 20px;
}

.role_radio {
    margin-left: 20px;
    margin-right: 90px;
}

.login-buttons {
    display: flex;
    justify-content: flex-end;
    height: 25px;
}
</style>
