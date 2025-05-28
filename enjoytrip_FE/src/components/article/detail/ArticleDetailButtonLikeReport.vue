<template>
  <div class="action-buttons">
    <button @click="handleLike(article)" class="like-button">
      <font-awesome-icon
        :icon="[article.liked ? 'fa' : 'far', 'heart']"
        :style="{
          fontSize: '20px',
          color: 'red',
        }"
      />
      <span>좋아요</span>
    </button>
    <button @click="reportArticle(article)" class="report-button">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="20"
        height="20"
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
      <span>신고하기</span>
    </button>
  </div>
</template>

<script setup>
const { article } = defineProps({
  article: Object,
});
const emit = defineEmits(["likeArticle", "reportArticle"]);
const handleLike = (article) => {
  emit("likeArticle", article);
};
const reportArticle = (article) => {
  emit("reportArticle", article);
};
</script>

<style scoped>
/* 좋아요 및 신고 버튼 */
.action-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 24px 0;
  border-top: 1px solid var(--border-color);
  border-bottom: 1px solid var(--border-color);
}

.like-button,
.report-button {
  color: black;
  background-color: var(--background-color);
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: 9999px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  border: none;
}

.like-button:hover {
  background-color: var(--primary-lighter);
}

.report-button:hover {
  background-color: #fecaca;
}
</style>
