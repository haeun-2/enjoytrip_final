<template>
  <div class="container">
    <main class="main-content">
      <!-- 사이드바 -->
      <aside class="sidebar">
        <SideCategory @changeCategory="searchCategory" />
        <SideSido @changeSido="searchSido" />
      </aside>
      <div class="hotplace-content">
        <div class="header">
          <h1 class="title">인기 핫플레이스</h1>
          <p class="subtitle">여행자들이 추천하는 인기 장소들을 확인해보세요</p>
        </div>

        <div class="search-bar-container">
          <BaseSearchBar @search="searchKeyword" />
          <BaseSortOption :options="options" @changeSortOpt="changeSortOpt" />
          <BaseButton text="+ 핫플레이스 등록" @click="goCreateHotplace" class="create-button" />
        </div>

        <div class="checkbox-container">
          <label class="mine-checkbox" v-if="userStore.isLogined">
            <input type="checkbox" v-model="searchCondition.mine" />
            내가 쓴 글만 보기
          </label>
        </div>

        <!-- 핫플레이스 카드 그리드 -->
        <div class="hotplace-grid">
          <HotplaceListItem
            v-for="hotplace in hotplaces"
            :key="hotplace.id"
            @click="openModal(hotplace)"
            :hotplace="hotplace"
          />

          <div v-if="hotplaces.length === 0" class="no-results">
            <div class="no-results-message">검색 결과가 없습니다.</div>
          </div>
        </div>

        <!-- 페이지네이션 -->
        <BasePagination :page="page" @changePage="changePage" />
      </div>
    </main>

    <!-- 핫플레이스 상세 모달 -->
    <HotplaceDetail
      :selectedPlace="selectedPlace"
      @deletePlace="deletePlace"
      @closeModal="closeModal"
      @likePlace="likePlace"
    />
  </div>
</template>

<script setup>
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BaseButton from "@/components/common/BaseButton.vue";
import BaseSortOption from "@/components/common/BaseSortOption.vue";
import BasePagination from "@/components/common/BasePagination.vue";
import SideCategory from "@/components/sidebar/SideCategory.vue";
import SideSido from "@/components/sidebar/SideSido.vue";

import HotplaceDetail from "@/views/hotplaces/HotplaceDetail.vue";
import HotplaceListItem from "@/components/hotplaces/HotplaceListItem.vue";

import * as hotplaceApi from "@/apis/hotplaces/hotplaces.js";
import { ref, onMounted, watch, computed } from "vue";
import { useRouter, useRoute } from "vue-router";

import { useUserStore } from "@/stores/user";
const userStore = useUserStore();

const route = useRoute();
const router = useRouter();

const goCreateHotplace = () => {
  if (!userStore.isLogined) {
    alert("로그인이 필요한 기능입니다. 로그인 후 이용해주세요");
    return;
  }
  router.push({ name: "HotplaceCreate" });
};

// 핫플레이스 삭제
const deletePlace = async (hotplace) => {
  try {
    hotplaceApi.deleteHotplace(hotplace.id);
    hotplaces.value = hotplaces.value.filter((p) => p.id !== hotplace.id);
    alert("삭제되었습니다.");
    closeModal();
  } catch (e) {
    alert("핫플 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// 핫플레이스 데이터
const hotplaces = ref([]);
const page = ref({});

// 검색 조건
const searchCondition = ref({
  page: 1,
  size: 9,
  sort: "created_at",
  sidoIds: [],
  categoryId: null,
  keyword: "",
  mine: route.query.mine ? true : false,
});

// 검색 버튼 클릭
const searchKeyword = (keyword) => {
  searchCondition.value.keyword = keyword;
};

// 정렬 조건
const options = ref([
  { value: "created_at", name: "최신순" },
  { value: "like_count", name: "인기순" },
]);

const changeSortOpt = (sortOption) => {
  searchCondition.value.sort = sortOption;
};

// 카테고리 변경
const searchCategory = (category) => {
  searchCondition.value.categoryId = category.code;
};

// 지역 변경
const searchSido = (sidos) => {
  searchCondition.value.sidoIds = sidos.map((sido) => sido.id);
};

// 페이지 변경
const changePage = (num) => {
  searchCondition.value.page = num;
  console.log(searchCondition.value);
};

// 초기 데이터 로드
onMounted(async () => {
  loadHotplaces();
});

const loadHotplaces = async () => {
  try {
    const response = await hotplaceApi.searchHotplace(searchCondition.value);
    hotplaces.value = response.list;
    page.value = {
      navSize: response.navSize,
      currentPage: response.currentPage,
      startPage: response.startPage,
      endPage: response.endPage,
      hasPre: response.hasPre,
      hasNext: response.hasNext,
    };
  } catch (e) {
    alert("핫플 목록 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

watch(
  searchCondition,
  () => {
    loadHotplaces();
  },
  {
    deep: true,
  }
);

// 선택된 핫플레이스 (모달용)
const selectedPlace = ref(null);

const likePlace = (selectedPlace, likeStatus) => {
  selectedPlace.isLiked = likeStatus;
  if (likeStatus) selectedPlace.likeCount++;
  else selectedPlace.likeCount--;
};

// 모달 열기
const openModal = (place) => {
  selectedPlace.value = place;
  document.body.style.overflow = "hidden"; // 스크롤 방지
};

// 모달 닫기
const closeModal = () => {
  selectedPlace.value = null;
  document.body.style.overflow = ""; // 스크롤 복원
};
</script>

<style scoped>
.main-content {
  display: flex;
  gap: 24px;
}

.hotplace-content {
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 2rem;
}

.title {
  font-size: 1.875rem;
  font-weight: bold;
  color: var(--text-color);
  margin-bottom: 0.5rem;
}

.subtitle {
  align-self: flex-end;
  color: var(--text-light);
}

.search-bar-container {
  display: flex;
  flex-direction: row;
  gap: 20px;
  margin-bottom: 10px;
  flex-wrap: wrap;
  align-items: center;
}

/* 사이드바 스타일 */
.sidebar {
  width: 260px;
  flex-shrink: 0;
}

/* 게시판 콘텐츠 스타일 */
.board-content {
  flex: 1;
}
/* 결과 없음 */
.no-results {
  width: 100%;
  grid-column: 1 / -1;
  padding: 3rem 0;
  text-align: center;
}

.no-results-message {
  font-size: 1rem;
  color: var(--text-light);
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);

  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
  padding: 1rem;
}

/* 핫플레이스 그리드 */
.hotplace-grid {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

@media (min-width: 640px) {
  .hotplace-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .hotplace-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

/* 체크박스 스타일링 */
.checkbox-container {
  margin-bottom: 20px;
  display: flex;
  justify-content: end;
}

.mine-checkbox {
  display: flex;
  align-items: center;
  font-size: 0.875rem;
  color: #666;
  cursor: pointer;
  user-select: none;
  margin-left: 0.5rem;
}

.mine-checkbox input[type="checkbox"] {
  appearance: none;
  -webkit-appearance: none;
  width: 1.125rem;
  height: 1.125rem;
  border: 1.5px solid #d1d5db;
  border-radius: 0.25rem;
  margin-right: 0.5rem;
  display: grid;
  place-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.mine-checkbox input[type="checkbox"]:checked {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.mine-checkbox input[type="checkbox"]:hover {
  border-color: var(--primary-color);
  background-color: rgba(45, 212, 191, 0.05);
}

.mine-checkbox input[type="checkbox"]:checked::before {
  content: "";
  width: 0.65rem;
  height: 0.65rem;
  transform: scale(1);
  box-shadow: inset 1em 1em white;
  transform-origin: center;
  clip-path: polygon(14% 44%, 0 65%, 50% 100%, 100% 16%, 80% 0%, 43% 62%);
}

.mine-checkbox:hover {
  color: var(--primary-color);
}
</style>
