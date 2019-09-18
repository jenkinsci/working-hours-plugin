import React from "react";
import DateInput from "./dateInput";
import {DATE_TYPE, PERIODS} from "../constants";
import {formatDate} from "../../../utils/date";
import {getBrief} from "../../../utils";
import moment from "moment";
import Alert from '../../common/presetAlert'
import {
  NameInput,
  PresetSelect,
  RepeatCheckbox,
  RepeatCount,
  RepeatInterval,
  RepeatPeriod
} from "./formItems";

const REPEAT_NO_END = -1;

export default class ExcludeDate extends React.Component {
  constructor() {
    super();
    this.state = {
      nextOccurrence: undefined,

      holiday: undefined,
      selectedHoliday: undefined,

      name: "",
      type: DATE_TYPE.TYPE_CUSTOM,

      startDate: {
        dynamic: false,
        date: new Date().toISOString(),
        dynamicMonth: 1,
        dynamicWeek: 1,
        dynamicWeekday: 1
      },
      endDate: {
        date: new Date().toISOString(),
      },
      noEnd: true, //No end in repeat
      repeat: true,
      repeatCount: REPEAT_NO_END,
      repeatInterval: 1,
      repeatPeriod: PERIODS.Year,

      isNew: false,

      errorString: "please input name",
      passCheck: false,
    };
  }

  isHoliday = () => {
    return this.state.type === DATE_TYPE.TYPE_HOLIDAY
  };

  /*Apply the selected preset.*/
  applyPreset = () => {
    Alert.open({
      onApply: (result) => {
        this.setState({
          type: DATE_TYPE.TYPE_HOLIDAY,
          holiday: result.selectedHoliday,
          holidayRegion: result.holidayRegion,
          name: this.state.name || result.selectedHoliday.name,
          passCheck: true,
          repeat:true,
          repeatPeriod:PERIODS.Year,
          repeatCount:REPEAT_NO_END,
          repeatInterval:1,
        })
      }
    });
  };

  removePreset = () => {
    this.setState({
      holiday: undefined,
      type: DATE_TYPE.TYPE_CUSTOM,
    })
  }


  handleRepeatChange = (event) => {
    this.setState({repeat: event.target.checked});
  };

  handleRepeatPeriodChange = (e) => {
    this.setState({repeatPeriod: Number.parseInt(e.target.value)});
  };

  handleNoEndChange = (event) => {
    this.setState({
      noEnd: event.target.checked,
    });
    /*If it's null or undefined or empty, give it the initial value.*/
    if (!this.state.endDate) {
      this.setState({
        endDate: {
          dynamic: false,
          date: new Date(),
        }
      })
    }
  };

  handleIntervalChange = (e) => {
    this.setState({repeatInterval: e.target.value});
  };

  handleCountChange = (e) => {
    if (e.target.value === "-1") {
      this.setState({noEnd: true});
    }
    this.setState({repeatCount: e.target.value});
  };


  /*Tell the parent that this child want to be toggle open state.*/
  toggleEdit = () => {
    if (this.props.opened) {
      //If close
      if (this.checkValue()) {

      }
    }
    /*Call onEdit, also emit data to parent(for serializing use).*/
    this.props.onEdit(this.props.index, !this.props.opened, this.state);
  };

  deleteDate = () => {
    if (window.confirm("Are you sure to delete?")
    ) {
      this.props.onDelete(this.props.index);
    }
  };


  checkValue() {
    if (!this.state.name) {
      this.setState({
        errorString: "Please input name",
        passCheck: false
      })
    } else {
      this.setState({
        passCheck: true
      })
    }
  }

  showRepeatControl() {
    return !this.isHoliday()
  }

  componentDidMount() {
    this.setState(this.props.date, () => {
      /*If the date is new, submit it.*/
      if (this.props.date.isNew) {
        this.props.onEdit(this.props.index, true, this.state);
      }
      this.checkValue();
    });
  }

  render() {
    const {repeat, noEnd, type, holiday, startDate,passCheck} = this.state;
    return (
      <div className={"config-item"}>
        {/*Allow each date item to open or close, need help of the parent component.*/}
        {this.props.opened ? <div>
            {NameInput.call(this)}
            {type === DATE_TYPE.TYPE_HOLIDAY && PresetSelect.call(this)}
            {this.showRepeatControl() && RepeatCheckbox.call(this)}
            {this.showRepeatControl() && repeat && <div>
              {RepeatPeriod.call(this)}
              {RepeatInterval.call(this)}
              {RepeatCount.call(this)}
            </div>}
            {!this.isHoliday() && DateInput.call(this,
              {
                field: "startDate",
                name: "Start Date"
              }
            )}

            {/*Show next occurrence when it's a regional date.*/}
            {type === DATE_TYPE.TYPE_CUSTOM && !startDate.dynamic &&
            <div className={"form-row"} style={{marginTop: "20px"}}>
              <label className={"form-item-label"}>Next Occurrence</label>
              <div className={"text-highlight"}>
                {formatDate(startDate.date)}
              </div>
            </div>}

            {type === DATE_TYPE.TYPE_HOLIDAY &&
            <div className={"form-row"} style={{marginTop: "20px"}}>
              <label className={"form-item-label"}>Next Occurrence</label>
              <div className={"text-highlight"}>
                {formatDate(moment(holiday.nextOccurrence))}
              </div>
            </div>}

            {repeat && !noEnd && DateInput.call(this,
              {
                field: "endDate",
                name: "End Date",
                isEndDate: true,
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
              <button type="button" className={"btn btn-gray btn-save"}
                      disabled={!passCheck}
                      onClick={this.toggleEdit}>{passCheck?"Save and Close":this.state.errorString}
              </button>
              <button type="button" className={"btn btn-delete"}
                      onClick={this.deleteDate}>Delete
              </button>
            </div>
          </div> :
          <div>
            <span style={{lineHeight: "20px"}}>{getBrief.call(this)}</span>
            <button type="button" className={"btn btn-gray"}
                    onClick={this.toggleEdit}>Edit
            </button>
            <button type="button" className={"btn btn-delete"}
                    onClick={this.deleteDate}>X
            </button>
          </div>}
      </div>
    );
  }

}
