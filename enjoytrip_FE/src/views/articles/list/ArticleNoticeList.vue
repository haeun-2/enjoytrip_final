<template>
  <div class="container">
    <div class="header">
      <div>
        <h1 class="title">공지사항</h1>
        <p class="subtitle">여행 게시판의 중요 소식과 업데이트 정보를 확인하세요</p>
      </div>
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

    <div class="board-container">
      <table class="board-table">
        <thead>
          <tr>
            <th class="id-column">#</th>
            <th class="title-column">제목</th>
            <th class="date-column">작성일</th>
            <th class="stats-column">조회수</th>
          </tr>
        </thead>
        <tbody>
          <ArticleNoticeListItem
            @click="showArticle(article)"
            v-for="(article, index) in articles"
            :key="article.id"
            :article="article"
            :page="page"
            :index="index"
          />
        </tbody>
      </table>
    </div>

    <BasePagination :page="page" @changePage="changePage" />
  </div>
</template>

<script setup>
import { onMounted, ref, watch, computed } from "vue";
import BaseSortOption from "@/components/common/BaseSortOption.vue";
import ArticleNoticeListItem from "@/components/article/list/ArticleNoticeListItem.vue";
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BasePagination from "@/components/common/BasePagination.vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";
import * as articleApi from "@/apis/articles/article.js";
const router = useRouter();

const userStore = useUserStore();

// 공지사항 데이터 (실제로는 API에서 가져와야 함)
const articles = ref([]);
const searchCondition = ref({
  page: 1,
  size: 8,
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

const showArticle = (article) => {
  router.push({
    name: "ArticleDetail",
    params: { type: "notice", id: article.id },
  });
};

const writeArticle = () => {
  router.push({ name: "ArticleCreate", parms: { type: "notice" } });
};

const loadArticles = async () => {
  try {
    const response = await articleApi.searchArticle(searchCondition.value);
    articles.value = response.list;
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
    alert("게시글 목록 조회 중 오류가 발생했습니다.  다시 시도해주세요.");
  }
};

onMounted(() => {
  loadArticles();
});

watch(
  searchCondition,
  () => {
    loadArticles();
  },
  { deep: true }
);
</script>

<style scoped>
.main-content {
  display: flex;
  gap: 24px;
}

.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 2rem;
}

.title {
  font-size: 1.875rem;
  font-weight: bold;
  color: var(--primary-color); /* 파란색 테마 */
  margin-bottom: 0.5rem;
}

.subtitle {
  color: var(--text-light);
  align-self: flex-end;
}

.search-bar-container {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.write-button {
  align-self: center;
}

/* 사이드바 스타일 */
.sidebar {
  width: 260px;
  flex-shrink: 0;
}

/* 게시판 콘텐츠 스타일 */
.articles-content {
  flex: 1;
}

/* 게시판 테이블 */
.board-container {
  margin-top: 20px;
  border-radius: 0.375rem;
  overflow: hidden;
  margin-bottom: 2rem;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.875rem;
}

.board-table th {
  padding: 0.75rem 1rem;
  text-align: left;
  font-weight: 500;
  color: var(--text-light);
  border-bottom: 1px solid var(--border-color);
}

/* 결과 없음 */
.no-results {
  padding: 2rem;
  text-align: center;
  color: var(--text-color);
}

.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border-width: 0;
}
</style>
