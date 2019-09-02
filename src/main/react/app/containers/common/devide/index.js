import React from "react";
import './index.css'

export function divide(props) {
  return <div className={"divide"}>
    <div className={"divide-left"}>
      <div className={"divide-line-left"}/>
      <div className={"divide-content"}>{props.content}</div>
    </div>
    <div className={"divide-line-right"}/>
  </div>
}
