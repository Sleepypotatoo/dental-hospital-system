<!--
 * 管理员用户管理页面
 *
 * @Author: ShanZhu
 * @Date: 2023-11-17
-->
<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-vertical-demo"
    @select="handleSelect"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
  >
    <el-submenu index="sub-menu1">
      <template slot="title">
        <span style="font-size: 18px;color: #2b5d88">用户管理</span>
      </template>
      <el-menu-item index="doctorList" @click="menuClick('doctorList')">
        <i class="iconfont icon-doctor-menu" style="font-size: 18px;"> 医生用户管理</i>
      </el-menu-item>

      <el-menu-item index="patientList" @click="menuClick('patientList')">
        <i class="iconfont icon-patient-menu" style="font-size: 18px;"> 患者用户管理</i>
      </el-menu-item>
    </el-submenu>

    <el-submenu index="sub-menu2">
      <template slot="title">
        <span style="font-size: 18px;color: #2b5d88">挂号管理</span>
      </template>
      <el-menu-item index="orderList" @click="menuClick('orderList')">
        <i class="iconfont icon-orders-menu" style="font-size: 18px;"> 挂号信息管理</i>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>
<script>
import request from "@/utils/request.js";
export default {
  name: "AdminUser",
  data() {
    return {
      activeIndex: "1",
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    menuClick(path) {
      this.$router.push({ path: `/${path}` });
    },
  },
};
</script>
<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>