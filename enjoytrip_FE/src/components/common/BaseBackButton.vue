<template>
  <div class="back-button-container">
    <button class="back-button" @click="goBack">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="16"
        height="16"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
        class="chevron-left-icon"
      >
        <path d="m15 18-6-6 6-6"></path>
      </svg>
      목록으로 돌아가기
    </button>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
const props = defineProps({
  back: Object,
});
const router = useRouter();

const goBack = () => {
  if (props.back) router.push(props.back);
  else router.back();
};
</script>

<style scoped>
.back-button-container {
  margin: 0;
}

.chevron-left-icon {
  margin-right: 4px;
}

.back-button {
  display: inline-flex;
  align-items: center;
  font-size: 14px;
  color: var(--text-light);
  background: none;
  border: none;
  cursor: pointer;
  transition: color 0.2s;
}

.back-button:hover {
  color: var(--primary-hover);
}
</style>
