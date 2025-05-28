import axios from "@/apis/axios.js";

export const loadComment = (articleId) => {
  return axios.get(`/articles/${articleId}/comments`);
};

export const editComment = (articleId, commentId, content) => {
  return axios.put(`articles/${articleId}/comments/${commentId}`, { content });
};

export const deleteComment = (articleId, commentId) => {
  return axios.delete(`articles/${articleId}/comments/${commentId}`);
};

export const createComment = (articleId, parentId, content) => {
  return axios.post(`/articles/${articleId}/comments`, { parentId, content });
};

export const reportComment = (articleId, commentId, reason) => {
  return axios.post(`/articles/${articleId}/comments/report/${commentId}`, {
    reason,
  });
};
