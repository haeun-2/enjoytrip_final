import axios from "@/apis/axios.js";

export const searchArticle = (searchParams) => {
  return axios.get(`/articles`, {
    params: searchParams,
  });
};
export const detailArticle = (id) => {
  return axios.get(`articles/${id}`);
};

export const editArticle = (id, article) => {
  return axios.put(`articles/${id}`, article);
};

export const deleteArticle = (id) => {
  return axios.delete(`articles/${id}`);
};

export const createArticle = (article) => {
  return axios.post(`/articles`, article);
};

export const reportArticle = (id, reason) => {
  return axios.post(`/articles/report/${id}`, { reason });
};

export const likeArticleApi = (articleId) => {
  return axios.post(`/articles/like`, null, { params: { articleId } });
};

export const unlikeArticleApi = (articleId) => {
  return axios.delete(`/articles/unlike`, {
    params: { articleId },
  });
};

export const searchQna = (searchParmas) => {
  return axios.get(`/articles/qna`, {
    params: searchParmas,
  });
};
