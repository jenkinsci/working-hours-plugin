import React from "react";
import {WEEKDAYS} from "../constants";

import "../style/components.css";
import "rc-time-picker/assets/index.css";
import "rc-slider/assets/index.css";
import Slider, {createSliderWithTooltip, Range, Handle} from "rc-slider";
import {debounce} from "lodash";
import {getTimeRanges, setTimeRanges} from "../../../api";
import {LOADING_STATE, SavingState} from "../../common/savingState";
import ExcludedDateContainer from "../excludedDate";
import TimeRange from "./timeRange";
import only from "only";

const timeRegExp = /^(^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])|^(24:00))$/;

const timeMarks = {
  0: "00:00",
  360: "6:00",
  720: "12:00",
  1080: "18:00",
  1440: "24:00"
};

/**
 * Convert number of minutes to string like '00:00'.
 * @param v Number of minutes
 * @returns {string}
 */
function timeFormatter(v) {
  let totalMinutes = v;
  let hours = Math.floor(totalMinutes / 60);
  let minutes = Math.floor(totalMinutes % 60);
  return `${hours < 10 ? "0" + hours : hours}:${minutes < 10 ? "0" + minutes : minutes}`;
}


/**
 * Convert time string like 00:00 to a number of its minutes from 00:00.
 */
function timeStringToMinutes(timeString) {
  let hour = parseInt(timeString.split(":")[0]);
  let minute = parseInt(timeString.split(":")[1]);

  return hour * 60 + minute;
}

export default class TimeRangeContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      timeRanges: [],
      loadingState: LOADING_STATE.WAITING,
    };
  }

  debouncedClearLoading = debounce(()=>{
    this.setState({
      loadingState:LOADING_STATE.WAITING
    })
  },1000)

  /**
   * Handler for changing a excluded date
   * @param index The index of the child.
   * @param state The new state of the time range.
   * */
  handleTimeRangeChange = (index, state) => {
    let list = this.state.timeRanges;

    list[index] = state;

    /*Set both data for child and data for submit.*/
    this.setState({
      timeRanges: list
    });

    this.uploadTimes(list);
  };


  uploadTimes(param) {
    this.setState({
      loadingState: LOADING_STATE.LOADING
    })
    setTimeRanges({
      data: param.map(item => JSON.stringify(only(item, "dayOfWeek endTime startTime")))
    }).then(res => {
      console.log("time ranges updated");
      this.setState({
        loadingState: LOADING_STATE.SUCCESS
      })
      this.debouncedClearLoading();
    });
  }


  /**
   * Handler for deleting a time range
   * @param index
   */
  handleTimeRangeDelete = (index) => {
    console.log(index);
    let list = this.state.timeRanges;
    list.splice(index, 1);
    this.setState({
      timeRanges: list
    });
    this.uploadTimes(list);
  };


  /**
   * Handler for adding a new time range.
   * @param weekday The preset weekday passed to the new time range, default to sunday.
   */
  addTimeRange = (weekday = WEEKDAYS.Sunday) => {
    let list = this.state.timeRanges;
    let newItem = {};
    list.push({dayOfWeek: weekday, isNew: true});
    list.sort((a, b) => a.dayOfWeek - b.dayOfWeek);
    this.setState({
      timeRanges: list
    });
    this.uploadTimes(list);
  };

  weekdayList = () => {
    let res = [];
    Object.entries(WEEKDAYS).forEach(entry => {
      if (!this.state.timeRanges.some(item => item.dayOfWeek === entry[1])) {
        res.push(entry);
      }
    });
    return res;
  };

  /**
   * Fetching data once the app is mounted.
   */
  componentDidMount() {
    this.setState({
      loadingState: LOADING_STATE.LOADING
    })
    getTimeRanges().then(res => {
      this.setState({
        timeRanges: res.data.data.map(item => JSON.parse(item))
      });
      this.setState({
        loadingState: LOADING_STATE.SUCCESS
      })
      this.debouncedClearLoading();
    });
  }

  render() {
    return (
      <div>
        Time Range
        <SavingState loadingState={this.state.loadingState}/>
        <div className={"config-item"}>
          {this.state.timeRanges.length <= 0 ?
            <div>Time range is not
              configured.</div> : this.state.timeRanges.map((item, index) => (
              <TimeRange key={item.dayOfWeek}
                         index={index}
                         range={item}
                         onEdit={this.handleTimeRangeChange}
                         onDelete={this.handleTimeRangeDelete}
              />
            ))}
          {/*No more than 7 time ranges*/}
          {this.state.timeRanges.length < 7 && <div className={"form-row"} style={{marginBottom: 0}}>
            Add
            {this.weekdayList().map(item => (
              <button onClick={() => this.addTimeRange(item[1])} className='btn btn-gray'>{item[0]}</button>
            ))}
          </div>
          }
        </div>
      </div>
    );
  }
}
