import axios from "axios";
import qs from "qs";
import {UrlConfig} from "./utils/urlConfig";

let axiosInstance = {};

export function stringifyQuery(args) {
  return qs.stringify(args, {
    arrayFormat: "repeat",
    allowDots: true
  });
}

/**
 * Get relative url from the parent page, because the plugin is running in iframe.
 */
function getApiBaseUrl() {
  return window.parent.location.href
}

const AXIOS_DEFAULT_CONFIG = {
  baseURL: process.env.BASE_URL || getApiBaseUrl(),
  timeout: 20000,
  maxContentLength: 2000,
  headers: {},
  withCredentials: true, // 允许携带cookie
  paramsSerializer: stringifyQuery
};

axiosInstance = axios.create(AXIOS_DEFAULT_CONFIG);

const crumbHeaderName = UrlConfig.getCrumbHeaderName();
console.log(crumbHeaderName);
console.log(UrlConfig.getCrumbToken());
if (crumbHeaderName) {
  axiosInstance.defaults.headers.common[crumbHeaderName] = UrlConfig.getCrumbToken();
}

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

export const setTimezone = (params) => {
  return axiosInstance.post("/set-timezone", params);
};

export const apiGetTimezone = ()=>{
  return axiosInstance.post("/get-timezone");
}

export function fetchRegionalHolidays(regionCode) {
  return axiosInstance.post(`/regions/${regionCode}`)
}
