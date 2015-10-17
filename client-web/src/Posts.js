import React from 'react';
import { Link } from 'react-router';

export default class extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h1>Posts</h1>

        {this.props.children || [1, 2, 3].map(id => {
          return (
            <div>
              <h2>Post {id}</h2>
              <p>Summary for Post {id}. <Link to={`/posts/${id}`}>Read more…</Link></p>
            </div>
          );
        })}
      </div>
    );
  }

}
