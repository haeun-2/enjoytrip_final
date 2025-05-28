import axios from "@/apis/axios";

export const getAttractionPage = (
  searchCondition,
  sortCondition,
  pageRequest
) => {
  const params = {
    sidoCode: searchCondition.sido,
    gugunCode: searchCondition.gugun,
    categoryId: searchCondition.category,
    sort: sortCondition,
    ...pageRequest,
  };
  return axios.get("/attractions/filter", { params });
};

export const getAttractionDetail = (attractionId) => {
  return axios.get(`/attractions/${attractionId}`);
};

export const getAttractionByKeyword = (keyword) => {
  return axios.get(`/attractions/search`, {
    params: { keyword },
  });
};

export const likeAttraction = (attractionId) => {
  return axios.post("/attractions/like", null, { params: { attractionId } });
};

export const unlikeAttraction = (attractionId) => {
  return axios.delete("/attractions/unlike", { params: { attractionId } });
};
