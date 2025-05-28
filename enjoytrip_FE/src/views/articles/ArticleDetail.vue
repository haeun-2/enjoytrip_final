<template>
  <div class="container">
    <ArticleBackButton :back="{ name: 'ArticleList', params: { type } }" />

    <div v-if="article" class="article-container">
      <!-- 게시글 헤더 -->
      <ArticleDetailHeader :article="article" :type="type" />

      <!-- 게시글 내용 -->
      <div class="article-content" v-html="article.content"></div>

      <!-- 좋아요 및 신고하기 버튼 -->
      <ArticleDetailButtonLikeReport
        v-if="type == 'free' && userStore.isLogined"
        :article="article"
        @likeArticle="likeArticle"
        @reportArticle="reportArticle"
      />

      <div class="adjacent-buttons">
        <button v-show="prev != null" @click="goToArticle(prev?.id)" class="button">이전글 : {{ prev?.title }}</button>
        <button v-show="prev == null">이전글이 없습니다</button>
        <button v-if="next != null" @click="goToArticle(next?.id)" class="button">다음글 : {{ next?.title }}</button>
        <button v-show="next == null">다음글이 없습니다</button>
      </div>

      <!-- 댓글 섹션 -->
      <CommentList v-if="type == 'free'" :articleId="Number(id)" />
    </div>

    <div v-else class="not-found">
      <div class="not-found-content">
        <div class="not-found-message">게시글을 찾을 수 없습니다.</div>
      </div>
    </div>

    <BaseReportModal v-if="userStore.isLogined" ref="reportModal" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import ArticleBackButton from "@/components/common/BaseBackButton.vue";
import ArticleDetailHeader from "@/components/article/detail/ArticleDetailHeader.vue";
import ArticleDetailButtonLikeReport from "@/components/article/detail/ArticleDetailButtonLikeReport.vue";
import CommentList from "@/components/article/comment/CommentList.vue";
import BaseReportModal from "@/components/common/BaseReportModal.vue";
import * as articleApi from "@/apis/articles/article";
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";
import { likeArticleApi, unlikeArticleApi } from "@/apis/articles/article";

const userStore = useUserStore();

const article = ref({});
const prev = ref({});
const next = ref({});

const route = useRoute();
const type = route.params.type;
let id = route.params.id;

const router = useRouter();
const goToArticle = (articleId) => {
  router.push({ name: "ArticleDetail", params: { type, id: articleId } });
};

const loadArticleDetail = async () => {
  try {
    const response = await articleApi.detailArticle(id);
    article.value = response.article;
    prev.value = response.prev;
    next.value = response.next;
  } catch (e) {
    alert("게시글 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

onMounted(() => {
  loadArticleDetail();
});

// 좋아요/신고하기
const likeArticle = async () => {
  if (article.value.liked) {
    await unlikeArticleApi(article.value.id);
    article.value.liked = false;
    article.value.likeCount -= 1;
  } else {
    await likeArticleApi(article.value.id);
    article.value.liked = true;
    article.value.likeCount += 1;
  }
};

const reportModal = ref(null);

const reportArticle = async () => {
  reportModal.value.open(id);
};

watch(
  () => route.params.id,
  (newVal) => {
    id = route.params.id;
    loadArticleDetail();
  }
);
</script>

<style>
/* 기본 스타일 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 16px;
}

.article-container {
  margin-top: 0;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

/* 게시글 내용 */
.article-content {
  padding: 0 10px;
  line-height: 1.6;
}

.article-content h3 {
  font-size: 20px;
  font-weight: 600;
  margin-top: 24px;
  margin-bottom: 8px;
}

.article-content p {
  margin-top: 16px;
  margin-bottom: 16px;
}

/* 게시글을 찾을 수 없을 때 */
.not-found {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 256px;
}

.not-found-content {
  text-align: center;
}

.not-found-message {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 16px;
}

/* 이전글, 다음글 버튼 */
.adjacent-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
  padding-top: 1rem;
  background-color: white;
}

.adjacent-buttons button {
  padding: 0.5rem 1rem;
  color: var(--text-light);
  font-size: 0.875rem;
  border-radius: 0.5rem;
  cursor: pointer;
  border: none;
  background-color: white;
}

.adjacent-buttons .button:hover {
  color: var(--primary-color);
}

/* 반응형 디자인 */
@media (max-width: 640px) {
  .container {
    padding: 16px;
  }
}
</style>
