<template>
  <div class="comments-section">
    <h2 class="comments-title">댓글 {{ comments.length }}개</h2>

    <!-- 댓글 작성 폼 -->
    <CommentCreateForm @addComment="addComment" />

    <!-- 댓글 목록 -->
    <div class="comments-list">
      <CommentListItem
        v-for="comment in comments"
        :key="comment.id"
        :comment="comment"
        @addComment="addComment"
        @editComment="editComment"
        @deleteComment="deleteComment"
        @reportComment="reportComment"
      />
    </div>

    <BaseReportModal v-if="userStore.isLogined" ref="reportModal" />
  </div>
</template>

<script setup>
import CommentCreateForm from "@/components/article/comment/CommentCreateForm.vue";
import CommentListItem from "@/components/article/comment/CommentListItem.vue";
import BaseReportModal from "@/components/common/BaseReportModal.vue";
import * as commentApi from "@/apis/articles/comment";
import { ref, onMounted, computed, watch } from "vue";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

const props = defineProps({ articleId: Number });

let articleId = props.articleId;
const comments = ref([]);

const loadComment = async () => {
  try {
    comments.value = await commentApi.loadComment(articleId);
  } catch (e) {
    alert("댓글 목록 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

onMounted(() => {
  loadComment();
});

// 댓글 작성
const addComment = async (comment, content) => {
  try {
    const { message, commentId } = await commentApi.createComment(articleId, comment ? comment.id : null, content);
    const newComment = {
      id: commentId,
      memberId: userStore.user.id,
      memberName: userStore.user.name,
      memberProfileImage: userStore.user.profileImage,
      createdAt: new Date().toISOString(),
      modifiedAt: null,
      content: content,
    };
    if (comment == null) {
      comments.value.push({
        ...newComment,
        replies: [],
      });
    } else {
      if (!comment.replies) comment.replies = [];
      comment.replies.push({
        ...newComment,
      });
    }
  } catch (e) {
    alert("댓글 등록 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// 댓글 수정
const editComment = async (comment, content) => {
  try {
    const response = await commentApi.editComment(articleId, comment.id, content);
    comment.content = content;
  } catch (e) {
    alert("댓글글 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// 댓글 삭제
const deleteComment = async (comment) => {
  try {
    const response = await commentApi.deleteComment(articleId, comment.id);
    const idx = comments.value.findIndex((c) => c.id == comment.id);
    if (idx != -1) comments.value.splice(idx, 1);
  } catch (e) {
    console.log(e);
    alert("댓글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// 댓글 신고
const reportModal = ref(null);

const reportComment = async (comment) => {
  reportModal.value.open(articleId, comment.id);
};

watch(
  () => props.articleId,
  (newVal) => {
    articleId = newVal;
    loadComment();
  }
);
</script>

<style scoped>
/* 댓글 섹션 */
.comments-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comments-title {
  font-size: 24px;
  font-weight: 700;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comment-item {
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 16px;
}

.comment-item:last-child {
  border-bottom: none;
}
</style>
