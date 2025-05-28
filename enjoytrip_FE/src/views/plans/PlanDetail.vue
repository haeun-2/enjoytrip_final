<template>
  <div style="overflow: hidden; height: 100%">
    <TripPlanRouteMapView ref="mapViewRef" :tripDayList="tripDayList" />
    <SidePanel :minSize="minSize" :maxSize="maxSize" style="z-index: 100">
      <SideNav style="position: fixed" />
      <div style="margin-left: 100px">
        <TripPlanTimeListHeader
          :headerInfo="headerInfo"
          :days="days"
          :selectedDay="selectedDay"
          @changeDay="handleDayChange"
          class="plan-header"
        />
        <TripPlanTimeLineList
          :items="selectedDay === -1 ? tripDayList : tripDayList[selectedDay]"
          :isAll="selectedDay === -1"
          :selectedDay="selectedDay"
        />
      </div>
    </SidePanel>
  </div>
</template>

<script setup>
import SidePanel from "@/components/sidebar/SidePanel.vue";
import SideNav from "@/components/sidebar/SideNav.vue";
import TripPlanTimeListHeader from "@/components/tripplan/detail/TripPlanTimeListHeader.vue";
import TripPlanTimeLineList from "@/components/tripplan/detail/TripPlanTimeLineList.vue";
import TripPlanRouteMapView from "@/components/tripplan/detail/TripPlanRouteMapView.vue";
import { ref, onMounted, watch } from "vue";
import { getDetail } from "@/apis/tripplan/plan";
import { useRoute } from "vue-router";
const route = useRoute();

// panel size
const maxSize = ref(0);
const minSize = ref(0);

// panel header 에 들어갈 정보
const days = ref([]);
const selectedDay = ref(-1);
const headerInfo = ref({});

// panel content에 들어갈 정보
const tripDayList = ref([]);

// 지도업데이트
const mapViewRef = ref(null);

watch(selectedDay, (newVal) => {
  maxSize.value = newVal == -1 ? tripDayList.value.length * 400 + 110 : 500;
  minSize.value = 500;
  reorderedMapMarkers(tripDayList, selectedDay);
});

onMounted(async () => {
  const response = await getDetail(route.params.id);
  days.value = Array.from(
    { length: response.dailySchedules.length },
    (_, i) => `${i + 1}일차`
  );
  headerInfo.value = {
    name: response.title,
    startDate: response.startDate,
    endDate: response.endDate,
  };
  tripDayList.value = response.dailySchedules.map((tripDay) =>
    tripDay.items.map((item) => ({
      itemNumber: item.itemOrder,
      placeType: item.placeCategory,
      placeName: item.placeName || "",
      placeAddress: item.placeAddress,
      imageUrl: item.placeImageUrl,
      transportTime: item.routeFromPrevious
        ? item.routeFromPrevious.duration
        : null,
      description: item.description || "",
      latitude: item.latitude,
      longitude: item.longitude,
    }))
  );
  // 초기 설정
  maxSize.value =
    selectedDay.value === -1 ? tripDayList.value.length * 400 + 110 : 500;
  minSize.value = 500;
  reorderedMapMarkers(tripDayList);
});
const handleDayChange = (dayIndex) => {
  selectedDay.value = dayIndex;
};

// 지도 마커 업데이트
const reorderedMapMarkers = (tripDayList, selectedDay = -1) => {
  if (mapViewRef.value?.drawMarkers) {
    mapViewRef.value.drawMarkers(tripDayList, selectedDay);
  }
};
</script>

<style scoped>
.plan-header {
  position: sticky;
  top: 0;
  left: 100px;
  z-index: 1000;
  background: white; /* 배경 없으면 겹침 주의 */
  margin-bottom: 10px;
}
</style>
