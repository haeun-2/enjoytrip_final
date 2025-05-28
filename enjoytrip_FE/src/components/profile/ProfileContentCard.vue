<template>
  <div class="content-card">
    <img v-show="item.image" :src="item.image" class="card-image" />
    <img v-show="!item.image" src="@/assets/noimage.jpeg" class="card-image" />
    <div class="card-content">
      <h3 class="card-title">{{ item.title }}</h3>
      <p class="card-location">{{ item.address }}</p>
      <span class="rating-text">{{ item.categoryName }}</span>
    </div>
  </div>
</template>

<script setup>
defineProps({ item: Object });
</script>

<style scoped>
.content-card {
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  overflow: hidden;
  transition: box-shadow 0.2s;
}

.content-card:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.card-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.card-content {
  padding: 1rem;
}

.card-title {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #111827;
}

.card-location {
  font-size: 0.875rem;
  color: #6b7280;
  margin-bottom: 0.75rem;
}

.card-rating {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.stars {
  display: flex;
  gap: 0.125rem;
}

.rating-text {
  font-size: 0.875rem;
  color: #6b7280;
}

.card-category {
  margin-bottom: 0.75rem;
}

.category-tag {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  background-color: rgba(45, 212, 191, 0.1);
  color: #2dd4bf;
  border-radius: 9999px;
  font-size: 0.75rem;
}

.card-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.75rem;
}

.card-action-button {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.5rem;
  border: none;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.card-action-button.edit-action {
  background-color: rgba(45, 212, 191, 0.1);
  color: #2dd4bf;
}

.card-action-button.edit-action:hover {
  background-color: rgba(45, 212, 191, 0.2);
}

.card-action-button.delete-action {
  background-color: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.card-action-button.delete-action:hover {
  background-color: rgba(239, 68, 68, 0.2);
}
</style>
