<template>
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
            <font-awesome-icon icon="heart" style="color: var(--heart-color); font-size: 15px" />
            {{ place.likeCount }}
          </span>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import noImage from "@/assets/noImage.jpeg";
const { place } = defineProps({
  place: Object,
});
</script>

<style scoped>
.place-content {
  display: flex;
  flex: 1;
  cursor: pointer;
  padding: 14px 14px;
  transition: transform 0.3s ease, background-color 0.3s ease;
}

.place-content:hover {
  transform: translateY(-3px);
  background-color: var(--background-color);
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
  overflow: hidden; /* 넘치는 내용 숨김 */
  white-space: nowrap; /* 텍스트를 한 줄로 강제 */
  text-overflow: ellipsis;
}

.place-description {
  margin: 0 0 8px;
  font-size: 13px;
  color: #666;
  overflow: hidden; /* 넘치는 내용 숨김 */
  white-space: nowrap; /* 텍스트를 한 줄로 강제 */
  text-overflow: ellipsis;
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
</style>
