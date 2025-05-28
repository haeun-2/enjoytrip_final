import { ref } from "vue";
import { defineStore } from "pinia";

export const useCategoryStore = defineStore("category", () => {
  const _categoryList = ref([
    { id: 12, name: "관광지" },
    { id: 14, name: "문화시설" },
    { id: 15, name: "축제공연행사" },
    { id: 25, name: "여행코스" },
    { id: 28, name: "레포츠" },
    { id: 32, name: "숙박" },
    { id: 38, name: "쇼핑" },
  ]);
  const getCategoryList = () => {
    return _categoryList.value;
  };
  return { getCategoryList };
});
