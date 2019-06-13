import React from "react";
import DateInput from "./dateInput";
import { DATE_TYPE, getDatePresets, PERIODS} from "../constants";
import {
  formatDate,
  nextOccurrenceChineseLunar
} from "../../../utils/date";
import { getBrief } from "../../../utils";
import moment from "moment";
import {
  RepeatCount,
  RepeatInterval,
  RepeatPeriod,
  NameInput,
  TimezoneInput,
  PresetSelect,
  RepeatCheckbox
} from "./formItems";


export default class ExcludeDate extends React.Component {
  constructor() {
    super();
    this.state = {
      selectedDateType: DATE_TYPE.TYPE_GREGORIAN,
      selectedPreset: -1,
      nextOccurrence: undefined,

      name: "",
      type: "",

      utcOffset: moment().utcOffset(),
      timezone:"",

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
      timezones: [],
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
        let preset = this.getDatePresets()[this.state.selectedPreset];
        this.setState(preset);

        /*For types that are not gregorian, we calculate its occurrence
        * using its corresponding algorithm.*/
        this.setState({
          nextOccurrence: nextOccurrenceChineseLunar(preset.params)
        });
    }
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




  /*For the selected date type, get its presets.*/
  getDatePresets() {
    return getDatePresets(this.state.selectedDateType);
  }

  /*Tell the parent that this child want to be toggle open state.*/
  toggleEdit = () => {
    /*Call onEdit, also emit data to parent(for serializing use).*/
    this.props.onEdit(this.props.index, !this.props.opened, this.state);
  };

  deleteDate = () => {
    if (window.confirm("Are you sure to delete?")
    ) {
      this.props.onDelete(this.props.index);
    }
  };

  /*Helper function to judge whether the day is based on gregorian calendar*/
  isGregorian = () => {
    return this.state.type === DATE_TYPE.TYPE_GREGORIAN;
  };

  componentDidMount() {
    this.setState(this.props.date);
  }

  render() {
    const { repeat, noEnd } = this.state;
    return (
      <div className={"config-item"}>
        {/*Allow each date item to open or close, need help of the parent component.*/}
        {this.props.opened ? <div>
            {NameInput.call(this)}
            {TimezoneInput.call(this)}
            {PresetSelect.call(this)}
            {RepeatCheckbox.call(this)}
            {repeat && <div>
              {RepeatPeriod.call(this)}
              {RepeatInterval.call(this)}
              {RepeatCount.call(this)}
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
            <span style={{ lineHeight: "20px" }}>{getBrief.call(this)}</span>
            <button type="button" className={"btn btn-gray"} onClick={this.toggleEdit}>Edit</button>
            <button type="button" className={"btn btn-delete"} onClick={this.deleteDate}>X</button>
          </div>}
      </div>
    );
  }
}