<template>
  <div class="container">
    <ArticleNavigation />

    <ArticleFreeList v-if="type == 'free'" />
    <ArticleNoticeList v-else-if="type == 'notice'" />
    <ArticleQNAList v-else-if="type == 'qna'" />
  </div>
</template>

<script setup>
import ArticleNavigation from "@/components/article/ArticleNavigation.vue";
import ArticleFreeList from "@/views/articles/list/ArticleFreeList.vue";
import ArticleNoticeList from "@/views/articles/list/ArticleNoticeList.vue";
import ArticleQNAList from "@/views/articles/list/ArticleQNAList.vue";
import { ref, watch } from "vue";
import { useRoute } from "vue-router";
const route = useRoute();
const type = ref(route.params.type);

watch(
  () => route.params.type,
  () => {
    type.value = route.params.type;
  }
);
</script>

<style scoped></style>
