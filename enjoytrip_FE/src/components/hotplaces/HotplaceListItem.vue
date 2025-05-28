<template>
  <div class="hotplace-card">
    <div class="card-image-container">
      <img :src="hotplace.image" alt="Hotplace" class="card-image" />
      <div class="card-category">
        <BaseBadge :text="hotplace.category.categoryName" />
      </div>
    </div>
    <div class="card-content">
      <div class="featured-stats">
        <h3 class="card-title">{{ hotplace.name }}</h3>
        <div class="stat">
          <font-awesome-icon
            :icon="[hotplace.isLiked ? 'fa' : 'far', 'heart']"
            :style="{
              fontSize: '15px',
              color: 'red',
            }"
          />

          <span>{{ hotplace.likeCount }}</span>
        </div>
      </div>
      <p class="card-address">{{ hotplace.address }}</p>
      <div class="card-meta">
        <div class="card-region">{{ hotplace.sido.sidoName }} {{ hotplace.gugun.guugnName }}</div>
        <div class="card-date">{{ formatDate(hotplace.visitedAt) }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { formatDate } from "@/utils/date.js";
import BaseBadge from "@/components/common/BaseBadge.vue";

const props = defineProps({
  hotplace: Object,
});
</script>

<style scoped>
.hotplace-card {
  border-radius: 0.5rem;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  background-color: white;
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.hotplace-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.card-image-container {
  position: relative;
  height: 200px;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-category {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  padding: 0.25rem 0.5rem;
  color: white;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.card-content {
  padding: 1rem;
}

.card-title {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.25rem;
  color: var(--text-color);
}

.card-address {
  font-size: 0.875rem;
  color: var(--text-light);
  margin-bottom: 0.5rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.75rem;
  color: var(--text-lighter);
}

.featured-stats {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.stat {
  display: flex;
  align-items: end;
  gap: 4px;
  color: var(--text-light);
  font-size: 14px;
}
.heart-icon {
  color: var(--heart-color);
}
</style>
