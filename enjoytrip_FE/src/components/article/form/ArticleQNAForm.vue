<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal-container" @click.stop>
      <button class="modal-close-button" @click="closeModal">
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
        >
          <line x1="18" y1="6" x2="6" y2="18"></line>
          <line x1="6" y1="6" x2="18" y2="18"></line>
        </svg>
      </button>

      <div class="modal-content">
        <h2 class="modal-title">새 질문 작성</h2>

        <form @submit.prevent="createQuestion" class="question-form">
          <div class="form-group">
            <label for="question-title" class="form-label">제목</label>
            <input
              type="text"
              id="question-title"
              v-model="question.title"
              class="form-input"
              placeholder="질문 제목을 입력하세요"
              required
            />
          </div>

          <div class="form-group">
            <label for="question-content" class="form-label">내용</label>
            <textarea
              id="question-content"
              v-model="question.content"
              class="form-textarea"
              placeholder="질문 내용을 자세히 입력해주세요"
              rows="10"
              required
            ></textarea>
          </div>

          <div class="form-actions">
            <button type="button" @click="closeModal" class="cancel-button">취소</button>
            <button type="submit" class="submit-button">질문 등록</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

// Props
const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["close", "createQuestion"]);
const question = ref({
  title: "",
  content: "",
});

// 모달 닫기
const closeModal = () => {
  emit("close");
  question.value.title = "";
  question.value.content = "";
};

const createQuestion = () => {
  if (question.value.title.trim() && question.value.content.trim()) {
    emit("createQuestion", question.value);
  }
};
</script>

<style scoped>
/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
  padding: 1rem;
}

.modal-container {
  position: relative;
  width: 100%;
  max-width: 600px;
  background-color: white;
  border-radius: 0.5rem;
  overflow: hidden;
}

.modal-close-button {
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
  width: 2rem;
  height: 2rem;
  border-radius: 9999px;
  background-color: var(--background-color);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: background-color 0.2s;
}

.modal-close-button:hover {
  background-color: var(--background-color);
}

.modal-content {
  padding: 1.5rem;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-color);
  margin-bottom: 1.5rem;
}

/* 질문 폼 스타일 */
.question-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.cancel-button {
  padding: 0.75rem 1rem;
  background-color: var(--background-color);
  color: #666;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.cancel-button:hover {
  background-color: var(--background-color);
}

.submit-button {
  padding: 0.75rem 1rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.submit-button:hover {
  background-color: var(--primary-hover);
}
</style>
