import Vue from "vue";
import VueRouter from "vue-router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import "@/assets/css/global.css";
import Login from "@/views/Login.vue";
import AdminHome from "@/views/admin/AdminHome.vue";
import AdminUser from "@/views/admin/AdminUser.vue";
import DoctorUser from "@/views/doctor/DoctorUser.vue";
import Patient from "@/views/patient/PatientUser.vue";
import PatientList from "@/views/patient/PatientList.vue";
import DoctorList from "@/views/doctor/DoctorList.vue";
import OrderList from "@/views/order/OrderList.vue";
import OrderOperate from "@/views/order/OrderOperate.vue";
import SectionMessage from "@/views/section/SectionMessage.vue";
import MyOrder from "@/views/patient/MyOrder.vue";
import OrderToday from "@/views/order/OrderToday.vue";
import DealOrder from "@/views/doctor/DealOrder.vue";
import CheckList from "@/views/check/CheckList.vue";
import DoctorOrder from "@/views/doctor/DoctorOrder.vue";
import ArrangeIndex from "@/views/arrange/ArrangeIndex.vue";
import SectionList from "@/views/section/SectionList.vue";
import DoctorHome from "@/views/doctor/DoctorHome.vue";
import PatientHome from "@/views/patient/PatientHome.vue";
import DataExpore from "@/views/DataExpore.vue";
import echarts from 'echarts'; // 引入echarts
import DealOrderAgain from "@/views/doctor/DealOrderAgain.vue";
import DoctorCard from "@/views/doctor/DoctorCard.vue";
import PatientCard from "@/views/patient/PatientCard.vue"

Vue.prototype.$echarts = echarts; // 引入echarts
Vue.use(ElementUI);
Vue.use(VueRouter);

const routes = [
  {
    path: "*",
    redirect: "/login"
  },
  {
    path: "/login",
    component: Login
  },
  {
    path: "/admin",
    component: AdminUser,
    children: [
      {
        path: "/adminLayout",
        component: AdminHome
      },
      {
        path: "/doctorList",
        component: DoctorList
      },
      {
        path: "/patientList",
        component: PatientList
      },
      {
        path: "/orderList",
        component: OrderList
      },
      {
        path: "/checkList",
        component: CheckList
      },
      {
        path: "/dataExpore",
        component: DataExpore
      },
      {
        path: "/arrangeIndex",
        component: ArrangeIndex
      },
      {
        path: "/sectionList",
        component: SectionList
      }
    ]
  },
  {
    path: "/patient",
    component: Patient,
    children: [
      {
        path: "/patientLayout",
        component: PatientHome
      },
      {
        path: "/orderOperate",
        component: OrderOperate
      },
      {
        path: "/sectionMessage",
        component: SectionMessage
      },
      {
        path: "/myOrder",
        component: MyOrder
      },
      {
        path: "/patientCard",
        component: PatientCard
      }
    ]
  },
  {
    path: "/doctor",
    component: DoctorUser,
    children: [
      {
        path: "/doctorLayout",
        component: DoctorHome
      },
      {
        path: "/orderToday",
        component: OrderToday
      },
      {
        path: "/dealOrder",
        component: DealOrder
      },
      {
        path: "/dealOrderAgain",
        component: DealOrderAgain
      },
      {
        path: "/doctorOrder",
        component: DoctorOrder
      },
      {
        path: "/doctorCard",
        component: DoctorCard
      }
    ]
  }
];

const router = new VueRouter({
  routes
});

export default router;