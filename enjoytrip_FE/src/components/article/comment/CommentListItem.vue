<template>
  <div class="comment-item">
    <div class="comment-container">
      <BaseAvatar :profileImage="comment.memberProfileImage" />

      <div class="comment-content">
        <div class="comment-header">
          <div class="comment-author">{{ comment.memberName }}</div>
          <div class="comment-date">
            {{ formatDateTime(comment.createdAt) }}
          </div>
        </div>

        <!-- 댓글 수정 모드 -->
        <div v-if="userStore.isLogined" v-show="isEditing" class="comment-edit-form">
          <textarea v-model="editCommentText" class="comment-edit-textarea"></textarea>
          <div class="comment-edit-buttons">
            <button @click="cancelEditComment" class="comment-edit-cancel">취소</button>
            <button @click="saveEditComment" class="comment-edit-save">저장</button>
          </div>
        </div>
        <!-- 댓글 내용 (수정 모드가 아닐 때) -->
        <p v-show="!isEditing" class="comment-text">{{ comment.content }}</p>

        <!-- 답글 -->
        <div class="comment-actions">
          <!-- 답글 달기 버튼 -->
          <button v-if="userStore.isLogined" class="reply-button" @click="toggleReplyForm">답글 달기</button>

          <!-- 댓글 수정 / 삭제 / 신고 버튼 -->
          <CommentReplyEditDeleteReportButton
            v-if="userStore.isLogined"
            :authorId="comment.memberId"
            @edit="editComment"
            @delete="deleteComment"
            @report="reportComment"
          />
        </div>

        <!-- 답글 폼 -->
        <ReplyCreateForm
          v-if="userStore.isLogined"
          v-show="isEditingReply"
          :commentId="comment.id"
          @addReply="addReply"
          @cancelReply="cancelReply"
        />

        <!-- 답글 목록 -->
        <ReplyList
          :replies="comment.replies"
          @editReply="editReply"
          @deleteReply="deleteReply"
          @reportReply="reportReply"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import BaseAvatar from "@/components/common/BaseAvatar.vue";
import CommentReplyEditDeleteReportButton from "@/components/article/comment/CommentButtonReplyEditDeleteReport.vue";
import ReplyCreateForm from "@/components/article/comment/reply/ReplyCreateForm.vue";
import ReplyList from "@/components/article/comment/reply/ReplyList.vue";
import { formatDateTime } from "@/utils/date";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

const props = defineProps({
  comment: Object,
});
const emit = defineEmits(["addComment", "editComment", "deleteComment", "reportComment"]);

// 댓글 수정 관련 상태
const isEditing = ref(false);
const editCommentText = ref("");

// 댓글 수정 시작
const editComment = () => {
  isEditing.value = true;
  editCommentText.value = props.comment.content;
};

// 댓글 수정 취소
const cancelEditComment = () => {
  isEditing.value = false;
  editCommentText.value = "";
};

// 댓글 수정 저장
const saveEditComment = () => {
  if (!editCommentText.value.trim()) return;

  emit("editComment", props.comment, editCommentText.value);

  isEditing.value = false;
  editCommentText.value = "";
};

// 댓글 삭제
const deleteComment = () => {
  if (confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
    emit("deleteComment", props.comment);
  }
};

// 댓글 신고
const reportComment = () => {
  emit("reportComment", props.comment);
};

// 답글 관련 상태
const isEditingReply = ref(false);

// 답글 폼 토글
const toggleReplyForm = () => {
  isEditingReply.value = !isEditingReply.value;
};

// 답글 작성
const addReply = (replyText) => {
  emit("addComment", props.comment, replyText);
};

// 답글 취소
const cancelReply = () => {
  isEditingReply.value = false;
};

const editReply = (reply, replyText) => {
  emit("editComment", reply, replyText);
};

const deleteReply = (reply) => {
  reply.parentId = props.comment.id;
  emit("deleteComment", reply);
};

const reportReply = (reply) => {
  emit("reportComment", reply);
};
</script>

<style scoped>
.comment-container {
  display: flex;
  gap: 12px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}

.comment-author {
  font-weight: 500;
}

.comment-date {
  font-size: 12px;
  color: var(--text-light);
}

.comment-text {
  font-size: 14px;
  margin-bottom: 8px;
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 12px;
  color: var(--text-light);
}

.comment-action-buttons {
  display: flex;
  align-items: center;
  gap: 12px;
}

.reply-button {
  background: none;
  border: none;
  padding: 0;
  font-size: 12px;
  color: var(--text-light);
  cursor: pointer;
  transition: color 0.2s;
}

.reply-button:hover {
  color: var(--text-primary); /* 민트색 */
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
</style>
