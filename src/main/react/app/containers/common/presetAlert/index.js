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
  alertTip: "提示",
  regions: [],
  regionalHolidays: [],
  selectedHolidayId: undefined,
  selectedHoliday: undefined,
  onApply: function () {

  }
}

class Alert extends Component {

  state = {
    ...defaultState
  };

  FirstChild = props => {
    const childrenArray = React.Children.toArray(props.children);
    return childrenArray[0] || null;
  }

  handleRegionChange = (e) => {
    fetchRegionalHolidays(e.value).then(res => {
      let holidays = res.data.data;
      holidays.forEach(item=>{
        item.region = e.value;
      })
      this.setState({
        regionalHolidays: holidays
      })
    })
  };

  handleHolidaySelected = (e) => {
    const holidayId = e.value;
    this.setState({
      /*Set the preset index for later apply.*/
      selectedHolidayId: holidayId,
      selectedHoliday: this.state.regionalHolidays.find(item => item.propertiesKey === holidayId)
    });
  };

  confirm = () => {
    const selectedHoliday = this.state.regionalHolidays.find(item => item.propertiesKey === this.state.selectedHolidayId)
    if (!this.state.selectedHoliday && this.state.selectedHolidayId) {
      this.setState({
        selectedHoliday: selectedHoliday
      })
    }

    this.state.onApply({
      selectedHoliday: selectedHoliday
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
      ...defaultState,
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
                      defaultValue={this.state.selectedDateType}
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
                      defaultValue={this.state.selectedHolidayId}
                      onChange={this.handleHolidaySelected}
                      options={this.state.regionalHolidays.map((item, index) => {
                          return {
                            value: item.propertiesKey,
                            label: item.description
                          }
                        }
                      )}
              />
              <p><strong>Next Occurrence</strong></p>
              <div>
                {this.state.selectedHoliday && formatDate(moment(this.state.selectedHoliday.date.values))}
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

let div = document.createElement('div');
let props = {};
document.body.appendChild(div);

let Box = ReactDOM.render(React.createElement(
  Alert,
  props
), div);


export default Box;
