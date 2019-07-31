import DatePicker from "react-datepicker/es";
import React from "react";
import {MONTHS, ORDERS, PERIODS, WEEKDAYS} from "../constants";
import {format, nextOccurrenceByMonth, nextOccurrenceByYear} from "../../../utils/date";

export default function DateInput(props) {
  const {field, name,isEndDate} = props;
  const self = this;
  const {repeatPeriod, repeat} = self.state;

  function updateCheckbox(subField) {
    return function (e) {
      let changedState = {};
      changedState[field] = {...self.state[field]};
      changedState[field][subField] = e.target.checked;
      self.setState(changedState);
    };
  }

  function updateDate() {
    return function (e) {
      let changedState = {};
      changedState[field] = {...self.state[field]};
      changedState[field].date = e;
      self.setState(changedState);
    };
  }

  function updateDynamicDateData(subField) {
    return function (e) {
      let changedState = {};
      changedState[field] = {...self.state[field]};
      changedState[field][subField] = parseInt(e.target.value);
      self.setState(changedState);
    };
  }

  const dateObject = self.state[field];

  return <div>
    <hr/>
    <div className={"form-row"}>
      <div className={"form-item-label"}>{name}</div>

      {!isEndDate && repeat && <div className={"form-item-label form-item-label-sub"}>
        <input type='checkbox' checked={dateObject.dynamic}
               onChange={updateCheckbox("dynamic")}/>
        <label>Dynamic</label>
      </div>}
    </div>
    <hr/>

    {(!dateObject.dynamic || !repeat) &&
    <div className={"form-row form-row-indent"}>
      <label style={{marginRight: 10}}>Date</label>
      <DatePicker className={"input input-text"} selected={new Date(dateObject.date)} placeholder="select"
                  onChange={updateDate()}/>
    </div>
    }

    {!isEndDate && repeat && dateObject.dynamic && <div>
      <div className={"form-row form-row-indent"} style={{display: "flex", flexDirection: "row", lineHeight: "40px"}}>

        {(repeatPeriod >= PERIODS.Month || !repeat) &&
        <div className={"date-control-item"}>
          The
          <select className={"input input-select select-dynamic-item"}
                  value={dateObject.dynamicWeek}
                  onChange={updateDynamicDateData("dynamicWeek")}>
            {Object.keys(ORDERS).map(key => <option value={ORDERS[key]} key={key}>{key}</option>
            )}
          </select>
        </div>
        }
        {(repeatPeriod >= PERIODS.Week || !repeat) &&
        <div className={"date-control-item"}>
          {repeatPeriod === PERIODS.Week && <div className={"hint"}>Each</div>}
          <select className={"input input-select select-dynamic-item"}
                  value={dateObject.dynamicWeekday}
                  onChange={updateDynamicDateData("dynamicWeekday")}>
            {Object.keys(WEEKDAYS).map(key => <option value={WEEKDAYS[key]} key={key}>{key}</option>)}
          </select></div>
        }

        {(repeatPeriod >= PERIODS.Year || !repeat) &&
        <div className={"date-control-item"}>Of
          <select className={"input input-select select-dynamic-item"}
                  value={dateObject.dynamicMonth}
                  onChange={updateDynamicDateData("dynamicMonth")}>
            {Object.keys(MONTHS).map(key => <option value={MONTHS[key]} key={key}>{key}</option>)}
          </select></div>
        }

      </div>
      {repeatPeriod > PERIODS.Week && (
        <div className={"form-row"} style={{marginTop: "20px"}}>
          <label className={"form-item-label"}>Next Occurrence</label>
          <div className={"text-highlight"}>{repeatPeriod === PERIODS.Year ?
            format(nextOccurrenceByYear(dateObject.dynamicMonth, dateObject.dynamicWeek, dateObject.dynamicWeekday)) :
            format(nextOccurrenceByMonth(dateObject.dynamicWeek, dateObject.dynamicWeekday))}</div>
        </div>
      )}
    </div>}
  </div>;
}


