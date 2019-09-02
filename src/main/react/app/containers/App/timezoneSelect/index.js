import React from 'react'
import {getDefaultTimezone} from "../../../utils";
import moment from "moment";
import TimezonePicker from '../../common/timezonePicker'
import {apiGetTimezone, setTimezone} from "../../../api";
import {LOADING_STATE, LoadingState} from "../../common/savingState";
import {debounce} from "lodash";

export default class extends React.Component {
  constructor() {
    super();
    this.state = {
      timezone: getDefaultTimezone(),
      offset: moment().utcOffset(),
      loadingState: LOADING_STATE.WAITING,
    };
  }

  handleTimezoneChange = (e) => {
    this.setState({
      timezone: e.name,
      utcOffset: e.offset * 60,
    });
    const params = {
      timezone:e.name,
      utcOffset:e.offset * 60,
    }
    this.setState({
      loadingState:LOADING_STATE.LOADING
    })
    setTimezone({data:params}).then(res=>{
      this.setState({
        loadingState:LOADING_STATE.SUCCESS
      })
      this.debouncedClearLoading()
    }).catch(err=>{
      this.setState({
        loadingState:LOADING_STATE.FAIL
      })
    })
  };

  componentDidMount() {
    this.setState({
      loadingState:LOADING_STATE.LOADING
    })
    apiGetTimezone().then(res=>{
      this.setState({
        timezone:res.data.data.timezone,
        utcOffset:res.data.data.utcOffset,
        loadingState:LOADING_STATE.SUCCESS
      })
      this.debouncedClearLoading()
    }).catch(err=>{
      this.setState({
        loadingState:LOADING_STATE.FAIL
      })
    })
  }

  /*A debounced function used to clear loading state.
* */
  debouncedClearLoading = debounce(() => {
    this.setState({
      loadingState: LOADING_STATE.WAITING
    })
  }, 1000)

  render() {
    return <div>
      <label className={"form-item-label"}>Timezone</label>
      <TimezonePicker value={this.state.timezone}
                      onChange={this.handleTimezoneChange}/>
                    <LoadingState loadingState={this.state.loadingState}/>
    </div>;
  }
}
