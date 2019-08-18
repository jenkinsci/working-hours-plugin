import React from "react";
import {WEEKDAYS} from "../constants";

import "../style/components.css";
import "rc-slider/assets/index.css";
import {Range} from "rc-slider";
import {debounce} from "lodash";

const timeRegExp = /^(^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])|^(24:00))$/;

const timeMarks = {
  0: "00:00",
  360: "6:00",
  720: "12:00",
  1080: "18:00",
  1440: "24:00"
};

export const INITIAL_TIME_RANGE_STATE = {
  dayOfWeek: WEEKDAYS.Sunday,
  startTime: 8 * 60,
  endTime: 18 * 60,
}

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

export default class TimeRange extends React.Component {
  static contextType = PluginContext;

  constructor(props) {
    super(props);
    this.state = {
      ...INITIAL_TIME_RANGE_STATE,

      /*Strings used for store temp times, which would be checked on blur.*/
      tempStartTime: "08:00",
      tempEndTime: "18:00",

      /*Symbol that indicates whether time is valid.*/
      validStartTime: true,
      validEndTime: true,

      /*Symbol for new*/
      isNew: false,
    };
  }

  /**
   * A debounced function to avoid the save function to be called directly and frequently,
   * also provide ability to save automatically.
   * @type {debounce} The debounce function provided by @lodash.
   */
  debouncedSave = debounce(() => {
    if (this.validate()) {
      this.props.onEdit(this.props.index, this.state);
    } else {
      return
    }
    this.setState({
      isNew: false,
    })
  }, 500)

  /**
   * Update the temp start time string.
   * @param e
   */
  updateStartTime = (e) => {
    this.setState({
      tempStartTime: e.target.value.trim()
    });
  };

  /**
   * Update the temp end time string.
   * @param e
   */
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
        validStartTime: true,
        isNew: true,
      });
      this.debouncedSave()
    } else {
      this.setState({
        validStartTime: false
      });
    }

    if (timeRegExp.test(this.state.tempEndTime)) {
      this.setState({
        endTime: timeStringToMinutes(this.state.tempEndTime),
        tempEndTime: timeFormatter(timeStringToMinutes(this.state.tempEndTime)),
        validEndTime: true,
        isNew: true,
      });
      this.debouncedSave();
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


  /**
   * Return whether the temp time string is valid.
   * @returns {boolean}
   */
  validate = () => {
    return this.state.validEndTime && this.state.validStartTime;
  };

  handleRangeChange = (range) => {
    this.setState({

      tempStartTime: timeFormatter(range[0]),
      tempEndTime: timeFormatter(range[1]),

      startTime: range[0],
      endTime: range[1],
    }, () => {
      if (this.validate()) {
        this.setState({
          isNew: true, // Set the symbol to @true so the indicator could be available.
        })
        this.debouncedSave()
      }
    });
  };

  /**
   * Set the initial data passed in (currently with just weekday)
   */
  componentDidMount() {
    this.setState(this.props.range,()=>{
      this.setState({
        tempStartTime:timeFormatter(this.state.startTime),
        tempEndTime:timeFormatter(this.state.endTime),
      })
      if(this.state.isNew){
        this.debouncedSave()
      }
    });
  }

  render() {
    return (
      <div className={[this.validate() ? "" : "time-range-invalid",].join(" ")}
      >

        <div className={["time-range"]}>
          <div className={"label-weekday"}>
            {Object.keys(WEEKDAYS)[this.state.dayOfWeek]}
            {this.state.isNew && <div className={"is-new"}/>}
          </div>

          <input value={this.state.tempStartTime} className={["input", "input-text", "input-time",].join(' ')}
                 style={{width: 70, marginLeft: 10}}
                 onChange={this.updateStartTime}
                 onBlur={this.handleInputBlur}
          />
          <Range
            style={{margin: "5px 25px"}}
            max={1440}
            min={0}
            step={30}
            dotStyle={{borderColor: "#e9e9e9"}}
            activeDotStyle={{borderColor: "rgb(177,177,177)"}}
            marks={timeMarks}
            pushable={true}
            trackStyle={[{backgroundColor: "#b1b1b1"}, {backgroundColor: "#b1b1b1"}]}
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
          <input value={this.state.tempEndTime} className={"input input-text input-time"} style={{width: 70}}
                 onChange={this.updateEndTime}
                 onBlur={this.handleInputBlur}
          />
          <button type="button" className={"btn btn-delete"} onClick={this.delete}>X</button>
        </div>
        {!this.validate() ? <div style={{color: "red", fontWeight: "100"}}>Invalid time string</div> : ""}

      </div>
    );
  }
}
