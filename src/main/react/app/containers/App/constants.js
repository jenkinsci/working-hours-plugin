export const WEEKDAYS = {
  Monday: 1,
  Tuesday: 2,
  Wednesday: 3,
  Thursday: 4,
  Friday: 5,
  Saturday: 6,
  Sunday: 7
};

export const PERIODS = {
  Week: 1,
  Month: 2,
  Year: 3,
};

export const ORDERS = { first: 1, second: 2, third: 3, fourth: 4 };

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


export const DATE_PRESETS = [
  {
    name: "Thanksgiving Day", repeat: true, startDate: {
      dynamic: true, dynamicDate: { month: 11, week: 4, day: 4 }
    }, repeatPeriod: PERIODS.Year, repeatInterval: 1, repeatCound: -1
  },
  {
    name: "Mother's Day", repeat: true, startDate: {
      dynamic: true, dynamicDate: { month: 5, week: 2, day: 7 }
    }, repeatPeriod: PERIODS.Year, repeatInterval: 1, repeatCound: -1
  }
];