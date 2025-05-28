import axios from "@/apis/axios.js";

export const getAllSido = () => {
  return axios.get(`/region/sido`);
};

export const getAllGugun = (sidoCode) => {
  return axios.get("/region/v2/gugun", {
    params: { sidoCode },
  });
};
export const getSidoList = () => {
  return axios.get("/region/sido");
};

export const getGugunList = (sidoId) => {
  return axios.get("/region/gugun", { params: { sidoId } });
};
