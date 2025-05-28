import axios from "@/apis/axios.js";

export const searchArticleReports = (searchParams) => {
  return axios.get(`/admin/report/articles`, { params: searchParams });
};
export const searchCommentReports = (searchParams) => {
  return axios.get(`/admin/report/comments`, { params: searchParams });
};

export const changeArticleStatus = (id, status) => {
  return axios.put(`/admin/report/articles/${id}`, { status });
};

export const changeCommentStatus = (id, status) => {
  return axios.put(`/admin/report/comments/${id}`, { status });
};
