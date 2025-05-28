<template>
  <div class="container">
    <div class="article-form-container">
      <h1 class="form-title">
        {{ isEditPage ? "공지사항 수정" : "새 공지사항 작성" }}
      </h1>

      <!-- 게시글 폼 -->
      <form @submit.prevent="submitArticle" class="article-form">
        <!-- 제목 입력 -->
        <div class="form-group">
          <label for="title" class="form-label">제목</label>
          <input
            type="text"
            id="title"
            v-model="article.title"
            class="form-input"
            placeholder="제목을 입력하세요"
            required
          />
        </div>

        <!-- 내용 입력 -->
        <BaseEditor ref="editor" />

        <!-- 버튼 그룹 -->
        <div class="form-actions">
          <button type="button" @click="goBack" class="cancel-button">취소</button>
          <button type="submit" class="submit-button">저장</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import * as adminArticleApi from "@/apis/admin/article";
import { useUserStore } from "@/stores/user";
import BaseEditor from "@/components/common/BaseEditor.vue";

const router = useRouter();
const route = useRoute();
const type = "notice";

const userStore = useUserStore();

// 수정 페이지
const isEditPage = computed(() => route.name === "AdminNoticeEdit");
const id = isEditPage ? route.params.id : null;

const editor = ref(null);

// 게시글 데이터
const article = ref({
  title: "",
  content: "",
  articleType: type,
});

// 게시글 제출
const submitArticle = () => {
  // 유효성 검사
  if (!article.value.title.trim()) {
    alert("제목을 입력해주세요.");
    return;
  }

  article.value.content = editor.value.getContent();
  if (!article.value.content.trim()) {
    alert("내용을 입력해주세요.");
    return;
  }

  if (isEditPage.value) editArticle();
  else createArticle();
};

const createArticle = async () => {
  try {
    const { message, id } = await adminArticleApi.createArticle(article.value);
    router.push({ name: "AdminNoticeList", params: { id } });
  } catch (e) {
    alert("공지사항 등록 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

const editArticle = async () => {
  try {
    const response = await adminArticleApi.editArticle(id, article.value);
    router.push({ name: "AdminNoticeList", params: { id } });
  } catch (e) {
    alert("공지사항 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

const goBack = () => {
  if (isEditPage.value) router.push({ name: "AdminNoticeList", params: { id } });
  else router.push({ name: "AdminNoticeList" });
};

onMounted(async () => {
  if (isEditPage.value) {
    try {
      const response = await adminArticleApi.detailArticle(id);
      article.value = {
        title: response.title,
        content: response.content,
      };
      editor.value.setContent(article.value.content);
    } catch (e) {
      alert("공지사항 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  }
});
</script>

<style scoped>
/* 기본 스타일 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 16px;
}

.article-form-container {
  margin-top: 0;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.article-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 반응형 디자인 */
@media (max-width: 640px) {
  .container {
    padding: 16px;
  }
}
</style>
