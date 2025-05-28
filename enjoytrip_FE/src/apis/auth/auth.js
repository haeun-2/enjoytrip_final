import axios from '@/apis/axios.js';

export const login = (email, password) => {
  return axios.post('/auth/login', { email, password });
};

export const sendEmailCode = (email) => {
  return axios.post("/auth/email/send", null, {
    params: {email}
  });
}

export const verifyEmailCode = (email, code) => {
  return axios.get("/auth/email/verify", {
    params: { email, code },
  });
}

export const signup = (userInfo) => {
    return axios.post("/auth/signin", userInfo)
}

export const logout = () => {
  return axios.post("/auth/logout")
}