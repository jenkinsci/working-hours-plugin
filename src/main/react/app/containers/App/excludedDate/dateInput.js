import DatePicker from "react-datepicker/es";
import React from "react";
import { MONTHS, ORDERS, PERIODS, WEEKDAYS } from "../constants";
import { nextOccurrenceByYear, nextOccurrenceByMonth, format, formatDate } from "../../../utils/date";

export default function DateInput(props) {
  let { field, name } = props;
  let self = this;
  let { repeatPeriod, repeat } = self.state;

  function updateCheckbox(subField) {
    return function(e) {
      let changedState = {};
      changedState[field] = { ...self.state[field] };
      changedState[field][subField] = e.target.checked;
      self.setState(changedState);
    };
  }

  function updateDate() {
    return function(e) {
      let changedState = {};
      changedState[field] = { ...self.state[field] };
      changedState[field].date = e;
      self.setState(changedState);
    };
  }

  function updateDynamicDateData(subField) {
    return function(e) {
      let changedState = {};
      changedState[field] = { ...self.state[field] };
      changedState[field][subField] = e.target.value;
      self.setState(changedState);
    };
  }

  let dateObject = self.state[field];

  return <div>
    <hr/>
    <div className={"form-row"}>
      <div className={"form-item-label"}>{name}</div>
      <input type='checkbox' checked={dateObject.dynamic}
             onChange={updateCheckbox("dynamic")}/>
      <label className={"form-item-label"} style={{ width: "auto" }}>Dynamic</label>
    </div>
    <hr/>

    {!dateObject.dynamic &&
    <div className={"form-row form-row-indent"}>
      <label style={{ marginRight: 10 }}>Date</label>
      <DatePicker className={"input input-text"} selected={dateObject.date} placeholder="select"
                  onChange={updateDate()}/>
    </div>
    }
    {dateObject.dynamic && <div>
      <div className={"form-row form-row-indent"} style={{ display: "flex", flexDirection: "row", lineHeight: "40px" }}>

        {(repeatPeriod >= PERIODS.Month || !repeat) &&
        <div className={"custom-control custom-control-inline"} style={{ paddingLeft: 0, marginRight: 0 }}>
          The
          <select className={"input input-select"} style={{ width: 120, marginLeft: 10 }}
                  value={dateObject.dynamicWeek}
                  onChange={updateDynamicDateData("dynamicWeek")}>
            {Object.keys(ORDERS).map(key => <option value={ORDERS[key]} key={key}>{key}</option>
            )}
          </select>
        </div>
        }
        {(repeatPeriod >= PERIODS.Week || !repeat) &&
        <div className={"custom-control custom-control-inline"} style={{ paddingLeft: 0, marginRight: 0 }}>
          {repeatPeriod === PERIODS.Week && <div className={"hint"}>Each</div>}
          <select className={"input input-select"} style={{ width: 120, marginLeft: 10 }}
                  value={dateObject.dynamicWeekday}
                  onChange={updateDynamicDateData("dynamicWeekday")}>
            {Object.keys(WEEKDAYS).map(key => <option value={WEEKDAYS[key]} key={key}>{key}</option>)}
          </select></div>
        }

        {(repeatPeriod >= PERIODS.Year || !repeat) &&
        <div style={{ paddingLeft: 0, marginRight: 0 }} className={"custom-control custom-control-inline"}>Of
          <select className={"input input-select"} style={{ width: 120, marginLeft: 10 }}
                  value={dateObject.dynamicMonth}
                  onChange={updateDynamicDateData("dynamicMonth")}>
            {Object.keys(MONTHS).map(key => <option value={MONTHS[key]} key={key}>{key}</option>)}
          </select></div>
        }

      </div>
      {repeatPeriod > PERIODS.Week && (
        <div className={"form-row"} style={{ marginTop: "20px" }}>
          <label className={"form-item-label"}>Next Occurrence</label>
          <div className={"text-highlight"}>{repeatPeriod === PERIODS.Year ?
            format(nextOccurrenceByYear(dateObject.dynamicMonth, dateObject.dynamicWeek, dateObject.dynamicWeekday)) :
            format(nextOccurrenceByMonth(dateObject.dynamicWeek, dateObject.dynamicWeekday))}</div>
        </div>
      )}
    </div>}
  </div>;
}


