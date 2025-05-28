import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    user: null,
  }),
  actions: {
    setUser(user) {
      this.user = user;
    },
    clearUser() {
      this.user = null;
    },
  },
  getters: {
    isLogined: (state) => !!state.user,
    isAdmin: (state) => state.user?.role === "ADMIN",
    isAuthor: (state) => (authorId) => {
      return state.user?.id === authorId;
    },
  },
  persist: {
    enabled: true,
    strategies: [
      {
        storage: localStorage, // 또는 sessionStorage
        paths: ["user"], // 저장할 state만 지정
      },
    ],
  },
});
