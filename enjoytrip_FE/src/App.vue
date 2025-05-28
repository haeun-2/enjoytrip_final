<script setup>
import { computed } from "vue";
import { RouterView, useRoute } from "vue-router";
import BaseHeader from "@/components/common/BaseHeader.vue";
import BaseFooter from "@/components/common/BaseFooter.vue";

const route = useRoute();

// header/footer 숨길 경로 목록 예시
const noHeaderFooterPatterns = [
  /^\/login$/,
  /^\/signup$/,
  /^\/plans\/new$/,
  /^\/attractions$/,
  /^\/plans\/\d+$/, // 숫자나 문자 ID 허용. 숫자만 원하면 \d+
  // /^\/admin(?:\/.*)?$/,
];

// 현재 경로가 숨김 대상인지 체크
const hideHeaderFooter = computed(() => noHeaderFooterPatterns.some((pattern) => pattern.test(route.path)));
</script>

<template>
  <div class="wrapper">
    <BaseHeader v-if="!hideHeaderFooter" />
    <div class="content">
      <RouterView />
    </div>
    <BaseFooter v-if="!hideHeaderFooter" />
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
}
</style>

<style>
body {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
  font-family: "Noto Sans KR", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell,
    "Open Sans", "Helvetica Neue", sans-serif;
}

.container {
  max-width: 1200px;
  margin: 24px auto;
  padding: 0 20px;
}

a {
  text-decoration: none;
  color: inherit;
}

ul {
  list-style: none;
  padding-left: 0;
  margin-left: 0;
}

button {
  cursor: pointer;
  font-family: inherit;
}

img {
  max-width: 100%;
  height: auto;
}
</style>
