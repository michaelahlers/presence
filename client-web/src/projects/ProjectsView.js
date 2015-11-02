import React from 'react';
import { Router, Route, Link } from 'react-router';

export default class extends React.Component {

  render() {
    return (
      <div>
        <h2>Projects</h2>

        <ul>
          <li><Link to={`/projects/1`}>Project 1</Link></li>
          <li><Link to={`/projects/2`}>Project 2</Link></li>
          <li><Link to={`/projects/3`}>Project 3</Link></li>
        </ul>

        {this.props.children}
      </div>
    );
  }

}
