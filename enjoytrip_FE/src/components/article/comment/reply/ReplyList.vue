<template>
  <div v-show="replies && replies.length > 0" class="replies-list">
    <ReplyListItem
      v-for="reply in replies"
      :key="reply.id"
      :reply="reply"
      @editReply="
        (reply, editReplyText) => $emit('editReply', reply, editReplyText)
      "
      @deleteReply="(reply) => $emit('deleteReply', reply)"
      @reportReply="(reply) => $emit('reportReply', reply)"
    ></ReplyListItem>
  </div>
</template>

<script setup>
import ReplyListItem from "@/components/article/comment/reply/ReplyListItem.vue";

defineProps({
  replies: Array,
});
</script>

<style scoped>
/* 답글 목록 */
.replies-list {
  margin-top: 12px;
  margin-left: 20px;
  border-left: 2px solid var(--border-color);
  padding-left: 12px;
}
</style>
