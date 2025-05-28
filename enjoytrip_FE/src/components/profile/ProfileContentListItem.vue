<template>
  <div class="list-item">
    <div class="item-content">
      <h3 class="item-title">{{ item.title }}</h3>
      <div class="item-meta">
        <span class="meta-item">{{
          item.createdAt ? formatDate(item.createdAt) : formatDate(item.startDate) + "~" + formatDate(item.endDate)
        }}</span>
      </div>
    </div>
    <div class="item-actions">
      <button class="action-button" @click="goItem">
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
          class="eye-icon"
        >
          <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"></path>
          <circle cx="12" cy="12" r="3"></circle>
        </svg>
        보기
      </button>
    </div>
  </div>
</template>

<script setup>
import { formatDate } from "@/utils/date";
import { useRouter } from "vue-router";
const props = defineProps({ item: Object, type: String });
const router = useRouter();
const goItem = () => {
  if (confirm("해당 게시글로 이동하시겠습니까?")) {
    if (props.type == "articles")
      router.push({
        name: "ArticleDetail",
        params: { type: "free", id: props.item.id },
      });
    if (props.type == "plans") router.push({ name: "PlanDetail", params: { id: props.item.id } });
  }
};
</script>

<style scoped>
.list-header,
.grid-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.list-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #111827;
}

.create-button {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: #2dd4bf;
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.create-button:hover {
  background-color: #14b8a6;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 1.5rem;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  transition: box-shadow 0.2s;
}

.list-item:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.item-content {
  flex: 1;
}

.item-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}

.category-badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  background-color: rgba(45, 212, 191, 0.1);
  color: #2dd4bf;
  border-radius: 9999px;
  font-size: 0.75rem;
}

.item-date {
  font-size: 0.75rem;
  color: #6b7280;
}

.item-title {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #111827;
}

.item-description {
  font-size: 0.875rem;
  color: #6b7280;
  margin-bottom: 0.75rem;
  line-height: 1.5;
}

.item-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  font-size: 0.75rem;
  color: #6b7280;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.item-stats {
  display: flex;
  gap: 1rem;
  font-size: 0.75rem;
  color: #6b7280;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.item-actions {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
}

.action-button {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.5rem 0.75rem;
  border: none;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.action-button {
  background-color: #f3f4f6;
  color: #6b7280;
}

.action-button:hover {
  background-color: #e5e7eb;
}

.action-button.edit-action {
  background-color: rgba(45, 212, 191, 0.1);
  color: #2dd4bf;
}

.action-button.edit-action:hover {
  background-color: rgba(45, 212, 191, 0.2);
}

.action-button.delete-action {
  background-color: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.action-button.delete-action:hover {
  background-color: rgba(239, 68, 68, 0.2);
}
</style>
