<template>
  <div>
    <div id="map" ref="mapContainer"></div>
    <button class="btn-shortest-path" @click="findShortestPath">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="icon"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="2"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M3 12h18M9 6l6 6-6 6"
        />
      </svg>
      최단 경로 찾기
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {
  getTime,
  convertDistanceScale,
  createOverlayWithShortDistance,
  createOverlayWithDistance,
} from "@/components/tripplan/detail/mapUtil";
const props = defineProps({
  tripDayList: Array,
});

const mapContainer = ref(null);
let map = null;
let markers = ref(new Map());
let polylines = ref(new Map());
let overlays = ref(new Map());

onMounted(() => {
  const options = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 3,
  };
  map = new kakao.maps.Map(mapContainer.value, options);
});

const markerColor = [
  "#f44336",
  "#fb8500",
  "#ffc300",
  "#80b918",
  "#1f7a8c",
  "#023047",
  "#5a189a",
];

// 기존 마커, 선, 오버레이 제거
const clearMap = (dayIndex) => {
  const dayMarkers = markers.value.get(dayIndex);
  if (dayMarkers) {
    dayMarkers.forEach((marker) => marker.setMap(null));
    markers.value.delete(dayIndex);
  }
  const dayPolyline = polylines.value.get(dayIndex);
  if (dayPolyline) {
    dayPolyline.setMap(null);
    polylines.value.delete(dayIndex);
  }
  const dayOverlays = overlays.value.get(dayIndex);
  if (dayOverlays) {
    dayOverlays.forEach((overlay) => overlay.setMap(null));
    overlays.value.delete(dayIndex);
  }
};

const drawDayMarkers = (places, selectedDay) => {
  const bounds = new kakao.maps.LatLngBounds();
  const dayIndex = selectedDay.value;

  const dayMarkers = [];
  const dayOverlays = [];
  const linePath = [];
  places.forEach((place, placeIndex) => {
    const imageSrc = createMarkerImageWithNumber(
      placeIndex + 1,
      markerColor[dayIndex]
    );
    const imageSize = new kakao.maps.Size(40, 40);
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
    const position = new kakao.maps.LatLng(place.latitude, place.longitude);
    const marker = new kakao.maps.Marker({
      map,
      position,
      image: markerImage,
    });
    dayMarkers.push(marker);
    bounds.extend(position);
    linePath.push(position);

    addInfoWindow(marker, place.placeName);

    if (linePath.length > 1) {
      const polyline = new kakao.maps.Polyline({
        path: linePath.slice(linePath.length - 2, linePath.length),
      });
      const pathDistance = polyline.getLength();
      const distanceContent = createOverlayWithShortDistance(pathDistance);
      const customOverlay = new kakao.maps.CustomOverlay({
        position: position,
        content: distanceContent,
        xAnchor: -0.2,
        yAnchor: 1,
      });
      // 커스텀 오버레이를 지도에 표시합니다
      customOverlay.setMap(map);
      dayOverlays.push(customOverlay);
    }
  });

  const polyline = new kakao.maps.Polyline({
    map: map,
    path: linePath,
    strokeWeight: 4,
    strokeColor: markerColor[dayIndex],
    strokeOpacity: 0.6,
    strokeStyle: "dashed",
  });

  const distance = Math.round(polyline.getLength());
  const walkTime = getTime(distance, 67);
  const bicycleTime = getTime(distance, 227);
  const carTime = getTime(distance, 1000);
  const content = createOverlayWithDistance(
    distance,
    walkTime,
    bicycleTime,
    carTime
  );
  // 커스텀 오버레이를 생성합니다
  var customOverlay = new kakao.maps.CustomOverlay({
    position: new kakao.maps.LatLng(
      places[places.length - 1].latitude,
      places[places.length - 1].longitude
    ),
    content: content,
    xAnchor: 0.5,
    yAnchor: 1.5,
  });
  customOverlay.setMap(map);
  dayOverlays.push(customOverlay);

  markers.value.set(dayIndex, dayMarkers);
  polylines.value.set(dayIndex, polyline);
  overlays.value.set(dayIndex, dayOverlays);

  // 모든 마커가 보이도록 지도 영역 조정
  map.setBounds(bounds);
};

const drawMarkers = (tripDayList, selectedDay) => {
  if (!tripDayList.value) return;
  const newTripDayList = tripDayList.value;
  for (let i = 0; i < newTripDayList.length; i++) {
    clearMap(i);
  }
  console.log(selectedDay.value);
  if (selectedDay.value >= 0) {
    drawDayMarkers(newTripDayList[selectedDay.value], selectedDay);
    return;
  }
  // 지도 영역 범위
  const bounds = new kakao.maps.LatLngBounds();
  newTripDayList.forEach((places, dayIndex) => {
    const dayMarkers = [];
    const dayOverlays = [];
    const linePath = [];
    places.forEach((place, placeIndex) => {
      // dayIndex에 맞는 마커 이미지 생성
      const imageSrc = createMarkerImageWithNumber(
        placeIndex + 1,
        markerColor[dayIndex]
      );
      const imageSize = new kakao.maps.Size(40, 40);
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
      const position = new kakao.maps.LatLng(place.latitude, place.longitude);
      const marker = new kakao.maps.Marker({
        map,
        position,
        image: markerImage,
      });
      dayMarkers.push(marker);
      bounds.extend(position);
      linePath.push(position);

      addInfoWindow(marker, place.placeName);

      if (linePath.length > 1) {
        const polyline = new kakao.maps.Polyline({
          path: linePath.slice(linePath.length - 2, linePath.length),
        });
        const pathDistance = polyline.getLength();
        const distanceContent = createOverlayWithShortDistance(pathDistance);
        const customOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: distanceContent,
          xAnchor: -0.2,
          yAnchor: 1,
        });
        // 커스텀 오버레이를 지도에 표시합니다
        customOverlay.setMap(map);
        dayOverlays.push(customOverlay);
      }
    });

    const polyline = new kakao.maps.Polyline({
      map: map,
      path: linePath,
      strokeWeight: 4,
      strokeColor: markerColor[dayIndex],
      strokeOpacity: 0.6,
      strokeStyle: "dashed",
    });

    const distance = Math.round(polyline.getLength());
    const walkTime = getTime(distance, 67);
    const bicycleTime = getTime(distance, 227);
    const carTime = getTime(distance, 1000);
    const content = createOverlayWithDistance(
      distance,
      walkTime,
      bicycleTime,
      carTime
    );
    // 커스텀 오버레이를 생성합니다
    var customOverlay = new kakao.maps.CustomOverlay({
      position: new kakao.maps.LatLng(
        places[places.length - 1].latitude,
        places[places.length - 1].longitude
      ),
      content: content,
      xAnchor: 0.5,
      yAnchor: 1.5,
    });
    customOverlay.setMap(map);
    dayOverlays.push(customOverlay);

    markers.value.set(dayIndex, dayMarkers);
    polylines.value.set(dayIndex, polyline);
    overlays.value.set(dayIndex, dayOverlays);
  });

  // 모든 마커가 보이도록 지도 영역 조정
  map.setBounds(bounds);
};

function createInfoWindow(name) {
  return `<div style="padding:5px;">${name}</div>`;
}

function addInfoWindow(marker, name) {
  // 인포윈도우를 생성합니다
  var infowindow = new kakao.maps.InfoWindow({
    content: createInfoWindow(name),
  });

  // 마커에 마우스오버 이벤트를 등록합니다
  kakao.maps.event.addListener(marker, "mouseover", function () {
    infowindow.open(map, marker);
  });

  // 마커에 마우스아웃 이벤트를 등록합니다
  kakao.maps.event.addListener(marker, "mouseout", function () {
    infowindow.close();
  });
}
function createMarkerImageWithNumber(number, color) {
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40">
        <circle cx="20" cy="20" r="20" fill="${color}" stroke="white" stroke-width="2"/>
        <text x="50%" y="50%"
                font-family="Arial"
                font-size="18"
                font-weight="bold"
                fill="white"
                text-anchor="middle"
                dominant-baseline="central">
            ${number}
        </text>
    </svg>
    `;
  const base64 = btoa(unescape(encodeURIComponent(svg)));

  return `data:image/svg+xml;base64,${base64}`;
}

defineExpose({ drawMarkers });
</script>

<style scoped>
#map {
  width: 100%;
  height: 100vh;
}

.customoverlay {
  display: inline-block; /* 블록 크기 제대로 인식 */
  background-color: red;
  color: white;
  border-radius: 50%; /* 둥근 원 만들기 */
  width: 30px;
  height: 30px;
  line-height: 30px; /* 세로 가운데 정렬 */
  text-align: center; /* 가로 가운데 정렬 */
  font-weight: bold;
  font-size: 16px;
  user-select: none;
}
</style>
