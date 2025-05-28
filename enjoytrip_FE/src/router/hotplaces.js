export default [
  {
    path: "/hotplaces",
    name: "HotplaceList",
    component: () => import("@/views/hotplaces/HotplaceList.vue"),
  },
  {
    path: "/hotplaces/new",
    name: "HotplaceCreate",
    component: () => import("@/views/hotplaces/HotplaceForm.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/hotplaces/:id",
    name: "HotplaceDetail",
    component: () => import("@/views/hotplaces/HotplaceDetail.vue"),
    props: true,
  },
  {
    path: "/hotplaces/:id/edit",
    name: "HotplaceEdit",
    component: () => import("@/views/hotplaces/HotplaceForm.vue"),
    props: true,
    meta: { requiresAuth: true },
  },
];
