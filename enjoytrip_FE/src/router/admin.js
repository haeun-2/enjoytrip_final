export default [
  {
    path: "/admin",
    component: () => import("@/views/admin/AdminLayout.vue"),
    // meta: { onlyAdmin: true },

    children: [
      {
        path: "dashboard",
        name: "AdminDashboard",
        component: () => import("@/views/admin/AdminDashboard.vue"),
      },
      {
        path: "members",
        name: "AdminMemberList",
        component: () => import("@/views/admin/members/AdminMemberList.vue"),
      },
      {
        path: "notices",
        name: "AdminNoticeList",
        component: () => import("@/views/admin/notices/AdminNoticeList.vue"),
      },
      {
        path: "notices/new",
        name: "AdminNoticeCreate",
        component: () => import("@/views/admin/notices/AdminNoticeForm.vue"),
      },
      {
        path: "notices/:id/edit",
        name: "AdminNoticeEdit",
        component: () => import("@/views/admin/notices/AdminNoticeForm.vue"),
      },
      {
        path: "qna",
        name: "AdminQnAList",
        component: () => import("@/views/admin/qna/AdminQnAList.vue"),
      },
      {
        path: "qna/:id",
        name: "AdminQnADetail",
        component: () => import("@/views/admin/qna/AdminQnADetail.vue"),
      },
      {
        path: "reports",
        name: "AdminReportList",
        component: () => import("@/views/admin/reports/AdminReportList.vue"),
      },
    ],
  },
];
