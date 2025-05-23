<!--
 * 诊断页面
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
  <div>
    <el-card>
      <el-row>
        <el-col :span="7">
          挂号单号：<el-input disabled v-model="oId" class="orderInput"></el-input>
        </el-col>
        <el-col :span="7">
          患者账号：<el-input disabled v-model="pId" class="orderInput"></el-input>
        </el-col>
        <el-col :span="7">
          患者姓名：<el-input disabled v-model="pName" class="orderInput"></el-input>
        </el-col>
        <el-col :span="3">
          <el-button size="small" type="primary" @click="submitClick">提交</el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="7">
          患者性别：<el-input disabled  v-model="pGender" class="orderInput"></el-input>
        </el-col>
        <el-col :span="7">
          联系方式：<el-input disabled v-model="pPhone" class="orderInput"></el-input>
        </el-col>
        <el-col :span="7">
          医生姓名：<el-input disabled v-model="dName" class="orderInput"></el-input>
        </el-col>
        <el-col :span="3">
          <el-button size="small" type="warning" @click="openReason">病因编写</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import request from "@/utils/request.js";
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
export default {
  name: "dealOrder",
  data() {
    return {
      oId: 1,
      pId: 1,
      dId: 0,
      pName: "aa",
      pGender: "as",
      pPhone: "asd",
      dName: "",
    };
  },
  methods: {
    submitClick() {
      // 提交逻辑
    },
    openReason() {
      // 打开病因编写对话框逻辑
    },
  },
  created() {
    // 初始化逻辑
  },
};
</script>
<style scoped>
.orderInput {
  width: 150px;
}
</style>