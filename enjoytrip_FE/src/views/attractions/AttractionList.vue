<template>
  <div class="attraction-container">
    <SidePanel minWidth="400" maxWidth="1000">
      <div class="side-container">
        <SideNav style="position: fixed" />
        <div class="filter-container">
          <div>
            <BaseSearchBar text="장소명을 입력하세요" @searchById="handleKeyWordSearch" />
            <BaseSearchConditionBar @changeSearch="handleSearch" />
          </div>
          <div class="place-list-container" ref="container">
            <AttractionItem
              v-for="place in places"
              class="place-item"
              :key="place.id"
              :place="place"
              @click="handlePlaceClick(place)"
            />
          </div>
        </div>
      </div>
    </SidePanel>
    <div class="map-area">
      <AttractionMap :places="places" ref="mapViewRef" @clickMarker="handlePlaceClick" />
    </div>
    <transition name="slide-left">
      <TripDetailPopup class="popup-wrapper" v-if="selectedPlace" :place="selectedPlace" @close="closePopup" />
    </transition>
  </div>
</template>

<script setup>
import SidePanel from "@/components/sidebar/SidePanel.vue";
import SideNav from "@/components/sidebar/SideNav.vue";
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BaseSearchConditionBar from "@/components/common/BaseSearchConditionBar.vue";
import TripDetailPopup from "@/components/tripplan/create/TripDetailPopup.vue";
import AttractionItem from "@/components/attraction/AttractionItem.vue";
import AttractionMap from "@/components/attraction/AttractionMap.vue";
import { getAttractionPage } from "@/apis/attraction/attraction";
import { ref, onMounted } from "vue";
import { useInfiniteScroll } from "@vueuse/core";
import { getAttractionDetail } from "@/apis/attraction/attraction";

const places = ref([]);
const selectedPlace = ref(null);
const sidoCode = ref(0);
const gugunCode = ref(0);
const categoryCode = ref(0);
const sortOption = ref("LATEST");
const pageRequest = ref({ page: 1, size: 15 });
const hasNext = ref(false);
// 지도 업데이트
const mapViewRef = ref(null);

const handleSearch = async ({ sido = "", gugun = "", category = "", sort = "LATEST" } = {}) => {
  sidoCode.value = sido;
  gugunCode.value = gugun;
  categoryCode.value = category;
  sortOption.value = sort;
  const requestData = { sido, gugun, category };
  pageRequest.value.page = 0;
  const response = await getAttractionPage(requestData, sort, pageRequest.value);
  hasNext.value = response.hasNext;
  places.value = response.list;

  // 지도 마커 초기화
  if (mapViewRef.value?.drawMarkers) {
    mapViewRef.value.drawMarkers(places.value);
  }
  // 위치 변경시 팝업도 닫음.
  closePopup();
};

const handleKeyWordSearch = async (placeId) => {
  const response = await getAttractionDetail(placeId);
  selectedPlace.value = response;
  if (mapViewRef.value?.addPin) {
    mapViewRef.value.addPin(response);
  }
};

const handlePlaceClick = async (place) => {
  if (mapViewRef.value?.addPin) {
    mapViewRef.value.addPin(place);
  }
  const response = await getAttractionDetail(place.id);
  selectedPlace.value = response;
};

const closePopup = () => {
  selectedPlace.value = null;
};

// 무한 스크롤
const container = ref(null);
useInfiniteScroll(
  container,
  async () => {
    pageRequest.value.page += 1;
    await handlePaging(pageRequest.value.page);
  },
  { distance: 100 }
);

const handlePaging = async (page) => {
  if (!hasNext.value) return;
  const requestData = {
    sido: sidoCode.value,
    gugun: gugunCode.value,
    category: categoryCode.value,
  };
  pageRequest.value.page = page;
  const response = await getAttractionPage(requestData, sortOption.value, pageRequest.value);
  hasNext.value = response.hasNext;
  places.value.push(...response.list);

  // 지도 마커 변경
  if (mapViewRef.value?.addMarkers) {
    mapViewRef.value.addMarkers(places.value);
  }
};

onMounted(() => {
  handleSearch();
});
</script>

<style scoped>
.attraction-container {
  display: flex;
  height: 100vh;
  position: relative;
}
.side-container {
  display: flex;
  flex-direction: row;
}
.filter-container {
  margin-left: 100px;
  width: 400px;
}
.filter-container > div:first-child {
  width: 400px;
  position: sticky;
  top: 0; /* 화면 상단에서 고정 */
  background-color: white; /* 배경을 덮어주기 위해 */
  z-index: 10; /* 다른 요소보다 위에 있도록 */
  padding-top: 20px;
}
.place-list-container {
  overflow-y: auto;
  max-height: 100vh;
  display: flex;
  gap: 10px;
  flex-direction: column;
  padding: 20px 0px;
  overflow-x: hidden; /* 넘치는 내용 숨김 */
}
.map-area {
  flex: 1;
  height: 100%;
  position: relative;
}

.popup-wrapper {
  position: fixed;
  top: 0;
  left: 500px; /* 최종 위치 */
  width: 400px;
  height: calc(100% - 32px);
  z-index: 10;
  display: flex;
  flex-direction: column;
  padding: 16px;
}

/* 트랜지션 정의 */
.slide-left-enter-active,
.slide-left-leave-active {
  transition: left 0.3s ease, opacity 0.3s ease;
}
.slide-left-enter-from {
  left: 0;
  opacity: 0;
}
.slide-left-enter-to {
  left: 500px;
  opacity: 1;
}
.slide-left-leave-from {
  left: 500px;
  opacity: 1;
}
.slide-left-leave-to {
  left: 0;
  opacity: 0;
}
</style>
