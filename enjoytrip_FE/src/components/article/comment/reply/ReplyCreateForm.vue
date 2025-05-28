<template>
  <div class="reply-form">
    <div class="reply-form-container">
      <div class="reply-input-container">
        <textarea
          v-model="replyText"
          placeholder="답글을 작성해주세요..."
          class="reply-textarea"
        ></textarea>
        <div class="reply-button-container">
          <button @click="cancelReply" class="reply-cancel-button">취소</button>
          <button @click="addReply" class="reply-submit-button">
            답글 작성
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const emit = defineEmits(["addReply", "cancelReply"]);

const replyText = ref("");

const addReply = () => {
  if (!replyText.value.trim()) return;
  emit("addReply", replyText.value);
  replyText.value = "";
};

const cancelReply = () => {
  emit("cancelReply");
  replyText.value = "";
};
</script>

<style scoped>
/* 답글 폼 */
.reply-form {
  margin-top: 12px;
  padding: 12px;
  background-color: var(--background-color);
  border-radius: 8px;
}

.reply-form-container {
  display: flex;
  gap: 8px;
}

.reply-input-container {
  width: 100%;
}

.reply-textarea {
  width: 100%;
  min-height: 80px;
  padding: 8px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  resize: vertical;
  font-family: inherit;
  font-size: 14px;
}

.reply-textarea:focus {
  outline: none;
  border-color: var(--primary-light);
  box-shadow: 0 0 0 2px var(--primary-light);
}

.reply-button-container {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 8px;
}

.reply-cancel-button {
  padding: 6px 12px;
  background-color: var(--background-color);
  color: var(--text-light);
  border: none;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.reply-cancel-button:hover {
  background-color: var(--background-color);
}

.reply-submit-button {
  padding: 6px 12px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.reply-submit-button:hover {
  background-color: var(--primary-hover);
}
</style>
