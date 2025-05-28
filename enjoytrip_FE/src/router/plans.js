export default [
  {
    path: "/plans",
    name: "PlanList",
    component: () => import("@/views/plans/PlanList.vue"),
    props: true,
  },
  {
    path: "/plans/new",
    name: "PlanCreate",
    component: () => import("@/views/plans/PlanForm.vue"),
    props: (route) => ({ planInfo: JSON.parse(route.query.planInfo) }),
  },
  {
    path: "/plans/:id",
    name: "PlanDetail",
    component: () => import("@/views/plans/PlanDetail.vue"),
  },
  {
    path: "/plans/:id/edit",
    name: "PlanEdit",
    component: () => import("@/views/plans/PlanForm.vue"),
  },
  {
    path: "/plans/mine",
    name: "PlanMine",
    component: () => import("@/views/plans/PlanMine.vue"),
  },
];
