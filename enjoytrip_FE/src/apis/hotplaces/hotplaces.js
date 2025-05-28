import axios from "@/apis/axios.js";
import qs from "qs";

export const searchHotplace = (searchParams) => {
  return axios.get(`/hotplaces`, {
    params: searchParams,
    paramsSerializer: (params) => qs.stringify(params, { arrayFormat: "repeat" }),
  });
};

export const detailHotplace = (id) => {
  return axios.get(`hotplaces/${id}`);
};

export const editHotplace = (id, hotplace) => {
  return axios.put(`hotplaces/${id}`, hotplace);
};

export const deleteHotplace = (id) => {
  return axios.delete(`hotplaces/${id}`);
};

export const createHotplace = (hotplace) => {
  return axios.post(`/hotplaces`, hotplace);
};

export const likeHotplace = (id) => {
  return axios.post(`/hotplaces/like`, null, {
    params: { memberPlaceId: id },
  });
};

export const deletelikeHotplace = (id) => {
  return axios.delete(`/hotplaces/like`, {
    params: { memberPlaceId: id },
  });
};
