<template>
  <div class="article-header">
    <div class="title-date-container">
      <h1 class="article-title">{{ article.title }}</h1>
      <span class="article-date">{{ formatDateTime(article.createdAt) }}</span>
    </div>

    <!-- 수정/삭제 버튼 추가 -->
    <BaseButtonEditDelete
      v-if="type == 'free' && userStore.isAuthor(article.member?.id)"
      @editClick="editArticle"
      @deleteClick="confirmDeleteArticle"
    />

    <div class="author-stats-container">
      <div class="author-info">
        <BaseAvatar class="author-avatar" :profileImage="article.member?.profileImage" />
        <div class="author-details">
          <div class="author-name">{{ article.member?.name }}</div>
          <div class="author-label">작성자</div>
        </div>
      </div>

      <ArticleStatus :article="article" />
    </div>
  </div>
</template>

<script setup>
import BaseAvatar from "@/components/common/BaseAvatar.vue";
import BaseButtonEditDelete from "@/components/common/BaseButtonEditDelete.vue";
import ArticleStatus from "@/components/article/ArticleStatus.vue";
import { deleteArticle } from "@/apis/articles/article";
import { useRouter, useRoute } from "vue-router";
import { formatDateTime } from "@/utils/date";
import { useUserStore } from "@/stores/user";
const userStore = useUserStore();

const route = useRoute();
const type = route.params.type;
const id = route.params.id;

const router = useRouter();

const props = defineProps({
  article: Object,
});

const editArticle = () => {
  router.push({ name: "ArticleEdit", params: { type, id } });
};
const confirmDeleteArticle = async () => {
  if (confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
    try {
      const response = await deleteArticle(id);
      router.push({ name: "ArticleList", params: { id, type } });
    } catch (e) {
      alert("게시글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  }
};
</script>

<style scoped>
/* 게시글 헤더 */
.article-header {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.article-date {
  font-size: 14px;
  color: #666;
}

.article-title {
  font-size: 30px;
  font-weight: 700;
  line-height: 1.2;
}

.title-date-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 수정/삭제 버튼 스타일 */
.article-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-bottom: 16px;
}

.article-action-button {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 13px;
  background-color: var(--background-color);
  color: var(--text-color);
  border: none;
  cursor: pointer;
  transition: background-color 0.2s, color 0.2s;
}

.article-action-button:hover {
  background-color: var(--primary-light);
}

.edit-icon,
.trash-icon {
  color: inherit;
}

.author-stats-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border-color);
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 500;
}

.author-label {
  font-size: 12px;
  color: var(--text-light);
}
</style>
