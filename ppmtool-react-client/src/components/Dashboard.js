import React, { Component } from "react";
import ProjectItem from "./Project/ProjectItem";

class Dashboard extends Component {
  render() {
    return (
      <div>
        {/* jsx allows us to have html in the render method */}
        <h1 className="alert alert-warning">Welcome to Dashboard</h1>
        <ProjectItem />
      </div>
    );
  }
}

export default Dashboard;
