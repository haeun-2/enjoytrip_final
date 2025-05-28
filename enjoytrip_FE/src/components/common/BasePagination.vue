<template>
  <div>
    <div class="pagination">
      <button
        class="pagination-prev"
        :disabled="!page.hasPre"
        @click="changePage(page.currentPage - 1)"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          width="18"
          height="18"
        >
          <path
            fill="currentColor"
            d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"
          />
        </svg>
        이전
      </button>
      <div class="pagination-numbers">
        <a
          href="#"
          v-for="num in pageNumbers"
          :key="num"
          :class="{ active: num == page.currentPage }"
          @click="changePage(num)"
          >{{ num }}</a
        >
      </div>
      <button
        class="pagination-next"
        :disabled="!page.hasNext"
        @click="changePage(page.currentPage + 1)"
      >
        다음
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          width="18"
          height="18"
        >
          <path
            fill="currentColor"
            d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"
          />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
const props = defineProps({
  page: {
    type: Object,
    default: () => ({
      navSize: 5,
      currentPage: 1,
      startPage: 1,
      endPage: 5,
      hasPre: false,
      hasNext: false,
    }),
  },
});
const emit = defineEmits(["changePage"]);
const changePage = (num) => {
  if (num > props.page.totalPage || num < 0) return;
  emit("changePage", num);
};

const pageNumbers = computed(() => {
  return Array.from(
    { length: props.page.endPage - props.page.startPage + 1 },
    (_, i) => i + props.page.startPage
  );
});
</script>

<style scoped>
/* 페이지네이션 스타일 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination-prev,
.pagination-next {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 16px;
  background-color: white;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  font-size: 14px;
  color: var(--text-color);
  transition: var(--transition);
}

.pagination-prev:hover,
.pagination-next:hover {
  background-color: var(--primary-light);
  color: var(--primary-color);
}

.pagination-prev:disabled,
.pagination-next:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background-color: var(--background-color);
}

.pagination-numbers {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 16px;
}

.pagination-numbers a {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  font-size: 14px;
  color: var(--text-color);
  transition: var(--transition);
}

.pagination-numbers a:hover {
  background-color: var(--primary-light);
  color: var(--primary-color);
}

.pagination-numbers a.active {
  background-color: var(--primary-color);
  color: white;
}

.pagination-numbers span {
  color: var(--text-light);
}
</style>
