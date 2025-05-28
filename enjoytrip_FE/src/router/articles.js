export default [
  {
    path: "/articles/:type",
    name: "ArticleList",
    component: () => import("@/views/articles/ArticleMainList.vue"),
  },
  {
    path: "/articles/:type/new",
    name: "ArticleCreate",
    component: () => import("@/views/articles/ArticleForm.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/articles/:type/:id",
    name: "ArticleDetail",
    component: () => import("@/views/articles/ArticleDetail.vue"),
  },
  {
    path: "/articles/:type/:id/edit",
    name: "ArticleEdit",
    component: () => import("@/views/articles/ArticleForm.vue"),
    meta: { requiresAuth: true },
  },
];
