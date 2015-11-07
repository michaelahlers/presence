import React from 'react';

class ProjectView extends React.Component {

  render() {
    const { projectId } = this.props.params;

    return (
      <div>
        <h3>Project {projectId}</h3>
      </div>
    );
  }

}

export default ProjectView;
