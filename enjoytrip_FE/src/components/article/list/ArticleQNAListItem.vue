<template>
  <div>
    <div class="qna-header">
      <div class="qna-status-category">
        <div :class="['status-badge', answered ? 'status-answered' : 'status-waiting']">
          {{ answered ? "답변 완료" : "답변 대기" }}
        </div>
      </div>
      <div class="qna-date">{{ formatDateTime(question.createdAt) }}</div>
    </div>

    <h3 class="qna-title">{{ question.title }}</h3>

    <p class="qna-excerpt">{{ question.content.substring(0, 150) }}{{ question.content.length > 150 ? "..." : "" }}</p>

    <div class="qna-footer">
      <div class="qna-author">
        <div class="author-avatar">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="user-icon"
          >
            <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
        </div>
        <span>{{ question.member.name }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { formatDateTime } from "@/utils/date";
import { computed } from "vue";
const props = defineProps({
  question: Object,
  answer: Object,
  status: String,
});
const answered = computed(() => {
  return props.status == "ANSWERED" || props.status == "answered";
});
</script>

<style scoped>
.qna-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
}

.qna-status-category {
  display: flex;
  align-items: center;
  gap: 0.5rem;
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

.category-badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  background-color: #f1f5f9;
  color: #666;
  border-radius: 9999px;
  font-size: 0.75rem;
}

.qna-date {
  font-size: 0.75rem;
  color: #666;
}

.qna-title {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #333;
}

.qna-excerpt {
  font-size: 0.875rem;
  color: #666;
  margin-bottom: 1rem;
  line-height: 1.5;
}

.qna-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.qna-author {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #666;
}

.author-avatar {
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  background-color: #f1f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
}

.qna-stats {
  display: flex;
  gap: 0.75rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.75rem;
  color: #666;
}
</style>
