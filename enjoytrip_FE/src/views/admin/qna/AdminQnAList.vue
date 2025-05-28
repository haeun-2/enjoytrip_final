<template>
  <div class="tab-content">
    <div class="page-header">
      <h2 class="page-title">QnA 관리</h2>
    </div>

    <div class="search-bar-container">
      <BaseSearchBar @search="changeTitle" />

      <!-- 답변 상태: 전체 질문, 답변 완료, 답변 대기 -->
      <BaseSortOption
        :options="[
          { value: 'all', name: '전체 질문' },
          { value: 'answered', name: '답변 완료' },
          { value: 'pending', name: '답변 대기' },
        ]"
        @changeSortOpt="changeStatus"
      />
    </div>

    <div class="table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>#</th>
            <th>제목</th>
            <th>작성자</th>
            <th>질문 작성일</th>
            <th>답변 작성일</th>
            <th>상태</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="qna in qnaList" :key="qna.question.id">
            <td>{{ qna.question.id }}</td>
            <td class="font-medium">{{ qna.question.title }}</td>
            <td>{{ qna.question.member.name }}</td>
            <td>{{ formatDateTime(qna.question.createdAt) }}</td>
            <td>{{ formatDateTime(qna.answer.createdAt) }}</td>
            <td>
              <div :class="['status-badge', qna.status == 'ANSWERED' ? 'status-answered' : 'status-waiting']">
                {{ qna.status == "ANSWERED" ? "답변 완료" : "답변 대기" }}
              </div>
            </td>
            <td>
              <button class="btn btn-sm btn-outline" @click="showAnswer(qna)">
                {{ qna.status == "ANSWERED" ? "답변보기" : "답변하기" }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <BasePagination :page="page" @changePage="changePage" />
  </div>
  <AdminQnADetail :isOpen="isOpen" :selectedQna="selectedQna" @close="close" @createAnswer="createAnswer" />
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import AdminQnADetail from "./AdminQnADetail.vue";
import * as adminArticleApi from "@/apis/admin/article";
import BasePagination from "@/components/common/BasePagination.vue";
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BaseSortOption from "@/components/common/BaseSortOption.vue";
import { formatDateTime } from "@/utils/date";

const isOpen = ref(false);
const selectedQna = ref(null);

const searchCondition = ref({
  page: 1,
  size: 12,
  status: "all",
  title: null,
});
const page = ref({});

const changePage = (num) => {
  searchCondition.value.page = num;
};

const changeTitle = (title) => {
  if (!title.trim()) searchCondition.value.title = null;
  else searchCondition.value.title = title;
};

const changeStatus = (status) => {
  searchCondition.value.status = status;
};

const qnaList = ref([]);

const loadQnaList = async () => {
  try {
    const response = await adminArticleApi.searchQna(searchCondition.value);
    qnaList.value = response.list;
    page.value = {
      navSize: response.navSize,
      currentPage: response.currentPage,
      startPage: response.startPage,
      endPage: response.endPage,
      hasPre: response.hasPre,
      hasNext: response.hasNext,
    };
  } catch (e) {
    alert("QnA 목록 조회 중 오류가 발생했습니다.  다시 시도해주세요.");
  }
};

onMounted(() => {
  loadQnaList();
});

watch(
  searchCondition,
  () => {
    loadQnaList();
  },
  { deep: true }
);

const createAnswer = async (qna, content) => {
  try {
    const response = await adminArticleApi.createAnswer(qna.question.id, qna.answer.id, content);
    qna.status = "ANSWERED";
    qna.answer.content = content;
    qna.answer.createdAt = new Date().toISOString();
  } catch (e) {
    alert("답변 등록 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// Qna 모달창 열기
const showAnswer = (qna) => {
  selectedQna.value = qna;
  isOpen.value = true;
};

// 모달창 닫기
const close = () => {
  isOpen.value = false;
};
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

/* 액션 버튼 */
.action-buttons {
  display: flex;
  gap: 0.25rem;
}

.filter-select,
.search-input,
.form-input {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
}

.search-input {
  width: 16rem;
}

.form-textarea {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  min-height: 8rem;
  resize: vertical;
  width: 100%;
}

.status-badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.status-answered {
  background-color: rgba(135, 206, 235, 0.3);
  color: var(--primary-color);
}

.status-waiting {
  background-color: rgba(244, 63, 94, 0.1);
  color: var(--error-color);
}
</style>
