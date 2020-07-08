import React, { Component } from "react";
import ProjectItem from "./Project/ProjectItem";
import CreateProjectButton from "./Project/CreateProjectButton";
import { connect } from "react-redux";
import { getProjects } from "../actions/projectActions";
import PropTypes from "prop-types";

class Dashboard extends Component {
  // after the first render, it calls the getProjects created in projectActions
  componentDidMount() {
    // it calls the action we created in projectAction
    this.props.getProjects();
  }

  render() {
    const { projects } = this.props.project;
    return (
      <div>
        <div className="projects">
          <div className="container">
            <div className="row">
              <div className="col-md-12">
                <h1 className="display-4 text-center">Projects</h1>
                <br />
                <CreateProjectButton />
                <br />
                <hr />
                {projects.map(project => (
                  <ProjectItem key={project.id} project={project} />
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

Dashboard.propTypes = {
  project: PropTypes.object.isRequired,
  getProjects: PropTypes.func.isRequired
};
// the project we have here is the one in the index
const mapStateToProps = state => ({
  // project in the index of the reducers folder
  project: state.project
});

export default connect(mapStateToProps, { getProjects })(Dashboard);
