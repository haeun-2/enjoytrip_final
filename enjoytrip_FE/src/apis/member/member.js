import axios from "@/apis/axios.js";

export const getMemberProfile = (memberId) => {
  return axios.get(`/members/${memberId}/profile`);
};

export const editMemberProfile = (
  memberId,
  { name, birthDate, profileImage }
) => {
  return axios.put(`/members/${memberId}/profile`, {
    name,
    birthDate,
    profileImage,
  });
};

export const changePassword = (memberId, currentPassword, newPassword) => {
  return axios.put(`/members/${memberId}/password`, {
    currentPassword,
    newPassword,
  });
};

export const signout = (memberId) => {
  return axios.delete(`/members/${memberId}`);
};
