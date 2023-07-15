import axios from "axios";
import { Promise } from "core-js";
import router from "@/router";

let prodHost = "http://121.5.75.176:8004";
let devHost = "http://localhost:8001";
let host = devHost;
import { showSuccess, showError } from "./utils.js";

axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

const http = axios.create({
  baseURL: host,
  timeout: 3000,
});

// 响应拦截器
http.interceptors.response.use(
  (response) => {
    if (response.status === 200) {
      let msg = response.data.msg;
      console.log(response.data);
      switch (response.data.code) {
        case 200:
          // showSuccess(msg);
          return Promise.resolve(response.data);
        case 400:
        case 401:
        case 402:
          showError(msg);
          return Promise.reject(response.data);
        case 403:
          sessionStorage.setItem("Authorization", null);
          showError(msg);
          router.push("/login");
          return Promise.reject(response.data);
        case 500:
          // showError("服务器内部错误");
          showError(msg);
          return Promise.reject(response.data);
      }
    } else {
      showError("服务器内部错误");
    }
  },
  (error) => {
    showError("服务器内部错误");
  }
);

export default http;
