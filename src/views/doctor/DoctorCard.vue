<template>
    <div>
        <el-card>
            <table>
                <tr>
                    <td>平均评分：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dAvgStar"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>简介：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dIntroduction"
                            type="textarea"
                            :rows="4"
                        ></el-input>
                    </td>
                </tr>
            </table>
        </el-card>
    </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";

export default {
    name: "DoctorCard",
    data() {
        return {
            userId: "",
            doctorData: {},
        };
    },
    methods: {
        // 请求医生信息
        requestDoctor() {
            // 注释掉以下代码，使用 mock 后端时暂时不需要
            request
               .get("admin/findDoctor", {
                    params: {
                        dId: this.userId,
                    },
                })
               .then((res) => {
                    if (res.data.status != 200)
                        return this.$message.error("获取数据失败");
                    this.doctorData = res.data.data;
                });
        },
        // token 解码
        tokenDecode(token) {
            if (token && typeof token === 'string') {
                try {
                    return jwtDecode(token);
                } catch (error) {
                    console.error('Token 解码失败:', error);
                    return null;
                }
            }
            return null;
        },
    },
    created() {
        // 解码 token 信息
        const token = getToken();
        const decodedToken = this.tokenDecode(token);
        if (decodedToken) {
            this.userId = decodedToken.dId;
            // 注释掉以下代码，使用 mock 后端时暂时不需要
            this.requestDoctor();
        } else {
            // 处理 token 无效的情况，例如跳转到登录页
            // this.$router.push('login');
        }
    },
};
</script>

<style lang="scss" scope>
td, th {
    white-space: nowrap;
    padding: 10px;
}
</style>