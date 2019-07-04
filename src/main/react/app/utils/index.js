import { MONTHS, ORDERS, PERIODS, WEEKDAYS } from "../containers/App/constants";
import { formatDate } from "./date";

/*Get a brief description of this excluded date.*/
export function getBrief() {
  let words = [];
  words.push(this.state.name);
  if (this.state.repeat) {
    words.push("repeat");
    words.push("on");
    if (this.state.startDate.dynamic) {
      words.push("each");
      if (this.state.repeatPeriod > PERIODS.Week) {
        words.push(Object.keys(ORDERS)[this.state.startDate.dynamicWeek - 1]);
      }
      words.push(Object.keys(WEEKDAYS)[this.state.startDate.dynamicWeekday- 1]);
      if (this.state.repeatPeriod > PERIODS.Month) {
        words.push("of");
        words.push(Object.keys(MONTHS)[this.state.startDate.dynamicMonth - 1]);
      }
    } else {
      words.push(formatDate(this.state.startDate.date));
    }
    if (!this.state.noEnd) {
      words.push("till");
      if (this.state.endDate.dynamic) {
        words.push("the");
        words.push(Object.keys(ORDERS)[this.state.endDate.dynamicWeek - 1]);
        words.push(Object.keys(WEEKDAYS)[this.state.endDate.dynamicWeekday - 1]);
        words.push("of");
        words.push(Object.keys(MONTHS)[this.state.endDate.dynamicMonth - 1]);
      } else {
        words.push(formatDate(this.state.endDate.date));
      }
    } else {

    }

  } else {
    words.push("on");
    if (this.state.startDate.dynamic) {
      if (this.state.repeatPeriod > PERIODS.Week) {
        words.push(Object.keys(ORDERS)[this.state.startDate.dynamicDate.week - 1]);
      }
      words.push(Object.keys(WEEKDAYS)[this.state.startDate.dynamicDate.day - 1]);
      if (this.state.repeatPeriod > PERIODS.Month) {
        words.push("of");
        words.push(Object.keys(MONTHS)[this.state.startDate.dynamicDate.month - 1]);
      }
    } else {
      words.push(formatDate(this.state.startDate.date));
    }
  }

  return words.join(" ");
}
