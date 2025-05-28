import axios from "@/apis/axios.js";

export const getlikedPlans = (memberId, { page, size }) => {
  return axios.get(`/members/${memberId}/like/plans`, {
    params: { page, size },
  });
};

export const getlikedHotplaces = (memberId, { page, size }) => {
  return axios.get(`/members/${memberId}/like/hotplaces`, {
    params: { page, size },
  });
};

export const getlikedAttractions = (memberId, { page, size }) => {
  return axios.get(`/members/${memberId}/like/attractions`, {
    params: { page, size },
  });
};

export const getlikedArticles = (memberId, { page, size }) => {
  return axios.get(`/members/${memberId}/like/articles`, {
    params: { page, size },
  });
};
