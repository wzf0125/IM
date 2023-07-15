import request from "@/util/request";

let prefix = "/user";

export const login = (params) => request.post(prefix+"/login", params);

export const register = (params) => request.post(prefix+"/register", params);