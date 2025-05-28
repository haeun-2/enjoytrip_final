<template>
  <tr class="article-row">
    <td class="id-cell">{{ number }}</td>
    <td class="title-cell">
      <div class="title-container">
        <span>{{ article.title }}</span>
      </div>
    </td>
    <td class="date-cell">{{ formatDate(article.createdAt) }}</td>
    <td class="stats-cell">{{ article.viewCount }}</td>
  </tr>
</template>

<script setup>
import { computed } from "vue";
import { formatDate } from "@/utils/date.js";

const props = defineProps({
  article: Object,
  page: Object,
  index: Number,
});

const number = computed(() => {
  const total = Number(props.page.totalCount);
  const currentPage = Number(props.page.currentPage);
  const pageSize = Number(props.page.size);
  const index = props.index;
  return total - ((currentPage - 1) * pageSize + index);
});
</script>

<style scoped>
.article-row {
  border-bottom: 1px solid color(--border-color);
  transition: background-color 0.2s;
  cursor: pointer;
}

.article-row:hover {
  background-color: var(--background-color);
}

.article-row td {
  padding: 1rem;
}

.id-cell {
  width: 3rem;
}

.title-container {
  display: flex;
  flex-direction: column;
}
</style>
