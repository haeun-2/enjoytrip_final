<template>
  <div class="sidebar-section">
    <h3>카테고리</h3>
    <ul class="category-list">
      <li v-for="category in categories" :key="category.code">
        <a
          href="#"
          @click="changeCategory(category)"
          :class="{ active: selectedCategory.code == category.code }"
          >{{ category.name }}</a
        >
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
const props = defineProps({
  categories: {
    type: Array,
    default: () => null,
  },
});
const defaultCategories = [
  { code: null, name: "모든 카테고리" },
  { code: 12, name: "관광지" },
  { code: 14, name: "문화시설" },
  { code: 15, name: "축제공연행사" },
  { code: 25, name: "여행코스" },
  { code: 28, name: "레포츠" },
  { code: 32, name: "숙박" },
  { code: 38, name: "쇼핑" },
];
const categories = computed(() => {
  return props.categories ? props.categories : defaultCategories;
});

const selectedCategory = ref(categories.value[0]);

const emit = defineEmits(["changeCategory"]);
const changeCategory = (category) => {
  selectedCategory.value = category;
  emit("changeCategory", category);
};
</script>

<style scoped>
.sidebar-section {
  background-color: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  padding: 16px;
  margin-bottom: 24px;
}

.sidebar-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--text-color);
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border-color);
}

.category-list li {
  margin-bottom: 4px;
}

.category-list a {
  display: block;
  padding: 8px 12px;
  font-size: 14px;
  color: var(--text-color);
  border-radius: var(--border-radius);
  transition: var(--transition);
}

.category-list a:hover {
  background-color: var(--primary-light);
  color: var(--primary-dark);
}

.category-list a.active {
  background-color: var(--primary-color);
  color: white;
}
</style>
