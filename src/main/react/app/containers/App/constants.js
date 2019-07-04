import {fetchTimezones} from "../../api";

export const WEEKDAYS = {
  Sunday: 0,
  Monday: 1,
  Tuesday: 2,
  Wednesday: 3,
  Thursday: 4,
  Friday: 5,
  Saturday: 6
};

export const PERIODS = {
  Week: 1,
  Month: 2,
  Year: 3
};

export const ORDERS = {first: 1, second: 2, third: 3, fourth: 4};

export const MONTHS = {
  January: 1,
  February: 2,
  March: 3,
  April: 4,
  May: 5,
  June: 6,
  July: 7,
  August: 8,
  September: 9,
  October: 10,
  November: 11,
  December: 12
};


export const DATE_TYPE = {
  TYPE_GREGORIAN: "Gregorian",
  TYPE_CHINESE_LUNAR: "Chinese Lunar"
};


export const getDatePresets = (type) => {
  switch (type) {
    case DATE_TYPE.TYPE_GREGORIAN:
      return GREGORIAN_DATE_PRESETS;
    case DATE_TYPE.TYPE_CHINESE_LUNAR:
      return CHINESE_LUNAR_DATE_PRESETS;
    default:
      return GREGORIAN_DATE_PRESETS;
  }
};

export const PLACEHOLDER_PRESET_NOT_SELECTED = "Empty"

export const GREGORIAN_DATE_PRESETS = [
  {
    type: DATE_TYPE.TYPE_GREGORIAN, name: "Thanksgiving Day", repeat: true, startDate: {
      dynamic: true, dynamicMonth: 11, dynamicWeek: 4, dynamicWeekday: 4
    }, repeatPeriod: PERIODS.Year
  },
  {
    type: DATE_TYPE.TYPE_GREGORIAN,
    name: "Mother's Day", repeat: true, startDate: {
      dynamic: true, dynamicMonth: 5, dynamicWeek: 2, dynamicWeekday: 7
    }, repeatPeriod: PERIODS.Year
  }
];

/*Temporarily use a constant here, maybe later we could use some standalone Api or some files.*/
export const CHINESE_LUNAR_DATE_PRESETS = [
  {
    type: DATE_TYPE.TYPE_CHINESE_LUNAR,
    name: "Chinese New Year", repeat: true,
    params: "1,1"
  },
  {
    type: DATE_TYPE.TYPE_CHINESE_LUNAR,
    name: "Dragon Boat Festival", repeat: true,
    params: "5,5"
  }

];
