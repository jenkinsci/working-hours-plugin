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
  RepeatPeriod,
  TimezoneInput
} from "./formItems";


export default class ExcludeDate extends React.Component {
  constructor() {
    super();
    this.state = {
      nextOccurrence: undefined,

      selectedHoliday: undefined,

      name: "",
      type: DATE_TYPE.TYPE_CUSTOM,

      utcOffset: moment().utcOffset(),
      timezone: "",

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
      repeatCount: -1,
      repeatInterval: 1,
      repeatPeriod: PERIODS.Year,

      isNew: false,
    };
  }

  isHoliday = () => {
    return this.state.type === DATE_TYPE.TYPE_HOLIDAY
  }

  /*Apply the selected preset.*/
  applyPreset = () => {
    Alert.open({
      onApply: (result) => {
        this.setState({
          type: DATE_TYPE.TYPE_HOLIDAY,
          selectedHoliday: result.selectedHoliday
        })
      }
    });
  };

  removePreset = () => {
    this.setState({
      selectedHoliday: undefined,
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
          dynamicMonth: 1,
          dynamicWeek: 1,
          dynamicWeekday: 1
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
    /*Call onEdit, also emit data to parent(for serializing use).*/
    if (this.isHoliday()) {
      this.setState({
        holidayId: this.state.selectedHoliday.propertiesKey,
        holidayRegion: this.state.selectedHoliday.region,
      }, () => {
        this.props.onEdit(this.props.index, !this.props.opened, this.state);
      })
    } else {
      this.props.onEdit(this.props.index, !this.props.opened, this.state);
    }
  };

  deleteDate = () => {
    if (window.confirm("Are you sure to delete?")
    ) {
      this.props.onDelete(this.props.index);
    }
  };

  componentDidMount() {
    this.setState(this.props.date, () => {
      /*If the date is new, submit it.*/
      if (this.props.date.isNew) {
        this.props.onEdit(this.props.index, true, this.state);
      }
    });
  }

  render() {
    const {repeat, noEnd, type, selectedHoliday, startDate} = this.state;
    return (
      <div className={"config-item"}>
        {/*Allow each date item to open or close, need help of the parent component.*/}
        {this.props.opened ? <div>
            {NameInput.call(this)}
            {type === DATE_TYPE.TYPE_HOLIDAY && PresetSelect.call(this)}
            {TimezoneInput.call(this)}
            {RepeatCheckbox.call(this)}
            {repeat && <div>
              {RepeatPeriod.call(this)}
              {RepeatInterval.call(this)}
              {RepeatCount.call(this)}
            </div>}
            {!this.isHoliday()&&DateInput.call(this,
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

            {type === DATE_TYPE.TYPE_HOLIDAY && <div className={"form-row"} style={{marginTop: "20px"}}>
              <label className={"form-item-label"}>Next Occurrence</label>
              <div className={"text-highlight"}>
                {formatDate(moment([
                  selectedHoliday.date.values[0],
                  selectedHoliday.date.values[1] - 1,
                  selectedHoliday.date.values[2]]))}
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
              <button type="button" className={"btn btn-gray"} onClick={this.toggleEdit}>Save and Close</button>
              <button type="button" className={"btn btn-delete"} onClick={this.deleteDate}>Delete</button>
            </div>
          </div> :
          <div>
            <span style={{lineHeight: "20px"}}>{getBrief.call(this)}</span>
            <button type="button" className={"btn btn-gray"} onClick={this.toggleEdit}>Edit</button>
            <button type="button" className={"btn btn-delete"} onClick={this.deleteDate}>X</button>
          </div>}
      </div>
    );
  }
}
