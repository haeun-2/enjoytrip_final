<template>
  <div>
    <div class="featured-plan">
      <div class="featured-image">
        <img
          src="https://images.unsplash.com/photo-1507525428034-b723cf961d3e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80"
          alt="제주도 해변"
        />
        <div class="featured-badge">BEST</div>
      </div>
      <div class="featured-details">
        <div class="featured-meta">
          <BaseBadge
            v-for="(category, index) in item.categories?.split(',') || []"
            :key="index"
            :text="categoryMap.get(category.trim())"
          />
          <span class="featured-duration">{{ item.startDate }} - {{ item.endDate }}</span>
        </div>
        <h3 class="featured-title">
          {{ item.title }}
        </h3>
        <p class="featured-description">
          {{ item.description }}
        </p>
        <div class="featured-footer">
          <div class="featured-author">
            <img src="https://randomuser.me/api/portraits/women/32.jpg" alt="작성자" />
            <span>김여행</span>
          </div>
          <div class="featured-stats">
            <div class="stat">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="16" height="16">
                <path
                  fill="currentColor"
                  d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                />
              </svg>
              <span>{{ item.likeCount }}</span>
            </div>
            <div class="stat">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="16" height="16">
                <path
                  fill="currentColor"
                  d="M21.99 4c0-1.1-.89-2-1.99-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18zM18 14H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"
                />
              </svg>
              <span>{{ item.viewCount }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import BaseBadge from "@/components/common/BaseBadge.vue";
const { item } = defineProps({
  item: Object,
});
const categoryMap = new Map([
  ["RELAXING", "휴양"],
  ["SIGHTSEEING", "관광"],
  ["NATURE", "자연"],
  ["CULTURAL", "문화"],
  ["FOODIE", "미식"],
  ["ADVENTURE", "액티비티"],
  ["SHOPPING", "소펭"],
  ["PHOTO", "사진"],
]);
</script>

<style scoped>
.featured-plan {
  display: flex;
  background-color: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-medium);
  overflow: hidden;
}

.featured-image {
  width: 40%;
  position: relative;
}

.featured-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.featured-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background-color: var(--secondary-color);
  color: white;
  padding: 4px 8px;
  font-size: 12px;
  font-weight: 600;
  border-radius: 4px;
}

.featured-details {
  width: 60%;
  padding: 24px;
  display: flex;
  flex-direction: column;
}

.featured-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 12px;
}

.featured-duration {
  font-size: 14px;
  color: var(--text-light);
}

.featured-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--text-color);
}

.featured-description {
  font-size: 14px;
  color: var(--text-light);
  margin-bottom: 16px;
  flex: 1;
}

.featured-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.featured-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.featured-author img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.featured-author span {
  font-size: 14px;
  font-weight: 500;
}

.featured-stats {
  display: flex;
  gap: 16px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  color: var(--text-light);
  font-size: 14px;
}
</style>
