import React from 'react';

import {Link} from 'react-router';

import Button from './Button.js';

class Dashboard extends React.Component {

  render() {
    return (
      <div>

        <p>Foo.</p>

        <div>
          <Button kind="primary">Button One</Button>
        </div>

        <p>Bear.</p>

        <div>
          <Button kind="primary">Button Two</Button>
        </div>

        <p>Fiz.</p>

        <div>
          <Button kind="primary">Button Three</Button>
        </div>

        <p>Ban.</p>

        <ul>
          <li>
            <Link to={`/posts`}>Posts</Link>

            <ul>
              <li><Link to={`/posts/123`}>123</Link></li>
              <li><Link to={`/posts/456`}>456</Link></li>
            </ul>
          </li>

          <li>
            <Link to={`/projects`}>Projects</Link>

            <ul>
              <li><Link to={`/projects/123`}>123</Link></li>
              <li><Link to={`/projects/456`}>456</Link></li>
            </ul>
          </li>
        </ul>

      </div>
    );
  }

}

class Application extends React.Component {

  render() {
    return (
      <div className="application">

        <h1>Michael Ahlers</h1>

        <Dashboard/>

        {this.props.children}

      </div>
    );
  }

}

module.exports = Application;
