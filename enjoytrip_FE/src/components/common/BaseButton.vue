<template>
  <div>
    <button class="primary-button">
      {{ text }}
    </button>
  </div>
</template>

<script setup>
defineProps({
  text: {
    type: String,
    default: "BUTTON",
  },
});
</script>

<style scoped>
.button-content {
  text-align: center;
}
.primary-button {
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius);
  padding: 10px 16px 10px 16px;
  font-size: 14px;
  font-weight: 500;
  transition: var(--transition);
}

.primary-button:hover {
  background-color: var(--primary-hover);
}
</style>
