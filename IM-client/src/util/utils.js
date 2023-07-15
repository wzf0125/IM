import { Message, MessageBox } from "element-ui";
export function showError(msg) {
  Message({
    showClose: true,
    message: msg,
    type: "error",
  });
}

export function showSuccess(msg) {
  Message({
    showClose: true,
    message: msg,
    type: "success",
  });
}

export function getTokenHeader() {
  let config = {
    headers: {
      Authorization: sessionStorage.getItem("Authorization"),
    },
  };
  return config;
}
