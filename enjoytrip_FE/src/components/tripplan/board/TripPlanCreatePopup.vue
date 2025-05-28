<template>
  <div>
    <!-- 팝업 열기 버튼 -->
    <button @click="openPopup" class="open-button">여행 계획 추가</button>

    <!-- 팝업 배경 오버레이 -->
    <div v-if="isOpen" class="popup-overlay" @click="closePopup"></div>

    <!-- 팝업 컨테이너 -->
    <div v-if="isOpen" class="popup-container">
      <div class="popup-content">
        <h2 class="popup-title">여행 기간이 어떻게 되시나요?</h2>
        <!-- <p class="popup-subtitle">* 여행 일자는 최대 10월까지 선정 가능합니다.</p> -->
        <p class="popup-info">현지 여행 기간(여행지 도착 날짜, 여행지 출발 날짜)으로 입력해 주세요.</p>

        <div class="calendar-container">
          <!-- 이전 달 버튼 -->
          <button @click="prevMonth" class="nav-button prev-button"><font-awesome-icon icon="chevron-left" /></button>

          <div class="calendars">
            <!-- 첫 번째 달력 -->
            <div class="calendar">
              <h3 class="month-title">{{ formatMonth(currentMonth) }}</h3>
              <div class="weekdays">
                <div v-for="day in weekdays" :key="day" class="weekday">{{ day }}</div>
              </div>
              <div class="days">
                <div
                  v-for="day in getDaysInMonth(currentMonth)"
                  :key="`${currentMonth.getFullYear()}-${currentMonth.getMonth()}-${day.date}`"
                  :class="[
                    'day',
                    { 'other-month': !day.currentMonth },
                    {
                      selected: isDateSelected(
                        day.date,
                        day.currentMonth,
                        currentMonth.getMonth(),
                        currentMonth.getFullYear()
                      ),
                    },
                    {
                      'in-range': isInRange(
                        day.date,
                        day.currentMonth,
                        currentMonth.getMonth(),
                        currentMonth.getFullYear()
                      ),
                    },
                    { today: isToday(day.date, currentMonth.getMonth(), currentMonth.getFullYear()) },
                  ]"
                  @click="day.currentMonth && selectDate(day.date, currentMonth.getMonth(), currentMonth.getFullYear())"
                >
                  {{ day.date }}
                </div>
              </div>
            </div>

            <!-- 두 번째 달력 -->
            <div class="calendar">
              <h3 class="month-title">{{ formatMonth(nextMonth) }}</h3>
              <div class="weekdays">
                <div v-for="day in weekdays" :key="day" class="weekday">{{ day }}</div>
              </div>
              <div class="days">
                <div
                  v-for="day in getDaysInMonth(nextMonth)"
                  :key="`${nextMonth.getFullYear()}-${nextMonth.getMonth()}-${day.date}`"
                  :class="[
                    'day',
                    { 'other-month': !day.currentMonth },
                    {
                      selected: isDateSelected(
                        day.date,
                        day.currentMonth,
                        nextMonth.getMonth(),
                        nextMonth.getFullYear()
                      ),
                    },
                    {
                      'in-range': isInRange(day.date, day.currentMonth, nextMonth.getMonth(), nextMonth.getFullYear()),
                    },
                    { today: isToday(day.date, nextMonth.getMonth(), nextMonth.getFullYear()) },
                  ]"
                  @click="day.currentMonth && selectDate(day.date, nextMonth.getMonth(), nextMonth.getFullYear())"
                >
                  {{ day.date }}
                </div>
              </div>
            </div>
          </div>

          <!-- 다음 달 버튼 -->
          <button @click="nextMonthBtn" class="nav-button next-button">
            <font-awesome-icon icon="chevron-right" />
          </button>
        </div>

        <div class="popup-footer">
          <button @click="confirmSelection" class="confirm-button">선택</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DateRangePicker",
  data() {
    return {
      isOpen: false,
      weekdays: ["일", "월", "화", "수", "목", "금", "토"],
      currentMonth: new Date(2025, 4), // 2025년 5월
      startDate: null,
      endDate: null,
      selecting: false,
    };
  },
  computed: {
    nextMonth() {
      const next = new Date(this.currentMonth);
      next.setMonth(next.getMonth() + 1);
      return next;
    },
  },
  methods: {
    openPopup() {
      this.isOpen = true;
    },
    closePopup() {
      this.isOpen = false;
    },
    prevMonth() {
      const newMonth = new Date(this.currentMonth);
      newMonth.setMonth(newMonth.getMonth() - 1);
      this.currentMonth = newMonth;
    },
    nextMonthBtn() {
      const newMonth = new Date(this.currentMonth);
      newMonth.setMonth(newMonth.getMonth() + 1);
      this.currentMonth = newMonth;
    },
    formatMonth(date) {
      return `${date.getFullYear()}년 ${date.getMonth() + 1}월`;
    },
    getDaysInMonth(date) {
      const year = date.getFullYear();
      const month = date.getMonth();

      // 이전 달의 마지막 날짜와 요일 구하기
      const firstDay = new Date(year, month, 1).getDay();
      const prevMonthLastDate = new Date(year, month, 0).getDate();

      // 현재 달의 마지막 날짜 구하기
      const lastDate = new Date(year, month + 1, 0).getDate();

      const days = [];

      // 이전 달의 날짜 추가
      for (let i = 0; i < firstDay; i++) {
        days.push({
          date: prevMonthLastDate - firstDay + i + 1,
          currentMonth: false,
        });
      }

      // 현재 달의 날짜 추가
      for (let i = 1; i <= lastDate; i++) {
        days.push({
          date: i,
          currentMonth: true,
        });
      }

      // 다음 달의 날짜 추가 (6주 채우기)
      const remainingDays = 42 - days.length;
      for (let i = 1; i <= remainingDays; i++) {
        days.push({
          date: i,
          currentMonth: false,
        });
      }

      return days;
    },
    selectDate(day, month, year) {
      const selectedDate = new Date(year, month, day);

      if (!this.startDate || (this.startDate && this.endDate) || selectedDate < this.startDate) {
        // 시작일 선택 또는 새로운 범위 시작
        this.startDate = selectedDate;
        this.endDate = null;
        this.selecting = true;
      } else if (this.selecting && !this.endDate) {
        // 종료일 선택
        this.endDate = selectedDate;
        this.selecting = false;
      }
    },
    isSameDate(a, b) {
      return a.getFullYear() === b.getFullYear() && a.getMonth() === b.getMonth() && a.getDate() === b.getDate();
    },

    isDateSelected(day, current, month, year) {
      const date = new Date(current ? year : year + 1, current ? month : month + 1, day);
      return (
        (this.startDate && this.isSameDate(this.startDate, date)) ||
        (this.endDate && this.isSameDate(this.endDate, date))
      );
    },

    isInRange(day, current, month, year) {
      if (!this.startDate || !this.endDate) return false;
      const date = new Date(current ? year : year + 1, current ? month : month + 1, day).getTime();
      return date > this.startDate.getTime() && date < this.endDate.getTime();
    },

    isToday(day, month, year) {
      const today = new Date();
      const date = new Date(year, month, day);
      return this.isSameDate(today, date);
    },
    confirmSelection() {
      if (this.startDate && this.endDate) {
        // 여기서 선택된 날짜 범위를 부모 컴포넌트로 전달하거나 처리할 수 있습니다
        this.$emit("date-range-selected", {
          startDate: new Date(this.startDate.getFullYear(), this.startDate.getMonth(), this.startDate.getDate() + 1),
          endDate: new Date(this.endDate.getFullYear(), this.endDate.getMonth(), this.endDate.getDate() + 1),
        });
        this.closePopup();
      } else if (this.startDate) {
        // 시작일만 선택된 경우 동일한 날짜를 종료일로 설정
        this.endDate = new Date(this.startDate + 1);
        this.$emit("date-range-selected", {
          startDate: this.startDate,
          endDate: this.endDate,
        });
        this.closePopup();
      }
    },
  },
};
</script>

<style scoped>
.open-button {
  padding: 10px 20px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius);
  cursor: pointer;
  font-size: 15px;
}

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;
}

.popup-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 101;
  width: 90%;
  max-width: 1000px;
}

.popup-content {
  padding: 24px;
}

.popup-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 8px;
}

.popup-subtitle {
  font-size: 14px;
  color: #666;
  text-align: center;
  margin-bottom: 8px;
}

.popup-info {
  font-size: 14px;
  color: #666;
  text-align: center;
  margin-bottom: 24px;
}

.calendar-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.calendars {
  display: flex;
  gap: 80px;
}

@media (max-width: 900px) {
  .calendars {
    flex-direction: column;
  }
}

.calendar {
  flex: 1;
}

.month-title {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 16px;
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  font-weight: bold;
  margin-bottom: 8px;
}

.weekday {
  padding: 8px;
}

.days {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.day {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  width: 40px;
  cursor: pointer;
  border-radius: 50%;
  position: relative;
}

.day:hover {
  background-color: #f0f0f0;
}

.other-month {
  color: #ccc;
}

.selected {
  background-color: #000;
  color: white;
}

.in-range {
  background-color: #e6f7ff;
}

.today {
  font-weight: bold;
  border: 1px solid #4a90e2;
}

.nav-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  padding: 0 16px;
}

.popup-footer {
  display: flex;
  justify-content: flex-end;
}

.confirm-button {
  padding: 10px 24px;
  background-color: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.confirm-button:hover {
  background-color: var(--primary-color);
  color: white;
}
</style>
