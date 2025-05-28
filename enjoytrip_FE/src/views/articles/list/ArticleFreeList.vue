<template>
  <div class="container">
    <div class="header">
      <div>
        <h1 class="title">자유 게시판</h1>
        <p class="subtitle">다양한 여행 정보와 경험을 공유해보세요</p>
      </div>
      <BaseButton v-show="userStore.isLogined" text="+ 새 게시글 작성" @click="writeArticle" class="write-button" />
    </div>

    <div class="search-bar-container">
      <BaseSearchBar @search="changeTitle" />
      <BaseSortOption
        :options="[
          { value: 'created_at', name: '최신순' },
          { value: 'view_count', name: '조회순' },
          { value: 'like_count', name: '인기순' },
          { value: 'comment_count', name: '댓글순' },
        ]"
        @changeSortOpt="changeSortOpt"
      />
    </div>

    <div class="checkbox-container">
      <label class="mine-checkbox" v-if="userStore.isLogined">
        <input type="checkbox" v-model="searchCondition.mine" />
        내가 쓴 글만 보기
      </label>
    </div>

    <div class="board-container">
      <table class="board-table">
        <thead>
          <tr>
            <th class="id-column">#</th>
            <th class="title-column">제목</th>
            <th class="author-column">작성자</th>
            <th class="date-column">작성일</th>
            <th class="stats-column">조회수</th>
          </tr>
        </thead>
        <tbody>
          <ArticleFreeListItem
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
import { ref, computed, onMounted, watch } from "vue";
import BaseButton from "@/components/common/BaseButton.vue";
import BaseSortOption from "@/components/common/BaseSortOption.vue";
import ArticleFreeListItem from "@/components/article/list/ArticleFreeListItem.vue";
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BasePagination from "@/components/common/BasePagination.vue";
import { useRouter } from "vue-router";
import * as articleApi from "@/apis/articles/article.js";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

const router = useRouter();

const articles = ref([]);
const searchCondition = ref({
  page: 1,
  size: 8,
  sort: "created_at",
  title: null,
  articleType: "free",
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
    params: { type: "free", id: article.id },
  });
};
const writeArticle = () => {
  router.push({ name: "ArticleCreate", parms: { type: "free" } });
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
.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 2rem;
}

.title {
  font-size: 1.875rem;
  font-weight: bold;
  color: var(--primary-color);
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

/* 체크박스 스타일링 */
.checkbox-container {
  margin: 20px 0;
  display: flex;
  justify-content: end;
}

.mine-checkbox {
  display: flex;
  align-items: center;
  font-size: 0.875rem;
  color: #666;
  cursor: pointer;
  user-select: none;
  margin-left: 0.5rem;
}

.mine-checkbox input[type="checkbox"] {
  appearance: none;
  -webkit-appearance: none;
  width: 1.125rem;
  height: 1.125rem;
  border: 1.5px solid #d1d5db;
  border-radius: 0.25rem;
  margin-right: 0.5rem;
  display: grid;
  place-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.mine-checkbox input[type="checkbox"]:checked {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.mine-checkbox input[type="checkbox"]:hover {
  border-color: var(--primary-color);
  background-color: rgba(45, 212, 191, 0.05);
}

.mine-checkbox input[type="checkbox"]:checked::before {
  content: "";
  width: 0.65rem;
  height: 0.65rem;
  transform: scale(1);
  box-shadow: inset 1em 1em white;
  transform-origin: center;
  clip-path: polygon(14% 44%, 0 65%, 50% 100%, 100% 16%, 80% 0%, 43% 62%);
}

.mine-checkbox:hover {
  color: var(--primary-color);
}
</style>
