import axios from "@/apis/axios.js";
import qs from "qs";

export const getList = (sort, searchFilter, searchCategory, pageRequest) => {
  const params = {
    sort: sort,
    ...searchFilter,
    tripStyle: searchCategory,
    ...pageRequest,
  };
  return axios.get("/plans", {
    params,
    paramsSerializer: (params) =>
      qs.stringify(params, { arrayFormat: "repeat" }),
  });
};

export const getDetail = (id) => {
  return axios.get(`/plans/${id}`);
};

export const createTripPlan = (planInfo) => {
  return axios.post("plans", planInfo);
};

export const createDetailTripPlan = (planId, planDetail) => {
  return axios.post(`plans/${planId}`, planDetail);
};

export const deleteTripPlan = (planId) => {
  return axios.delete(`plans/${planId}`);
};

export const likePlan = (tripPlanId) => {
  return axios.post("plans/like", {}, { params: { tripPlanId } });
};

export const unLikePlan = (tripPlanId) => {
  return axios.delete("plans/like", { params: { tripPlanId } });
};

export const getTrendPlan = () => {
  return axios.get("plans/trending");
};

export const getPlansWithStatus = (status, pageRequest) => {
  const params = {
    status,
    ...pageRequest,
  };
  return axios.get("plans/mine", { params });
};
