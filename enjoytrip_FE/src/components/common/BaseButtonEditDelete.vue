<template>
  <div class="modal-actions">
    <button @click="editClick" class="edit-button">
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
        class="edit-icon"
      >
        <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
        <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
      </svg>
      {{ editText }}
    </button>
    <button @click="deleteClick" class="delete-button">
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
        class="trash-icon"
      >
        <polyline points="3 6 5 6 21 6"></polyline>
        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
        <line x1="10" y1="11" x2="10" y2="17"></line>
        <line x1="14" y1="11" x2="14" y2="17"></line>
      </svg>
      {{ deleteText }}
    </button>
  </div>
</template>

<script setup>
defineProps({
  editText: {
    type: String,
    default: "수정",
  },
  deleteText: {
    type: String,
    default: "삭제",
  },
});

const emit = defineEmits(["editClick", "deleteClick"]);
const editClick = () => {
  emit("editClick");
};
const deleteClick = () => {
  emit("deleteClick");
};
</script>

<style scoped>
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.edit-button,
.delete-button {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.5rem 0.75rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.2s, color 0.2s;
  border: none;
}

.delete-button,
.edit-button {
  background-color: var(--background-color);
  color: var(--text-light);
}

.edit-button:hover {
  background-color: var(--primary-lighter);
}

.delete-button:hover {
  background-color: #fecaca;
}

.edit-icon,
.trash-icon {
  color: inherit;
}
</style>
