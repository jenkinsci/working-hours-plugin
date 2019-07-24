import axios from "axios";
import qs from "qs";

let axiosInstance = {};

export function stringifyQuery(args) {
  return qs.stringify(args, {
    arrayFormat: "repeat",
    allowDots: true
  });
}


console.log("BASE_URL:"+process.env.BASE_URL);
console.log("NODE_ENV:"+process.env.NODE_ENV);
const AXIOS_DEFAULT_CONFIG = {
  baseURL: process.env.BASE_URL||"working-hours/",
  timeout: 20000,
  maxContentLength: 2000,
  headers: {},
  withCredentials: true, // 允许携带cookie
  paramsSerializer: stringifyQuery
};

axiosInstance = axios.create(AXIOS_DEFAULT_CONFIG);

export default axiosInstance;

export const getExcludedDates = () => {
  return axiosInstance.post("/list-excluded-dates");
};

export const setExcludedDates = (params) => {
  return axiosInstance.post("/set-excluded-dates", params);
};

export const getTimeRanges = () => {
  return axiosInstance.post("/list-time-ranges");
};

export const setTimeRanges = (params) => {
  return axiosInstance.post("/set-time-ranges", params);
};

export function fetchRegionalHolidays(regionCode) {
  return axiosInstance.post(`/regions/${regionCode}`)
}
