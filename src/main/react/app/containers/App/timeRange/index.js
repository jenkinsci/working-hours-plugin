import React from "react";
import { WEEKDAYS } from "../constants";
import moment from "moment";
import TimePicker from "rc-time-picker";
import "rc-time-picker/assets/index.css";

export default class Index extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      dayOfWeek: WEEKDAYS.Sunday,
      startTime: moment({ hour: 8 }),
      endTime: moment({ hour: 18 })
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

  validate(){
    return this.state.endTime.isSameOrAfter(this.state.startTime)
  }


  render() {
    return (
      <div className={["time-range", this.validate() ? "" : "time-range-invalid"].join(" ")}
           style={{ display: "flex" }}>
        <select value={this.state.dayOfWeek} onChange={this.updateDay} className={"input input-select"}>
          {Object.keys(WEEKDAYS).map(key => <option value={WEEKDAYS[key]} key={key}>{key}</option>)}
        </select>
        <div>
          <TimePicker className={"input-time"}
                      defaultValue={this.state.startTime}
                      showSecond={false}
                      onChange={this.updateStartTime}/>
        </div>
        <div>
          -
          <TimePicker className={"input-time"}
                      defaultValue={this.state.endTime}
                      showSecond={false}
                      onChange={this.updateEndTime}/>
        </div>
        <button type="button" className={"btn btn-delete"} onClick={this.delete}>X</button>
        <button type="button" className={"btn"} onClick={this.save}>Save</button>
      </div>
    );
  }
}