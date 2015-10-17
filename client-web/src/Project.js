import React from 'react';

export default class extends React.Component {

  //static propTypes = {
  //  params: React.PropTypes.shape({
  //    projectId: React.PropTypes.string.isRequired
  //  })
  //};

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h2>Project {this.props.params.projectId}</h2>

        <p>Summary for Project {this.props.params.projectId}.</p>
      </div>
    );
  }
}
