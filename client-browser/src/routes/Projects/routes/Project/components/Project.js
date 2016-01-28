import React from 'react';

class Project extends React.Component {
  render() {
    return (
      <div>
        <h4>Project ({this.props.params.projectId})</h4>
        <p>Lipsum.</p>
      </div>
    );
  }
}
;

module.exports = Project;
