import axios from "@/apis/axios.js";

// 공지사항
export const searchArticle = (searchParams) => {
  return axios.get(`/admin/articles`, { params: searchParams });
};
export const detailArticle = (id) => {
  return axios.get(`/admin/articles/${id}`);
};

export const editArticle = (id, article) => {
  return axios.put(`/admin/articles/${id}`, article);
};

export const deleteArticle = (id) => {
  return axios.delete(`/admin/articles/${id}`);
};

export const createArticle = (article) => {
  return axios.post(`/admin/articles`, article);
};

// QnA
export const searchQna = (searchParams) => {
  return axios.get(`/admin/articles/qna`, { params: searchParams });
};

export const createAnswer = (questionId, answerId, content) => {
  return axios.post(`/admin/articles/qna`, {
    questionId,
    answerId,
    content,
  });
};
