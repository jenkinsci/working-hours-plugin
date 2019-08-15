import {range} from "lodash";
import React from "react";
import {PERIODS} from "../constants";
import TimezonePicker from '../../common/timezonePicker'

/**
 * Functional render for select repeat count.
 * @returns {*} render content
 * @constructor
 */
export function RepeatCount() {
  return <div className={"form-row"}>
    <label className={"form-item-label"}>Repeat Count</label>
    <div><select className={"input input-select"} value={this.state.repeatCount}
                 style={{width: 120, marginRight: 10}}
                 onChange={this.handleCountChange}>
      <option value={-1}>No End</option>
      {range(1, 11).map(item =>
        <option key={item} value={item}>{item}</option>
      )}
    </select> {this.state.repeatCount > -1 && "Time"}{this.state.repeatCount > 1 && "s"}
    </div>
  </div>;
}

/**
 * Functional render for select repeat period.
 * @returns {*} render content
 * @constructor
 */
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

/**
 * Functional render for select repeat interval.
 * @returns {*} render content
 * @constructor
 */
export function RepeatInterval() {
  return <div className={"form-row"}>
    <label className={"form-item-label"}>Repeat Interval</label>
    <div>Each <select className={"input input-select"} value={this.state.repeatInterval}
                      style={{width: 70, marginRight: 10}}
                      onChange={this.handleIntervalChange}>
      {range(1, 6).map(item =>
        <option key={item} value={item}>{item}</option>
      )}
    </select>
      {Object.keys(PERIODS)[this.state.repeatPeriod - 1] + (this.state.repeatInterval > 1 ? "s" : "")}
    </div>
  </div>;
}

/**
 * Functional render for input date name.
 * @returns {*} render content
 * @constructor
 */
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
           style={{width: 200}}
           onChange={handleNameChange}/>
    <button type="button" className={"btn btn-gray"}
            onClick={this.applyPreset}>Select Preset
    </button>
  </div>;
}

/**
 * Functional render for select timezone.
 * @returns {*} render content
 * @constructor
 */
export function TimezoneInput() {
  const handleTimezoneChange = (e) => {
    this.setState({
      timezone: e.name,
      utcOffset: e.offset * 60,
    });
  };
  return <div className={"form-row"}>
    <label className={"form-item-label"}>Base Timezone</label>
    <TimezonePicker value={this.state.timezone} onChange={handleTimezoneChange}/>
  </div>;
}

/**
 * Functional render for select preset.
 * @returns {*} render content
 * @constructor
 */
export function PresetSelect() {
  return <div className="form-row">
    <label className={"form-item-label"}>Using Preset</label>
    <p><strong>{this.state.selectedHoliday.name}</strong></p>
    <button type="button" className={"btn"}
            onClick={this.removePreset}>X
    </button>
  </div>;
}

/**
 * Functional render for switch preset config.
 * @returns {*} render content
 * @constructor
 */
export function RepeatCheckbox() {
  return <div className={"form-row"}
  >
    <label className={"form-item-label"} htmlFor="checkbox-repeat">Repeat</label>
    <input id='checkbox-repeat' type='checkbox' checked={this.state.repeat} onChange={this.handleRepeatChange}>
    </input>
  </div>;
}
