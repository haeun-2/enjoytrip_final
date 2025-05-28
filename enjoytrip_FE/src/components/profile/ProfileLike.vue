<template>
  <div class="likes-content">
    <div class="likes-navigation">
      <button
        v-for="category in likeCategories"
        :key="category.id"
        @click="activeLikeCategory = category.id"
        :class="['like-tab-button', { 'like-tab-active': activeLikeCategory === category.id }]"
      >
        {{ category.label }} ({{ category.count }})
      </button>
    </div>

    <div class="like-container">
      <!-- 좋아요한 관광지 -->
      <div v-if="activeLikeCategory === 'attractions'" class="content-grid">
        <ProfileContentCard v-for="item in likedAttractions" :key="item.id" :item="item" />
      </div>

      <!-- 좋아요한 여행계획 -->
      <div v-if="activeLikeCategory === 'plans'" class="content-list">
        <ProfileContentListItem v-for="item in likedPlans" :key="item.id" :item="item" :type="activeLikeCategory" />
      </div>

      <!-- 좋아요한 게시글 -->
      <div v-if="activeLikeCategory === 'articles'" class="content-list">
        <ProfileContentListItem v-for="item in likedArticles" :key="item.id" :item="item" :type="activeLikeCategory" />
      </div>

      <!-- 좋아요한 핫플레이스 -->
      <div v-if="activeLikeCategory === 'hotplaces'" class="content-grid">
        <ProfileContentCard v-for="item in likedHotplaces" :key="item.id" :item="item" />
      </div>
    </div>

    <BasePagination :page="page" @changePage="changePage" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import ProfileContentListItem from "@/components/profile/ProfileContentListItem.vue";
import ProfileContentCard from "@/components/profile/ProfileContentCard.vue";
import * as memberLikeApi from "@/apis/member/member_like";
import { useUserStore } from "@/stores/user";
import BasePagination from "../common/BasePagination.vue";

const userStore = useUserStore();
const userId = userStore.user.id;

const page = ref({});
const activeLikeCategory = ref("attractions");

// 좋아요 카테고리
const likeCategories = ref([
  { id: "attractions", label: "관광지", count: 0 },
  { id: "plans", label: "여행계획", count: 0 },
  { id: "articles", label: "게시글", count: 0 },
  { id: "hotplaces", label: "핫플레이스", count: 0 },
]);

// 좋아요한 관광지 데이터
const likedAttractions = ref([]);
const attractionSearchCondition = ref({ page: 1, size: 6 });
const loadLikedAttractions = async () => {
  try {
    const response = await memberLikeApi.getlikedAttractions(userId, attractionSearchCondition.value);
    likedAttractions.value = response.list;
    likeCategories.value[0].count = response.totalCount;
    page.value = {
      navSize: response.navSize,
      currentPage: response.currentPage,
      startPage: response.startPage,
      endPage: response.endPage,
      hasPre: response.hasPre,
      hasNext: response.hasNext,
    };
  } catch (e) {
    alert("좋아요 목록 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};
// 좋아요한 여행계획 데이터
const likedPlans = ref([]);
const planSearchCondition = ref({ page: 1, size: 6 });
const loadLikedPlans = async () => {
  try {
    const response = await memberLikeApi.getlikedPlans(userId, planSearchCondition.value);
    likeCategories.value[1].count = response.totalCount;
    likedPlans.value = response.list;
    page.value = {
      navSize: response.navSize,
      currentPage: response.currentPage,
      startPage: response.startPage,
      endPage: response.endPage,
      hasPre: response.hasPre,
      hasNext: response.hasNext,
    };
  } catch (e) {
    alert("좋아요 목록 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// 좋아요한 게시글 데이터
const likedArticles = ref([]);
const articleSearchCondition = ref({ page: 1, size: 6 });
const loadLikedArticles = async () => {
  try {
    const response = await memberLikeApi.getlikedArticles(userId, articleSearchCondition.value);
    likeCategories.value[2].count = response.totalCount;
    likedArticles.value = response.list;
    page.value = {
      navSize: response.navSize,
      currentPage: response.currentPage,
      startPage: response.startPage,
      endPage: response.endPage,
      hasPre: response.hasPre,
      hasNext: response.hasNext,
    };
  } catch (e) {
    alert("좋아요 목록 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// 좋아요한 핫플레이스 데이터
const likedHotplaces = ref([]);
const hotplaceSearchCondition = ref({ page: 1, size: 6 });
const loadLikedHotplaces = async () => {
  try {
    const response = await memberLikeApi.getlikedHotplaces(userId, hotplaceSearchCondition.value);
    likeCategories.value[3].count = response.totalCount;
    likedHotplaces.value = response.list;
    page.value = {
      navSize: response.navSize,
      currentPage: response.currentPage,
      startPage: response.startPage,
      endPage: response.endPage,
      hasPre: response.hasPre,
      hasNext: response.hasNext,
    };
  } catch (e) {
    alert("좋아요 목록 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

const changePage = (num) => {
  if (activeLikeCategory.value == "attractions") attractionSearchCondition.value.page = num;
  if (activeLikeCategory.value == "plans") planSearchCondition.value.page = num;
  if (activeLikeCategory.value == "articles") articleSearchCondition.value.page = num;
  if (activeLikeCategory.value == "hotplaces") hotplaceSearchCondition.value.page = num;
};

onMounted(() => {
  loadLikedAttractions();
  loadLikedPlans();
  loadLikedArticles();
  loadLikedHotplaces();
});

watch(activeLikeCategory, () => {
  if (activeLikeCategory.value == "attractions") loadLikedAttractions();
  if (activeLikeCategory.value == "plans") loadLikedPlans();
  if (activeLikeCategory.value == "articles") loadLikedArticles();
  if (activeLikeCategory.value == "hotplaces") loadLikedHotplaces();
});
watch(
  () => attractionSearchCondition.value.page,
  () => loadLikedAttractions()
);
watch(
  () => planSearchCondition.value.page,
  () => loadLikedPlans()
);
watch(
  () => articleSearchCondition.value.page,
  () => loadLikedArticles()
);
watch(
  () => hotplaceSearchCondition.value.page,
  () => loadLikedHotplaces()
);
</script>

<style scoped>
.like-container {
  margin: 20px;
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.content-grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}
.content-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

/* 좋아요한 항목 */
.likes-content {
  padding: 2rem;
}

.likes-navigation {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 2rem;
  overflow-x: auto;
}

.like-tab-button {
  padding: 0.5rem 1rem;
  background-color: #f3f4f6;
  color: var(--text-color);
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
}

.like-tab-button:hover {
  background-color: #e5e7eb;
}

.like-tab-button.like-tab-active {
  background-color: var(--primary-color);
  color: white;
}
</style>
