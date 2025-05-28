<template>
  <div class="tab-content">
    <div class="page-header">
      <h2 class="page-title">신고 관리</h2>
      <div class="options">
        <BaseSortOption
          :options="[
            { value: 'article', name: '게시글' },
            { value: 'comment', name: '댓글' },
          ]"
          @changeSortOpt="changeType"
        />
        <BaseSortOption :options="[{ value: null, name: '전체' }, ...statusList]" @changeSortOpt="changeStatus" />
      </div>
    </div>

    <div class="table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>유형</th>
            <th>제목/내용</th>
            <th>신고자</th>
            <th>신고일</th>
            <th>상태</th>
            <th>처리일</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="report in reports" :key="report.id">
            <td>
              <span class="badge badge-outline">{{ report.targetType === "Article" ? "게시글" : "댓글" }}</span>
            </td>
            <td class="font-medium">{{ report.targetName.substr(0, 10) }}</td>
            <td>{{ report.memberName }}</td>
            <td>{{ formatDateTime(report.createdAt) }}</td>
            <td>
              <div
                class="status-badge"
                :class="{
                  'status-pending': report.status == 'PENDING',
                  'status-approved': report.status == 'APPROVED',
                  'status-rejected': report.status == 'REJECTED',
                }"
              >
                {{ report.status }}
              </div>
            </td>
            <td>{{ formatDateTime(report.handledAt) }}</td>
            <td>
              <div class="action-buttons">
                <button class="btn btn-sm btn-outline" @click="viewReport(report)">보기</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <BasePagination :page="page" @changePage="changePage" />
  </div>

  <!-- 신고 상세 모달 -->
  <div v-if="showReportModal" class="modal-overlay" @click="showReportModal = false">
    <div class="modal" @click.stop>
      <div class="modal-header">
        <h3>신고 상세 정보</h3>
        <button class="modal-close" @click="showReportModal = false">×</button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>신고된 내용</label>
          <div class="content-box">{{ selectedReport?.targetName }}</div>
        </div>
        <div class="form-group">
          <label>신고 사유</label>
          <div class="content-box">
            {{ selectedReport?.reason }}
          </div>
        </div>
      </div>
      <div v-show="selectedReport?.status == 'PENDING'" class="modal-footer">
        <button class="btn btn-danger" @click="rejectReport(selectedReport)">거부</button>
        <button class="btn btn-primary" @click="approveReport(selectedReport)">승인</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import BaseSortOption from "@/components/common/BaseSortOption.vue";
import { ref, watch, onMounted } from "vue";
import * as adminReportApi from "@/apis/admin/report";
import BasePagination from "@/components/common/BasePagination.vue";
import { formatDateTime } from "@/utils/date";

const showReportModal = ref(false);
const selectedReport = ref(null);

const statusList = [
  { value: "PENDING", name: "대기" },
  { value: "APPROVED", name: "승인" },
  { value: "REJECTED", name: "반려" },
];

const reports = ref([]);
const searchCondition = ref({
  page: 1,
  size: 8,
  status: null,
  type: "article",
});
const page = ref({});

const changePage = (num) => {
  searchCondition.value.page = num;
};

const changeType = (type) => {
  searchCondition.value.type = type;
};

const changeStatus = (status) => {
  searchCondition.value.status = status;
};

const loadReports = async () => {
  try {
    let response;
    if (searchCondition.value.type == "article")
      response = await adminReportApi.searchArticleReports(searchCondition.value);
    else response = await adminReportApi.searchCommentReports(searchCondition.value);
    reports.value = response.list;
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
    alert("신고 목록 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

onMounted(() => {
  loadReports();
});

watch(
  searchCondition,
  () => {
    loadReports();
  },
  { deep: true }
);

const changeReportStatus = async (report, status) => {
  try {
    if (report.targetType == "Article") {
      await adminReportApi.changeArticleStatus(report.id, status);
    } else {
      await adminReportApi.changeCommentStatus(report.id, status);
    }
    report.status = status;
    report.handledAt = new Date().toISOString();
    showReportModal.value = false;
  } catch (e) {
    alert("신고 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

const approveReport = (report) => {
  changeReportStatus(report, "APPROVED");
};

const rejectReport = (report) => {
  changeReportStatus(report, "REJECTED");
};

const viewReport = (report) => {
  selectedReport.value = report;
  showReportModal.value = true;
};
</script>

<style scoped>
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

/* 프로필 이미지 */
.profile-image {
  width: 2.5rem;
  height: 2.5rem;
  background: #3b82f6;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

/* 배지 */
.badge {
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 500;
}

.badge-outline {
  background: transparent;
  border: 1px solid #d1d5db;
  color: #374151;
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
  background: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-hover);
}

.btn-outline {
  background: transparent;
  border: 1px solid #d1d5db;
  color: #374151;
}

.btn-outline:hover {
  background: #f9fafb;
}

.btn-danger {
  background: var(--error-color);
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

/* 폼 요소 */
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

/* 모달 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  max-width: 32rem;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 0 1rem;
  border: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1e293b;
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #64748b;
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  padding: 1.5rem;
  border-top: 1px solid #e2e8f0;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #374151;
}

.content-box {
  padding: 0.75rem;
  background: #f8fafc;
  border-radius: 0.375rem;
  border: 1px solid #e2e8f0;
  color: #374151;
}

.status-badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.status-approved {
  background-color: rgba(135, 206, 235, 0.3);
  color: var(--primary-color);
}

.status-rejected {
  background-color: rgba(244, 63, 94, 0.1);
  color: var(--error-color);
}

.status-pending {
  background-color: #ecf39e;
  color: #4f772d;
}

.options {
  display: flex;
  gap: 20px;
}
</style>
