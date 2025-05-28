<template>
  <div class="modal-overlay" v-if="visible">
    <div class="modal">
      <h2 class="modal-title">여행 설명</h2>
      <textarea
        v-model="input"
        placeholder="여행에 대한 간단한 설명을 입력해주세요"
        rows="5"
      />
      <div class="modal-buttons">
        <button class="confirm" @click="confirm">확인</button>
        <button class="cancel" @click="cancel">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineExpose, defineEmits } from "vue";

const visible = ref(false);
const input = ref("");

const emit = defineEmits(["submit"]);

const show = () => {
  visible.value = true;
  input.value = "";
};

const confirm = () => {
  if (input.value.trim() === "") {
    alert("설명을 입력해주세요.");
    return;
  }
  emit("submit", input.value);
  visible.value = false;
};

const cancel = () => {
  visible.value = false;
};

defineExpose({ show });
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}
.modal {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 450px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}
.modal-title {
  margin-bottom: 12px;
  font-size: 1.25rem;
  font-weight: bold;
}
textarea {
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  resize: none;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 16px;
}
.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
button.confirm {
  background-color: var(--primary-color);
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
button.confirm:hover {
  background-color: var(--primary-hover);
}
button.cancel {
  background-color: #eee;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
button.cancel:hover {
  background-color: var(--light-gray);
}
</style>
