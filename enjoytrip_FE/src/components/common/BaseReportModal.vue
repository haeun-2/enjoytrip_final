<template>
  <div v-show="visible" class="modal-overlay">
    <div class="modal">
      <h2 class="modal-title">댓글 신고</h2>
      <textarea v-model="reason" placeholder="신고 사유를 입력해주세요" class="form-textarea modal-textarea" rows="5" />
      <div class="modal-actions">
        <button @click="submitReport" class="submit-button">제출</button>
        <button @click="close" class="cancel-button">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineExpose } from "vue";
import { reportArticle } from "@/apis/articles/article";
import { reportComment } from "@/apis/articles/comment";

const visible = ref(false);
const reason = ref("");
const targetArticleId = ref(null);
const targetCommentId = ref(null);

const open = (articleId, commentId) => {
  reason.value = "";
  targetArticleId.value = articleId;
  targetCommentId.value = commentId;
  visible.value = true;
};

const close = () => {
  visible.value = false;
};

const submitReport = async () => {
  if (!reason.value.trim()) {
    alert("신고 사유를 입력해주세요.");
    return;
  }

  try {
    if (targetCommentId.value == null) {
      await reportArticle(targetArticleId.value, reason.value);
    } else {
      await reportComment(targetArticleId.value, targetCommentId.value, reason.value);
    }
    close();
    alert("신고가 접수되었습니다.");
  } catch (e) {
    alert("신고 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

defineExpose({ open });
</script>

<style scoped>
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

.modal {
  background-color: white;
  padding: 20px;
  width: 400px;
  border-radius: 8px;
  text-align: center;
}

.modal-title {
  padding-left: 10px;
  text-align: left;
  margin-bottom: 12px;
  font-size: 1.2rem;
  color: var(--text-color);
}

.modal-textarea {
  width: 90%;
}

.modal-actions {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.submit-button,
.cancel-button {
  padding: 8px 14px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-button {
  background-color: var(--error-color);
  color: white;
}

.cancel-button {
  background-color: var(--background-color);
  color: var(--text-light);
}
</style>
