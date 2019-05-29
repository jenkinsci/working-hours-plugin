import DatePicker from "react-datepicker/es";
import React from "react";
import { MONTHS, ORDERS, PERIODS, WEEKDAYS } from "./constants";
import { nextOccurrenceByYear, nextOccurrenceByMonth, format, formatDate } from "../../utils/date";

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
      changedState[field]["dynamicDate"] = { ...self.state[field]["dynamicDate"] };
      changedState[field]["dynamicDate"][subField] = e.target.value;
      self.setState(changedState);
    };
  }

  let dateObject = self.state[field];

  return <div>
    <hr/>
    <div className={"form-row"}>
      <div className={"form-item-label"}>{name}</div>
    </div>
    <hr/>
    <div className={"form-row"}
    >
      <label className={"form-item-label"}>Dynamic</label>
      <input type='checkbox' checked={dateObject.dynamic}
             onChange={updateCheckbox("dynamic")}/>
    </div>

    {!dateObject.dynamic && <div className={"form-row-indent"}
    >
      <div className={"form-row"}>
        <label style={{ marginRight: 10 }}>Date</label>
        <div style={{ border: "1px solid black" }}>
          <DatePicker selected={dateObject.date} placeholder="select"
                      onChange={updateDate()}/>
        </div>
      </div>
    </div>}
    {dateObject.dynamic && <div className={"form-row-indent"}>
      <div className={"form-row"} style={{ display: "flex", flexDirection: "row" }}>
        {(repeatPeriod >= PERIODS.Month || !repeat) &&
        <div className={"custom-control custom-control-inline"} style={{ paddingLeft: 0, marginRight: 0 }}>
          The
          <select className={"custom-select"} style={{ width: 120, marginLeft: 10 }}
                  value={dateObject.dynamicDate.week}
                  onChange={updateDynamicDateData("week")}>
            {Object.keys(ORDERS).map(key => <option value={ORDERS[key]} key={key}>{key}</option>
            )}
          </select>
        </div>
        }
        {(repeatPeriod >= PERIODS.Week || !repeat) &&
        <div className={"custom-control custom-control-inline"} style={{ paddingLeft: 0, marginRight: 0 }}>
          <select className={"custom-select"} style={{ width: 120, marginLeft: 10 }}
                  value={dateObject.dynamicDate.day}
                  onChange={updateDynamicDateData("day")}>
            {Object.keys(WEEKDAYS).map(key => <option value={WEEKDAYS[key]} key={key}>{key}</option>)}
          </select></div>
        }

        {(repeatPeriod >= PERIODS.Year || !repeat) &&
        <div style={{ paddingLeft: 0, marginRight: 0 }} className={"custom-control custom-control-inline"}>Of
          <select className={"custom-select"} style={{ width: 120, marginLeft: 10 }}
                  value={dateObject.dynamicDate.month}
                  onChange={updateDynamicDateData("month")}>
            {Object.keys(MONTHS).map(key => <option value={ORDERS[key]} key={key}>{key}</option>)}
          </select></div>
        }

      </div>
      {repeatPeriod > PERIODS.Week && (
        <div className={"form-row"}>
          <label className={"form-item-label"} style={{ width: 120 }}>Next Occurrence</label>
          <div>{repeatPeriod === PERIODS.BY_YEAR ?
            format(nextOccurrenceByYear(dateObject.dynamicDate.month, dateObject.dynamicDate.week, dateObject.dynamicDate.day)) :
            format(nextOccurrenceByMonth(dateObject.dynamicDate.week, dateObject.dynamicDate.day))}</div>
        </div>
      )}
    </div>}
  </div>;
}


