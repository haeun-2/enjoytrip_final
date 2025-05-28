export default [
  {
    path: "/profile",
    name: "Profile",
    component: () => import("@/views/mypage/Profile.vue"),
    meta: { requiresAuth: true },
  },
];
