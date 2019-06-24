import React from "react";

/**
 * @return {string}
 */
export function SavingState(props) {
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
  let {loading} = props;
  return (!loading ? <div style={style}
                          className="spinner-border text-secondary" role="status">
    <span className="sr-only">Loading...</span>
  </div> : <div style={successStyle}>Success!</div>)
}
