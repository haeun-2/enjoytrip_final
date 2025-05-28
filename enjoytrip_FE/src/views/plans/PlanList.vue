<template>
  <div class="container">
    <main class="main-content">
      <!-- 사이드바 -->
      <aside class="sidebar">
        <SideCategory
          :categories="categories"
          @changeCategory="handleCategory"
        />
        <SideFilter @changeFilter="handleFilter" />
        <SidePopular />
      </aside>

      <!-- 게시판 콘텐츠 -->
      <div class="board-content">
        <!-- 검색 바 -->
        <div class="search-bar-container">
          <BaseSearchBar text="여행지, 키워드 검색..." />
          <TripPlanCreatePopup @date-range-selected="handleDateRange" />
        </div>
        <!-- 인기 여행 계획 -->
        <section class="featured-plans">
          <h2>인기 여행 계획</h2>
          <TripPlanBestCard :item="trend" @click="() => showPlan(trend.id)" />
        </section>

        <!-- 여행 계획 목록 -->
        <section class="travel-plans">
          <div class="travel-plans-header">
            <h2>여행 계획 목록</h2>
            <BaseSortOption @changeSortOpt="handleSort" />
          </div>
          <div class="travel-plan-grid">
            <!-- 여행 계획 카드 -->
            <TripPlanItemCard
              v-for="item in items"
              :key="item.id"
              :item="item"
              @click="() => showPlan(item.id)"
            />
          </div>
        </section>
        <!-- 페이지네이션 -->
        <BasePagination
          class="pagenation-container"
          :page="pagination"
          @changePage="handlePageChange"
        />
      </div>
    </main>
  </div>
</template>

<script setup>
import BaseSortOption from "@/components/common/BaseSortOption.vue";
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BasePagination from "@/components/common/BasePagination.vue";
import SideCategory from "@/components/sidebar/SideCategory.vue";
import SidePopular from "@/components/sidebar/SidePopular.vue";
import SideFilter from "@/components/sidebar/SideFilter.vue";
import TripPlanItemCard from "@/components/tripplan/board/TripPlanItemCard.vue";
import TripPlanBestCard from "@/components/tripplan/board/TripPlanBestCard.vue";
import TripPlanCreatePopup from "@/components/tripplan/board/TripPlanCreatePopup.vue";

import { createTripPlan } from "@/apis/tripplan/plan";

import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { getList, getTrendPlan } from "@/apis/tripplan/plan";
const router = useRouter();
const goCreatePlan = (planInfo) => {
  router.push({
    name: "PlanCreate",
    query: { planInfo: JSON.stringify(planInfo) },
  });
};
const showPlan = (id) => {
  router.push({ name: "PlanDetail", params: { id: id } });
};

const categories = [
  { code: "", name: "전체 여행" },
  { code: "RELAXING", name: "휴양 여행" },
  { code: "SIGHTSEEING", name: "관광 여행" },
  { code: "NATURE", name: "자연 여행" },
  { code: "CULTURAL", name: "문화 여행" },
  { code: "FOODIE", name: "미식 여행" },
  { code: "ADVENTURE", name: "액티비티 여행" },
  { code: "SHOPPING", name: "소핑 여행" },
  { code: "PHOTO", name: "사진 여행" },
];

// 여행 계획 목록 구성
const items = ref([]);
// 인기 여행
const trend = ref({});

const searchFilter = ref({ ranges: [], seasons: [] });
const searchCategory = ref("");
const sortOption = ref("LATEST");
const pageRequest = ref({ page: 0, size: 6 });
const pagination = ref({});

onMounted(async () => {
  const result = await getTrendPlan();
  trend.value = result;

  const response = await getList(
    sortOption.value,
    searchFilter.value,
    searchCategory.value,
    pageRequest.value
  );
  items.value = response.list;

  const navSize = Math.min(response.totalPage, 5);
  pagination.value = {
    navSize,
    currentPage: 1,
    startPage: 1,
    endPage: navSize,
    hasPre: false,
    hasNext: response.hasNext,
  };
});

const handlePageChange = async (num) => {
  pageRequest.value.page = num - 1;
  const response = await getList(
    sortOption.value,
    searchFilter.value,
    searchCategory.value,
    pageRequest.value
  );
  items.value = response.list;
  const navSize = Math.min(response.totalPage, 5);
  pagination.value = {
    navSize,
    currentPage: response.currentPage + 1,
    startPage: response.startPage,
    endPage: response.endPage,
    hasPre: response.hasPre,
    hasNext: response.hasNext,
  };
};

const handleFilter = async (filters) => {
  searchFilter.value = { ranges: filters.duration, seasons: filters.season };
  const response = await getList(
    sortOption.value,
    searchFilter.value,
    searchCategory.value,
    pageRequest.value
  );
  items.value = response.list;
};
const handleSort = async (sort) => {
  sortOption.value = sort;
  const response = await getList(
    sortOption.value,
    searchFilter.value,
    searchCategory.value,
    pageRequest.value
  );
  items.value = response.list;
};
const handleCategory = async (category) => {
  searchCategory.value = category?.code;
  const response = await getList(
    sortOption.value,
    searchFilter.value,
    searchCategory.value,
    pageRequest.value
  );
  items.value = response.list;
};

const handleDateRange = async (range) => {
  const planInfo = {
    title: "여행 제목",
    startDate: range.startDate,
    endDate: range.endDate,
  };
  const response = await createTripPlan(planInfo);
  goCreatePlan(response);
};
</script>

<style scoped>
/* 메인 콘텐츠 스타일 */
.main-content {
  display: flex;
  gap: 24px;
}

.search-bar-container {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

/* 사이드바 스타일 */
.sidebar {
  width: 260px;
  flex-shrink: 0;
}

/* 게시판 콘텐츠 스타일 */
.board-content {
  flex: 1;
  overflow: visible; /* 필수 */
  position: relative;
}

/* 추천 여행 계획 스타일 */
.featured-plans {
  margin-bottom: 32px;
  margin-top: 32px;
}

.featured-plans h2 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 16px;
  color: var(--text-color);
}

/* 여행 계획 목록 스타일 */
.travel-plans-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.travel-plans-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-color);
}

.travel-plan-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}
.pagenation-container {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  padding: 10px;
  z-index: 10;
}
</style>
