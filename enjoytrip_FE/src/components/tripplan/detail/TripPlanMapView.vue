<template>
  <div id="map" ref="mapContainer"></div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { tspDPBitmask } from "@/components/tripplan/detail/mapUtil";

const props = defineProps({
  places: Array,
  dayPlaces: Array,
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

const findShortestPath = (places) => {
  const n = places.length;
  const dist = Array.from({ length: n }, () => Array(n).fill(0));
  for (let i = 0; i < n; i++) {
    for (let j = 0; j < n; j++) {
      if (i !== j) dist[i][j] = getDistance(places[i], places[j]);
    }
  }
  const sortedPlaces = tspDPBitmask(places, dist);
  return sortedPlaces;
};

function getDistance(p1, p2) {
  const line = new kakao.maps.Polyline({
    path: [
      new kakao.maps.LatLng(p1.latitude, p1.longitude),
      new kakao.maps.LatLng(p2.latitude, p2.longitude),
    ],
  });
  return line.getLength();
}

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

const drawMarkers = (places, dayIndex) => {
  clearMap(dayIndex);
  const linePath = [];
  const bounds = new kakao.maps.LatLngBounds();
  const imageSize = new kakao.maps.Size(40, 40);
  const newMarkers = [];
  const newOverlays = [];

  places.forEach((place, index) => {
    const position = new kakao.maps.LatLng(place.latitude, place.longitude);
    const imageSrc = createMarkerImageWithNumber(
      index + 1,
      markerColor[dayIndex]
    );
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
    linePath.push(position);

    const marker = new kakao.maps.Marker({
      position,
      map,
      title: `${index + 1}. ${place.name}`,
      image: markerImage,
    });

    newMarkers.push(marker);
    bounds.extend(position);

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
      newOverlays.push(customOverlay);
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
  const content = createOverlayWithDistance(distance, walkTime, bicycleTime);
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
  newOverlays.push(customOverlay);

  polylines.value.set(dayIndex, polyline);
  markers.value.set(dayIndex, newMarkers);
  overlays.value.set(dayIndex, newOverlays);

  if (places.length > 0) {
    map.setBounds(bounds);
  }
};

function getTime(distance, speed) {
  const minutes = Math.floor(distance / speed);
  const hours = Math.floor(minutes / 60);
  const remainMin = minutes % 60;
  return (hours > 0 ? `${hours}시간 ` : "") + `${remainMin}분`;
}

function convertDistanceScale(distance) {
  return distance < 1000
    ? distance.toFixed(0) + "m"
    : (distance / 1000).toFixed(2) + "km";
}

function createOverlayWithShortDistance(distance) {
  const distanceWithScale = convertDistanceScale(distance);
  const content = `
    <div style="
      background: white;
      border-radius: 8px;
      padding: 8px 12px;
      font-size: 14px;
      font-weight: bold;
      color: #333;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
      white-space: nowrap;
    ">
      거리 <span style="color:#007bff;">${distanceWithScale}</span>
  `;
  return content;
}

function createOverlayWithDistance(distance, walkTime, bicycleTime) {
  const distanceWithScale = convertDistanceScale(distance);
  const content = `
            <div style="
    background: white;
    padding: 12px 16px;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.15);
    font-size: 14px;
    line-height: 1.6;
    color: #333;
    font-family: 'Arial', sans-serif;
    white-space: nowrap;
  ">
    <div>총 거리 <span style="color: #f44336; font-weight: bold;">${distanceWithScale}</span></div>
    <div>도보    <span style="color: #000; font-weight: bold;">${walkTime}</span></div>
    <div>자전거  <span style="color: #000; font-weight: bold;">${bicycleTime}</span></div>
    <div>자동차  <span style="color: #000; font-weight: bold;">${bicycleTime}</span></div>
  </div>
        `;
  return content;
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
defineExpose({ drawMarkers, findShortestPath });
</script>

<style scoped>
#map {
  width: 100%;
  height: 100vh;
}
</style>
