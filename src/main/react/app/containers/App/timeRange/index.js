import React from "react";
import { WEEKDAYS } from "../constants";
import moment from "moment";
import DatePicker from "react-datepicker/es";

export default class Index extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      dayOfWeek: WEEKDAYS.Sunday,
      startTime: moment({ hour: 8 }).toDate(),
      endTime: moment({ hour: 18 }).toDate()
    };
  }

  updateStartTime = (e) => {
    this.setState({
      startTime: e
    });
  };

  updateEndTime = (e) => {
    this.setState({
      endTime: e
    });
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


  render() {
    return (
      <div className={"time-range"} style={{ display: "flex" }}>
        <select value={this.state.dayOfWeek} onChange={this.updateDay} className={"input input-select"}>
          {Object.keys(WEEKDAYS).map(key => <option value={WEEKDAYS[key]} key={key}>{key}</option>)}
        </select>
        <div>
          <DatePicker className={"input input-time"}
                      selected={this.state.startTime}
                      placeholder="select"
                      dateFormat="h:mm aa"
                      minTime={moment({ hour: 0, minute: 0 }).toDate()}
                      maxTime={this.state.endTime}
                      showTimeSelect
                      showTimeSelectOnly
                      onChange={this.updateStartTime}/>
        </div>
        <div>
          -
          <DatePicker className={"input input-time"}
                      selected={this.state.endTime}
                      placeholder="select"
                      dateFormat="h:mm aa"
                      timeIntervals={1}
                      minTime={this.state.startTime}
                      maxTime={moment({ hour: 24, minute: 0 }).toDate()}
                      showTimeSelect
                      showTimeSelectOnly
                      onChange={this.updateEndTime}/>
        </div>
        <button type="button" className={"btn btn-delete"} onClick={this.delete}>X</button>
        <button type="button" className={"btn"} onClick={this.save}>Save</button>
      </div>
    );
  }
}