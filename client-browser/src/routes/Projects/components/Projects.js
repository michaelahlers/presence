import React from 'react';

class Projects extends React.Component {
  render() {
    return (
      <div>
        <h3>Projects</h3>
        {this.props.children || <p>Choose a project from the sidebar.</p>}
      </div>
    );
  }
}

module.exports = Projects;
