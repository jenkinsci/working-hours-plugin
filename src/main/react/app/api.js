import axios from "axios";
import qs from "qs";

let axiosInstance = {};

export function stringifyQuery(args) {
  return qs.stringify(args, {
    arrayFormat: "repeat",
    allowDots: true
  });
}

const AXIOS_DEFAULT_CONFIG = {
  timeout: 20000,
  maxContentLength: 2000,
  headers: {},
  withCredentials: true, // 允许携带cookie
  paramsSerializer: stringifyQuery
};

axiosInstance = axios.create(AXIOS_DEFAULT_CONFIG);

export default axiosInstance;

export const getExcludedDates = () => {
  return axiosInstance.post("/working-hours/list-excluded-dates");
};

export const setExcludedDates = (params) => {
  return axiosInstance.post("/working-hours/set-excluded-dates", params);
};

export const getTimeRanges = () => {
  return axiosInstance.post("/working-hours/list-time-ranges");
};

export const setTimeRanges = (params) => {
  return axiosInstance.post("/working-hours/set-time-ranges", params);
};

export const fetchTimezones = () => {
  return axiosInstance.get("http://worldtimeapi.org/api/timezone")
};