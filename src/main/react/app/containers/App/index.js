/**
 *
 * App
 *
 * This component is the skeleton around the actual pages, and should only
 * contain code that should be seen on all pages. (e.g. navigation bar)
 */

import React from "react";

import "react-datepicker/dist/react-datepicker.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./style/index.css";
import ExcludedDate from "./excludedDate";
import TimeRange from "./timeRange";
import { getExcludedDates, getTimeRanges, setExcludedDates, setTimeRanges } from "../../api";
import { cloneDeep } from "lodash";
import { WEEKDAYS } from "./constants";
import only from "only";

let openIndex = [];

export default class App extends React.Component {
  constructor() {
    super();
    this.state = {
      /*Data used to pass to child*/
      excludedDates: [],
      timeRanges: []
    };
  }

  /**
   * Handler for changing a excluded date
   * @param index The index of the child
   * @param show Status that indicates whether the child want to be unfold or fold
   * @param state The new state.
   */
  handleExcludedDateChange = (index, show, state) => {
    let list = this.state.excludedDates;
    if (show) {
      openIndex = index;
    } else {
      openIndex = -1;
    }

    list[index] = state;

    /*Set both data for child and data for submit.*/
    this.setState({
      excludedDates: list
    });

    this.uploadDates(list);
  };

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


  uploadDates(param) {
    setExcludedDates({
      data: param
        .map(item => JSON.stringify(only(item, [
          "name",
          "type",
          "utcOffset",
          "startDate",
          "endDate",
          "noEnd",
          "repeat",
          "repeatCount",
          "repeatInterval",
          "repeatPeriod"].join(" "))))
    }).then(res => {
      console.log("excluded dates updated");
    });
  }

  uploadTimes(param) {
    setTimeRanges({
      data: param.map(item => JSON.stringify(only(item, "dayOfWeek endTime startTime")))
    }).then(res => {
      console.log("time ranges updated");
    });
  }


  /**
   * Handler for deleting a excluded date
   * @param index
   */
  handleExcludedDateDelete = (index) => {
    if (openIndex === index) {
      openIndex = -1;
    }
    let list = this.state.excludedDates;
    list.splice(index, index + 1);
    this.setState({
      excludedDates: list
    });

    this.uploadDates(list);
  };

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
   * Handler for adding a new excluded date
   */
  addExcludedDate = () => {
    let list = this.state.excludedDates;
    list.push({});
    this.setState({
      excludedDates: list
    });
    this.uploadDates(list);
  };

  /**
   * Handler for adding a new time range.
   * @param weekday The preset weekday passed to the new time range, default to sunday.
   */
  addTimeRange = (weekday = WEEKDAYS.Sunday) => {
    let list = this.state.timeRanges;
    let newItem = {};
    list.push({ dayOfWeek: weekday });
    list.sort((a,b)=>a.dayOfWeek - b.dayOfWeek)
    this.setState({
      timeRanges: list
    });
    this.uploadTimes(list);
  };

  /**
   * Fetching data once the app is mounted.
   */
  componentDidMount() {
    getExcludedDates().then(res => {
      this.setState({
        excludedDates: res.data.data.map(item => JSON.parse(item))
      });
    });

    getTimeRanges().then(res => {
      this.setState({
        timeRanges: res.data.data.map(item => JSON.parse(item))
      });
    });
  }

  weekdayList = () => {
    let res = [];
    Object.entries(WEEKDAYS).forEach(entry => {
      if (!this.state.timeRanges.some(item => item.dayOfWeek === entry[1])) {
        res.push(entry);
      }
    });
    return res;
  };

  render() {
    return (
      <div>
        Time Range
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
          <div className={"form-row"}>
            {this.weekdayList().map(item => (
              <button onClick={() => this.addTimeRange(item[1])} className='btn btn-gray'>{item[0]}</button>
            ))}
          </div>
        </div>
        Excluded Dates
        <div>
          {this.state.excludedDates.length <= 0 ?
            <div className={"config-item"}>There's no excluded
              dates</div> : this.state.excludedDates.map((item, index) => (
              <ExcludedDate key={index}
                            index={index}
                            opened={index === openIndex}
                            date={item}
                            onEdit={this.handleExcludedDateChange}
                            onDelete={this.handleExcludedDateDelete}
              />
            ))}
          <button type={"button"} className='btn btn-gray' onClick={this.addExcludedDate}>Add</button>
        </div>
      </div>
    );
  }
}
