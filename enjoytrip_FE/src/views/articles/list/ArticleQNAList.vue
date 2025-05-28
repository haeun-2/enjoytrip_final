<template>
  <div class="container">
    <div class="header">
      <div>
        <h1 class="title">질문과 답변</h1>
        <p class="subtitle">여행에 관한 궁금한 점을 질문하고 답변을 받아보세요</p>
      </div>
      <BaseButton text="+ 질문하기" @click="writeQuestion" class="write-button" />
    </div>

    <div class="search-bar-container">
      <BaseSearchBar @search="changeTitle" />

      <!-- 답변 상태: 전체 질문, 답변 완료, 답변 대기 -->
      <BaseSortOption
        :options="[
          { value: null, name: '전체 질문' },
          { value: 'answered', name: '답변 완료' },
          { value: 'pending', name: '답변 대기' },
        ]"
        @changeSortOpt="changeStatus"
      />
    </div>

    <div class="board-container">
      <div class="qna-list">
        <ArticleQNAListItem
          v-for="question in questions"
          :key="question.id"
          class="qna-item"
          @click="openQuestionDetail(question)"
          :question="question.question"
          :status="question.status"
          :answer="question.answer"
        />
      </div>

      <div v-if="questions.length === 0" class="no-results">
        <div class="no-results-message">검색 결과가 없습니다.</div>
      </div>
    </div>

    <BasePagination :page="page" @changePage="changePage" />

    <!-- 질문하기 모달 -->
    <ArticleQNAForm
      :isOpen="showAskModal"
      @close="closeAskModal"
      @click="closeAskModal"
      @createQuestion="createQuestion"
    />

    <!-- 질문 상세 모달 -->
    <ArticleQNADetail
      :isOpen="showDetailModal"
      :selectedQuestion="selectedQuestion"
      @close="closeDetailModal"
      @editQuestion="editQuestion"
      @deleteQuestion="deleteQuestion"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from "vue";
import BaseButton from "@/components/common/BaseButton.vue";
import BaseSortOption from "@/components/common/BaseSortOption.vue";
import ArticleQNAListItem from "@/components/article/list/ArticleQNAListItem.vue";
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BasePagination from "@/components/common/BasePagination.vue";
import { useRouter } from "vue-router";
import ArticleQNADetail from "@/components/article/detail/ArticleQNADetail.vue";
import ArticleQNAForm from "@/components/article/form/ArticleQNAForm.vue";
import * as articleApi from "@/apis/articles/article";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
const router = useRouter();

const searchCondition = ref({
  page: 1,
  size: 8,
  status: "all",
  title: null,
});
const page = ref({});

const changePage = (num) => {
  searchCondition.value.page = num;
};

const changeTitle = (title) => {
  if (!title.trim()) searchCondition.value.title = null;
  else searchCondition.value.title = title;
};

const changeStatus = (status) => {
  searchCondition.value.status = status;
};

// 질문 데이터
const questions = ref([]);

const loadQuestions = async () => {
  try {
    const response = await articleApi.searchQna(searchCondition.value);
    questions.value = response.list;
    page.value = {
      navSize: response.navSize,
      currentPage: response.currentPage,
      startPage: response.startPage,
      endPage: response.endPage,
      hasPre: response.hasPre,
      hasNext: response.hasNext,
    };
  } catch (e) {
    alert("QnA 목록 조회 중 오류가 발생했습니다.  다시 시도해주세요.");
  }
};

onMounted(() => {
  if (!userStore.isLogined) {
    alert("로그인 후 이용해주세요");
    router.back();
    return;
  }
  loadQuestions();
});

watch(
  searchCondition,
  () => {
    loadQuestions();
  },
  { deep: true }
);

// 질문하기 모달 상태
const showAskModal = ref(false);

// 질문하기 모달 열기
const writeQuestion = () => {
  showAskModal.value = true;
  document.body.style.overflow = "hidden"; // 스크롤 방지
};

// 질문하기 모달 닫기
const closeAskModal = () => {
  showAskModal.value = false;
  document.body.style.overflow = ""; // 스크롤 복원
};

// 질문 상세 모달 상태
const showDetailModal = ref(false);
const selectedQuestion = ref(null);

// 질문 상세 모달 열기
const openQuestionDetail = (question) => {
  selectedQuestion.value = question;
  showDetailModal.value = true;
  document.body.style.overflow = "hidden"; // 스크롤 방지
};

// 질문 상세 모달 닫기
const closeDetailModal = () => {
  showDetailModal.value = false;
  document.body.style.overflow = ""; // 스크롤 복원
};

// 질문 데이터 업데이트
const createQuestion = async (newQuestion) => {
  try {
    const { message, id } = await articleApi.createArticle({
      ...newQuestion,
      articleType: "qna",
    });

    questions.value.unshift({
      question: {
        id: id,
        title: newQuestion.title,
        content: newQuestion.content,
        createdAt: new Date().toISOString(),
        modifiedAt: null,
        deletedAt: null,
        member: {
          id: userStore.user.id,
          name: userStore.user.name,
          profileImage: userStore.user.profileImage,
        },
      },
      status: "PENDING",
      answer: null,
    });
  } catch (e) {
    alert("질문 등록 중 오류가 발생했습니다. 다시 시도해주세요.");
  }

  newQuestion.title = "";
  newQuestion.content = "";
  closeAskModal();
};

const editQuestion = async (question, content) => {
  try {
    const response = await articleApi.editArticle(question.id, { content });
    question.content = content;
  } catch (e) {
    alert("질문 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

const deleteQuestion = async (question) => {
  try {
    const response = await articleApi.deleteArticle(question.id);
    alert(response);
    questions.value.splice(
      questions.value.findIndex((q) => q.question.id == question.id),
      1
    );
    closeDetailModal();
  } catch (e) {
    alert("질문 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};
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

/* QnA 목록 */
.board-container {
  margin-top: 20px;
  border-radius: 0.375rem;
  overflow: hidden;
  margin-bottom: 2rem;
}

.qna-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.qna-item {
  border: 1px solid #ddd;
  border-radius: 0.5rem;
  padding: 1.25rem;
  background-color: white;
  transition: box-shadow 0.2s, transform 0.2s;
  cursor: pointer;
}

.qna-item:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  transform: translateY(-2px);
}

.eye-icon {
  color: var(--text-light);
}

/* 결과 없음 */
.no-results {
  padding: 3rem 0;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.no-results-message {
  font-size: 1rem;
  color: var(--text-light);
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .ask-button {
    width: 100%;
  }
}
</style>
