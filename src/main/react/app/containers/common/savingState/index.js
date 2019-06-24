import React from "react";

/**
 * @return {string}
 */
export const LOADING_STATE = {
  WAITING: "waiting",
  LOADING: "loading",
  SAVING: "saving",
  SUCCESS: "success",
  FAIL: "fail"
}

export function LoadingState(props) {
  let style = {
    width: 15,
    height: 15,
    borderWidth: 1.5,
    marginLeft: 10
  }

  let successStyle = {
    color: "#1c7931",
    marginLeft: 10,
    display: "inline",
    fontWeight: 600
  }

  let failStyle = {
    color: "#e04745",
    marginLeft: 10,
    display: "inline",
    fontWeight: 600
  }
  let {loadingState} = props;
  switch (loadingState) {
    case LOADING_STATE.WAITING:
      return <div/>
    case LOADING_STATE.LOADING:
      return <div style={{display: 'inline'}}>
        <div style={style}
             className="spinner-border text-secondary" role="status">
        </div>
        Loading
      </div>
    case LOADING_STATE.SAVING:
      return <div style={{display: 'inline'}}>
        <div style={style}
             className="spinner-border text-secondary" role="status">
        </div>
        Saving
      </div>
    case LOADING_STATE.SUCCESS:
      return <div style={successStyle}>Success</div>
    case LOADING_STATE.FAIL:
      return <div style={failStyle}>Fail</div>
  }
}
