<template>
  <div class="edit-delete-report">
    <!-- 댓글 수정 버튼 -->
    <button v-if="isAuthor" @click="$emit('edit')" class="edit-button">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="12"
        height="12"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
        class="edit-icon"
      >
        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
      </svg>
      <span>수정</span>
    </button>
    <!-- 댓글 삭제 버튼튼 -->
    <button v-if="isAuthor" @click="$emit('delete')" class="delete-button">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="12"
        height="12"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
        class="trash-icon"
      >
        <polyline points="3 6 5 6 21 6"></polyline>
        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
        <line x1="10" y1="11" x2="10" y2="17"></line>
        <line x1="14" y1="11" x2="14" y2="17"></line>
      </svg>
      <span>삭제</span>
    </button>
    <!-- 댓글 신고 버튼 -->
    <button @click="$emit('report')" class="report-button">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="12"
        height="12"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
        class="flag-icon"
      >
        <path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z"></path>
        <line x1="4" y1="22" x2="4" y2="15"></line>
      </svg>
      <span>신고</span>
    </button>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useUserStore } from "@/stores/user";
const userStore = useUserStore();

const props = defineProps(["authorId"]);
const isAuthor = computed(() => userStore.isAuthor(props.authorId));
</script>

<style scoped>
.edit-delete-report {
  display: flex;
  gap: 8px;
}

.edit-button,
.delete-button,
.report-button {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: none;
  border: none;
  padding: 0;
  font-size: 12px;
  color: var(--text-lighter);
  cursor: pointer;
  transition: color 0.2s;
}

.edit-button:hover,
.delete-button:hover,
.report-button:hover {
  color: var(--text-light);
}

.edit-icon,
.trash-icon,
.flag-icon {
  color: inherit;
}
</style>
