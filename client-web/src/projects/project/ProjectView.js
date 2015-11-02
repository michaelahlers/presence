import React from 'react';

export default class extends React.Component {

  render() {
    const { projectId } = this.props.params;

    return (
      <div>
        <h3>Project {projectId}</h3>
      </div>
    );
  }

}
