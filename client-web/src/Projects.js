import React from 'react';
import { Link } from 'react-router';

export default class extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h1>Projects</h1>

        {this.props.children || [1, 2, 3].map(id => {
          return (
            <div key={id}>
              <h2>Project {id}</h2>
              <p>Summary for Project {id}. <Link to={`/projects/${id}`}>Read more…</Link></p>
            </div>
          );
        })}
      </div>
    );
  }

}
