import React from 'react';

export default class extends React.Component {

  //static propTypes = {
  //  params: React.PropTypes.shape({
  //    postId: React.PropTypes.string.isRequired
  //  })
  //};

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h2>Post {this.props.params.postId}</h2>

        <p>Summary for Post {this.props.params.postId}.</p>
      </div>
    );
  }
}
