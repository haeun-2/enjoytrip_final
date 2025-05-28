<template>
  <div class="comment-form">
    <div class="comment-form-container">
      <div class="comment-input-container">
        <textarea v-model="commentText" placeholder="댓글을 작성해주세요..." class="comment-textarea"></textarea>
        <div class="comment-button-container">
          <button @click="addComment" class="comment-submit-button">댓글 작성</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/user";
const userStore = useUserStore();

const emit = defineEmits(["addComment"]);

const commentText = ref("");
const addComment = () => {
  if (!userStore.isLogined) {
    alert("로그인이 필요한 기능입니다. 로그인 후 이용해주세요.");
  }
  if (!commentText.value.trim()) {
    alert("내용을 입력주세요.");
    return;
  }
  emit("addComment", null, commentText.value);
  commentText.value = "";
};
</script>

<style scoped>
.comment-form {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
}

.comment-form-container {
  display: flex;
  gap: 12px;
}

.comment-input-container {
  width: 100%;
}

.comment-textarea {
  width: 98%;
  min-height: 100px;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  resize: vertical;
  font-family: inherit;
  font-size: 14px;
}

.comment-textarea:focus {
  outline: none;
  border-color: var(--primary-light);
  box-shadow: 0 0 0 2px var(--primary-light);
}

.comment-button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

.comment-submit-button {
  padding: 8px 16px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.comment-submit-button:hover {
  background-color: var(--primary-hover);
}
</style>
