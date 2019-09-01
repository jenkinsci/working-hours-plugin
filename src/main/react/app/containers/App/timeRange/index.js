import React from "react";
import {WEEKDAYS} from "../constants";

import "../style/components.css";
import {debounce} from "lodash";
import {getTimeRanges, setTimeRanges} from "../../../api";
import {LOADING_STATE, LoadingState} from "../../common/savingState";
import TimeRange from "./timeRange";
import only from "only";


/**
 * Container for time ranges. In charge of data fetch/submit, loading state control.
 */
export default class TimeRangeContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      timeRanges: [],
      loadingState: LOADING_STATE.WAITING,
    };
  }

  /*A debounced function used to clear loading state.
  * */
  debouncedClearLoading = debounce(() => {
    this.setState({
      loadingState: LOADING_STATE.WAITING
    })
  }, 1000)

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


  /**
   * Upload time ranges to server.
   * @param param
   */
  uploadTimes(param) {
    this.setState({
      loadingState: LOADING_STATE.LOADING
    })
    setTimeRanges({
      data: param.map(item => only(item, "dayOfWeek endTime startTime"))
    }).then(res => {
      this.setState({
        loadingState: LOADING_STATE.SUCCESS
      })
      this.debouncedClearLoading();
    }).catch((err => {
      this.setState({
        loadingState: LOADING_STATE.FAIL
      })
    }));
  }


  /**
   * Handler for deleting a time range
   * @param index
   */
  handleTimeRangeDelete = (index) => {
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
    // this.uploadTimes(list);
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
        timeRanges: res.data.data
      });
      this.setState({
        loadingState: LOADING_STATE.SUCCESS
      })
      this.debouncedClearLoading();
    }).catch(err => {
      this.setState({
        loadingState: LOADING_STATE.FAIL
      })
    });
  }

  render() {
    return (
      <div>
        <div className={"config-header"}>
          <div className={'config-title'}>Time Range</div>
          <div className={'config-loading-status'}>
            <LoadingState loadingState={this.state.loadingState}/>
          </div>
          <div
            className={'config-count'}>Total:{this.state.timeRanges.length}</div>
        </div>
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
          {this.state.timeRanges.length < 7 &&
          <div className={"form-row"} style={{marginBottom: 0}}>
            <span className={"time-range-add-hint"}>Add</span>
            {this.weekdayList().map(item => (
              <button onClick={() => this.addTimeRange(item[1])}
                      className='btn btn-gray'>{item[0]}</button>
            ))}
          </div>
          }
        </div>
      </div>
    );
  }
}
