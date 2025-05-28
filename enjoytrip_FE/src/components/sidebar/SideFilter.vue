<template>
  <div>
    <div class="sidebar-section">
      <h3>필터</h3>
      <div class="filter-section">
        <h4>여행 기간</h4>
        <div class="filter-options">
          <label class="filter-option">
            <input type="checkbox" v-model="filters.duration" value="1-3" />
            <span>1-3일</span>
          </label>
          <label class="filter-option">
            <input type="checkbox" v-model="filters.duration" value="4-7" />
            <span>4-7일</span>
          </label>
          <label class="filter-option">
            <input type="checkbox" v-model="filters.duration" value="8-14" />
            <span>8-14일</span>
          </label>
          <label class="filter-option">
            <input type="checkbox" v-model="filters.duration" value="15~" />
            <span>15일 이상</span>
          </label>
        </div>
      </div>

      <div class="filter-section">
        <h4>여행 계절</h4>
        <div class="filter-options">
          <label class="filter-option">
            <input type="checkbox" v-model="filters.season" value="SPRING" />
            <span>봄</span>
          </label>
          <label class="filter-option">
            <input type="checkbox" v-model="filters.season" value="SUMMER" />
            <span>여름</span>
          </label>
          <label class="filter-option">
            <input type="checkbox" v-model="filters.season" value="FALL" />
            <span>가을</span>
          </label>
          <label class="filter-option">
            <input type="checkbox" v-model="filters.season" value="WINTER" />
            <span>겨울</span>
          </label>
        </div>
      </div>

      <button class="apply-filter-btn" @click="handle">필터 적용</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
const emit = defineEmits(["changeFilter"]);

const filters = ref({
  duration: [],
  season: [],
});

const handle = () => {
  emit("changeFilter", {
    duration: filters.value.duration,
    season: filters.value.season,
  });
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

.filter-section {
  margin-bottom: 16px;
}

.filter-section h4 {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
}

.filter-options {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-option {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: var(--text-color);
  cursor: pointer;
}

.filter-option input[type="checkbox"] {
  margin-right: 8px;
  accent-color: var(--primary-color);
}

.apply-filter-btn {
  width: 100%;
  padding: 8px 0;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius);
  font-size: 14px;
  font-weight: 500;
  transition: var(--transition);
  margin-top: 8px;
}

.apply-filter-btn:hover {
  background-color: var(--primary-dark);
}
</style>
