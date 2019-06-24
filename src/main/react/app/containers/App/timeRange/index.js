import React from "react";
import { WEEKDAYS } from "../constants";
import moment from "moment";

import TimePicker from "rc-time-picker";
import "../style/components.css"
import "rc-time-picker/assets/index.css";
import "rc-slider/assets/index.css";
import Slider, { createSliderWithTooltip, Range,Handle } from "rc-slider";
import Tooltip from "rc-tooltip"

const timeRegExp = /^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])$/;

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

const SliderWithTooltip = createSliderWithTooltip(Slider);

export default class TimeRange extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      dayOfWeek: WEEKDAYS.Sunday,
      startTime: 8 * 60,
      endTime: 18 * 60,

      /*Strings used for store temp times, which would be checked on blur.*/
      tempStartTime: "08:00",
      tempEndTime: "18:00",

      /*Symbol that indicates whether time is valid.*/
      validStartTime: true,
      validEndTime: true

    };
  }

  updateStartTime = (e) => {
    this.setState({
      tempStartTime: e.target.value.trim()
    });
  };

  updateEndTime = (e) => {
    this.setState({
      tempEndTime: e.target.value.trim()
    });
  };

  /**
   * Process time after the input blur.
   */
  handleInputBlur = () => {
    /*Test input format*/
    if (timeRegExp.test(this.state.tempStartTime)) {
      this.setState({
        startTime: timeStringToMinutes(this.state.tempStartTime),
        tempStartTime: timeFormatter(timeStringToMinutes(this.state.tempStartTime)),
        validStartTime: true
      });
    } else {
      this.setState({
        validStartTime: false
      });
    }

    if (timeRegExp.test(this.state.tempEndTime)) {
      this.setState({
        endTime: timeStringToMinutes(this.state.tempEndTime),
        tempEndTime: timeFormatter(timeStringToMinutes(this.state.tempEndTime)),
        validEndTime: true
      });
    } else {
      this.setState({
        validEndTime: false
      });
    }
  };


  delete = () => {
    if (window.confirm("Are you sure to delete this range?")) {
      this.props.onDelete(this.props.index);
    }
  };

  updateDay = (event) => {
    this.setState({
      dayOfWeek: event.target.value
    });
  };

  save = () => {
    this.props.onEdit(this.props.index, this.state);
  };

  validate = () => {
    return this.state.validEndTime && this.state.validStartTime;
  };

  handleRangeChange = (range) => {
    this.setState({

      tempStartTime: timeFormatter(range[0]),
      tempEndTime: timeFormatter(range[1]),

      startTime: range[0],
      endTime: range[1]
    });
  };

  render() {
    return (
      <div className={["time-range", this.validate() ? "" : "time-range-invalid"].join(" ")}
           style={{ display: "flex" }}>
        <select value={this.state.dayOfWeek} onChange={this.updateDay} className={"input input-select"}>
          {Object.keys(WEEKDAYS).map(key => <option value={WEEKDAYS[key]} key={key}>{key}</option>)}
        </select>

        <input value={this.state.tempStartTime} className={"input input-text"} style={{ width: 70, marginLeft: 10 }}
               onChange={this.updateStartTime}
               onBlur={this.handleInputBlur}
        />
        <Range
          style={{ margin: 10 }}
          max={1439}
          min={0}
          step={30}
          trackStyle={[{ backgroundColor: "#b1b1b1" }, { backgroundColor: "#b1b1b1" }]}
          handleStyle={[
            {
              boxShadow: "0 0 5px #868686",
              backgroundColor: "white",
              border: "4px solid rgb(200, 200, 200)"
            },
            {
              boxShadow: "0 0 5px #868686",
              backgroundColor: "white",
              border: "4px solid rgb(200, 200, 200)"
            }]}
          value={[this.state.startTime, this.state.endTime]}
          onChange={this.handleRangeChange}

        >
        </Range>
        <input value={this.state.tempEndTime} className={"input input-text"} style={{ width: 70 }}
               onChange={this.updateEndTime}
               onBlur={this.handleInputBlur}
        />
        <button type="button" className={"btn btn-delete"} onClick={this.delete}>X</button>
        <button type="button" className={"btn"} onClick={this.save}>Save</button>
      </div>
    );
  }
}