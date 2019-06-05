import React from "react";
import { WEEKDAYS } from "./constants";
import moment from "moment";
import DatePicker from "react-datepicker/es";

export default class TimeRange extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      day: WEEKDAYS.Monday,
      timeStart: null,
      timeEnd: null
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

  delete = ()=>{
    if(window.confirm("Are you sure to delete this range?")){
      this.props.onDelete(this.props.index)
    }
  }

  render() {
    return (
      <div className={"time-range"} style={{ display: "flex" }}>
        <select value={this.state.day} className={'input input-select'}>
          {Object.keys(WEEKDAYS).map(item=><option label={item} value={WEEKDAYS[item]}/>)}
        </select>
        <div>
          <DatePicker className={"input input-time"}
                      selected={this.state.timeStart}
                      placeholder="select"
                      dateFormat="h:mm aa"
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
                      showTimeSelect
                      showTimeSelectOnly
                      onChange={this.updateEndTime}/>
        </div>
        <button type="button" className={"btn btn-delete"} onClick={this.delete}>X</button>
      </div>
    );
  }
}