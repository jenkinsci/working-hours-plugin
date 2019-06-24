import { range } from "lodash";
import React from "react";
import { DATE_TYPE, PERIODS } from "../constants";
import TimezonePicker from '../../common/timezonePicker'
export function RepeatCount() {
  return <div className={"form-row"}>
    <label className={"form-item-label"}>Repeat Count</label>
    <div><select className={"input input-select"} value={this.state.repeatCount}
                 style={{ width: 120, marginRight: 10 }}
                 onChange={this.handleCountChange}>
      <option value={-1}>No End</option>
      {range(1, 11).map(item =>
        <option key={item} value={item}>{item}</option>
      )}
    </select> {this.state.repeatCount > -1 && "Time"}{this.state.repeatCount > 1 && "s"}
    </div>
  </div>;
}

export function RepeatPeriod() {
  return <div className={"form-row"}>
    <label className={"form-item-label"}>Repeat Period</label>
    <input id='radio-week' name="period" type="radio"
           checked={this.state.repeatPeriod === PERIODS.Week}
           onChange={this.handleRepeatPeriodChange} value={PERIODS.Week}/>
    <label className={"label-inline"}>Week </label>

    <input id='radio-month' name="period" type="radio"
           checked={this.state.repeatPeriod === PERIODS.Month}
           onChange={this.handleRepeatPeriodChange} value={PERIODS.Month}/>
    <label className={"label-inline"}>Month</label>

    <input id='radio-year' name="period" type="radio"
           checked={this.state.repeatPeriod === PERIODS.Year}
           onChange={this.handleRepeatPeriodChange} value={PERIODS.Year}/>
    <label className={"label-inline"}>Year </label>
  </div>;
}

export function RepeatInterval() {
  return <div className={"form-row"}>
    <label className={"form-item-label"}>Repeat Interval</label>
    <div>Each <select className={"input input-select"} value={this.state.repeatInterval}
                      style={{ width: 70, marginRight: 10 }}
                      onChange={this.handleIntervalChange}>
      {range(1, 6).map(item =>
        <option key={item} value={item}>{item}</option>
      )}
    </select>
      {Object.keys(PERIODS)[this.state.repeatPeriod - 1] + (this.state.repeatInterval > 1 ? "s" : "")}
    </div>
  </div>;
}

export function NameInput() {
  const handleNameChange = (e) => {
    this.setState({
      name: e.target.value
    });
  };
  return <div className={"form-row"}>
    <label className={"form-item-label"}>Name</label>
    <input placeholder={"Enter a name"} value={this.state.name}
           className={"input input-text"}
           style={{ width: 200 }}
           onChange={handleNameChange}/>
  </div>;
}


export function TimezoneInput() {
  const handleTimezoneChange = (e) => {
    this.setState({
      timezone: e.name,
      utcOffset:e.offset*60,
    });
  };
  return <div className={"form-row"}>
    <label className={"form-item-label"}>Base Timezone</label>
    <TimezonePicker value={this.state.timezone} onChange={handleTimezoneChange}/>
  </div>;
}

export function PresetSelect() {
  return <div>
    <label className={"form-item-label"}>Preset</label>
    <select className={"input input-select"}
            value={this.state.selectedDateType}
            onChange={this.handleDateTypeChange}
            style={{ width: 150, marginRight: 10 }}
            placeholder="select preset date"
    >
      {Object.keys(DATE_TYPE).map((key, index) =>
        <option key={index} value={DATE_TYPE[key]}>{DATE_TYPE[key]}</option>
      )}
    </select>
    <select className={"input input-select"}
            value={this.state.selectedPreset}
            defaultValue=""
            onChange={this.handlePresetChange}
            style={{ width: 300 }}
            placeholder="select preset date"
    >
      {this.getDatePresets().map((item, index) =>
        <option key={index} value={index}>{item.name}</option>
      )}
    </select>
    <button type="button" className={"btn btn-gray"} onClick={this.applyPreset}>Apply</button>

  </div>;
}

export function RepeatCheckbox() {
  return <div className={"form-row"}
  >
    <label className={"form-item-label"} htmlFor="checkbox-repeat">Repeat</label>
    <input id='checkbox-repeat' type='checkbox' checked={this.state.repeat} onChange={this.handleRepeatChange}>
    </input>
  </div>;
}
