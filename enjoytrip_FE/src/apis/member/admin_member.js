import axios from "@/apis/axios.js";

export const blockingMember = (memberId) => {
  return axios.post(`admin/members/block`, null, {
    params: { memberId },
  });
};

export const unblockingMember = (memberId) => {
  return axios.delete(`admin/members/block`, {
    params: { memberId },
  });
};

export const getPagingMember = (keyword, status, { page, size }) => {
  return axios.get(`admin/members`, {
    params: { page, size, status, keyword },
  });
};
