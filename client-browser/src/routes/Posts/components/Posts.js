import React from 'react';

class Posts extends React.Component {
  render() {
    return (
      <div>
        <h3>Posts</h3>
        {this.props.children || <p>Choose a post from the sidebar.</p>}
      </div>
    );
  }
}

module.exports = Posts;
