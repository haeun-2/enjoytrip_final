import axios from "@/apis/axios.js";

export const getStatCount = () => {
  return axios.get(`/admin/stat/count`);
};

export const getMemberCount = () => {
  return axios.get("/admin/stat/member");
};

export const getMonthlyStat = () => {
  return axios.get("/admin/stat/monthly");
};

export const getCatogoryCount = () => {
  return axios.get("/admin/stat/category");
};
