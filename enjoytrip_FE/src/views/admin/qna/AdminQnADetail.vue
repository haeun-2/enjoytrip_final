<template>
  <div v-if="isOpen" class="modal-overlay" @click="closeModal">
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

      <div v-if="question" class="modal-content">
        <!-- 질문 헤더 -->
        <div class="question-header">
          <div class="question-badges">
            <div :class="['status-badge', status == 'ANSWERED' ? 'status-answered' : 'status-waiting']">
              {{ status == "ANSWERED" ? "답변 완료" : "답변 대기" }}
            </div>
          </div>

          <h2 class="question-title">{{ question?.title }}</h2>

          <div class="question-meta">
            <div class="author-info">
              <div class="author-avatar">
                <BaseAvatar :profielImage="question?.member.profileImage" />
              </div>
              <div class="author-details">
                <div class="author-name">{{ question?.member.name }}</div>
                <div class="author-date">
                  {{ formatDateTime(question?.createdAt) }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 질문 내용 -->
        <div class="question-content">
          {{ question?.content }}
        </div>

        <!-- 답변 내용 -->
        <div class="question-edit-form">
          <textarea
            v-model="answer.content"
            class="question-edit-textarea"
            placeholder="답변을 입력해주세요..."
            v-show="status == 'ANSWERED'"
            readonly
          ></textarea>
        </div>

        <!-- 답변 폼 -->
        <div class="question-edit-form">
          <textarea
            v-model="newAnswerContent"
            class="question-edit-textarea"
            placeholder="답변을 입력해주세요..."
            v-show="status == 'PENDING'"
          ></textarea>
        </div>

        <!-- 답변 버튼 -->
        <div class="answers-section" v-show="status == 'PENDING'">
          <div class="question-actions">
            <BaseButtonEditDelete
              editText="저장"
              deleteText="취소"
              @editClick="createAnswer"
              @deleteClick="closeModal"
            />
          </div>
        </div>
      </div>

      <div v-else class="loading-container">
        <div class="loading-spinner"></div>
        <p>질문 정보를 불러오는 중...</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import BaseAvatar from "@/components/common/BaseAvatar.vue";
import BaseButtonEditDelete from "@/components/common/BaseButtonEditDelete.vue";
import { ref, watch } from "vue";
import { formatDateTime } from "@/utils/date";

// Props
const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
  selectedQna: {
    type: Object,
    default: null,
  },
});

// Emits
const emit = defineEmits(["close", "createAnswer"]);

// 질문 데이터
const question = ref({});
const answer = ref({});
const status = ref("");

const newAnswerContent = ref("");

// 질문 ID가 변경되면 데이터 로드
watch(
  () => props.selectedQna,
  () => {
    console.log(props.selectedQna);
    question.value = props.selectedQna.question;
    answer.value = props.selectedQna.answer;
    status.value = props.selectedQna.status;
  },
  {
    deep: true,
  }
);

const createAnswer = () => {
  if (newAnswerContent.value.trim()) {
    emit("createAnswer", props.selectedQna, newAnswerContent.value);
  } else {
    alert("내용을 입력하세요");
  }
};

// 모달 닫기
const closeModal = () => {
  newAnswerContent.value = "";
  emit("close");
};
</script>

<style scoped>
/* 모달 오버레이 */
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
  overflow-y: auto;
}

/* 모달 컨테이너 */
.modal-container {
  position: relative;
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  background-color: white;
  border-radius: 0.5rem;
  overflow-y: auto;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

/* 모달 닫기 버튼 */
.modal-close-button {
  position: absolute;
  top: 1rem;
  right: 1rem;
  width: 2rem;
  height: 2rem;
  border-radius: 9999px;
  background-color: #f1f5f9;
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

/* 모달 내용 */
.modal-content {
  padding: 2rem;
}

/* 로딩 컨테이너 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  color: var(--text-light);
}

.loading-spinner {
  width: 2.5rem;
  height: 2.5rem;
  border: 3px solid var(--border-color);
  border-top-color: var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 질문 헤더 */
.question-header {
  margin-bottom: 1.5rem;
}

.question-badges {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
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

.question-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  line-height: 1.3;
}

.question-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.author-avatar {
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
  background-color: #f1f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 500;
}

.author-date {
  font-size: 0.75rem;
  color: var(--text-light);
}

.question-stats {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.875rem;
  color: var(--text-light);
}

/* 질문 내용 */
.question-content {
  margin-bottom: 2rem;
  line-height: 1.6;
  white-space: pre-line;
}

/* 질문 수정 */
.question-edit-delete {
  display: flex;
  gap: 0.5rem;
}

.question-edit-button,
.question-delete-button {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  background: none;
  border: none;
  padding: 0;
  font-size: 0.75rem;
  color: #666;
  cursor: pointer;
  transition: color 0.2s;
}

.question-edit-button:hover {
  color: var(--primary-color);
}

.question-delete-button:hover {
  color: var(--error-color);
}

.question-edit-form {
  margin-bottom: 0.5rem;
}

.question-edit-textarea {
  width: 100%;
  min-height: 6rem;
  padding: 0.5rem;
  border: 1px solid var(--border-color);
  border-radius: 0.25rem;
  resize: vertical;
  font-family: inherit;
  font-size: 0.875rem;
  margin-bottom: 0.5rem;
}

.question-edit-textarea:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px var(--primary-light);
}

.question-edit-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.question-edit-cancel {
  padding: 0.25rem 0.5rem;
  background-color: var(--background-color);
  color: #666;
  border: none;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.question-edit-cancel:hover {
  background-color: #e2e8f0;
}

.question-edit-save {
  padding: 0.25rem 0.5rem;
  background-color: var(--primary-color); /* 민트색 */
  color: white;
  border: none;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.question-edit-save:hover {
  background-color: var(--primary-hover); /* 더 어두운 민트색 */
}

/* 답변 섹션 */
.answers-section {
  border-top: 1px solid var(--border-color);
  padding-top: 1.5rem;
}

.answers-title {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
}

.answer-item {
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  overflow: hidden;
}

.best-answer {
  border-color: var(--border-color);
  box-shadow: 0 0 0 1px var(--primary-light);
}

.answer-container {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}
/* 반응형 디자인 */
@media (max-width: 640px) {
  .modal-content {
    padding: 1.5rem 1rem;
  }

  .question-meta {
    flex-direction: column;
    align-items: flex-start;
  }

  .question-stats {
    width: 100%;
    justify-content: flex-start;
  }

  .question-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .answer-header {
    flex-direction: column;
  }

  .answer-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .answer-form-container {
    flex-direction: column;
  }
}
</style>
