<template>
  <div class="base-avatar">
    <img :src="profileImage || defaultImage" alt="프로필 이미지" />
  </div>
</template>

<script setup>
import defaultImage from "@/assets/profile-default.jpg";

const props = defineProps(["profileImage"]);
</script>

<style scoped>
.base-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f1f5f9;
  overflow: hidden; /* ✅ 핵심! */
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.base-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* ✅ 이미지 비율 유지하며 채우기 */
}
</style>
