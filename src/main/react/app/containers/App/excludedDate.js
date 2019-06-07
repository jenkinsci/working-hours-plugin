import React from "react";
import DateInput from "./dateInput";
import { DATE_PRESETS, DATE_TYPE, getDatePresets, MONTHS, ORDERS, PERIODS, WEEKDAYS } from "./constants";
import {
  format,
  formatDate,
  nextOccurrenceByMonth,
  nextOccurrenceByYear,
  nextOccurrenceChineseLunar
} from "../../utils/date";
import moment from "moment";
import _ from 'lodash'


export default class ExcludeDate extends React.Component {
  constructor() {
    super();
    this.state = {
      selectedDateType: DATE_TYPE.TYPE_GREGORIAN,
      selectedPreset: -1,
      nextOccurrence:undefined,

      name: "",
      type: "",
      utcOffset: moment().utcOffset(),
      startDate: {
        dynamic: false,
        date: new Date(),
        dynamicDate: {
          month: 1,
          week: 1,
          day: 1
        }
      },
      endDate: {
        dynamic: false, date: new Date(), dynamicDate: {
          month: 1,
          week: 1,
          day: 1
        }
      },
      noEnd: true, //No end in repeat
      repeat: true,
      repeatCount: -1,
      repeatInterval: 1,
      repeatPeriod: PERIODS.Year
    };
  }

  /*Apply the selected preset.*/
  applyPreset = () => {
    /*According to the selected date type, use different algorithms.*/
    switch (this.state.selectedDateType) {
      case DATE_TYPE.TYPE_GREGORIAN:
        this.setState(this.getDatePresets()[this.state.selectedPreset]);
        break;
      case DATE_TYPE.TYPE_CHINESE_LUNAR:
        let preset = this.getDatePresets()[this.state.selectedPreset]
        this.setState(preset);

        /*For types that are not gregorian, we calculate its occurrence
        * using its corresponding algorithm.*/
        debugger
        this.setState({
          nextOccurrence:nextOccurrenceChineseLunar(preset.params)
        })
    }
  };

  getPeriodText = () => {
    return Object.keys(PERIODS)[this.state.repeatPeriod - 1];
  };

  handleRepeatChange = (event) => {
    this.setState({ repeat: event.target.checked });
  };

  handleRepeatPeriodChange = (e) => {
    this.setState({ repeatPeriod: Number.parseInt(e.target.value) });
  };

  handleNoEndChange = (event) => {
    this.setState({ noEnd: event.target.checked });
  };

  handleIntervalChange = (e) => {
    this.setState({ repeatInterval: e.target.value });
  };

  handleCountChange = (e) => {
    if (e.target.value === "-1") {
      this.setState({ noEnd: true });
    }
    this.setState({ repeatCount: e.target.value });
  };

  handlePresetChange = (e) => {
    this.setState({
      /*Set the preset index for later apply.*/
      selectedPreset: parseInt(e.target.value)
    });
  };

  handleDateTypeChange = (e) => {
    this.setState({
      selectedDateType: e.target.value,
      /*Set the default index of the selected preset*/
      selectedPreset: 0
    });
  };

  handleTimezoneChange = (e) => {
    this.setState({
      utcOffset: e.target.value
    });
  };

  handleNameChange = (e) => {
    this.setState({
      name: e.target.value
    });
  };

  /*For the selected date type, get its presets.*/
  getDatePresets() {
    return getDatePresets(this.state.selectedDateType);
  }


  /*Tell the parent that this child want to be toggle open state.*/
  toggleEdit = () => {

    /*Call onEdit, also emit data to parent(for serializing use).*/
    if (this.props.opened) {
      this.props.onEdit(this.props.index, false, this.state);
    } else {
      this.props.onEdit(this.props.index, true, this.state);
    }
  };

  deleteDate = () => {
    if (window.confirm("Are you sure to delete?")
    ) {
      this.props.onDelete(this.props.index);
    } else {

    }
  };

  /*Helper function to judge whether the day is based on gregorian calendar*/
  isGregorian = () => {
    return this.state.type === DATE_TYPE.TYPE_GREGORIAN;
  };

  /*Get a brief description of this excluded date.*/
  getBrief() {
    let words = [];
    words.push(this.state.name);
    if (this.state.repeat) {
      words.push("repeat");
      words.push("on");
      if (this.state.startDate.dynamic) {
        words.push("each");
        if (this.state.repeatPeriod > PERIODS.Week) {
          words.push(Object.keys(ORDERS)[this.state.startDate.dynamicDate.week - 1]);
        }
        words.push(Object.keys(WEEKDAYS)[this.state.startDate.dynamicDate.day - 1]);
        if (this.state.repeatPeriod > PERIODS.Month) {
          words.push("of");
          words.push(Object.keys(MONTHS)[this.state.startDate.dynamicDate.month - 1]);
        }
      } else {
        words.push(formatDate(this.state.startDate.date));
      }
      if (!this.state.noEnd) {
        words.push("till");
        if (this.state.endDate.dynamic) {
          words.push("the");
          words.push(Object.keys(ORDERS)[this.state.endDate.dynamicDate.week - 1]);
          words.push(Object.keys(WEEKDAYS)[this.state.endDate.dynamicDate.day - 1]);
          words.push("of");
          words.push(Object.keys(MONTHS)[this.state.endDate.dynamicDate.month - 1]);
        } else {
          words.push(formatDate(this.state.endDate.date));
        }
      } else {

      }

    } else {
      words.push("on");
      if (this.state.startDate.dynamic) {
        if (this.state.repeatPeriod > PERIODS.Week) {
          words.push(Object.keys(ORDERS)[this.state.startDate.dynamicDate.week - 1]);
        }
        words.push(Object.keys(WEEKDAYS)[this.state.startDate.dynamicDate.day - 1]);
        if (this.state.repeatPeriod > PERIODS.Month) {
          words.push("of");
          words.push(Object.keys(MONTHS)[this.state.startDate.dynamicDate.month - 1]);
        }
      } else {
        words.push(formatDate(this.state.startDate.date));
      }
    }

    return words.join(" ");
  }

  componentWillUpdate(nextProps, nextState, nextContext) {
    console.log(nextState.selectedDateType);
  }

  render() {
    const { repeat, noEnd } = this.state;
    return (
      <div className={"config-item"}>
        {/*Allow each date item to open or close, need help of the parent component.*/}
        {this.props.opened ? <div>
            <div className={"form-row"}>
              <label className={"form-item-label"}>Name</label>
              <input placeholder={"Enter a name"} value={this.state.name}
                     className={"input input-text"}
                     style={{ width: 200 }}
                     onChange={this.handleNameChange}/>
            </div>
            <div className={"form-row"}>
              <label className={"form-item-label"}>Base Timezone</label>
              <input placeholder={"Enter a name"} value={this.state.utcOffset}
                     className={"input input-text"}
                     style={{ width: 200 }}
                     onChange={this.handleTimezoneChange}/>
            </div>
            <div>
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

            </div>
            <div className={"form-row"}
            >
              <label className={"form-item-label"} htmlFor="checkbox-repeat">Repeat</label>
              <input id='checkbox-repeat' type='checkbox' checked={this.state.repeat} onChange={this.handleRepeatChange}>
              </input>
            </div>
            {repeat && <div className={"form-row"}>
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
            </div>
            }

            {repeat && <div className={"form-row"}>
              <label className={"form-item-label"}>Repeat Interval</label>
              <div>Each <select className={"input input-select"} value={this.state.repeatInterval}
                                style={{ width: 70, marginRight: 10 }}
                                onChange={this.handleIntervalChange}>

                <option value={1}>1</option>
                <option value={2}>2</option>
                <option value={3}>3</option>
                <option value={4}>4</option>
                <option value={5}>5</option>

              </select>
                {this.getPeriodText() + (this.state.repeatInterval > 1 ? "s" : "")}
              </div>
            </div>}
            {repeat && <div className={"form-row"}>
              <label className={"form-item-label"}>Repeat Count</label>
              <div><select className={"input input-select"} value={this.state.repeatCount}
                           style={{ width: 120, marginRight: 10 }}
                           onChange={this.handleCountChange}>
                <option value={-1}>No End</option>
                <option value={1}>1</option>
                <option value={2}>2</option>
                <option value={3}>3</option>
                <option value={4}>4</option>
                <option value={5}>5</option>
                <option value={6}>6</option>
                <option value={7}>7</option>
                <option value={8}>8</option>
                <option value={9}>9</option>
                <option value={10}>10</option>
              </select> {this.state.repeatCount > -1 && "Time"}{this.state.repeatCount > 1 && "s"}
              </div>
            </div>}
            {this.isGregorian() && DateInput.call(this,
              {
                field: "startDate",
                name: "Start Date"
              }
            )}

            {/*Show next occurrence when it's a regional date.*/}
            {!this.isGregorian() && <div className={"form-row"} style={{ marginTop: "20px" }}>
              <label className={"form-item-label"}>Next Occurrence</label>
              <div className={"text-highlight"}>{
                formatDate(this.state.nextOccurrence)}
              </div>
            </div>}


            {repeat && !noEnd && DateInput.call(this,
              {
                field: "endDate",
                name: "End Date"
              })
            }

            {repeat &&
            <div className={"form-row"}>
              <label className={"form-item-label"}>No End</label>
              <input type='checkbox' checked={this.state.noEnd}
                     onChange={this.handleNoEndChange}/>
            </div>}
            <div className={"form-row"}>
              <div className={"form-item-label"}/>
              {/*<button type="button" className="btn btn-outline-primary">Save</button>*/}
              <button type="button" className={"btn btn-gray"} onClick={this.toggleEdit}>Save and Close</button>
              <button type="button" className={"btn-delete"} onClick={this.deleteDate}>Delete</button>
            </div>
          </div> :
          <div>
            <span style={{ lineHeight: "20px" }}>{this.getBrief()}</span>
            <button type="button" className={"btn btn-gray"} onClick={this.toggleEdit}>Edit</button>
            <button type="button" className={"btn btn-delete"} onClick={this.deleteDate}>X</button>
          </div>}
      </div>
    );
  }

  componentDidMount() {
    this.setState(this.props.date);
  }
}