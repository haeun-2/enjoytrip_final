<template>
  <div class="form-group">
    <label for="category" class="form-label">카테고리</label>
    <select id="category" :value="category" @change="handleChange" class="form-select" required>
      <option value="" disabled>카테고리 선택</option>
      <option v-for="cat in categoryList" :key="cat.value" :value="cat.value">
        {{ cat.label }}
      </option>
    </select>
  </div>
</template>

<script setup>
import { ref } from "vue";

// 카테고리 목록
const categoryList = ref([
  { label: "맛집", value: "RESTAURANT" },
  { label: "카페", value: "CAFE" },
  { label: "관광지", value: "ATTRACTION" },
  { label: "숙소", value: "ACCOMMODATION" },
  { label: "쇼핑", value: "SHOPPING" },
  { label: "기타", value: "ETC" },
]);

const category = ref("");

const handleChange = (event) => {
  const selected = event.target.value;
  category.value = selected === category.value ? "" : selected;
};
</script>

<style scoped></style>
