import React, {Component} from 'react';
import {fromJS, is} from 'immutable';
import ReactDOM from 'react-dom';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import './alert.css';
import {getRegions} from "../../App/constants";

import {fetchRegionalHolidays} from "../../../api";
import Select from 'react-select'
import {formatDate} from "../../../utils/date";
import moment from "moment";

let defaultState = {
  alertStatus: false,
  alertTip: "Select",
  regions: [],
  regionalHolidays: [],// regional holiday(This year).
  selectedHolidayKey: undefined,
  selectedHoliday: undefined,
  selectedRegion:undefined,
}

class Alert extends Component {

  state = {
    ...defaultState
  };

  FirstChild = props => {
    const childrenArray = React.Children.toArray(props.children);
    return childrenArray[0] || null;
  }

  /*Fetch holidays with the new region*/
  handleRegionChange = (e) => {
    this.setState({
      selectedRegion:e.value,
    })
    fetchRegionalHolidays(e.value).then(res => {
      let holidays = res.data.data;
      holidays.forEach(item => {
        item.region = e.value;
      })
      this.setState({
        regionalHolidays: holidays,
      })
    })
  };

  handleHolidaySelected = (e) => {
    const key = e.value;
    this.setState({
      /*Set the preset index for later apply.*/
      selectedHolidayKey: key,
      selectedHoliday: this.state.regionalHolidays.find(item => item.key === key)
    });
  };

  confirm = () => {
    const selectedHoliday = this.state.regionalHolidays.find(item => item.key === this.state.selectedHolidayKey)
    if (!this.state.selectedHoliday && this.state.selectedHolidayKey) {
      this.setState({
        selectedHoliday: selectedHoliday
      })
    }

    this.state.onApply({
      selectedHoliday: selectedHoliday,
      holidayRegion:this.state.selectedRegion,
    })

    this.setState({
      alertStatus: false
    })
  }

  cancel = () => {
    this.setState({
      alertStatus: false
    })
  }

  open = (options) => {
    options = options || {};
    options.alertStatus = true;
    this.setState({
      ...options
    })
  }

  close() {
    this.setState({
      ...defaultState
    })
  }

  componentDidMount() {
    this.setState({
      regions: getRegions()
    })
  }

  shouldComponentUpdate(nextProps, nextState) {
    return !is(fromJS(this.props), fromJS(nextProps)) || !is(fromJS(this.state), fromJS(nextState))
  }

  render() {
    const regions = getRegions()
    return (
      <ReactCSSTransitionGroup
        component={this.FirstChild}
        transitionName='hide'
        transitionEnterTimeout={300}
        transitionLeaveTimeout={300}>
        <div className="alert-con" style={this.state.alertStatus ? {display: 'block'} : {display: 'none'}}>
          <div className="alert-context">
            <div className="alert-content-detail">

              <p><strong>Select Region</strong></p>
              <Select className='select-region '
                      defaultValue={this.state.selectedRegion}
                      onChange={this.handleRegionChange}
                      options={Object.keys(regions).map((key, index) => {
                          return {
                            value: key,
                            label: regions[key]
                          }
                        }
                      )
                      }
              />

              <p><strong>Select Day</strong></p>
              <Select className='select-holiday'
                      defaultValue={this.state.selectedHolidayKey}
                      onChange={this.handleHolidaySelected}
                      options={this.state.regionalHolidays.map((item, index) => {
                          return {
                            value: item.key,
                            label: item.name
                          }
                        }
                      )}
              />
              <p><strong>Next Occurrence</strong></p>
              <div>
                {this.state.selectedHoliday && formatDate(moment(this.state.selectedHoliday.nextOccurrence))}
              </div>

            </div>

            <div>
              <button type="button"
                      className={"btn btn-alert btn-cancel"}
                      onClick={this.cancel}>Cancel
              </button>
              <button type="button" className={"btn btn-gray btn-alert btn-confirm"}
                      onClick={this.confirm}>Apply
              </button>
            </div>
          </div>
        </div>
      </ReactCSSTransitionGroup>
    );
  }
}

/*Mount the alert to DOM*/
let div = document.createElement('div');
let props = {};
document.body.appendChild(div);

let Box = ReactDOM.render(React.createElement(
  Alert,
  props
), div);


export default Box;
