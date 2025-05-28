export default [
  {
    path: "/attractions",
    name: "AttractionList",
    component: () => import("@/views/attractions/AttractionList.vue"),
  },
  {
    path: "/attractions/:id",
    name: "AttractionDetail",
    component: () => import("@/views/attractions/AttractionDetail.vue"),
  },
];
