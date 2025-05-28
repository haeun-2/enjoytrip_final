<template>
  <div>
    <div class="travel-plan-card">
      <div class="card-image">
        <img
          src="https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=800&q=80"
          alt="여행 사진"
        />
        <div class="card-like" @click.prevent.stop="onClickLike">
          <font-awesome-icon
            icon="heart"
            :style="{
              fontSize: '20px',
              color: data.liked ? 'red' : 'white',
            }"
          />
        </div>
      </div>
      <div class="card-content">
        <div class="card-meta">
          <span class="card-location">
            <span v-for="(tag, index) in parsedTags" :key="index" class="tag">
              {{ tag }}
            </span>
          </span>
          <span class="card-duration"
            >{{ data.startDate }} - {{ data.endDate }}</span
          >
        </div>
        <h3 class="card-title">{{ data.title }}</h3>
        <p class="card-description">
          {{ data.description }}
        </p>
        <div class="card-footer">
          <div class="card-author">
            <img :src="data.memberProfileUrl" alt="작성자" />
            <span>{{ data.memberName }}</span>
          </div>
          <div class="card-stats">
            <div class="stat">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="16"
                height="16"
              >
                <path
                  fill="currentColor"
                  d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
                />
              </svg>
              <span>{{ data.likeCount }}</span>
            </div>
            <div class="stat">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              >
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                <circle cx="12" cy="12" r="3" />
              </svg>
              <span>{{ data.viewCount }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { likePlan, unLikePlan } from "@/apis/tripplan/plan";
import { computed } from "vue";

const props = defineProps({
  item: Object,
});
const data = props.item;

const onClickLike = async () => {
  if (!data.liked) {
    console.log(data.liked, data);
    await likePlan(data.id);
    data.liked = true;
    data.likeCount += 1;
  } else {
    await unLikePlan(data.id);
    data.likeCount -= 1;
    data.liked = false;
  }
};
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
const parsedTags = computed(() =>
  data.categories
    .split(",")
    .map((tag) => categoryMap.get(tag.trim()))
    .filter((tag) => tag)
);
</script>

<style scoped>
.travel-plan-card {
  background-color: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  overflow: hidden;
  transition: var(--transition);
}

.travel-plan-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-large);
}

.card-image {
  position: relative;
  height: 165px;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-like {
  position: absolute;
  top: 12px;
  right: 12px;
  color: white;
  cursor: pointer;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.3));
}

.card-content {
  padding: 12px;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.card-location {
  font-size: 12px;
  font-weight: 500;
  color: var(--primary-color);
}

.tag {
  background-color: white;
  color: var(--blue-3);
  padding: 2px 10px;
  border-radius: 20px;
  border: 1px solid var(--blue-3);
  font-size: 11px;
  margin-right: 4px;
}
.card-duration {
  font-size: 12px;
  color: var(--text-light);
}

.card-title {
  margin-left: 2px;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-color);
}

.card-description {
  margin-left: 2px;
  font-size: 14px;
  color: var(--text-light);
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 60px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
  padding-top: 12px;
  border-top: 1px solid var(--border-color);
}

.card-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-author img {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.card-author span {
  font-size: 13px;
  font-weight: 500;
}

.card-stats {
  display: flex;
  gap: 16px;
  color: var(--text-light);
}
.card-stats .stat {
  display: flex;
  gap: 4px;
  justify-content: center;
  align-items: center;
}
</style>
