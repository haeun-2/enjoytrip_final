<template>
  <div class="tab-content">
    <div class="page-header">
      <h2 class="page-title">멤버 관리</h2>
      <div class="page-controls">
        <select v-model="memberFilter" class="filter-select">
          <option value="전체">전체</option>
          <option value="활성">활성</option>
          <option value="정지">정지</option>
          <option value="탈퇴">탈퇴</option>
        </select>
        <input v-model="memberSearch" type="text" placeholder="회원 검색..." class="search-input" />
      </div>
    </div>

    <div class="table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>프로필</th>
            <th>이름</th>
            <th>이메일</th>
            <th>가입일자</th>
            <th>생일</th>
            <th>탈퇴일자</th>
            <th>상태</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in members" :key="member.id">
            <td>
              <div class="profile-image">{{ member.name[0] }}</div>
            </td>
            <td class="font-medium">{{ member.name }}</td>
            <td>{{ member.email }}</td>
            <td>{{ member.createdAt }}</td>
            <td>{{ member.birthday }}</td>
            <td>{{ member.deletedAt || "-" }}</td>
            <td>
              <span :class="['badge', `badge-${statusMap[member.status]}`]">
                {{ member.status }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button
                  v-if="member.status === '활성'"
                  @click="handleBlockMember(member)"
                  class="btn btn-sm btn-outline btn-danger"
                >
                  🚫
                </button>
                <button v-else class="btn btn-sm btn-outline btn-success" @click="handleBlockMember(member)">♻</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";
import { getPagingMember, blockingMember, unblockingMember } from "@/apis/member/admin_member";
const memberFilter = ref("전체");
const memberSearch = ref("");
const pageRequest = ref({});
const keyword = ref("");
const status = ref("");
const statusMap = {
  null: "all",
  활성: "active",
  탈퇴: "deleted",
  정지: "blocked",
};

const members = ref([]);

const loadMembers = async () => {
  const statusValue = memberFilter.value === "전체" ? "" : memberFilter.value;
  const response = await getPagingMember(memberSearch.value, statusValue, pageRequest.value);
  members.value = response.list;
};

watch([memberFilter, memberSearch], async () => {
  pageRequest.value.page = 0;
  await loadMembers();
});

const handleBlockMember = async (member) => {
  if (member.status == "활성" && confirm(`${member.name}님의 계정을 정지하시겠습니까?`)) {
    const response = await blockingMember(member.id);
    member.status = "정지";
  } else if (member.status == "정지" && confirm(`${member.name}님의 계정을 활성하시겠습니까?`)) {
    const response = await unblockingMember(member.id);
    member.status = "활성";
  }
};

onMounted(async () => {
  pageRequest.value.page = 0;
  pageRequest.value.size = 10;
  await loadMembers();
});
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
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.badge-active {
  background-color: rgba(135, 206, 235, 0.3);
  color: var(--primary-color);
}

.badge-blocked {
  background-color: rgba(244, 63, 94, 0.1);
  color: var(--error-color);
}

.badge-deleted {
  background-color: #ecf39e;
  color: #4f772d;
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
</style>
