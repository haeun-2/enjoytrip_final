<template>
  <div class="places-list">
    <div class="place-content">
      <div class="place-image">
        <img :src="place.image || noImage" :alt="place.name" />
      </div>
      <div class="place-details">
        <h3 class="place-name">{{ place.name }}</h3>
        <p class="place-description">{{ place.address }}</p>
        <div class="place-meta">
          <p class="place-category">{{ place.category }}</p>
          <p class="place-stats">
            <span class="likes">
              <font-awesome-icon
                icon="heart"
                style="color: var(--heart-color); font-size: 15px"
              />
              {{ place.likeCount }}
            </span>
          </p>
        </div>
      </div>
    </div>
    <button
      class="select-button"
      @click.stop="togglePlaceSelection(place.id)"
      :class="{ selected: selectedPlaces.includes(place.id) }"
    >
      <font-awesome-icon
        v-if="selectedPlaces.includes(place.id)"
        icon="check"
      />
      <font-awesome-icon v-else icon="plus" />
    </button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import noImage from "@/assets/noImage.jpeg";
const { place, selectedPlaces } = defineProps({
  place: Object,
  selectedPlaces: Array,
});
const emit = defineEmits(["togglePlaceSelection"]);
// 부모에게 선택/해제 이벤트 전달
function togglePlaceSelection(placeId) {
  emit("togglePlaceSelection", placeId);
}
</script>

<style scoped>
.place-content {
  display: flex;
  flex: 1;
}

.place-image {
  position: relative;
  width: 80px;
  height: 80px;
  margin-right: 12px;
  border-radius: 4px;
  overflow: hidden;
}

.place-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.popular-tag {
  position: absolute;
  top: 0;
  left: 0;
  padding: 2px 6px;
  background-color: #ff5722;
  color: white;
  font-size: 11px;
  font-weight: bold;
}

.place-details {
  flex: 1;
}

.place-name {
  margin: 0 0 4px;
  font-size: 16px;
  font-weight: 600;
}

.place-description {
  margin: 0 0 8px;
  font-size: 13px;
  color: #666;
}
.place-meta {
  display: flex;
  flex-direction: row;
  gap: 15px;
}

.place-category {
  margin: 0 0 8px;
  font-size: 13px;
  color: var(--blue-4);
}
.place-stats {
  margin: 0 0 5px;
  display: flex;
  align-items: center;
}

.likes {
  display: flex;
  align-items: center;
  font-size: 13px;
  gap: 5px;
}

.select-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 50%;
  cursor: pointer;
  color: #666;
}

.select-button.selected {
  background-color: #2196f3;
  border-color: #2196f3;
  color: white;
}
</style>
