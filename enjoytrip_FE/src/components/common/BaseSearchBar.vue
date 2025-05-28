<template>
  <div class="search-bar" ref="autocompleteRef">
    <input
      type="text"
      :placeholder="text"
      v-model="searchQuery"
      @keyup.enter="search"
      @input="onInput"
    />
    <ul v-if="suggestions.length > 0" class="suggestions">
      <li
        v-for="suggestion in suggestions"
        :key="suggestion.id"
        @click="selectSuggestion(suggestion.id)"
      >
        <font-awesome-icon
          icon="location-dot"
          style="color: var(--text-light); margin-right: 5px"
        />
        {{ suggestion.word }}
      </li>
    </ul>
    <button class="search-button" @click="search">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        viewBox="0 0 24 24"
        width="18"
        height="18"
      >
        <path
          fill="currentColor"
          d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"
        />
      </svg>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import { getAttractionByKeyword } from "@/apis/attraction/attraction";

// 상태 관리
const suggestions = ref([]);
const searchQuery = ref("");
const autocompleteRef = ref(null);

defineProps({
  text: {
    type: String,
    default: "검색어를 입력하세요",
  },
});

const emit = defineEmits(["search", "searchById"]);
const search = () => {
  emit("search", searchQuery.value);
};

// 검색어 자동 완성
const onInput = async () => {
  if (searchQuery.value.trim() === "") {
    suggestions.value = [];
    return;
  }
  try {
    const response = await getAttractionByKeyword(searchQuery.value);
    suggestions.value = response;
  } catch (err) {
    console.error("자동완성 요청 실패", err);
    suggestions.value = [];
  }
};
const selectSuggestion = (placeId) => {
  emit("searchById", placeId);
};
// 외부 클릭 감지
const handleClickOutside = (event) => {
  if (autocompleteRef.value && !autocompleteRef.value.contains(event.target)) {
    suggestions.value = [];
  }
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style scoped>
.search-bar {
  flex: 1;
  position: relative;
  display: flex;
  padding: 0px 10px;
  flex-wrap: wrap; /* 줄바꿈 허용 */
  width: 100%;
  box-sizing: border-box;
}

.search-bar input {
  width: 100%;
  padding: 10px 40px 10px 16px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  font-size: 14px;
  transition: var(--transition);
}

.search-bar input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px var(--primary-light);
}

.search-button {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: var(--text-light);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 4px;
}
.suggestions {
  position: absolute;
  top: 100%;
  width: calc(100% - 22px);
  border: 1px solid #ccc;
  background: white;
  max-height: 300px;
  overflow-y: auto;
  list-style: none;
  margin: 0;
  padding: 0;
  z-index: 20;
}
.suggestions li {
  padding: 12px;
  cursor: pointer;
}
.suggestions li:hover {
  background: var(--background-color);
}
</style>
