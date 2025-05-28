<template>
  <div>
    <div id="map" ref="mapContainer"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
const emit = defineEmits(["clickMarker"]);
const mapContainer = ref(null);
let map = null;
let markers = [];
let overlays = ref([]);
let pointMarker = ref(null);

const imageSrc =
  "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
const imageSize = new kakao.maps.Size(20, 30);
const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

const clear = () => {
  markers.forEach((marker) => marker.setMap(null));
  markers = [];
};

const addPin = (place) => {
  if (pointMarker.value) {
    pointMarker.value.setMap(null);
  }
  const position = new kakao.maps.LatLng(place.latitude, place.longitude);
  const imageSrc =
    "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
  const imageSize = new kakao.maps.Size(20, 30);
  const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
  const marker = new kakao.maps.Marker({
    map,
    position,
  });

  pointMarker.value = marker;
  const movePosition = new kakao.maps.LatLng(
    place.latitude,
    place.longitude - 0.001
  );
  map.panTo(movePosition);
  map.setLevel(1);
};

const addMarkers = (places) => {
  places.forEach((place) => {
    const position = new kakao.maps.LatLng(place.latitude, place.longitude);
    const marker = new kakao.maps.Marker({
      map,
      position,
      image: markerImage,
      clickable: true,
    });
    markers.push(marker);
    addInfoWindowOnMouse(marker, place);
    handleClick(marker, place);
  });
  fitMapToMarkers(markers);
};

const drawMarkers = (places) => {
  clear();
  addMarkers(places);
};

const addInfoWindowOnMouse = (marker, place) => {
  var iwContent = `
        <div class="customoverlay" style="border-radius: 16px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); padding: 12px; max-width: 250px;
            position: relative; ">
            <h4 style="margin: 0 0 6px 0; font-size: 14px; font-weight: 600; color: #1a1a1a;">
                ${place.name}
            </h4>
            <p style="margin: 0 0 4px 0; font-size: 12px; color: #666;">
                ${place.category}
            </p>
            <p style="margin: 0 0 4px 0; font-size: 11px;color: #888; line-height: 1.3;">
                ${place.address}
            </p>
        </div>
  `;

  var infowindow = new kakao.maps.InfoWindow({
    content: iwContent,
  });
  kakao.maps.event.addListener(marker, "mouseover", function () {
    infowindow.open(map, marker);
  });
  kakao.maps.event.addListener(marker, "mouseout", function () {
    infowindow.close();
  });
};

const handleClick = (marker, place) => {
  kakao.maps.event.addListener(marker, "click", function () {
    emit("clickMarker", place);
  });
};
// 모든 마커가 보이도록 지도 영역 조정
function fitMapToMarkers(markers) {
  const bounds = new kakao.maps.LatLngBounds();
  markers.forEach((marker) => {
    bounds.extend(marker.getPosition());
  });
  map.setBounds(bounds);
}

onMounted(() => {
  const options = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 3,
  };
  map = new kakao.maps.Map(mapContainer.value, options);
});
defineExpose({ drawMarkers, addMarkers, addPin });
</script>

<style scoped>
#map {
  width: 100%;
  height: 100vh;
}
</style>
