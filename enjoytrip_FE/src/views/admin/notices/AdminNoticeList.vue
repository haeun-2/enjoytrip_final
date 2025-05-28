<template>
  <div class="tab-content">
    <div class="page-header">
      <h2 class="page-title">공지 관리</h2>
      <button class="btn btn-primary" @click="wirteNotice">새 공지 작성</button>
    </div>

    <div class="search-bar-container">
      <BaseSearchBar @search="changeTitle" />
      <BaseSortOption
        :options="[
          { value: 'created_at', name: '최신순' },
          { value: 'view_count', name: '조회순' },
        ]"
        @changeSortOpt="changeSortOpt"
      />
    </div>

    <div class="table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>#</th>
            <th>제목</th>
            <th>작성일</th>
            <th>수정일</th>
            <th>삭제일</th>
            <th>조회수</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="notice in notices" :key="notice.id">
            <td>{{ notice.id }}</td>
            <td class="font-medium">{{ notice.title }}</td>
            <td>{{ formatDateTime(notice.createdAt) }}</td>
            <td>{{ formatDateTime(notice.modifiedAt) }}</td>
            <td>{{ formatDateTime(notice.deletedAt) }}</td>
            <td>{{ notice.viewCount }}</td>
            <td>
              <div class="action-buttons">
                <button class="btn btn-sm btn-outline btn-show" @click="goNotice(notice)">보기</button>
                <button class="btn btn-sm btn-outline btn-edit" @click="editNotice(notice)">수정</button>
                <button class="btn btn-sm btn-outline btn-delete" @click="deleteNotice(notice)">삭제</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <BasePagination :page="page" @changePage="changePage" />
  </div>
</template>

<script setup>
import BasePagination from "@/components/common/BasePagination.vue";
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BaseSortOption from "@/components/common/BaseSortOption.vue";
import * as adminArticleApi from "@/apis/admin/article";
import { formatDateTime } from "@/utils/date";

import { ref, watch, onMounted } from "vue";
import { useRouter } from "vue-router";

// 공지사항 데이터 (실제로는 API에서 가져와야 함)
const notices = ref([]);
const searchCondition = ref({
  page: 1,
  size: 12,
  sort: "created_at",
  title: null,
  articleType: "NOTICE",
  mine: false,
});
const page = ref({});

const changePage = (num) => {
  searchCondition.value.page = num;
};

const changeTitle = (title) => {
  if (!title.trim()) searchCondition.value.title = null;
  else searchCondition.value.title = title;
};

const changeSortOpt = (sort) => {
  searchCondition.value.sort = sort;
};

const router = useRouter();
const wirteNotice = () => {
  router.push({ name: "AdminNoticeCreate" });
};

const goNotice = (notice) => {
  router.push({ name: "ArticleDetail", params: { type: "notice", id: notice.id } });
};

const editNotice = (notice) => {
  router.push({ name: "AdminNoticeEdit", params: { id: notice.id } });
};

const deleteNotice = async (notice) => {
  if (confirm("공지를 삭제하시겠습니까?")) {
    try {
      const response = await adminArticleApi.deleteArticle(notice.id);
      alert(response);
      notice.deletedAt = new Date().toISOString();
    } catch (e) {
      alert("공지사항 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  }
};

const loadNotices = async () => {
  try {
    const response = await adminArticleApi.searchArticle(searchCondition.value);
    notices.value = response.list;
    page.value = {
      navSize: response.navSize,
      totalCount: response.totalCount,
      size: searchCondition.value.size,
      currentPage: response.currentPage,
      startPage: response.startPage,
      endPage: response.endPage,
      hasPre: response.hasPre,
      hasNext: response.hasNext,
    };
  } catch (e) {
    alert("공지사항 목록 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

onMounted(() => {
  loadNotices();
});

watch(
  searchCondition,
  () => {
    loadNotices();
  },
  { deep: true }
);
</script>

<style scoped>
.search-bar-container {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.page-title {
  font-size: 1.875rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  color: #1e293b;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.page-controls {
  display: flex;
  gap: 0.5rem;
}

.tab-content {
  max-width: 100%;
}

/* 테이블 */
.table-card {
  margin: 20px 0;
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
}

.data-table th {
  background: #f8fafc;
  font-weight: 600;
  color: #374151;
}

.data-table tr:hover {
  background: #f8fafc;
}

.font-medium {
  font-weight: 500;
}

/* 버튼 */
.btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.2s;
}

.btn-primary {
  background: #3b82f6;
  color: white;
}

.btn-primary:hover {
  background: #2563eb;
}

.btn-outline {
  background: transparent;
  border: 1px solid #d1d5db;
  color: #374151;
}

.btn-outline:hover {
  background: #f9fafb;
}

.btn-success {
  background: #10b981;
  color: white;
}

.btn-success:hover {
  background: #059669;
}

.btn-danger {
  background: #ef4444;
  color: white;
}

.btn-danger:hover {
  background: #dc2626;
}

.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
}

.btn-show:hover {
  background-color: rgba(135, 206, 235, 0.3);
  color: var(--primary-color);
}

.btn-delete:hover {
  background-color: rgba(244, 63, 94, 0.1);
  color: var(--error-color);
}

.btn-edit:hover {
  background-color: #ecf39e;
  color: #4f772d;
}

/* 액션 버튼 */
.action-buttons {
  display: flex;
  gap: 0.25rem;
}
</style>
