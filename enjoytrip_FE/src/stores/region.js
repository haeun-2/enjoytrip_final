import { ref, computed } from "vue";
import { defineStore } from "pinia";
import * as regionApi from "@/apis/region/region.js";

export const useRegionStore = defineStore("region", () => {
  const _sidoList = ref([]);
  const getSidoList = async () => {
    if (_sidoList.value.length > 0) return _sidoList.value;
    try {
      _sidoList.value = await regionApi.getSidoList();
      return _sidoList.value;
    } catch (e) {
      alert("시도 목록 조회 중 오류가 발생했습니다.");
    }
  };
  const getGugunList = async (sidoId) => {
    try {
      const gugunList = await regionApi.getGugunList(sidoId);
      return gugunList;
    } catch (e) {
      alert("구군 목록 조회 중 오류가 발생했습니다.");
    }
  };
  return { getSidoList, getGugunList };
});
