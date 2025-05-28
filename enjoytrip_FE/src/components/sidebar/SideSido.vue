<template>
  <div>
    <div class="sidebar-section">
      <h3>시도별 핫플</h3>
      <div class="sido-list" v-for="sido in sidoList" :key="sido.id">
        <label class="sido-list-item" :class="{ active: isSelected(sido) }">
          <input type="checkbox" :value="sido" v-model="selectedSido" class="hidden-checkbox" />
          <span class="sido-name">{{ sido.sidoName }}</span>
        </label>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRegionStore } from "@/stores/region.js";

const emit = defineEmits(["changeSido"]);

const selectedSido = ref([]);

const isSelected = (sido) => {
  return selectedSido.value.includes(sido);
};

watch(selectedSido, () => {
  emit("changeSido", selectedSido.value);
});

// 시도 정보 가져오기
const regionStore = useRegionStore();
const sidoList = ref([]);

onMounted(async () => {
  sidoList.value = await regionStore.getSidoList();
});
</script>

<style scoped>
.sidebar {
  width: 260px;
  flex-shrink: 0;
}

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

.sido-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 5px;
}

.sido-list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background-color: var(--background-color);
  border-radius: var(--border-radius);
  transition: var(--transition);
  cursor: pointer;
}

.sido-list-item:hover {
  background-color: var(--primary-light);
}

.sido-name {
  font-size: 14px;
  font-weight: 500;
}

.active {
  background-color: var(--primary-color);
  color: white;
}

.sido-list-item input[type="checkbox"] {
  accent-color: var(--primary-color);
}

/* 체크박스 자체 숨기기 */
.hidden-checkbox {
  position: absolute;
  opacity: 0;
  pointer-events: none;
  width: 0;
  height: 0;
}
</style>
