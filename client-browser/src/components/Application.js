import React from 'react';

import Radium, { Style, StyleRoot } from 'radium';

const rules = {
  body: {
    margin: 0,
    fontFamily: 'Helvetica Neue, Helvetica, Arial, sans-serif'
  },
  html: {
    background: '#ccc',
    fontSize: '100%'
  },
  mediaQueries: {
    '(min-width: 550px)': {
      html: {
        fontSize: '120%'
      }
    },
    '(min-width: 1200px)': {
      html: {
        fontSize: '140%'
      }
    }
  },
  'h1, h2, h3': {
    fontWeight: 'bold'
  }
};

import Button from './Button.js';
import Loading from './Loading.js';

class Dashboard extends React.Component {

  constructor() {
    super();
    this.state = {
      component: <Loading/>
    };
  }

  componentDidMount() {

    setTimeout(() => {
      require.ensure([], () => {
        const Hello = require('./Hello.js');
        this.setState({
          component: <Hello/>
        });
      });
    }, 500);

  }

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

        {this.state.component}

      </div>
    );
  }

}

class Application extends React.Component {

  render() {
    return (
      <StyleRoot>

        <Style rules={{rules}}/>

        <h1>Michael Ahlers</h1>

        {this.props.children || <Dashboard />}

      </StyleRoot>
    );
  }

}

module.exports = Application;
