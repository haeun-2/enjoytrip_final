<template>
  <div class="time-line-container" style="padding: 20px">
    <template v-if="isAll">
      <div v-for="(dayItems, dayIndex) in items" :key="dayIndex">
        <div>
          <TripPlanTimeLine
            v-for="(item, itemIndex) in dayItems"
            :key="item.itemNumber"
            v-bind="item"
            :dayOrder="dayIndex"
            :itemOrder="itemIndex + 1"
          />
        </div>
      </div>
    </template>

    <div v-else>
      <TripPlanTimeLine
        v-for="(item, itemIndex) in items"
        :key="item.itemNumber"
        v-bind="item"
        :dayOrder="selectedDay"
        :itemOrder="itemIndex + 1"
      />
    </div>
  </div>
</template>

<script setup>
import TripPlanTimeLine from "./TripPlanTimeLine.vue";
const props = defineProps({
  items: Array,
  isAll: Boolean,
  selectedDay: Number,
});
</script>

<style scoped>
.time-line-container {
  display: flex;
  flex-direction: row;
  gap: 50px;
  overflow: auto;
  -ms-overflow-style: none;
  scrollbar-width: thin;
  scrollbar-color: #e5e4e4 transparent;
  height: calc(100vh - 220px);
}
.time-line-container::-webkit-scrollbar {
  width: 6px;
}
.time-line-container::-webkit-scrollbar-thumb {
  background-color: transparent;
}
.time-line-container::-webkit-scrollbar-track {
  background-color: transparent;
}
</style>
