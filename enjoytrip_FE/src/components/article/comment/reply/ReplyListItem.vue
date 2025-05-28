<template>
  <div class="reply-item" v-show="!isDeleted">
    <div class="reply-container">
      <BaseAvatar class="reply-avatar" :profileImage="reply.memberProfileImage" />

      <div class="reply-content">
        <div class="reply-header">
          <div class="reply-author">{{ reply.memberName }}</div>
          <div class="reply-date">{{ formatDateTime(reply.createdAt) }}</div>
        </div>
        <!-- 답글 수정 모드 -->
        <div v-show="isEditing" class="reply-edit-form">
          <textarea v-model="editReplyText" class="reply-edit-textarea"></textarea>
          <div class="reply-edit-buttons">
            <button @click="cancelEditReply" class="reply-edit-cancel">취소</button>
            <button @click="saveEditReply" class="reply-edit-save">저장</button>
          </div>
        </div>

        <!-- 답글 내용 (수정 모드가 아닐 때) -->
        <p v-show="!isEditing" class="reply-text">{{ reply.content }}</p>

        <div class="reply-actions">
          <!-- 답글 수정 / 삭제 / 신고 버튼 -->
          <CommentReplyEditDeleteReportButton
            v-if="userStore.isLogined"
            :authorId="reply.memberId"
            @edit="editReply"
            @delete="deleteReply"
            @report="reportReply"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import CommentReplyEditDeleteReportButton from "@/components/article/comment/CommentButtonReplyEditDeleteReport.vue";
import BaseAvatar from "@/components/common/BaseAvatar.vue";
import { formatDateTime } from "@/utils/date";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

const props = defineProps({
  reply: Object,
});
const isDeleted = ref(false);

// 답글 수정 관련 상태
const isEditing = ref(false);
const editReplyText = ref("");

const emit = defineEmits(["editReply", "deleteReply", "reportReply"]);

// 답글 수정 시작
const editReply = () => {
  isEditing.value = true;
  editReplyText.value = props.reply.content;
};

// 답글 수정 취소
const cancelEditReply = () => {
  isEditing.value = false;
  editReplyText.value = "";
};

// 답글 수정 저장
const saveEditReply = () => {
  emit("editReply", props.reply, editReplyText.value);
  isEditing.value = false;
  editReplyText.value = "";
};

// 답글 삭제
const deleteReply = () => {
  if (confirm("정말로 이 답글을 삭제하시겠습니까?")) {
    emit("deleteReply", props.reply);
    isDeleted.value = true;
  }
};

// 답글 신고
const reportReply = () => {
  emit("reportReply", props.reply);
};
</script>

<style scoped>
.reply-item {
  margin-top: 12px;
}

.reply-container {
  display: flex;
  gap: 8px;
}

.reply-content {
  flex: 1;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}

.reply-author {
  font-weight: 500;
  font-size: 13px;
}

.reply-date {
  font-size: 11px;
  color: var(--text-light);
}

.reply-text {
  font-size: 13px;
  margin-bottom: 6px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  font-size: 11px;
  color: var(--text-light);
}

/* 댓글 수정 폼 */
.comment-edit-form,
.reply-edit-form {
  margin-bottom: 8px;
}

.comment-edit-textarea,
.reply-edit-textarea {
  width: 100%;
  min-height: 80px;
  padding: 8px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  resize: vertical;
  font-family: inherit;
  font-size: 14px;
  margin-bottom: 8px;
}

.comment-edit-textarea:focus,
.reply-edit-textarea:focus {
  outline: none;
  border-color: var(--primary-light);
  box-shadow: 0 0 0 2px var(--primary-light);
}

.comment-edit-buttons,
.reply-edit-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.comment-edit-cancel,
.reply-edit-cancel {
  padding: 4px 8px;
  background-color: var(--background-color);
  color: var(--text-light);
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.comment-edit-cancel:hover,
.reply-edit-cancel:hover {
  background-color: var(--primary-light);
}

.comment-edit-save,
.reply-edit-save {
  padding: 4px 8px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.comment-edit-save:hover,
.reply-edit-save:hover {
  background-color: var(--primary-hover);
}

.reply-avatar {
  width: 32px;
  height: 32px;
}
</style>
