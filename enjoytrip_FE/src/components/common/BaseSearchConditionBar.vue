<template>
    <!-- 필터 버튼 -->
    <div class="filters">
        <select v-model="selectedSido" class="filter-select">
            <option value="">전체 시도</option>
            <option v-for="sido in sidos" :key="sido.sidoCode" :value="sido.sidoCode">
                {{ sido.sidoName }}
            </option>
        </select>

        <select v-model="selectedGugun" :disabled="!selectedSido" class="filter-select">
            <option value="">전체 구군</option>
            <option v-for="gugun in guguns" :key="gugun.gugunCode" :value="gugun.gugunCode">
                {{ gugun.gugunName }}
            </option>
        </select>

        <select v-model="selectedCategory" class="filter-select">
            <option value="">전체 카테고리</option>
            <option v-for="category in categories" :key="category.code" :value="category.code">
                {{ category.name }}
            </option>
        </select>
        <select v-model="selectedSort" class="filter-select">
            <option value="LATEST">최신순</option>
            <option value="POPULAR">인기순</option>
        </select>
        <button class="search-btn" @click="search">검색</button>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { getAllSido, getAllGugun } from '@/apis/region/region';

// 상태 정의
const categories = [
  { code: 12, name: "관광지" },
  { code: 14, name: "문화시설" },
  { code: 15, name: "축제공연행사" },
  { code: 25, name: "여행코스" },
  { code: 28, name: "레포츠" },
  { code: 32, name: "숙박" },
  { code: 38, name: "쇼핑" },
];
const activeTab = ref("places");
const selectedSido = ref('');
const selectedGugun = ref('');
const selectedCategory = ref('');
const selectedSort = ref('');
const sidos = ref([])
const guguns = ref([])

onMounted(async () => {
  const data  = await getAllSido();
  sidos.value = data;
});

// 시도 변경 시 구군 재로드
watch(selectedSido, async (newSido) => {
  selectedGugun.value = '';
  if (newSido) {
    const data = await getAllGugun(newSido);
    guguns.value = data;
  } else {
    guguns.value = [];
  }
});

const emit = defineEmits(['changeSearch'])
const search = () => {
    emit('changeSearch', {
        sido: selectedSido.value,
        gugun: selectedGugun.value,
        category: selectedCategory.value,
        sort: selectedSort.value
    });
}
</script>

<style scoped>

.search-bar {
  display: flex;
  padding: 12px;
  border-bottom: 1px solid #e0e0e0;
}

.filters {
  display: flex;
  padding: 12px;
  border-bottom: 1px solid #e0e0e0;
  gap: 10px;
  flex-wrap: wrap;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background-color: white;
  font-size: 14px;
  flex: 1;
  min-width: 120px;
  max-width: 200px;
  height: 38px;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24' fill='none' stroke='%23666' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'%3E%3C/polyline%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 10px center;
  cursor: pointer;
}

.filter-select:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
  opacity: 0.7;
}

.search-btn {
  padding: 8px 20px;
  background-color: var(--blue-3);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  height: 38px;
  transition: background-color 0.2s;
}

.search-btn:hover {
  background-color: var(--blue-2);
}
</style>