import React from "react";
import { WEEKDAYS } from "../constants";
import moment from "moment";
import DatePicker from "react-datepicker/es";

export default class TimeRange extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      day: WEEKDAYS.Monday,
      timeStart: moment({hour: 8}).toDate(),
      timeEnd: moment({hour: 18}).toDate()
    };
  }

  updateStartTime = (e) => {
    this.setState({
      timeStart: e
    });
  };

  updateEndTime = (e) => {
    this.setState({
      timeEnd: e
    });
  };

  delete = () => {
    if (window.confirm("Are you sure to delete this range?")) {
      this.props.onDelete(this.props.index);
    }
  };

  updateDay = (event) => {
    this.setState({
      day: event.target.value
    });
  };

  render() {
    return (
      <div className={"time-range"} style={{ display: "flex" }}>
        <select value={this.state.day} onChange={this.updateDay} className={"input input-select"}>
          {Object.keys(WEEKDAYS).map(key => <option value={WEEKDAYS[key]} key={key}>{key}</option>)}
        </select>
        <div>
          <DatePicker className={"input input-time"}
                      selected={this.state.timeStart}
                      placeholder="select"
                      dateFormat="h:mm aa"
                      minTime={moment({hour:0,minute:0}).toDate()}
                      maxTime={this.state.timeEnd}
                      showTimeSelect
                      showTimeSelectOnly
                      onChange={this.updateStartTime}/>
        </div>
        <div>
          -
          <DatePicker className={"input input-time"}
                      selected={this.state.timeEnd}
                      placeholder="select"
                      dateFormat="h:mm aa"
                      minTime={this.state.timeStart}
                      maxTime={moment({hour:24,minute:0}).toDate()}
                      showTimeSelect
                      showTimeSelectOnly
                      onChange={this.updateEndTime}/>
        </div>
        <button type="button" className={"btn btn-delete"} onClick={this.delete}>X</button>
      </div>
    );
  }
}