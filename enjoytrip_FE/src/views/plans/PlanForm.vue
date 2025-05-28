<template>
  <div class="trip-planner-container">
    <!-- 왼쪽 사이드 패널 (장소 검색 및 목록) -->
    <SidePanel minWidth="400" maxWidth="1000" style="z-index: 100">
      <SideNav style="position: fixed" />
      <div class="trip-planner">
        <div class="main-content">
          <PlanCreateHeader
            @changeSearch="handleSearch"
            @clear="handleClear"
            @save="(desc) => handleSave(desc)"
            @changeTab="handleChangeTab"
            v-model:title="title"
            :startDate="props.planInfo.startDate"
            :endDate="props.planInfo.endDate"
          />
          <!-- 장소 목록 -->
          <div class="places-list" ref="container">
            <PlanCreateListItem
              v-for="place in places"
              :key="place.id"
              class="place-item"
              :class="{ selected: isPlaceSelected(place.id) }"
              :place="place"
              :selectedPlaces="currentDayPlaces"
              @togglePlaceSelection="(placeId) => handleTogglePlaceSelection(placeId, place)"
              @click="handlePlaceClick(place)"
            />
          </div>
        </div>
      </div>
    </SidePanel>

    <!-- 지도 영역 (중앙) -->
    <div class="map-area">
      <button class="btn-shortest-path" @click="findShortestPath">최단 경로 찾기</button>
      <TripPlanMapView ref="mapViewRef" :places="places" />
    </div>

    <!-- 오른쪽 일차 탭 패널-->
    <DayTabsPanel
      v-model:selectedDay="selectedDay"
      :totalDays="totalDays"
      :dayPlaces="dayPlaces"
      :placesCache="placesCache"
      @removePlace="removePlace"
      @reorderPlaces="handleReorderPlaces"
      style="z-index: 100"
    />
    <TripDetailCenterPopup
      v-if="selectedPlace"
      :place="selectedPlace"
      @close="closePopup"
      @add="(placeId) => handleTogglePlaceSelection(placeId, selectedPlace, true)"
    />
    <BaseLodingSpinner v-if="isLoading" />
  </div>
</template>

<script setup>
import TripPlanMapView from "@/components/tripplan/detail/TripPlanMapView.vue";
import PlanCreateHeader from "@/components/tripplan/create/PlanCreateHeader.vue";
import PlanCreateListItem from "@/components/tripplan/create/PlanCreateListItem.vue";
import DayTabsPanel from "@/components/tripplan/create/DayTabsPanel.vue";
import TripDetailCenterPopup from "@/components/tripplan/create/TripDetailCenterPopup.vue";
import SidePanel from "@/components/sidebar/SidePanel.vue";
import SideNav from "@/components/sidebar/SideNav.vue";
import BaseLodingSpinner from "@/components/common/BaseLodingSpinner.vue";
import { getAttractionPage } from "@/apis/attraction/attraction";
import { searchHotplace } from "@/apis/hotplaces/hotplaces";
import { createDetailTripPlan } from "@/apis/tripplan/plan";
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useInfiniteScroll } from "@vueuse/core";
import { getTime } from "@/components/tripplan/detail/mapUtil";
const router = useRouter();
// 상태 정의
const activeTab = ref("places");
const selectedDay = ref(1);
const totalDays = ref(3); // 기본 3일 일정
const dayPlaces = ref({}); // 일차별 선택된 장소 ID 저장 객체
const placesCache = ref({}); // 장소 ID를 키로 하는 장소 정보 캐시

// 장소 목록
const places = ref([]);
const pageRequest = ref({ page: 1, size: 10 });

// 여행 계획 제목
const title = ref("제목");
const props = defineProps({
  planInfo: {
    type: Object,
    required: true,
  },
});

// 무한 스크롤
const container = ref(null);
useInfiniteScroll(
  container,
  async () => {
    pageRequest.value.page += 1;
    await handlePaging(pageRequest.value.page);
  },
  { distance: 10 }
);

// 장소 팝업
const selectedPlace = ref(null); // 선택된 장소
const handlePlaceClick = (place) => {
  selectedPlace.value = place;
};
const closePopup = () => {
  selectedPlace.value = null;
};

// 지도업데이트
const mapViewRef = ref(null);

// 현재 선택된 일차의 장소 목록
const currentDayPlaces = computed(() => {
  return dayPlaces.value[selectedDay.value] || [];
});

// 모든 일차에 선택된 장소 ID 목록
const allSelectedPlaces = computed(() => {
  const allPlaces = [];
  Object.values(dayPlaces.value).forEach((dayPlaceIds) => {
    allPlaces.push(...dayPlaceIds);
  });
  return allPlaces;
});

// 장소가 어떤 일차에든 선택되었는지 확인
const isPlaceSelected = (placeId) => {
  return allSelectedPlaces.value.includes(placeId);
};

// 일정 초기화 함수
const handleClear = () => {
  // 모든 일차에 대해 빈 배열로 초기화
  const clearedDayPlaces = {};
  for (let i = 1; i <= totalDays.value; i++) {
    clearedDayPlaces[i] = [];
  }
  // 반응성을 위해 참조를 새로 할당
  dayPlaces.value = clearedDayPlaces;
};

const isLoading = ref(false);
const handleSave = async (description) => {
  isLoading.value = true;

  const request = {
    title: title.value,
    description: description,
    tripPlanDayInfo: Object.entries(dayPlaces.value).map(([dayKey, placeIdList]) => {
      return {
        date: getDateAfterN(Number(dayKey) - 1),
        dayOrder: Number(dayKey),
        items: placeIdList.map((placeId, index, arr) => {
          const place = placesCache.value[placeId];
          const prevPlace = index === 0 ? null : placesCache.value[arr[index - 1]];
          return {
            description: "구현해야댐",
            itemOrder: placeId + 1,
            itemType: place.category,
            attractionId: place.member ? null : place.id,
            memberPlaceId: place.member ? place.id : null,
            routeFromPrevious: index === 0 ? null : getDistanceAndDuration(prevPlace, place),
          };
        }),
      };
    }),
  };
  try {
    const response = await createDetailTripPlan(props.planInfo.id, request);
  } catch (e) {
  } finally {
    isLoading.value = false;
    router.push({ name: "PlanMine" });
  }
};

function getDistanceAndDuration(prevPlace, place) {
  const position1 = new kakao.maps.LatLng(prevPlace.latitude, prevPlace.longitude);
  const position2 = new kakao.maps.LatLng(place.latitude, place.longitude);
  const polyline = new kakao.maps.Polyline({
    path: [position1, position2],
  });
  const pathDistance = polyline.getLength();
  return {
    distance: pathDistance,
    duration: getTime(pathDistance, 833), // 시속 50km
    walkDuration: getTime(pathDistance, 67),
  };
}

function getDateAfterN(n) {
  const date = new Date(props.planInfo.startDate);
  date.setDate(date.getDate() + n);
  return date.toISOString().split("T")[0];
}

// 장소 선택/해제 처리 함수
const handleTogglePlaceSelection = (placeId, place, alwaysAdd = false) => {
  // 장소 캐시에 저장
  placesCache.value[placeId] = place;
  // 현재 일차의 장소 목록이 없으면 초기화
  if (!dayPlaces.value[selectedDay.value]) {
    dayPlaces.value[selectedDay.value] = [];
  }
  const currentDayPlacesList = dayPlaces.value[selectedDay.value];
  const index = currentDayPlacesList.indexOf(placeId);

  if (index === -1 || alwaysAdd) {
    // 장소가 선택되지 않았으면 추가
    currentDayPlacesList.push(placeId);
  } else if (index != -1) {
    // 이미 선택된 장소면 제거
    currentDayPlacesList.splice(index, 1);
  }
  // 지도 마커 업데이트 및 지도 이동
  reorderedMapMarkers(currentDayPlacesList);
  // 반응성을 위해 객체 참조 갱신
  dayPlaces.value = { ...dayPlaces.value };
};

// 선택된 장소 제거 함수
const removePlace = (placeId) => {
  const currentDayPlacesList = dayPlaces.value[selectedDay.value];
  const index = currentDayPlacesList.indexOf(placeId);

  if (index !== -1) {
    currentDayPlacesList.splice(index, 1);
    // 반응성을 위해 객체 참조 갱신
    dayPlaces.value = { ...dayPlaces.value };
    reorderedMapMarkers(currentDayPlacesList);
  }
};

// 장소 순서 변경 처리 함수
const handleReorderPlaces = ({ day, fromIndex, toIndex }) => {
  console.log(`순서 변경: ${day}일차, ${fromIndex} -> ${toIndex}`);

  // 해당 일차의 장소 목록 복사
  const dayPlacesList = [...(dayPlaces.value[day] || [])];

  // 유효한 인덱스인지 확인
  if (fromIndex < 0 || fromIndex >= dayPlacesList.length || toIndex < 0 || toIndex >= dayPlacesList.length) {
    console.error("Invalid index for reordering");
    return;
  }

  // 드래그한 항목을 배열에서 제거
  const [movedItem] = dayPlacesList.splice(fromIndex, 1);

  // 새 위치에 항목 삽입
  dayPlacesList.splice(toIndex, 0, movedItem);

  // 변경된 배열로 업데이트
  dayPlaces.value = {
    ...dayPlaces.value,
    [day]: dayPlacesList,
  };

  // drawMarkers 호출 (장소 정보로 변환)
  reorderedMapMarkers(dayPlacesList);
};

// 지도 마커 업데이트
const reorderedMapMarkers = (currentDayItemIdList) => {
  const reorderedPlaces = currentDayItemIdList.map((placeId) => placesCache.value[placeId]).filter(Boolean);
  if (mapViewRef.value?.drawMarkers) {
    mapViewRef.value.drawMarkers(reorderedPlaces, selectedDay.value - 1);
  }
};

// 최단 경로 계산
const findShortestPath = () => {
  const currentDayItemIdList = dayPlaces.value[selectedDay.value];
  const reorderedPlaces = currentDayItemIdList.map((placeId) => placesCache.value[placeId]).filter(Boolean);
  const result = mapViewRef.value.findShortestPath(reorderedPlaces);
  const shortestPathIdList = result.map((result) => result.id);
  dayPlaces.value = {
    ...dayPlaces.value,
    [selectedDay.value]: shortestPathIdList,
  };
  reorderedMapMarkers(shortestPathIdList);
};
const sidoCode = ref(0);
const gugunCode = ref(0);
const categoryCode = ref(0);
const sidoId = ref(0);
const gugunId = ref(0);
const categoryId = ref(0);
const sortCode = ref("");
const hasNext = ref(true);

// 검색 처리 함수
const handleSearchPlace = async ({ sido = "", gugun = "", category = "", sort = "LATEST" } = {}) => {
  sidoCode.value = sido;
  gugunCode.value = gugun;
  categoryCode.value = category;
  sortCode.value = sort;
  const requestData = { sido, gugun, category };
  pageRequest.value.page = 0;
  hasNext.value = true;
  const response = await getAttractionPage(requestData, sort, pageRequest.value);
  places.value = response.list;

  // 검색 결과를 캐시에 저장
  places.value.forEach((place) => {
    placesCache.value[place.id] = place;
  });
};

const handlePaging = async (page) => {
  if (!hasNext.value) return;
  if (activeTab.value == "myPlaces") {
    handlePagingMine(page);
    return;
  }
  const requestData = {
    sido: sidoCode.value,
    gugun: gugunCode.value,
    category: categoryCode.value,
  };
  pageRequest.value.page = page;
  const response = await getAttractionPage(requestData, sortCode.value, pageRequest.value);
  hasNext.value = response.hasNext;
  places.value.push(...response.list);

  // 검색 결과를 캐시에 저장
  places.value.forEach((place) => {
    placesCache.value[place.id] = place;
  });
};

const handleSearchMyPlace = async ({ sido = "", gugun = "", category = "", sort = "LATEST" } = {}) => {
  sidoId.value = sido;
  gugunId.value = gugun;
  categoryId.value = category;
  sortCode.value = sort;
  pageRequest.value.page = 0;
  const requestData = {
    page: pageRequest.value.page + 1,
    size: pageRequest.value.size,
    sidoIds: [sido],
    categoryId: category,
    mine: true,
  };
  const response = await searchHotplace(requestData);
  places.value = response.list;

  // 검색 결과를 캐시에 저장
  places.value.forEach((place) => {
    place.category = place.category.categoryName;
    placesCache.value[place.id] = place;
  });
};

const handlePagingMine = async (page) => {
  if (!hasNext.value) return;
  pageRequest.value.page = page;
  const requestData = {
    page: pageRequest.value.page + 1,
    size: pageRequest.value.size,
    sidoIds: [sidoId.value],
    categoryId: categoryId.value,
    mine: true,
  };
  const response = await searchHotplace(requestData);
  hasNext.value = response.hasNext;
  places.value.push(...response.list);

  // 검색 결과를 캐시에 저장
  places.value.forEach((place) => {
    place.category = place.category.categoryName;
    placesCache.value[place.id] = place;
  });
};

const handleChangeTab = (to) => {
  activeTab.value = to;
  if (activeTab.value == "myPlaces") {
    sidoCode.value = "";
    handleSearchMyPlace();
  } else {
    handleSearchPlace();
  }
};

const handleSearch = (data) => {
  if (activeTab.value == "myPlaces") {
    handleSearchMyPlace(data);
  } else {
    handleSearchPlace(data);
  }
};
// 컴포넌트 마운트 시 초기화
onMounted(() => {
  // 초기 검색 실행
  handleSearchPlace();
  title.value = props.planInfo.title;
  totalDays.value = props.planInfo.totalDays;
  // 각 일차별 빈 배열 초기화
  // TODO: 로컬 스토리지에서 저장된 내용 불러오기
  for (let i = 1; i <= totalDays.value; i++) {
    dayPlaces.value[i] = [];
  }
});
</script>

<style scoped>
.trip-planner-container {
  display: flex;
  height: 100vh;
  position: relative;
}

.trip-planner {
  margin-left: 100px;
  display: flex;
  flex-direction: column;
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.main-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.places-list {
  flex: 1;
  overflow-y: auto;
  padding: 0;
}

.place-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid #e0e0e0;
  cursor: pointer;
  width: 410px;
  box-sizing: border-box;
}

.place-item:hover {
  background-color: #f9f9f9;
}

.place-item.selected {
  background-color: #e3f2fd;
}

.map-area {
  flex: 1;
  height: 100%;
  position: relative;
}
.btn-shortest-path {
  position: fixed;
  bottom: 20px;
  right: 420px;
  display: flex;
  align-items: center;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 100px;
  padding: 10px 18px;
  font-weight: 500;
  font-size: 15px;
  cursor: pointer;
  transition: background-color 0.25s ease;
  z-index: 20;
}

.btn-shortest-path:hover {
  background-color: var(--primary-hover);
}
@media (max-width: 768px) {
  .trip-planner-container {
    flex-direction: column;
  }
}
</style>
