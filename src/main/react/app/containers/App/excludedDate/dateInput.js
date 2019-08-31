import DatePicker from "react-datepicker/es";
import React from "react";
import {MONTHS, ORDERS, PERIODS, WEEKDAYS} from "../constants";
import {
  format,
  nextOccurrenceByMonth,
  nextOccurrenceByYear
} from "../../../utils/date";
import {divide} from "../../common/devide";

export default function DateInput(props) {
  const {field, name, isEndDate} = props;
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
    {divide({content:name})}
    {!isEndDate && repeat && <div className={"form-row"}>
      <label className={"form-item-label"}>Dynamic</label>
      <input type='checkbox' checked={dateObject.dynamic}
             onChange={updateCheckbox("dynamic")}/>
    </div>}

    {(!dateObject.dynamic || !repeat) &&
    <div className={"form-row"}>
      <label className={"form-item-label"}>Date</label>
      <DatePicker className={"input input-text"}
                  selected={new Date(dateObject.date)} placeholder="select"
                  onChange={updateDate()}/>
    </div>
    }

    {!isEndDate && repeat && dateObject.dynamic && <div>
      <div className={"form-row"}>
        <div
          className={"form-item-label"}> {repeatPeriod === PERIODS.Week ? 'Each' : 'The'}</div>
        {(repeatPeriod >= PERIODS.Month || !repeat) &&
        <select className={"input input-select select-dynamic-week"}
                value={dateObject.dynamicWeek}
                onChange={updateDynamicDateData("dynamicWeek")}>
          {Object.keys(ORDERS).map(key => <option value={ORDERS[key]}
                                                  key={key}>{key}</option>
          )}
        </select>
        }
        {(repeatPeriod >= PERIODS.Week || !repeat) &&
        <select className={"input input-select select-dynamic-weekday"}
                value={dateObject.dynamicWeekday}
                onChange={updateDynamicDateData("dynamicWeekday")}>
          {Object.keys(WEEKDAYS).map(key => <option value={WEEKDAYS[key]}
                                                    key={key}>{key}</option>)}
        </select>
        }

        {(repeatPeriod >= PERIODS.Year || !repeat) &&
        <div>Of
          <select className={"input input-select select-dynamic-month"}
                  value={dateObject.dynamicMonth}
                  onChange={updateDynamicDateData("dynamicMonth")}>
            {Object.keys(MONTHS).map(key => <option value={MONTHS[key]}
                                                    key={key}>{key}</option>)}
          </select>
        </div>
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


