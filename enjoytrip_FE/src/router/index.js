import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/stores/user";
import authRoutes from "./auth";
import attractionRoutes from "./attractions";
import articleRoutes from "./articles";
import hotplaceRoutes from "./hotplaces";
import planRoutes from "./plans";
import profileRoutes from "./profile";
import commonRoutes from "./common";
import adminRoutes from "./admin";

const routes = [
  ...authRoutes,
  ...attractionRoutes,
  ...articleRoutes,
  ...hotplaceRoutes,
  ...planRoutes,
  ...profileRoutes,
  ...commonRoutes,
  ...adminRoutes,
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 전역 네비게이션 가드
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  const isLoggedIn = !!userStore.user;
  const isAdmin = userStore.user?.role == "ADMIN";

  if (to.meta.onlyAdmin && !isAdmin) {
    alert("접근 권한이 없습니다");
    next({ name: "Home" });
  }
  if (to.meta.requiresAuth && !isLoggedIn) {
    alert("로그인이 필요한 기능입니다. 로그인 후 이용해주세요.");
    next({ name: "Login" });
  } else {
    next();
  }
});

export default router;
