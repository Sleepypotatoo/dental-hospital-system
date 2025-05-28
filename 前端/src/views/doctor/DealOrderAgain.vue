<!--
 * 追诊页面
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
          <el-button type="primary" size="small" style="font-size: 18px;" icon="iconfont icon-sure-button" @click="submitClick"> 提交</el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="7">
          患者性别：<el-input disabled v-model="pGender" class="orderInput"></el-input>
        </el-col>
        <el-col :span="7">
          联系方式：<el-input disabled v-model="pPhone" class="orderInput"></el-input>
        </el-col>
        <el-col :span="7">
          医生姓名：<el-input disabled v-model="dName" class="orderInput"></el-input>
        </el-col>
        <el-col :span="3">
          <el-button size="small" type="warning" style="font-size: 18px;" @click="openAdvice">
            <i class="iconfont icon-add-button" style="font-size: 20px;"></i>
            诊断/意见
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 病因编写对话框 -->
    <el-dialog title="诊断及医生建议编写" :visible.sync="adviceFormVisible">
      <el-input type="textarea" :rows="8" placeholder="请输入内容" v-model="advice"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adviceFormVisible = false" style="font-size: 18px;">
          <i class="iconfont icon-cancel-button" style="font-size: 20px;"></i> 取 消
        </el-button>
        <el-button type="primary" @click="holdAdvice">
          <i class="iconfont icon-sure-button" style="font-size: 20px;"></i>  保 存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";
export default {
  name: "dealOrderAgain",
  data() {
    return {
      oId: 1,
      pId: 1,
      dId: 0,
      pName: "aa",
      pGender: "as",
      pPhone: "asd",
      dName: "",
      advice: "",
      adviceFormVisible: false,
    };
  },
  methods: {
    submitClick() {
      // 提交逻辑
    },
    openAdvice() {
      this.adviceFormVisible = true;
    },
    holdAdvice() {
      // 保存建议逻辑
      this.adviceFormVisible = false;
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