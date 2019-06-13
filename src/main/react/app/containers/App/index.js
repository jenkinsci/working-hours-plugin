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
import Index from "./timeRange";
import { getExcludedDates, getTimeRanges, setExcludedDates, setTimeRanges } from "../../api";
import { cloneDeep } from "lodash";

let openIndex = [];

export default class App extends React.Component {
  constructor() {
    super();
    this.state = {
      /*Data used to pass to child*/
      excludedDates: [],

      /*A copy of the data, and will be used to post data*/
      excludedDatesSrcData: [],

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

    let srcDataList = this.state.excludedDatesSrcData;

    /*Use lodash to deepcopy an object, so we could delete some helper fields.*/
    let newItem = cloneDeep(state);

    /*Delete some field that is not relevant about data serializing*/
    delete newItem.selectedDateType;
    delete newItem.selectedPreset;

    srcDataList[index] = newItem;

    /*Set both data for child and data for submit.*/
    this.setState({
      excludedDates: list,
      excludedDatesSrcData: srcDataList
    });

    this.uploadDates(srcDataList);

  };

  /**
   * Handler for changing a excluded date
   * @param index The index of the child.
   * @param state The new state of the time range.
   * */
  handleTimeRangeChange = (index, state) => {
    let list = this.state.timeRanges;

    list[index] = state;

    let srcDataList = this.state.timeRangesSrcData;

    /*Use lodash to deepcopy an object, so we could delete some helper fields.*/
    let newItem = cloneDeep(state);

    /*Delete some field that is not relevant about data serializing*/

    srcDataList[index] = newItem;

    /*Set both data for child and data for submit.*/
    this.setState({
      timeRanges: list,
      excludedDatesSrcData: srcDataList
    });

    this.uploadTimes(srcDataList);
  };


  uploadDates(param) {
    setExcludedDates({
      data: param
        .map(item => JSON.stringify(item))
    }).then(res => {
      console.log("excluded dates updated");
    });
  }

  uploadTimes(param) {
    setTimeRanges({
      data: param.map(item => JSON.stringify(item))
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
  };

  /**
   * Handler for deleting a time range
   * @param index
   */
  handleTimeRangeDelete = (index) => {
    let list = this.state.timeRanges;
    list.splice(index, index + 1);
    this.setState({
      timeRanges: list
    });
  };

  componentDidUpdate(prevProps, prevState, snapshot) {
  }


  /**
   * Handler for adding a new excluded date
   */
  addExcludedDate = () => {

    let list = this.state.excludedDates;
    let newItem = {};
    list.push({});
    list.forEach((item, index) => {
      item.index = index;
    });
    this.setState({
      excludedDates: list
    });
  };

  /**
   * Handler for adding a new time range
   */
  addTimeRange = () => {
    let list = this.state.timeRanges;
    let newItem = {};
    list.push({});
    list.forEach((item, index) => {
      item.index = index;
    });
    this.setState({
      timeRanges: list
    });
  };

  /**
   * Fetching data once the app is mounted.
   */
  componentDidMount() {
    getExcludedDates().then(res => {
      this.setState({
        excludedDates: res.data.data.map(item => JSON.parse(item)),
        excludedDatesSrcData: res.data.data.map(item => JSON.parse(item))
      });
    });

    getTimeRanges().then(res => {
      this.setState({
        timeRanges: res.data.data.map(item => JSON.parse(item)),
        timeRangesSrcData: res.data.data.map(item => JSON.parse(item))
      });
    });
  }

  render() {
    return (
      <div>
        Time Range
        <div className={"config-item"}>
          {this.state.timeRanges.length <= 0 ?
            <div>Time range is not
              configured.</div> : this.state.timeRanges.map((item, index) => (
              <Index key={index}
                     index={index}
                     range={item}
                     onEdit={this.handleTimeRangeChange}
                     onDelete={this.handleTimeRangeDelete}
              />
            ))}

          {/*No more than 7 time ranges*/}
          {this.state.timeRanges.length < 7 &&
          <button type={"button"} className='btn btn-gray' onClick={this.addTimeRange}>Add</button>}
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
