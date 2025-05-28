<template>
  <div class="sort-options">
    <select v-model="sortOption" @change="changeSortOpt">
      <option v-for="(option, index) in options" :key="option.value" :value="option.value">
        {{ option.name }}
      </option>
    </select>
  </div>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  options: {
    type: Array,
    default: [
      { value: "LATEST", name: "최신순" },
      { value: "POPULAR", name: "인기순" },
      { value: "comments", name: "댓글순" },
      { value: "views", name: "조회순" },
    ],
  },
});

// 상태 관리
const sortOption = ref(props.options[0].value);

const emit = defineEmits(["changeSortOpt"]);
const changeSortOpt = () => {
  emit("changeSortOpt", sortOption.value);
};
</script>

<style scoped>
.sort-options select {
  padding: 10px 40px 10px 16px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  font-size: 14px;
  color: var(--text-color);
  background-color: white;
  cursor: pointer;
}
</style>
