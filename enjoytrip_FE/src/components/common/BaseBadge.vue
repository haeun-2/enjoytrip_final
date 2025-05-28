<template>
  <span class="badge">{{ text }}</span>
</template>

<script setup>
defineProps({
  text: {
    type: String,
    default: "CATEGORY",
  },
});
</script>

<style scoped>
.badge {
  font-size: 14px;
  font-weight: 500;
  color: var(--primary-color);
  background-color: var(--primary-lighter);
  padding: 4px 8px;
  border-radius: 4px;
}
</style>
